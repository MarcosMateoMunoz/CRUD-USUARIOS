package daos;

import entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación JDBC de {@link UserDAO}.
 */
public class UserDAOImpl implements UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

    /**
     * Convierte un Timestamp en LocalDateTime, admitiendo null.
     * @param ts timestamp (puede ser null)
     * @return fecha local, o null si el timestamp era null
     */
    private LocalDateTime toLocal(Timestamp ts) {
        return ts == null ? null : ts.toLocalDateTime();
    }

    /**
     * Convierte la fila actual del ResultSet en un {@link User}.
     * @param rs resultado posicionado en una fila
     * @return usuario construido
     * @throws SQLException si falla la lectura
     */
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("passwordHash"),
                rs.getBoolean("active"),
                rs.getBoolean("accountNonLocked"),
                toLocal(rs.getTimestamp("lastPasswordChange")),
                toLocal(rs.getTimestamp("passwordExpiresAt")),
                rs.getInt("failedLoginAttempts"),
                rs.getBoolean("emailVerified"),
                rs.getBoolean("mustChangePassword"));
    }

    /**
     * Rellena los 9 parámetros comunes a INSERT y UPDATE.
     * @param ps prepared statement
     * @param u  usuario con los datos
     * @throws SQLException si falla el seteo
     */
    private void fillStatement(PreparedStatement ps, User u) throws SQLException {
        ps.setString(1, u.getUsername());
        ps.setString(2, u.getPasswordHash());
        ps.setBoolean(3, u.getActive());
        ps.setBoolean(4, u.getAccountNonLocked());
        ps.setTimestamp(5, Timestamp.valueOf(u.getLastPasswordChange()));
        ps.setTimestamp(6, Timestamp.valueOf(u.getPasswordExpiresAt()));
        ps.setInt(7, u.getFailedLoginAttempts());
        ps.setBoolean(8, u.getEmailVerified());
        ps.setBoolean(9, u.getMustChangePassword());
    }

    /** {@inheritDoc} */
    @Override
    public List<User> listAllUsers() throws SQLException {
        logger.info("Entrando en listAllUsers");
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection c = DatabaseConnectionManager.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        }
        logger.info("Saliendo de listAllUsers: {} usuarios", users.size());
        return users;
    }

    /** {@inheritDoc} */
    @Override
    public User getUserById(Long id) throws SQLException {
        logger.info("Entrando en getUserById id={}", id);
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = null;
        try (Connection c = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = mapRow(rs);
                }
            }
        }
        logger.info("Saliendo de getUserById, encontrado={}", user != null);
        return user;
    }

    /** {@inheritDoc} */
    @Override
    public void insertUser(User user) throws SQLException {
        logger.info("Entrando en insertUser username={}", user.getUsername());
        String sql = "INSERT INTO users (username, passwordHash, active, accountNonLocked, "
                + "lastPasswordChange, passwordExpiresAt, failedLoginAttempts, "
                + "emailVerified, mustChangePassword) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection c = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            fillStatement(ps, user);
            ps.executeUpdate();
        }
        logger.info("Saliendo de insertUser");
    }

    /** {@inheritDoc} */
    @Override
    public void updateUser(User user) throws SQLException {
        logger.info("Entrando en updateUser id={}", user.getId());
        String sql = "UPDATE users SET username=?, passwordHash=?, active=?, accountNonLocked=?, "
                + "lastPasswordChange=?, passwordExpiresAt=?, failedLoginAttempts=?, "
                + "emailVerified=?, mustChangePassword=? WHERE id=?";
        try (Connection c = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            fillStatement(ps, user);
            ps.setLong(10, user.getId());
            ps.executeUpdate();
        }
        logger.info("Saliendo de updateUser");
    }

    /** {@inheritDoc} */
    @Override
    public void deleteUser(Long id) throws SQLException {
        logger.info("Entrando en deleteUser id={}", id);
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection c = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
        logger.info("Saliendo de deleteUser");
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsUserByUsername(String username) throws SQLException {
        logger.info("Entrando en existsUserByUsername username={}", username);
        String sql = "SELECT COUNT(*) FROM users WHERE UPPER(username) = ?";
        try (Connection c = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username.toUpperCase());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                boolean exists = rs.getInt(1) > 0;
                logger.info("Saliendo de existsUserByUsername: {}", exists);
                return exists;
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsUserByUsernameAndNotId(String username, Long id) throws SQLException {
        logger.info("Entrando en existsUserByUsernameAndNotId username={}, id={}", username, id);
        String sql = "SELECT COUNT(*) FROM users WHERE UPPER(username) = ? AND id <> ?";
        try (Connection c = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username.toUpperCase());
            ps.setLong(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                boolean exists = rs.getInt(1) > 0;
                logger.info("Saliendo de existsUserByUsernameAndNotId: {}", exists);
                return exists;
            }
        }
    }
}