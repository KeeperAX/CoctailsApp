package services;                                            // Объявление пакета — класс находится в пакете services

import models.Cocktail;                                      // Импорт модели коктейля
import models.Ingredient;                                     // Импорт модели ингредиента
import models.PreparationStep;                                // Импорт модели шага приготовления
import models.User;                                           // Импорт модели пользователя
import com.google.gson.*;                                     // Импорт всех классов библиотеки Gson (JsonObject, JsonArray, Gson и т.д.)
import java.io.*;                                              // Импорт классов для работы с файлами (File, FileReader, FileWriter, BufferedReader и т.д.)
import java.util.*;                                          // Импорт всех коллекций Java (List, ArrayList, Map и т.д.)

public class DatabaseService {                               // Объявление публичного класса — имитация базы данных на JSON-файлах

    private static final String COCKTAILS_FILE = "resources/data/cocktails.json";  // Константа: путь к файлу с коктейлями
    private static final String USERS_FILE = "resources/data/users.json";          // Константа: путь к файлу с пользователями
    private static final String RATINGS_FILE = "resources/data/ratings.json";      // Константа: путь к файлу с оценками (пока не используется отдельно)

    private List<Cocktail> cocktails;                        // Поле: список всех коктейлей, загруженных в память
    private List<User> users;                                // Поле: список всех пользователей, загруженных в память
    private Gson gson;                                       // Поле: объект Gson для сериализации/десериализации JSON

    public DatabaseService() {                               // Конструктор класса — вызывается при создании объекта
        this.gson = new GsonBuilder().setPrettyPrinting().create();  // Создаём Gson с красивым форматированием (отступы, переносы)
        this.cocktails = new ArrayList<>();                  // Инициализируем пустой список коктейлей
        this.users = new ArrayList<>();                      // Инициализируем пустой список пользователей
        loadAllData();                                       // Загружаем все данные из файлов при старте приложения
    }

    // ===== ОПЕРАЦИИ С КОКТЕЙЛЯМИ =====

    public List<Cocktail> getAllCocktails() {                // Метод: возвращает копию списка всех коктейлей
        return new ArrayList<>(cocktails);                   // Возвращаем новый список — защита от изменения оригинала извне
    }

    public Cocktail getCocktailById(int id) {                // Метод: ищет коктейль по его ID
        return cocktails.stream()                            // Создаём поток из списка коктейлей
                .filter(c -> c.getId() == id)                // Оставляем только коктейль с нужным ID
                .findFirst()                                 // Берём первый найденный (должен быть только один)
                .orElse(null);                               // Если не найден — возвращаем null
    }

    public void addCocktail(Cocktail cocktail) {             // Метод: добавляет новый коктейль
        cocktails.add(cocktail);                             // Добавляем коктейль в список в памяти
        saveCocktails();                                     // Сохраняем обновлённый список в файл
    }

    public void updateCocktail(Cocktail cocktail) {          // Метод: обновляет существующий коктейль
        for (int i = 0; i < cocktails.size(); i++) {         // Проходим по всему списку коктейлей
            if (cocktails.get(i).getId() == cocktail.getId()) {  // Если нашли коктейль с таким же ID
                cocktails.set(i, cocktail);                  // Заменяем старый объект на новый
                saveCocktails();                             // Сохраняем изменения в файл
                return;                                      // Выходим из метода — работа завершена
            }
        }
    }

    public void deleteCocktail(int id) {                     // Метод: удаляет коктейль по ID
        cocktails.removeIf(c -> c.getId() == id);            // Удаляем все коктейли с указанным ID (должен быть один)
        saveCocktails();                                     // Сохраняем изменения в файл
    }

    // ===== ОПЕРАЦИИ С ПОЛЬЗОВАТЕЛЯМИ =====

    public List<User> getAllUsers() {                        // Метод: возвращает копию списка всех пользователей
        return new ArrayList<>(users);                       // Возвращаем новый список для безопасности
    }

    public User getUserById(int id) {                        // Метод: ищет пользователя по ID
        return users.stream()                                // Создаём поток из списка пользователей
                .filter(u -> u.getId() == id)                // Оставляем только с нужным ID
                .findFirst()                                 // Берём первый найденный
                .orElse(null);                               // Если не найден — возвращаем null
    }

    public User getUserByUsername(String username) {        // Метод: ищет пользователя по имени
        return users.stream()                                // Создаём поток
                .filter(u -> u.getUsername().equals(username))  // Сравниваем имя точно
                .findFirst()                                 // Берём первый найденный
                .orElse(null);                               // Если не найден — null
    }

