-- Crear tabla users si no existe
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     username VARCHAR(40) NOT NULL UNIQUE,
    passwordHash VARCHAR(500) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    accountNonLocked BOOLEAN NOT NULL DEFAULT TRUE,
    lastPasswordChange DATETIME NULL,
    passwordExpiresAt DATETIME NULL,
    failedLoginAttempts INT DEFAULT 0,
    emailVerified BOOLEAN NOT NULL DEFAULT FALSE,
    mustChangePassword BOOLEAN NOT NULL DEFAULT FALSE
    );

-- Ejemplos de inserts con IGNORE y id fijado
INSERT IGNORE INTO users (
    id, username, passwordHash, active, accountNonLocked,
    lastPasswordChange, passwordExpiresAt, failedLoginAttempts,
    emailVerified, mustChangePassword
) VALUES
(1, 'admin', 'admin123', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, FALSE),
(2, 'jdoe', '1234', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 1, FALSE, FALSE),
(3, 'maria', 'changeme', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, TRUE),
(4, 'blockeduser', 'secret', FALSE, FALSE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 5, FALSE, FALSE);