package arch.homework.warehouse.repository.impl;

import arch.homework.warehouse.entity.*;
import arch.homework.warehouse.repository.WarehouseRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.Types;
import java.util.List;

@Component
@AllArgsConstructor
public class WarehouseRepositoryImpl implements WarehouseRepository {

    private static final String RESTOCK_ITEMS_SQL = "update items set quantity = quantity + :quantity where id = :id";
    private static final String EXIST_RESERVATION = "select count(*) from ordered_items where order_id = :order_id";
    private static final String GET_ITEM_WITH_LOCK = "select * from items where id = :id for update";
    private static final String GET_ITEM_BY_ID = "select * from items where id = :id";
    private static final String GET_ITEMS_BY_CATEGORY = "select * from items where category = :category";
    private static final String GET_ITEM_BY_NAME = "select * from items where name = :name";
    private static final String GET_ORDERED_ITEMS = "select * from ordered_items where order_id = :order_id for update";
    private static final String CHANGE_STATUS_ORDERED_ITEMS = "update ordered_items set status = :status where order_id = :order_id";

    private static final BeanPropertyRowMapper<Item> BPRM_ITEM = BeanPropertyRowMapper.newInstance(Item.class);
    private static final BeanPropertyRowMapper<OrderedItem> BPRM_ORDERED_ITEM = BeanPropertyRowMapper.newInstance(OrderedItem.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public void addNewItem(AddItem item) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("items")
                .usingGeneratedKeyColumns("id");

        insert.execute(new MapSqlParameterSource()
                .addValue("name", item.getName(), Types.VARCHAR)
                .addValue("quantity", item.getQuantity(), Types.NUMERIC)
                .addValue("category", item.getCategory(), Types.VARCHAR)
                .addValue("price", item.getPrice(), Types.NUMERIC));
    }

    @Override
    public void restockItems(RestockItemsRequest restockItemsRequest) {
        namedParameterJdbcTemplate.update(RESTOCK_ITEMS_SQL, new MapSqlParameterSource()
                .addValue("quantity", restockItemsRequest.getQuantity(), Types.NUMERIC)
                .addValue("id", restockItemsRequest.getItemId(), Types.NUMERIC));
    }

    @Override
    public boolean orderItemsReserved(Long orderId) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(EXIST_RESERVATION,
                new MapSqlParameterSource("order_id", orderId),
                (rs, rn) -> rs.getBoolean(1)));
    }

    @Override
    public Item getItemWithLock(Long id) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(GET_ITEM_WITH_LOCK,
                new MapSqlParameterSource("id", id),
                BPRM_ITEM));
    }

    @Override
    public void reserveItem(Long itemId, Long orderId, Long quantity) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("ordered_items");

        insert.execute(new MapSqlParameterSource()
                .addValue("order_id", orderId, Types.NUMERIC)
                .addValue("item_id", itemId, Types.NUMERIC)
                .addValue("quantity", quantity, Types.NUMERIC)
                .addValue("status", OrderWarehouseStatus.PREPARED, Types.VARCHAR));
    }

    @Override
    public void decreaseItemQuantity(Long itemId, Long quantity) {
        namedParameterJdbcTemplate.update(RESTOCK_ITEMS_SQL, new MapSqlParameterSource()
                .addValue("id", itemId, Types.NUMERIC)
                .addValue("quantity", quantity * -1, Types.NUMERIC));
    }

    @Override
    public List<OrderedItem> getOrderedItems(Long orderId) {
        return namedParameterJdbcTemplate.query(GET_ORDERED_ITEMS,
                new MapSqlParameterSource("order_id", orderId),
                BPRM_ORDERED_ITEM);
    }

    @Override
    public void changeReservationStatus(Long orderId, String status) {
        namedParameterJdbcTemplate.update(CHANGE_STATUS_ORDERED_ITEMS,
                new MapSqlParameterSource("order_id", orderId)
                        .addValue("status", status));
    }

    @Override
    public Item getItemById(Long itemId) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(GET_ITEM_BY_ID,
                new MapSqlParameterSource("id", itemId),
                BPRM_ITEM));
    }

    @Override
    public List<Item> getItemsByCategory(String itemCategory) {
        return namedParameterJdbcTemplate.query(GET_ITEMS_BY_CATEGORY,
                new MapSqlParameterSource("category", itemCategory),
                BPRM_ITEM);
    }

    @Override
    public Item getItemsByName(String itemName) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(GET_ITEM_BY_NAME,
                new MapSqlParameterSource("name", itemName),
                BPRM_ITEM));
    }
}
