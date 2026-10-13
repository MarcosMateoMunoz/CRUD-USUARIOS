package daos;

import entities.User;
import java.sql.SQLException;
import java.util.List;

/**
 * Operaciones de acceso a datos para la entidad {@link User}.
 */
public interface UserDAO {

    /**
     * Lista todos los usuarios.
     * @return lista de usuarios
     * @throws SQLException si falla la consulta
     */
    List<User> listAllUsers() throws SQLException;

    /**
     * Obtiene un usuario por su identificador.
     * @param id identificador
     * @return el usuario, o null si no existe
     * @throws SQLException si falla la consulta
     */
    User getUserById(Long id) throws SQLException;

    /**
     * Inserta un usuario nuevo.
     * @param user usuario a insertar
     * @throws SQLException si falla la inserción
     */
    void insertUser(User user) throws SQLException;

    /**
     * Actualiza un usuario existente.
     * @param user usuario con los datos nuevos (debe tener id)
     * @throws SQLException si falla la actualización
     */
    void updateUser(User user) throws SQLException;

    /**
     * Elimina un usuario.
     * @param id identificador del usuario
     * @throws SQLException si falla el borrado
     */
    void deleteUser(Long id) throws SQLException;

    /**
     * Comprueba si existe un usuario con ese username (sin distinguir mayúsculas).
     * @param username nombre de usuario
     * @return true si existe
     * @throws SQLException si falla la consulta
     */
    boolean existsUserByUsername(String username) throws SQLException;

    /**
     * Comprueba si existe otro usuario (distinto del id indicado) con ese username.
     * @param username nombre de usuario
     * @param id       id del usuario que se está editando
     * @return true si existe otro con ese username
     * @throws SQLException si falla la consulta
     */
    boolean existsUserByUsernameAndNotId(String username, Long id) throws SQLException;
}