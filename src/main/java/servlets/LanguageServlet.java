package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Locale;

/**
 * Servlet que cambia el idioma de la sesión del usuario.
 * Se invoca con /changeLanguage?lang=es (o en).
 */
@WebServlet("/changeLanguage")
public class LanguageServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LanguageServlet.class);

    /**
     * Guarda el locale elegido en la sesión y redirige a la página anterior.
     *
     * @param request  solicitud HTTP con el parámetro "lang"
     * @param response respuesta HTTP
     * @throws ServletException si falla el servlet
     * @throws IOException      si falla la E/S
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Entrando en doGet de LanguageServlet");

        String language = request.getParameter("lang");
        if (language != null) {
            Locale locale = "es".equals(language) ? Locale.forLanguageTag("es") : Locale.forLanguageTag("en");
            request.getSession().setAttribute("locale", locale);
            logger.info("Locale guardado en sesión: {}", locale);
        } else {
            logger.warn("No se ha recibido el parámetro 'lang'");
        }

        String referer = request.getHeader("Referer");
        if (referer != null) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
        logger.info("Saliendo de doGet de LanguageServlet");
    }
}