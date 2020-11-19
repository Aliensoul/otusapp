package arch.homework.notifications.repository.impl;

import arch.homework.notifications.entity.User;
import arch.homework.notifications.repository.MailRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.sql.Types;

@Component
@AllArgsConstructor
public class MailRepositoryImpl implements MailRepository {

    public static final String USER_EXISTS_QUERY = "select count(*) from users_mailboxes where user_id = :user_id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveMail(Long mailboxId, String mailText) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("mails")
                .usingGeneratedKeyColumns("id");

        insert.execute(new MapSqlParameterSource("text", mailText)
                .addValue("mailbox_id", mailboxId, Types.NUMERIC));
    }

    @Override
    public long createUser(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users_mailboxes")
                .usingGeneratedKeyColumns("id");

        return insert.execute(new MapSqlParameterSource("email", user.getEmail())
                .addValue("user_id", user.getId(), Types.NUMERIC));
    }

    @Override
    public boolean existsUsersAccount(User user) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(USER_EXISTS_QUERY,
                new MapSqlParameterSource("user_id", user.getId()),
                (rs, rn) -> rs.getBoolean(1)));
    }
}
