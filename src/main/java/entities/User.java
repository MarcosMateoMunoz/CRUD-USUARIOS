package entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa a un usuario (tabla {@code users}).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /** Identificador único. */
    private Long id;

    /** Nombre de usuario (único, máximo 40 caracteres). */
    private String username;

    /** Contraseña (de momento en texto plano). */
    private String passwordHash;

    /** Indica si la cuenta está activa. */
    private Boolean active;

    /** Indica si la cuenta NO está bloqueada por intentos fallidos. */
    private Boolean accountNonLocked;

    /** Fecha del último cambio de contraseña. */
    private LocalDateTime lastPasswordChange;

    /** Fecha de caducidad de la contraseña (lastPasswordChange + 3 meses). */
    private LocalDateTime passwordExpiresAt;

    /** Número de intentos de login fallidos. */
    private Integer failedLoginAttempts;

    /** Indica si el correo fue verificado. */
    private Boolean emailVerified;

    /** Fuerza el cambio de contraseña en el próximo login. */
    private Boolean mustChangePassword;

    /**
     * Constructor sin {@code id}, para usuarios aún no insertados.
     *
     * @param username            nombre de usuario
     * @param passwordHash        contraseña
     * @param active              cuenta activa
     * @param accountNonLocked    cuenta no bloqueada
     * @param lastPasswordChange  fecha del último cambio de contraseña
     * @param passwordExpiresAt   fecha de caducidad
     * @param failedLoginAttempts intentos fallidos
     * @param emailVerified       correo verificado
     * @param mustChangePassword  debe cambiar la contraseña
     */
    public User(String username, String passwordHash, Boolean active, Boolean accountNonLocked,
                LocalDateTime lastPasswordChange, LocalDateTime passwordExpiresAt,
                Integer failedLoginAttempts, Boolean emailVerified, Boolean mustChangePassword) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.active = active;
        this.accountNonLocked = accountNonLocked;
        this.lastPasswordChange = lastPasswordChange;
        this.passwordExpiresAt = passwordExpiresAt;
        this.failedLoginAttempts = failedLoginAttempts;
        this.emailVerified = emailVerified;
        this.mustChangePassword = mustChangePassword;
    }
}