package arch.homework.authserver.repository.impl;

import arch.homework.authserver.entity.User;
import arch.homework.authserver.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@AllArgsConstructor
@Repository
public class UsersRepositoryImpl implements UsersRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> BPRM_USER = BeanPropertyRowMapper.newInstance(User.class);

    private static final String SELECT_USER_BY_ID = "select * from users u where u.id = :id";
    private static final String SELECT_USER_BY_CREDS = "select * from users u where u.username = :username and u.password = :password";
    private static final String DELETE_USER_BY_ID = "delete from users u where u.id = :id";
    /*private static final String CREATE_USER = "insert into users (id, username, first_name, last_name, email, phone)" +
            " values (:id, :username, :firstName, :lastName, :email, :phone)";*/
    private static final String UPDATE_USER = "update users set username = coalesce(:username, username)," +
            "password = coalesce(:password, password)," +
            "first_name = coalesce(:firstName, first_name)," +
            "last_name = coalesce(:lastName, last_name)," +
            "email = coalesce(:email, email)," +
            "phone = coalesce(:phone, phone)" +
            "  where id = :id";

    public User getUser(Long id) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(SELECT_USER_BY_ID,
                new MapSqlParameterSource().addValue("id", id, Types.NUMERIC),
                BPRM_USER));
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Number key = insert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("id", user.getId(), Types.NUMERIC)
                .addValue("username", user.getUsername(), Types.VARCHAR)
                .addValue("password", user.getPassword(), Types.VARCHAR)
                .addValue("firstName", user.getFirstName(), Types.VARCHAR)
                .addValue("lastName", user.getLastName(), Types.VARCHAR)
                .addValue("email", user.getEmail(), Types.VARCHAR)
                .addValue("phone", user.getPhone(), Types.VARCHAR));
        return user.setId((Long) key);
    }

    @Override
    public void deleteUser(Long id) {
        namedParameterJdbcTemplate.update(DELETE_USER_BY_ID,
                new MapSqlParameterSource()
                        .addValue("id", id, Types.NUMERIC));
    }

    @Override
    public User updateUser(User user) {
        namedParameterJdbcTemplate.update(UPDATE_USER,
                new MapSqlParameterSource()
                        .addValue("id", user.getId(), Types.NUMERIC)
                        .addValue("username", user.getUsername(), Types.VARCHAR)
                        .addValue("password", user.getPassword(), Types.VARCHAR)
                        .addValue("firstName", user.getFirstName(), Types.VARCHAR)
                        .addValue("lastName", user.getLastName(), Types.VARCHAR)
                        .addValue("email", user.getEmail(), Types.VARCHAR)
                        .addValue("phone", user.getPhone(), Types.VARCHAR));
        return user;
    }

    @Override
    public User getUserByCreds(String username, String password) {
        return DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(SELECT_USER_BY_CREDS,
                new MapSqlParameterSource().addValue("username", username, Types.VARCHAR)
                        .addValue("password", password, Types.VARCHAR),
                BPRM_USER));
    }

}
