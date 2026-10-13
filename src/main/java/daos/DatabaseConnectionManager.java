package daos;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión JDBC a MariaDB leyendo las credenciales del archivo .env.
 */
public class DatabaseConnectionManager {

    /** Conexión compartida; se reabre automáticamente si está cerrada. */
    private static Connection connection = null;

    /** Logger para trazar eventos usando SLF4J. */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionManager.class);

    /** Variables del archivo .env. */
    private static final Dotenv dotenv = Dotenv.load();

    /** Constructor privado para evitar la creación de instancias. */
    private DatabaseConnectionManager() {}

    /**
     * Devuelve una conexión abierta a la base de datos.
     *
     * @return conexión JDBC abierta
     * @throws RuntimeException si no se puede conectar
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                logger.info("Iniciando la conexión a la base de datos MariaDB...");
                String dbUrl = dotenv.get("DB_URL");
                String dbUser = dotenv.get("DB_USER");
                String dbPassword = dotenv.get("DB_PASSWORD");
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                logger.info("Conexión a la base de datos establecida exitosamente.");
            }
        } catch (SQLException e) {
            logger.error("Error al conectar con la base de datos: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo conectar a la base de datos.", e);
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                logger.info("Cerrando la conexión a la base de datos...");
                connection.close();
                logger.info("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión a la base de datos: {}", e.getMessage(), e);
            }
        }
    }
}