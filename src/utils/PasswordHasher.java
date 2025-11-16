package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordHasher {
    /**
     * Хеширование пароля с использованием SHA-256
     */
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 не найден", e);
        }
    }

    /**
     * Проверка пароля
     */
    public boolean verifyPassword(String password, String hash) {
        return hashPassword(password).equals(hash);
    }
}