    public void addUser(User user) {                         // Метод: добавляет нового пользователя
        users.add(user);                                     // Добавляем в список в памяти
        saveUsers();                                         // Сохраняем в файл
    }

    public void updateUser(User user) {                      // Метод: обновляет данные пользователя
        for (int i = 0; i < users.size(); i++) {             // Проходим по списку
            if (users.get(i).getId() == user.getId()) {      // Находим пользователя по ID
                users.set(i, user);                          // Заменяем старые данные на новые
                saveUsers();                                 // Сохраняем изменения
                return;                                      // Выходим
            }
        }
    }

    public void deleteUser(int id) {                         // Метод: удаляет пользователя по ID
        users.removeIf(u -> u.getId() == id);                // Удаляем из списка
        saveUsers();                                         // Сохраняем изменения
    }

    // ===== РАБОТА С ОЦЕНКАМИ =====

    public void saveUserRating(int userId, int cocktailId, int rating) {  // Метод: сохраняет оценку пользователя
        User user = getUserById(userId);                     // Находим пользователя по ID
        if (user != null) {                                  // Если пользователь существует
            user.rateCocktail(cocktailId, rating);           // Сохраняем оценку в объекте User
            updateUser(user);                                // Обновляем пользователя в "БД"
            updateCocktailAverageRating(cocktailId);         // Пересчитываем средний рейтинг коктейля
        }
    }

    public Integer getUserRating(int userId, int cocktailId) {  // Метод: возвращает оценку пользователя
        User user = getUserById(userId);                     // Находим пользователя
        if (user != null) {                                  // Если найден
            return user.getRatingForCocktail(cocktailId);    // Возвращаем его оценку (может быть null)
        }
        return null;                                         // Пользователь не найден
    }

    private void updateCocktailAverageRating(int cocktailId) {  // Приватный метод: пересчитывает средний рейтинг коктейля
        double sum = 0;                                      // Сумма всех оценок
        int count = 0;                                       // Количество оценок
        for (User user : users) {                            // Проходим по всем пользователям
            Integer rating = user.getRatingForCocktail(cocktailId);  // Получаем оценку текущего пользователя
            if (rating != null) {                            // Если оценка есть
                sum += rating;                               // Добавляем к сумме
                count++;                                     // Увеличиваем счётчик
            }
        }
        Cocktail cocktail = getCocktailById(cocktailId);     // Находим коктейль
        if (cocktail != null) {                              // Если коктейль существует
            cocktail.setAverageRating(count > 0 ? sum / count : 0);  // Устанавливаем средний рейтинг (0, если оценок нет)
            updateCocktail(cocktail);                        // Сохраняем обновлённый коктейль
        }
    }

    // ===== ЗАГРУЗКА И СОХРАНЕНИЕ ДАННЫХ =====

    private void loadAllData() {                             // Приватный метод: загружает все данные при старте
        loadCocktails();                                     // Загружаем коктейли
        loadUsers();                                         // Загружаем пользователей
    }

    private void loadCocktails() {                           // Приватный метод: загрузка коктейлей из файла
        try {                                                // Начинаем обработку исключений
            File file = new File(COCKTAILS_FILE);            // Создаём объект файла
            if (file.exists()) {                             // Проверяем, существует ли файл
                String content = readFile(file);             // Читаем содержимое файла в строку
                JsonArray array = JsonParser.parseString(content).getAsJsonArray();  // Парсим строку как JSON-массив
                for (JsonElement element : array) {          // Проходим по всем элементам массива
                    cocktails.add(parseCocktail(element.getAsJsonObject()));  // Преобразуем каждый JSON-объект в Cocktail и добавляем
                }
            } else {                                         // Если файла нет
                initializeSampleCocktails();                 // Создаём начальные данные (примеры коктейлей)
            }
        } catch (Exception e) {                              // Ловим любые ошибки при загрузке
            System.out.println("Ошибка при загрузке коктейлей: " + e.getMessage());  // Выводим сообщение в консоль
            initializeSampleCocktails();                     // При любой ошибке — создаём примеры
        }
    }

