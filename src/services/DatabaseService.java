package services;

import models.Cocktail;
import models.Ingredient;
import models.PreparationStep;
import models.User;
import com.google.gson.*;

import java.io.*;
import java.util.*;

public class DatabaseService {
    private static final String COCKTAILS_FILE = "resources/data/cocktails.json";
    private static final String USERS_FILE = "resources/data/users.json";
    private static final String RATINGS_FILE = "resources/data/ratings.json";

    private List<Cocktail> cocktails;
    private List<User> users;
    private Gson gson;

    public DatabaseService() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.cocktails = new ArrayList<>();
        this.users = new ArrayList<>();
        loadAllData();
    }

    // ===== COCKTAIL OPERATIONS =====

    public List<Cocktail> getAllCocktails() {
        return new ArrayList<>(cocktails);
    }

    public Cocktail getCocktailById(int id) {
        return cocktails.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void addCocktail(Cocktail cocktail) {
        cocktails.add(cocktail);
        saveCocktails();
    }

    public void updateCocktail(Cocktail cocktail) {
        for (int i = 0; i < cocktails.size(); i++) {
            if (cocktails.get(i).getId() == cocktail.getId()) {
                cocktails.set(i, cocktail);
                saveCocktails();
                return;
            }
        }
    }

    public void deleteCocktail(int id) {
        cocktails.removeIf(c -> c.getId() == id);
        saveCocktails();
    }

    // ===== USER OPERATIONS =====

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public User getUserById(int id) {
        return users.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public User getUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        users.add(user);
        saveUsers();
    }

    public void updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == user.getId()) {
                users.set(i, user);
                saveUsers();
                return;
            }
        }
    }

    public void deleteUser(int id) {
        users.removeIf(u -> u.getId() == id);
        saveUsers();
    }

    // ===== RATINGS OPERATIONS =====

    public void saveUserRating(int userId, int cocktailId, int rating) {
        User user = getUserById(userId);
        if (user != null) {
            user.rateCocktail(cocktailId, rating);
            updateUser(user);
            updateCocktailAverageRating(cocktailId);
        }
    }

    public Integer getUserRating(int userId, int cocktailId) {
        User user = getUserById(userId);
        if (user != null) {
            return user.getRatingForCocktail(cocktailId);
        }
        return null;
    }

    private void updateCocktailAverageRating(int cocktailId) {
        double sum = 0;
        int count = 0;
        for (User user : users) {
            Integer rating = user.getRatingForCocktail(cocktailId);
            if (rating != null) {
                sum += rating;
                count++;
            }
        }
        Cocktail cocktail = getCocktailById(cocktailId);
        if (cocktail != null) {
            cocktail.setAverageRating(count > 0 ? sum / count : 0);
            updateCocktail(cocktail);
        }
    }

    // ===== DATA LOADING AND SAVING =====

    private void loadAllData() {
        loadCocktails();
        loadUsers();
    }

    private void loadCocktails() {
        try {
            File file = new File(COCKTAILS_FILE);
            if (file.exists()) {
                String content = readFile(file);
                JsonArray array = JsonParser.parseString(content).getAsJsonArray();
                for (JsonElement element : array) {
                    cocktails.add(parseCocktail(element.getAsJsonObject()));
                }
            } else {
                initializeSampleCocktails();
            }
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке коктейлей: " + e.getMessage());
            initializeSampleCocktails();
        }
    }

    private void loadUsers() {
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

    private void saveCocktails() {
        try {
            JsonArray array = new JsonArray();
            for (Cocktail cocktail : cocktails) {
                array.add(cocktailToJson(cocktail));
            }
            writeFile(COCKTAILS_FILE, gson.toJson(array));
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении коктейлей: " + e.getMessage());
        }
    }

    private void saveUsers() {
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

    // ===== JSON PARSING AND SERIALIZATION =====

    private Cocktail parseCocktail(JsonObject obj) {
        Cocktail c = new Cocktail(
                obj.get("id").getAsInt(),
                obj.get("name").getAsString(),
                obj.get("description").getAsString(),
                obj.get("alcoholBase").getAsString(),
                obj.get("difficulty").getAsString(),
                obj.get("preparationTime").getAsInt(),
                obj.get("imageUrl").getAsString()
        );

        JsonArray ingredients = obj.getAsJsonArray("ingredients");
        for (JsonElement elem : ingredients) {
            JsonObject ingObj = elem.getAsJsonObject();
            c.addIngredient(new Ingredient(
                    ingObj.get("name").getAsString(),
                    ingObj.get("quantity").getAsDouble(),
                    ingObj.get("unit").getAsString()
            ));
        }

        JsonArray steps = obj.getAsJsonArray("preparationSteps");
        for (JsonElement elem : steps) {
            JsonObject stepObj = elem.getAsJsonObject();
            c.addPreparationStep(new PreparationStep(
                    stepObj.get("stepNumber").getAsInt(),
                    stepObj.get("description").getAsString(),
                    stepObj.get("tips").getAsString(),
                    stepObj.get("duration").getAsInt()
            ));
        }

        c.setAverageRating(obj.get("averageRating").getAsDouble());
        return c;
    }

    private JsonObject cocktailToJson(Cocktail c) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", c.getId());
        obj.addProperty("name", c.getName());
        obj.addProperty("description", c.getDescription());
        obj.addProperty("alcoholBase", c.getAlcoholBase());
        obj.addProperty("difficulty", c.getDifficulty());
        obj.addProperty("preparationTime", c.getPreparationTime());
        obj.addProperty("imageUrl", c.getImageUrl());
        obj.addProperty("averageRating", c.getAverageRating());

        JsonArray ingredients = new JsonArray();
        for (Ingredient ing : c.getIngredients()) {
            JsonObject ingObj = new JsonObject();
            ingObj.addProperty("name", ing.getName());
            ingObj.addProperty("quantity", ing.getQuantity());
            ingObj.addProperty("unit", ing.getUnit());
            ingredients.add(ingObj);
        }
        obj.add("ingredients", ingredients);

        JsonArray steps = new JsonArray();
        for (PreparationStep step : c.getPreparationSteps()) {
            JsonObject stepObj = new JsonObject();
            stepObj.addProperty("stepNumber", step.getStepNumber());
            stepObj.addProperty("description", step.getDescription());
            stepObj.addProperty("tips", step.getTips());
            stepObj.addProperty("duration", step.getDuration());
            steps.add(stepObj);
        }
        obj.add("preparationSteps", steps);

        return obj;
    }

    private User parseUser(JsonObject obj) {
        User u = new User(
                obj.get("id").getAsInt(),
                obj.get("username").getAsString(),
                obj.get("email").getAsString(),
                obj.get("passwordHash").getAsString()
        );

        JsonObject ratings = obj.getAsJsonObject("ratings");
        for (String key : ratings.keySet()) {
            u.rateCocktail(Integer.parseInt(key), ratings.get(key).getAsInt());
        }

        return u;
    }

    private JsonObject userToJson(User u) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", u.getId());
        obj.addProperty("username", u.getUsername());
        obj.addProperty("email", u.getEmail());
        obj.addProperty("passwordHash", u.getPasswordHash());

        JsonObject ratings = new JsonObject();
        for (Map.Entry<Integer, Integer> entry : u.getRatings().entrySet()) {
            ratings.addProperty(entry.getKey().toString(), entry.getValue());
        }
        obj.add("ratings", ratings);

        return obj;
    }

    // ===== UTILITY METHODS =====

    private String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    private void writeFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    private void initializeSampleCocktails() {
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

        saveCocktails();
    }
}
