package arch.homework.orders.repository.impl;

import arch.homework.orders.entity.*;
import arch.homework.orders.repository.OrdersRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.List;

@AllArgsConstructor
@Repository
public class OrdersRepositoryImpl implements OrdersRepository {

    private final static String GET_ORDER_BY_ID_QUERY = "select * from orders where id = :id";
    private final static String GET_ORDERS = "select * from orders where user_id = :user_id";
    private final static String GET_ORDER_ITEMS = "select * from order_items where order_id = :order_id";
    private final static String GET_ORDER_DETAILS = "select * from order_details where order_id = :order_id";
    private final static String GET_ORDER_BY_ID_WITH_LOCK_QUERY = "select * from orders where id = :id for update";
    private final static String UPDATE_BILLING_STATUS = "update orders set billing_status = :status where id = :id";
    private final static String UPDATE_WAREHOUSE_STATUS = "update orders set warehouse_status = :status where id = :id";
    private final static String UPDATE_DELIVERY_STATUS = "update orders set delivery_status = :status where id = :id";
    private final static String FAIL_ORDER = "update orders set order_status = :status, reason = :reason where id = :id";
    private final static String UPDATE_ORDER_STATUS = "update orders set order_status = :status where id = :id";

    private final static BeanPropertyRowMapper<Order> BPRM_ORDER = BeanPropertyRowMapper.newInstance(Order.class);
    private final static BeanPropertyRowMapper<OrderInfo.ItemsInfo> BPRM_ORDER_ITEM = BeanPropertyRowMapper.newInstance(OrderInfo.ItemsInfo.class);
    private final static BeanPropertyRowMapper<OrderDetails> BPRM_ORDER_DETAILS = BeanPropertyRowMapper.newInstance(OrderDetails.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Long createOrder(CreateOrderRequest request, Long userId) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");

        Number number = insert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("userId", userId, Types.NUMERIC)
                .addValue("billing_status", SagaStatus.NEW.toString(), Types.VARCHAR)
                .addValue("warehouse_status", SagaStatus.NEW.toString(), Types.VARCHAR)
                .addValue("delivery_status", SagaStatus.NEW.toString(), Types.VARCHAR)
                .addValue("order_status", SagaStatus.NEW.toString(), Types.VARCHAR));
        return number.longValue();
    }

    @Override
    public void saveOrderDetails(CreateOrderRequest request, Long orderId) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("order_details")
                .usingGeneratedKeyColumns("id");

        Number number = insert.execute(new MapSqlParameterSource()
                .addValue("order_id", orderId, Types.NUMERIC)
                .addValue("address", request.getDeliveryInfo().getAddress(), Types.VARCHAR)
                .addValue("date", request.getDeliveryInfo().getDate(), Types.DATE)
                .addValue("price", request.getPrice(), Types.NUMERIC));

    }

    @Override
    @Transactional
    public void saveOrderItems(CreateOrderRequest request, Long orderId) {
        for (CreateOrderRequest.ItemsInfo item : request.getItems()) {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("order_items");

            insert.execute(new MapSqlParameterSource()
                    .addValue("order_id", orderId, Types.NUMERIC)
                    .addValue("item_id", item.getItemId(), Types.NUMERIC)
                    .addValue("quantity", item.getQuantity(), Types.NUMERIC));
        }
    }

    @Override
    public Order getOrder(Long orderId) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(GET_ORDER_BY_ID_QUERY,
                new MapSqlParameterSource("id", orderId),
                BPRM_ORDER));
    }

    @Override
    public Order getOrderWithLock(Long orderId) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(GET_ORDER_BY_ID_WITH_LOCK_QUERY,
                new MapSqlParameterSource("id", orderId),
                BPRM_ORDER));
    }

    @Override
    public void updateBillingStatus(Long id, SagaStatus status) {
        namedParameterJdbcTemplate.update(UPDATE_BILLING_STATUS, new MapSqlParameterSource()
                .addValue("status", status.toString(), Types.VARCHAR)
                .addValue("id", id, Types.NUMERIC));
    }

    @Override
    public void failOrder(Long id, String message) {
        namedParameterJdbcTemplate.update(FAIL_ORDER, new MapSqlParameterSource()
                .addValue("status", SagaStatus.FAILED.toString(), Types.VARCHAR)
                .addValue("reason", message, Types.VARCHAR)
                .addValue("id", id, Types.NUMERIC));
    }

    @Override
    public void updateOrderStatus(Long id, SagaStatus prepared) {
        namedParameterJdbcTemplate.update(UPDATE_ORDER_STATUS, new MapSqlParameterSource()
                .addValue("status", prepared.toString(), Types.VARCHAR)
                .addValue("id", id, Types.NUMERIC));
    }

    @Override
    public void updateWarehouseStatus(Long id, SagaStatus status) {
        namedParameterJdbcTemplate.update(UPDATE_WAREHOUSE_STATUS, new MapSqlParameterSource()
                .addValue("status", status.toString(), Types.VARCHAR)
                .addValue("id", id, Types.NUMERIC));
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return namedParameterJdbcTemplate.query(GET_ORDERS,
                new MapSqlParameterSource("user_id", userId),
                BPRM_ORDER);
    }

    @Override
    public List<OrderInfo.ItemsInfo> getOrderItems(Long id) {
        return namedParameterJdbcTemplate.query(GET_ORDER_ITEMS,
                new MapSqlParameterSource("order_id", id),
                BPRM_ORDER_ITEM);
    }

    @Override
    public OrderDetails getOrderDetails(Long id) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(GET_ORDER_DETAILS,
                new MapSqlParameterSource("order_id", id),
                BPRM_ORDER_DETAILS));
    }
}
