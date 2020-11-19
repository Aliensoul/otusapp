package arch.homework.billing.repository.imp;

import arch.homework.billing.entity.Account;
import arch.homework.billing.entity.MakeReplenishmentRequest;
import arch.homework.billing.entity.User;
import arch.homework.billing.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Types;

@AllArgsConstructor
@Component
public class AccountRepositoryImpl implements AccountRepository {

    private final static String USER_EXISTS_QUERY = "select count(*) from accounts where user_id = :user_id";
    private final static String GET_USERS_ACCOUNT = "select * from accounts where user_id = :user_id";
    private final static String LOCK_FOR_PAYMENT = "select * from accounts where user_id = :user_id for update";
    private final static String MAKE_REPLENISHMENT = "update accounts a set balance = a.balance + :amount where user_id = :user_id";
    private final static String PAY = "update accounts a set balance = a.balance - :amount where id = :id";

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static BeanPropertyRowMapper<Account> ACCOUNT_BPRM = BeanPropertyRowMapper.newInstance(Account.class);

    @Override
    public Long createAccount(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("accounts")
                .usingGeneratedKeyColumns("id");

        Number key = insert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("user_id", user.getId(), Types.NUMERIC)
                .addValue("balance", 0, Types.NUMERIC));
        return (long) key;
    }

    @Override
    public boolean existsUsersAccount(User user) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(USER_EXISTS_QUERY,
                new MapSqlParameterSource("user_id", user.getId()),
                (rs, rn) -> rs.getBoolean(1)));
    }

    @Override
    public Account getUsersAccount(Long userId) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(GET_USERS_ACCOUNT,
                new MapSqlParameterSource("user_id", userId),
                ACCOUNT_BPRM));
    }

    @Override
    public void makeReplenishment(MakeReplenishmentRequest request, Long userId) {
        namedParameterJdbcTemplate.update(MAKE_REPLENISHMENT,
                new MapSqlParameterSource().addValue("amount", request.getAmount(), Types.NUMERIC)
                        .addValue("user_id", userId, Types.NUMERIC));
    }

    @Override
    public void pay(Long accountId, BigDecimal amount) {
        namedParameterJdbcTemplate.update(PAY,
                new MapSqlParameterSource().addValue("amount", amount, Types.NUMERIC)
                        .addValue("id", accountId, Types.NUMERIC));
    }

    @Override
    public Account lockAccountForPayment(Long userId) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(LOCK_FOR_PAYMENT,
                new MapSqlParameterSource("user_id", userId), ACCOUNT_BPRM));
    }
}
