package arch.homework.billing.repository.imp;

import arch.homework.billing.entity.Operation;
import arch.homework.billing.repository.OperationsRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Types;

@Repository
@AllArgsConstructor
public class OperationsRepositoryImpl implements OperationsRepository {

    private final static String ORDER_BILLED_QUERY = "select count(*) from order_operations o where o.order_id = :order_id";
    private final static String GET_OPERATION_BY_ORDER = "select * from order_operations o where o.order_id = :order_id for update";
    private final static String UPDATE_OPERATION_STATUS = "update order_operations set status = :status where order_id = :order_id";

    private final static BeanPropertyRowMapper<Operation> BPRM_OPERATION = BeanPropertyRowMapper.newInstance(Operation.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public boolean orderBilled(Long orderId) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(ORDER_BILLED_QUERY,
                new MapSqlParameterSource("order_id", orderId),
                (rs, rn) -> rs.getBoolean(1)));
    }

    @Override
    public void saveOperation(Long accountId, Long orderId, BigDecimal amount, String status) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("order_operations");

        insert.execute(new MapSqlParameterSource().addValue("order_id", orderId, Types.NUMERIC)
                .addValue("account_id", accountId, Types.NUMERIC)
                .addValue("amount", amount, Types.NUMERIC)
                .addValue("status", status, Types.VARCHAR));
    }

    @Override
    public Operation getOperationByOrderId(Long orderId) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(GET_OPERATION_BY_ORDER,
                new MapSqlParameterSource("order_id", orderId),
                BPRM_OPERATION));
    }

    @Override
    public void updateOperationStatus(Long orderId, String status) {
        namedParameterJdbcTemplate.update(UPDATE_OPERATION_STATUS, new MapSqlParameterSource("status", status)
                .addValue("order_id", orderId));
    }
}
