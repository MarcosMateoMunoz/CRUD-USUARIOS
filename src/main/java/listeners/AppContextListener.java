package listeners;



import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import daos.DataInitializer;
import daos.DatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener que inicializa la base de datos al arrancar la aplicación
 * y cierra la conexión al pararla.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    /**
     * Se ejecuta al arrancar la aplicación: carga data.sql.
     * @param sce evento del contexto
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Entrando en contextInitialized");
        DataInitializer.initialize();
        logger.info("Saliendo de contextInitialized");
    }

    /**
     * Se ejecuta al parar la aplicación: cierra la conexión.
     * @param sce evento del contexto
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Entrando en contextDestroyed");
        DatabaseConnectionManager.closeConnection();
        logger.info("Saliendo de contextDestroyed");
    }
}