    private void loadUsers() {                               // Приватный метод: загрузка пользователей
        try {
            File file = new File(USERS_FILE);
            if (file.exists()) {
                String content = readFile(file);
                JsonArray array = JsonParser.parseString(content).getAsJsonArray();
                for (JsonElement element : array) {
                    users.add(parseUser(element.getAsJsonObject()));
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке пользователей: " + e.getMessage());
        }
    }

    private void saveCocktails() {                           // Приватный метод: сохранение коктейлей в файл
        try {
            JsonArray array = new JsonArray();               // Создаём пустой JSON-массив
            for (Cocktail cocktail : cocktails) {            // Проходим по всем коктейлям
                array.add(cocktailToJson(cocktail));         // Преобразуем каждый в JSON и добавляем
            }
            writeFile(COCKTAILS_FILE, gson.toJson(array));   // Записываем массив в файл с красивым форматированием
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении коктейлей: " + e.getMessage());
        }
    }

    private void saveUsers() {                               // Приватный метод: сохранение пользователей
        try {
            JsonArray array = new JsonArray();
            for (User user : users) {
                array.add(userToJson(user));
            }
            writeFile(USERS_FILE, gson.toJson(array));
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении пользователей: " + e.getMessage());
        }
    }

    // ===== ПРЕОБРАЗОВАНИЕ JSON ↔ ОБЪЕКТЫ =====

    private Cocktail parseCocktail(JsonObject obj) {         // Приватный метод: преобразует JSON в объект Cocktail
        Cocktail c = new Cocktail(                           // Создаём новый коктейль с основными полями
                obj.get("id").getAsInt(),
                obj.get("name").getAsString(),
                obj.get("description").getAsString(),
                obj.get("alcoholBase").getAsString(),
                obj.get("difficulty").getAsString(),
                obj.get("preparationTime").getAsInt(),
                obj.get("imageUrl").getAsString()
        );

        JsonArray ingredients = obj.getAsJsonArray("ingredients");  // Получаем массив ингредиентов
        for (JsonElement elem : ingredients) {               // Проходим по каждому ингредиенту
            JsonObject ingObj = elem.getAsJsonObject();      // Получаем объект ингредиента
            c.addIngredient(new Ingredient(                  // Создаём и добавляем ингредиент
                    ingObj.get("name").getAsString(),
                    ingObj.get("quantity").getAsDouble(),
                    ingObj.get("unit").getAsString()
            ));
        }

        JsonArray steps = obj.getAsJsonArray("preparationSteps");  // Получаем массив шагов
        for (JsonElement elem : steps) {
            JsonObject stepObj = elem.getAsJsonObject();
            c.addPreparationStep(new PreparationStep(
                    stepObj.get("stepNumber").getAsInt(),
                    stepObj.get("description").getAsString(),
                    stepObj.get("tips").getAsString(),
                    stepObj.get("duration").getAsInt()
            ));
        }

        c.setAverageRating(obj.get("averageRating").getAsDouble());  // Устанавливаем средний рейтинг
        return c;                                            // Возвращаем готовый объект
    }

    private JsonObject cocktailToJson(Cocktail c) {          // Приватный метод: преобразует коктейль в JSON
        JsonObject obj = new JsonObject();                   // Создаём основной объект
        obj.addProperty("id", c.getId());                    // Добавляем ID
        obj.addProperty("name", c.getName());                // Добавляем название
        obj.addProperty("description", c.getDescription());  // Описание
        obj.addProperty("alcoholBase", c.getAlcoholBase());  // Алкогольная основа
        obj.addProperty("difficulty", c.getDifficulty());    // Сложность
        obj.addProperty("preparationTime", c.getPreparationTime());  // Время приготовления
        obj.addProperty("imageUrl", c.getImageUrl());        // Путь к изображению
        obj.addProperty("averageRating", c.getAverageRating());  // Средний рейтинг

        JsonArray ingredients = new JsonArray();             // Создаём массив ингредиентов
        for (Ingredient ing : c.getIngredients()) {          // Проходим по всем ингредиентам
            JsonObject ingObj = new JsonObject();            // Создаём объект для ингредиента
            ingObj.addProperty("name", ing.getName());       // Название
            ingObj.addProperty("quantity", ing.getQuantity());  // Количество
            ingObj.addProperty("unit", ing.getUnit());       // Единица измерения
            ingredients.add(ingObj);                         // Добавляем в массив
        }
        obj.add("ingredients", ingredients);                 // Добавляем массив в основной объект

        JsonArray steps = new JsonArray();                   // Создаём массив шагов
        for (PreparationStep step : c.getPreparationSteps()) {  // Проходим по шагам
            JsonObject stepObj = new JsonObject();
            stepObj.addProperty("stepNumber", step.getStepNumber());
            stepObj.addProperty("description", step.getDescription());
            stepObj.addProperty("tips", step.getTips());
            stepObj.addProperty("duration", step.getDuration());
            steps.add(stepObj);
        }
        obj.add("preparationSteps", steps);                  // Добавляем шаги

        return obj;                                          // Возвращаем готовый JSON-объект
    }

    private User parseUser(JsonObject obj) {                 // Приватный метод: преобразует JSON в User
        User u = new User(                                   // Создаём пользователя
                obj.get("id").getAsInt(),
                obj.get("username").getAsString(),
                obj.get("email").getAsString(),
                obj.get("passwordHash").getAsString()
        );

        JsonObject ratings = obj.getAsJsonObject("ratings"); // Получаем объект с оценками
        for (String key : ratings.keySet()) {                // Проходим по всем ключам (ID коктейлей)
            u.rateCocktail(Integer.parseInt(key), ratings.get(key).getAsInt());  // Восстанавливаем оценку
        }

        return u;                                            // Возвращаем пользователя
    }

    private JsonObject userToJson(User u) {                  // Приватный метод: преобразует User в JSON
        JsonObject obj = new JsonObject();
        obj.addProperty("id", u.getId());
        obj.addProperty("username", u.getUsername());
        obj.addProperty("email", u.getEmail());
        obj.addProperty("passwordHash", u.getPasswordHash());

        JsonObject ratings = new JsonObject();               // Создаём объект для оценок
        for (Map.Entry<Integer, Integer> entry : u.getRatings().entrySet()) {  // Проходим по всем оценкам
            ratings.addProperty(entry.getKey().toString(), entry.getValue());  // Ключ — ID коктейля, значение — оценка
        }
        obj.add("ratings", ratings);                         // Добавляем оценки

        return obj;
    }

    // ===== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ =====

    private String readFile(File file) throws IOException { // Метод: читает весь файл в строку
        StringBuilder content = new StringBuilder();         // Буфер для результата
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {  // Автозакрытие ридера
            String line;                                     // Переменная для строки
            while ((line = reader.readLine()) != null) {     // Читаем построчно
                content.append(line);                        // Добавляем строку (без \n)
            }
        }
        return content.toString();                           // Возвращаем весь текст
    }

    private void writeFile(String filePath, String content) throws IOException {  // Метод: записывает строку в файл
        File file = new File(filePath);                      // Создаём объект файла
        file.getParentFile().mkdirs();                       // Создаём папки, если их нет
        try (FileWriter writer = new FileWriter(file)) {     // Автозакрытие писателя
            writer.write(content);                          // Записываем содержимое
        }
    }

    private void initializeSampleCocktails() {               // Приватный метод: создаёт начальные коктейли при первом запуске
        // Мартини
        Cocktail martini = new Cocktail(1, "Мартини", "Классический коктейль из водки и вермута",
                "Vodka", "EASY", 5, "resources/images/martini.png");
        martini.addIngredient(new Ingredient("Водка", 60, "мл"));
        martini.addIngredient(new Ingredient("Сухой вермут", 10, "мл"));
        martini.addIngredient(new Ingredient("Оливка", 1, "шт"));
        martini.addPreparationStep(new PreparationStep(1, "Охладить коктейльный стакан",
                "Заполните стакан льдом и холодной водой", 30));
        martini.addPreparationStep(new PreparationStep(2, "Добавить ингредиенты",
                "Налейте водку и вермут в стакан", 20));
        martini.addPreparationStep(new PreparationStep(3, "Перемешать",
                "Перемешивайте со льдом в течение 30 секунд", 30));
        martini.addPreparationStep(new PreparationStep(4, "Процедить",
                "Процедите в охлажденный бокал", 15));
        cocktails.add(martini);

        // Дайкири
        Cocktail daiquiri = new Cocktail(2, "Дайкири", "Освежающий коктейль с ромом и лимоном",
                "Rum", "EASY", 5, "resources/images/daiquiri.png");
        daiquiri.addIngredient(new Ingredient("Белый ром", 45, "мл"));
        daiquiri.addIngredient(new Ingredient("Свежевыжатый лимонный сок", 25, "мл"));
        daiquiri.addIngredient(new Ingredient("Сахарный сироп", 15, "мл"));
        daiquiri.addPreparationStep(new PreparationStep(1, "Добавить ингредиенты в шейкер",
                "Используйте качественный свежий сок", 20));
        daiquiri.addPreparationStep(new PreparationStep(2, "Заполнить льдом",
                "Добавьте лед и закройте шейкер", 15));
        daiquiri.addPreparationStep(new PreparationStep(3, "Встряхнуть",
                "Встряхивайте в течение 10-15 секунд", 15));
        daiquiri.addPreparationStep(new PreparationStep(4, "Процедить",
                "Процедите в охлажденный бокал", 10));
        cocktails.add(daiquiri);

        saveCocktails();                                     // Сохраняем созданные коктейли в файл
    }
}