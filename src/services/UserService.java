package services;  // Объявляем, что класс находится в пакете services (бизнес-логика)

import models.User;                    // Импортируем модель пользователя
import utils.PasswordHasher;           // Импортируем утилиту для хеширования и проверки паролей
import utils.ValidationUtil;          // Импортируем утилиту для проверки корректности данных

public class UserService {             // Класс сервиса управления пользователями

    private DatabaseService databaseService;  // Поле: ссылка на сервис работы с "базой данных" (JSON-файлы)
    private PasswordHasher passwordHasher;     // Поле: объект для безопасной работы с паролями

    // Конструктор — получает зависимость от DatabaseService
    public UserService(DatabaseService databaseService) {
        this.databaseService = databaseService;        // Сохраняем ссылку на сервис БД
        this.passwordHasher = new PasswordHasher();    // Создаём новый объект для хеширования паролей
    }

    /**
     * Регистрация нового пользователя
     * Возвращает true — если регистрация прошла успешно, false — если данные невалидны или пользователь существует
     */
    public boolean registerUser(String username, String email, String password) {
        // Проверка валидности имени пользователя (длина, символы и т.д.)
        if (!ValidationUtil.isValidUsername(username)) {
            return false;                              // Имя пользователя не соответствует требованиям
        }
        // Проверка корректности email-адреса
        if (!ValidationUtil.isValidEmail(email)) {
            return false;                              // Email не прошёл проверку
        }
        // Проверка сложности и длины пароля
        if (!ValidationUtil.isValidPassword(password)) {
            return false;                              // Пароль слишком слабый
        }

        // Проверка, существует ли уже пользователь с таким именем
        if (databaseService.getUserByUsername(username) != null) {
            return false;                              // Такой username уже занят
        }

        // Генерируем следующий уникальный ID для нового пользователя
        int userId = getNextUserId();
        // Хешируем пароль (чтобы не хранить его в открытом виде)
        String passwordHash = passwordHasher.hashPassword(password);
        // Создаём объект нового пользователя
        User newUser = new User(userId, username, email, passwordHash);
        // Сохраняем пользователя в "базу данных" (файл users.json)
        databaseService.addUser(newUser);
        return true;                                   // Регистрация успешна
    }

    /**
     * Вход пользователя в систему
     * Возвращает объект User при успешном входе, иначе — null
     */
    public User loginUser(String username, String password) {
        // Ищем пользователя по имени в "базе данных"
        User user = databaseService.getUserByUsername(username);
        // Если пользователь найден и пароль совпадает (сравниваем хеши)
        if (user != null && passwordHasher.verifyPassword(password, user.getPasswordHash())) {
            return user;                               // Вход успешен — возвращаем пользователя
        }
        return null;                                   // Неверное имя или пароль
    }

    /**
     * Получить пользователя по его ID
     */
    public User getUserById(int id) {
        return databaseService.getUserById(id);        // Делегируем запрос в DatabaseService
    }

    /**
     * Проверить, существует ли пользователь с таким именем
     */
    public boolean userExists(String username) {
        return databaseService.getUserByUsername(username) != null; // Возвращает true, если найден
    }

    /**
     * Добавить или обновить оценку коктейля от пользователя
     */
    public void rateCocktail(int userId, int cocktailId, int rating) {
        // Проверяем, что оценка в допустимом диапазоне (1–5)
        if (rating >= 1 && rating <= 5) {
            // Сохраняем оценку в "базу данных"
            databaseService.saveUserRating(userId, cocktailId, rating);
        }
        // Если оценка вне диапазона — просто игнорируем (можно добавить логирование)
    }

    /**
     * Получить оценку, которую пользователь поставил конкретному коктейлю
     * Возвращает Integer (может быть null, если оценки нет)
     */
    public Integer getUserRating(int userId, int cocktailId) {
        return databaseService.getUserRating(userId, cocktailId); // Делегируем в DatabaseService
    }

    /**
     * Удалить пользователя по ID (не используется в текущем UI, но может пригодиться)
     */
    public void deleteUser(int id) {
        databaseService.deleteUser(id);                // Удаляем из "базы данных"
    }

    /**
     * Обновить данные профиля пользователя (например, email)
     */
    public void updateUserProfile(User user) {
        databaseService.updateUser(user);              // Сохраняем изменённого пользователя
    }

    // Вспомогательный приватный метод — генерирует следующий ID для нового пользователя
    private int getNextUserId() {
        return databaseService.getAllUsers().stream()  // Получаем поток всех пользователей
                .mapToInt(User::getId)                 // Преобразуем в поток ID
                .max()                                 // Находим максимальный ID
                .orElse(0)                             // Если пользователей нет — возвращаем 0
                + 1;                                   // Увеличиваем на 1 — это будет новый ID
    }
}