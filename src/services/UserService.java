package services;

import models.User;
import utils.PasswordHasher;
import utils.ValidationUtil;

public class UserService {
    private DatabaseService databaseService;
    private PasswordHasher passwordHasher;

    public UserService(DatabaseService databaseService) {
        this.databaseService = databaseService;
        this.passwordHasher = new PasswordHasher();
    }

    /**
     * Регистрация нового пользователя
     */
    public boolean registerUser(String username, String email, String password) {
        // Проверка валидности
        if (!ValidationUtil.isValidUsername(username)) {
            return false;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            return false;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            return false;
        }

        // Проверка уникальности
        if (databaseService.getUserByUsername(username) != null) {
            return false; // Пользователь с таким username уже существует
        }

        // Создание и сохранение пользователя
        int userId = getNextUserId();
        String passwordHash = passwordHasher.hashPassword(password);
        User newUser = new User(userId, username, email, passwordHash);
        databaseService.addUser(newUser);
        return true;
    }

    /**
     * Вход в систему
     */
    public User loginUser(String username, String password) {
        User user = databaseService.getUserByUsername(username);
        if (user != null && passwordHasher.verifyPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    /**
     * Получить пользователя по ID
     */
    public User getUserById(int id) {
        return databaseService.getUserById(id);
    }

    /**
     * Проверить существование пользователя
     */
    public boolean userExists(String username) {
        return databaseService.getUserByUsername(username) != null;
    }

    /**
     * Добавить оценку коктейля
     */
    public void rateCocktail(int userId, int cocktailId, int rating) {
        if (rating >= 1 && rating <= 5) {
            databaseService.saveUserRating(userId, cocktailId, rating);
        }
    }

    /**
     * Получить оценку пользователя для коктейля
     */
    public Integer getUserRating(int userId, int cocktailId) {
        return databaseService.getUserRating(userId, cocktailId);
    }

    /**
     * Удалить пользователя
     */
    public void deleteUser(int id) {
        databaseService.deleteUser(id);
    }

    /**
     * Обновить профиль пользователя
     */
    public void updateUserProfile(User user) {
        databaseService.updateUser(user);
    }

    private int getNextUserId() {
        return databaseService.getAllUsers().stream()
                .mapToInt(User::getId)
                .max()
                .orElse(0) + 1;
    }
}
