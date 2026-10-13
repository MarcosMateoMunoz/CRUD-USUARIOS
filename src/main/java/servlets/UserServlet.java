package servlets;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import daos.UserDAO;
import daos.UserDAOImpl;
import entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Servlet que gestiona el CRUD de {@link User} en la URL {@code /users}.
 */
@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(UserServlet.class);

    /** DAO para acceder a la tabla users. */
    private UserDAO userDAO;

    /**
     * Inicializa el DAO.
     * @throws ServletException si falla la inicialización
     */
    @Override
    public void init() throws ServletException {
        logger.info("Entrando en init de UserServlet");
        userDAO = new UserDAOImpl();
        logger.info("Saliendo de init de UserServlet");
    }

    /**
     * Gestiona las peticiones GET: list (por defecto), new y edit.
     * @param request  solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si falla el servlet
     * @throws IOException      si falla la E/S
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Entrando en doGet de UserServlet");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "new":
                    request.getRequestDispatcher("/views/user/user-form.jsp").forward(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (SQLException e) {
            logger.error("Error SQL en doGet", e);
            throw new ServletException(e);
        }
        logger.info("Saliendo de doGet de UserServlet");
    }

    /**
     * Gestiona las peticiones POST: insert, update y delete.
     * @param request  solicitud HTTP
     * @param response respuesta HTTP
     * @throws ServletException si falla el servlet
     * @throws IOException      si falla la E/S
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Entrando en doPost de UserServlet");
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "insert":
                    insertUser(request, response);
                    break;
                case "update":
                    updateUser(request, response);
                    break;
                case "delete":
                    deleteUser(request, response);
                    break;
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (SQLException e) {
            logger.error("Error SQL en doPost", e);
            throw new ServletException(e);
        }
        logger.info("Saliendo de doPost de UserServlet");
    }

    /**
     * Carga todos los usuarios y muestra la lista.
     * @param request  solicitud HTTP
     * @param response respuesta HTTP
     * @throws SQLException     si falla la consulta
     * @throws ServletException si falla el servlet
     * @throws IOException      si falla la E/S
     */
    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        logger.info("Entrando en listUsers");
        List<User> listUsers = userDAO.listAllUsers();
        request.setAttribute("listUsers", listUsers);
        request.getRequestDispatcher("/views/user/user-list.jsp").forward(request, response);
        logger.info("Saliendo de listUsers");
    }

    /**
     * Carga un usuario por id y muestra el formulario de edición.
     * @param request  solicitud HTTP
     * @param response respuesta HTTP
     * @throws SQLException     si falla la consulta
     * @throws ServletException si falla el servlet
     * @throws IOException      si falla la E/S
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        logger.info("Entrando en showEditForm");
        Long id = Long.valueOf(request.getParameter("id"));
        User user = userDAO.getUserById(id);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/views/user/user-form.jsp").forward(request, response);
        logger.info("Saliendo de showEditForm");
    }

    /**
     * Valida e inserta un usuario nuevo.
     * @param request  solicitud HTTP con los datos del formulario
     * @param response respuesta HTTP
     * @throws SQLException     si falla la inserción
     * @throws ServletException si falla el servlet
     * @throws IOException      si falla la E/S
     */
    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        logger.info("Entrando en insertUser");

        User user = buildUserFromRequest(request);
        if (user == null) {
            forwardToForm(request, response, null, "msg.user-form.error.format");
            return;
        }

        if (hasEmptyRequiredFields(request)) {
            forwardToForm(request, response, user, "msg.user-form.error.empty");
            return;
        }

        if (userDAO.existsUserByUsername(user.getUsername())) {
            logger.warn("Username duplicado: {}", user.getUsername());
            forwardToForm(request, response, user, "msg.user-form.error.duplicate");
            return;
        }

        // Cálculo en servidor: caducidad = último cambio + 3 meses
        user.setPasswordExpiresAt(user.getLastPasswordChange().plusMonths(3));

        userDAO.insertUser(user);
        logger.info("Usuario insertado: {}", user.getUsername());
        response.sendRedirect("users");
        logger.info("Saliendo de insertUser");
    }

    /**
     * Valida y actualiza un usuario existente.
     * @param request  solicitud HTTP con los datos del formulario
     * @param response respuesta HTTP
     * @throws SQLException     si falla la actualización
     * @throws ServletException si falla el servlet
     * @throws IOException      si falla la E/S
     */
    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        logger.info("Entrando en updateUser");

        User user = buildUserFromRequest(request);
        if (user == null) {
            forwardToForm(request, response, null, "msg.user-form.error.format");
            return;
        }
        user.setId(Long.valueOf(request.getParameter("id")));

        if (hasEmptyRequiredFields(request)) {
            forwardToForm(request, response, user, "msg.user-form.error.empty");
            return;
        }

        if (userDAO.existsUserByUsernameAndNotId(user.getUsername(), user.getId())) {
            logger.warn("Username duplicado en update: {}", user.getUsername());
            forwardToForm(request, response, user, "msg.user-form.error.duplicate");
            return;
        }

        // Cálculo en servidor: caducidad = último cambio + 3 meses
        user.setPasswordExpiresAt(user.getLastPasswordChange().plusMonths(3));

        userDAO.updateUser(user);
        logger.info("Usuario actualizado id={}", user.getId());
        response.sendRedirect("users");
        logger.info("Saliendo de updateUser");
    }

    /**
     * Elimina un usuario.
     * @param request  solicitud HTTP con el id
     * @param response respuesta HTTP
     * @throws SQLException si falla el borrado
     * @throws IOException  si falla la E/S
     */
    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        logger.info("Entrando en deleteUser");
        Long id = Long.valueOf(request.getParameter("id"));
        userDAO.deleteUser(id);
        logger.info("Usuario eliminado id={}", id);
        response.sendRedirect("users");
        logger.info("Saliendo de deleteUser");
    }

    /**
     * Comprueba los campos obligatorios usando un array.
     * @param request solicitud HTTP
     * @return true si algún campo obligatorio está vacío
     */
    private boolean hasEmptyRequiredFields(HttpServletRequest request) {
        logger.info("Entrando en hasEmptyRequiredFields");
        String[] requiredFields = {
                request.getParameter("username"),
                request.getParameter("passwordHash"),
                request.getParameter("lastPasswordChange")
        };
        for (String field : requiredFields) {
            if (field == null || field.isBlank()) {
                logger.warn("Validación fallida: campo obligatorio vacío");
                return true;
            }
        }
        logger.info("Saliendo de hasEmptyRequiredFields: todo correcto");
        return false;
    }

    /**
     * Construye un {@link User} a partir de los parámetros del formulario.
     * Si la fecha viene vacía se deja a null; la validación de campos vacíos
     * la detecta después, antes de calcular la caducidad.
     * @param request solicitud HTTP
     * @return el usuario, o null si hay un error de formato (fecha o número)
     */
    private User buildUserFromRequest(HttpServletRequest request) {
        logger.info("Entrando en buildUserFromRequest");
        try {
            String username = trim(request.getParameter("username"));
            String passwordHash = trim(request.getParameter("passwordHash"));
            Boolean active = Boolean.valueOf(request.getParameter("active"));
            Boolean accountNonLocked = Boolean.valueOf(request.getParameter("accountNonLocked"));
            Boolean emailVerified = Boolean.valueOf(request.getParameter("emailVerified"));
            Boolean mustChangePassword = Boolean.valueOf(request.getParameter("mustChangePassword"));

            String lpc = request.getParameter("lastPasswordChange");
            LocalDateTime lastPasswordChange =
                    (lpc == null || lpc.isBlank()) ? null : LocalDateTime.parse(lpc);

            String fla = request.getParameter("failedLoginAttempts");
            Integer failedLoginAttempts =
                    (fla == null || fla.isBlank()) ? Integer.valueOf(0) : Integer.valueOf(fla.trim());
            if (failedLoginAttempts < 0) {
                logger.warn("Intentos fallidos negativos: {}", failedLoginAttempts);
                return null;
            }

            logger.info("Saliendo de buildUserFromRequest");
            return new User(username, passwordHash, active, accountNonLocked,
                    lastPasswordChange, null, failedLoginAttempts, emailVerified, mustChangePassword);
        } catch (DateTimeParseException | NumberFormatException e) {
            logger.error("Error de formato al leer el formulario", e);
            return null;
        }
    }

    /**
     * Elimina espacios laterales; devuelve cadena vacía si es null.
     * @param s texto original
     * @return texto sin espacios laterales
     */
    private String trim(String s) {
        return s == null ? "" : s.trim();
    }

    /**
     * Vuelve al formulario con un mensaje de error (clave i18n).
     * Si no hay usuario (error de formato) se conserva al menos el id,
     * para no convertir una edición en una inserción.
     * @param request  solicitud HTTP
     * @param response respuesta HTTP
     * @param user     usuario con lo escrito (puede ser null)
     * @param errorKey clave del mensaje de error
     * @throws ServletException si falla el servlet
     * @throws IOException      si falla la E/S
     */
    private void forwardToForm(HttpServletRequest request, HttpServletResponse response,
                               User user, String errorKey) throws ServletException, IOException {
        logger.info("Volviendo al formulario con error: {}", errorKey);
        if (user == null) {
            user = new User();
            String id = request.getParameter("id");
            if (id != null && !id.isBlank()) {
                try {
                    user.setId(Long.valueOf(id));
                } catch (NumberFormatException e) {
                    logger.warn("Id no válido: {}", id);
                }
            }
        }
        request.setAttribute("user", user);
        request.setAttribute("errorKey", errorKey);
        request.getRequestDispatcher("/views/user/user-form.jsp").forward(request, response);
    }
}