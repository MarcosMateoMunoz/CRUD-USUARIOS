package daos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Ejecuta el script data.sql (crea tablas y carga datos de ejemplo).
 */
public class DataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Lee data.sql del classpath y ejecuta cada sentencia separada por ';'.
     * No debe haber ';' dentro de los textos de los INSERT.
     */
    public static void initialize() {
        logger.info("Entrando en DataInitializer.initialize");
        try (InputStream in = DataInitializer.class.getClassLoader().getResourceAsStream("data.sql")) {
            if (in == null) {
                logger.error("No se encontró data.sql en el classpath");
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String script = reader.lines()
                    .filter(l -> !l.trim().startsWith("--"))
                    .collect(Collectors.joining("\n"));

            try (Connection conn = DatabaseConnectionManager.getConnection();
                 Statement st = conn.createStatement()) {
                for (String sql : script.split(";")) {
                    if (!sql.isBlank()) {
                        st.execute(sql.trim());
                    }
                }
            }
            logger.info("data.sql ejecutado correctamente");
        } catch (IOException | SQLException e) {
            logger.error("Error al ejecutar data.sql", e);
        }
        logger.info("Saliendo de DataInitializer.initialize");
    }
}