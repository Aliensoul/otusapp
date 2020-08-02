package arch.homework.otusapp.repository.impl;

import arch.homework.otusapp.entity.User;
import arch.homework.otusapp.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@AllArgsConstructor
@Repository
public class UsersRepositoryImpl implements UsersRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<User> BPRM_USER = BeanPropertyRowMapper.newInstance(User.class);

    private static final String SELECT_USER_BY_ID = "select * from users u where u.id = :id";
    private static final String DELETE_USER_BY_ID = "delete from users u where u.id = :id";
    private static final String CREATE_USER = "insert into users (id, username, first_name, last_name, email, phone)" +
            " values (:id, :username, :firstName, :lastName, :email, :phone)";
    private static final String UPDATE_USER = "update users set username = coalesce(:username, username)," +
            "first_name = coalesce(:firstName, first_name)," +
            "last_name = coalesce(:lastName, last_name)," +
            "email = coalesce(:email, email)," +
            "phone = coalesce(:phone, phone)" +
            "  where id = :id";

    public User getUser(Long id) {
        return DataAccessUtils.singleResult(jdbcTemplate.query(SELECT_USER_BY_ID,
                new MapSqlParameterSource().addValue("id", id, Types.NUMERIC),
                BPRM_USER));
    }

    @Override
    public User createUser(User user) {
        jdbcTemplate.update(CREATE_USER,
                new MapSqlParameterSource()
                        .addValue("id", user.getId(), Types.NUMERIC)
                        .addValue("username", user.getUsername(), Types.VARCHAR)
                        .addValue("firstName", user.getFirstName(), Types.VARCHAR)
                        .addValue("lastName", user.getLastName(), Types.VARCHAR)
                        .addValue("email", user.getEmail(), Types.VARCHAR)
                        .addValue("phone", user.getPhone(), Types.VARCHAR));
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        jdbcTemplate.update(DELETE_USER_BY_ID,
                new MapSqlParameterSource()
                        .addValue("id", id, Types.NUMERIC));
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update(UPDATE_USER,
                new MapSqlParameterSource()
                        .addValue("id", user.getId(), Types.NUMERIC)
                        .addValue("username", user.getUsername(), Types.VARCHAR)
                        .addValue("firstName", user.getFirstName(), Types.VARCHAR)
                        .addValue("lastName", user.getLastName(), Types.VARCHAR)
                        .addValue("email", user.getEmail(), Types.VARCHAR)
                        .addValue("phone", user.getPhone(), Types.VARCHAR));
        return user;
    }

}
