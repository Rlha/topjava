package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    //private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final RowMapper<List<User>> COLLECTION_ROW_MAPPER = new RowMapper<List<User>>() {
        @Override
        public List<User> mapRow(ResultSet resultSet, int i) throws SQLException {

            LinkedHashMap<Integer, User> map = new LinkedHashMap<>();

            do {
                if (resultSet!=null) {
                    int id = resultSet.getInt("id");
                    map.computeIfAbsent(id, userId -> {
                        User user = new User();
                        user.setRoles(new HashSet<>());
                        try {
                            user.setId(resultSet.getInt("id"));
                            user.setEmail(resultSet.getString("email"));
                            user.setName(resultSet.getString("name"));
                            user.setPassword(resultSet.getString("password"));
                            user.setRegistered(resultSet.getDate("registered"));
                            user.setEnabled(resultSet.getBoolean("enabled"));
                            user.setCaloriesPerDay(resultSet.getInt("calories_per_day"));
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        return user;
                    });
                    String role = resultSet.getString("role");
                    if (Objects.nonNull(role)) {
                        map.get(id).getRoles().add(Role.valueOf(role));
                    }
                }
            } while (resultSet.next());
            return map.values().stream().collect(Collectors.toList());
        }
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
        }

        ArrayList<Role> roles = new ArrayList<>(user.getRoles());

        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());

        String sql = "INSERT INTO user_roles " +
                "(user_id, role) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Role role = roles.get(i);
                ps.setInt(1, user.getId() );
                ps.setString(2, role.name());
            }
            @Override
            public int getBatchSize() {
                return roles.size();
            }
        });

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<List<User>> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id = ur.user_id WHERE id=?", COLLECTION_ROW_MAPPER, id);
        return DataAccessUtils.singleResult(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<List<User>> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id = ur.user_id WHERE email=?", COLLECTION_ROW_MAPPER, email);
        return DataAccessUtils.singleResult(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        List<List<User>> users = jdbcTemplate.query("SELECT * FROM users u LEFT JOIN user_roles ur ON u.id = ur.user_id ORDER BY name, email", COLLECTION_ROW_MAPPER);
        return DataAccessUtils.singleResult(users);
    }
}
