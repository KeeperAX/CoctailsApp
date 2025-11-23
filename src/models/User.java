package models;                                              // Объявление пакета — класс относится к моделям данных приложения

import java.util.HashMap;                                    // Импорт конкретной реализации Map — HashMap
import java.util.Map;                                         // Импорт интерфейса Map для работы с коллекцией ключ-значение

public class User {                                          // Публичный класс, представляющий зарегистрированного пользователя

    private int id;                                          // Поле: уникальный идентификатор пользователя
    private String username;                                 // Поле: имя пользователя (логин, уникальное)
    private String email;                                    // Поле: электронная почта пользователя
    private String passwordHash;                             // Поле: хешированный пароль (никогда не хранится в открытом виде)
    private Map<Integer, Integer> ratings;                   // Поле: карта оценок пользователя: ID коктейля → оценка (1–5)

    // Конструктор — создаёт нового пользователя с обязательными полями
    public User(int id, String username, String email, String passwordHash) {
        this.id = id;                                        // Присваиваем уникальный ID
        this.username = username;                            // Устанавливаем имя пользователя
        this.email = email;                                  // Устанавливаем email
        this.passwordHash = passwordHash;                    // Сохраняем хеш пароля
        this.ratings = new HashMap<>();                      // Инициализируем пустую карту оценок
    }

    // ===== ГЕТТЕРЫ И СЕТТЕРЫ =====

    // Геттер: возвращает ID пользователя
    public int getId() {
        return id;                                           // Возвращаем значение поля id
    }

    // Сеттер: позволяет изменить ID (используется при загрузке из JSON)
    public void setId(int id) {
        this.id = id;                                        // Присваиваем новое значение ID
    }

    // Геттер: возвращает имя пользователя
    public String getUsername() {
        return username;                                     // Возвращаем логин
    }

    // Сеттер: изменяет имя пользователя (может быть запрещено в будущем)
    public void setUsername(String username) {
        this.username = username;                            // Присваиваем новое имя
    }

    // Геттер: возвращает email пользователя
    public String getEmail() {
        return email;                                        // Возвращаем электронную почту
    }

    // Сеттер: позволяет обновить email (например, в профиле)
    public void setEmail(String email) {
        this.email = email;                                  // Присваиваем новый email
    }

    // Геттер: возвращает хеш пароля
    public String getPasswordHash() {
        return passwordHash;                                 // Возвращаем хешированный пароль
    }

    // Сеттер: позволяет обновить хеш пароля (при смене пароля)
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;                    // Присваиваем новый хеш
    }

    // Геттер: возвращает карту всех оценок пользователя
    public Map<Integer, Integer> getRatings() {
        return ratings;                                      // Возвращаем ссылку на карту (внимание: можно изменить извне!)
    }

    // Метод: ставит оценку коктейлю (от 1 до 5)
    public void rateCocktail(int cocktailId, int rating) {
        if (rating >= 1 && rating <= 5) {                    // Проверяем, что оценка в допустимом диапазоне
            this.ratings.put(cocktailId, rating);            // Сохраняем или обновляем оценку по ID коктейля
        }
        // Если оценка вне диапазона — ничего не делаем (можно бросить исключение)
    }

    // Метод: возвращает оценку, которую пользователь поставил конкретному коктейлю
    public Integer getRatingForCocktail(int cocktailId) {
        return this.ratings.get(cocktailId);                 // Возвращаем оценку или null, если не оценивал
    }

    // Метод: удаляет оценку для указанного коктейля
    public void removeCocktailRating(int cocktailId) {
        this.ratings.remove(cocktailId);                     // Удаляем запись из карты оценок
    }

    // Переопределяем toString() для удобного вывода в логах и отладке
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';                                         // Возвращаем строку с основной информацией (без пароля и оценок)
    }
}