package utils;                                               // Объявление пакета — класс относится к вспомогательным утилитам приложения

import java.security.MessageDigest;                           // Импорт класса для криптографического хеширования (SHA-256, MD5 и др.)
import java.security.NoSuchAlgorithmException;               // Импорт исключения, если запрошенный алгоритм хеширования не найден
import java.util.Base64;                                      // Импорт класса для кодирования/декодирования в Base64 (чтобы хеш был читаемым текстом)

public class PasswordHasher {                                // Публичный утилитарный класс для безопасного хеширования и проверки паролей

    /**
     * Хеширует переданный пароль с использованием алгоритма SHA-256
     * @param password открытый пароль в виде строки
     * @return строка в формате Base64 — хешированный пароль
     */
    public String hashPassword(String password) {
        try {                                                // Начинаем блок обработки исключения — алгоритм может быть недоступен
            MessageDigest digest = MessageDigest.getInstance("SHA-256");  // Создаём объект хеширования с алгоритмом SHA-256
            byte[] hash = digest.digest(password.getBytes());   // Преобразуем строку пароля в байты и хешируем их
            return Base64.getEncoder().encodeToString(hash);     // Кодируем бинарный хеш в безопасную текстовую строку Base64
        } catch (NoSuchAlgorithmException e) {               // Ловим редкое исключение — если JVM не поддерживает SHA-256
            throw new RuntimeException("SHA-256 не найден", e); // Оборачиваем в RuntimeException — это критическая ошибка системы
        }
    }

    /**
     * Проверяет, совпадает ли введённый пароль с сохранённым хешем
     * @param password введённый пользователем пароль (в открытом виде)
     * @param hash сохранённый ранее хеш пароля (в Base64)
     * @return true — если пароль верный, false — если нет
     */
    public boolean verifyPassword(String password, String hash) {
        return hashPassword(password).equals(hash);          // Хешируем введённый пароль и сравниваем с сохранённым хешем
        // Если хеши совпадают — пароль правильный (SHA-256 детерминирован: один вход → один выход)
    }
}