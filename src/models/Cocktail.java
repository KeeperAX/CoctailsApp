package models;

import java.util.ArrayList;
import java.util.List;

public class Cocktail {
    private int id;
    private String name;
    private String description;
    private List<Ingredient> ingredients;
    private List<PreparationStep> preparationSteps;
    private String imageUrl; // путь к файлу изображения
    private String difficulty; // EASY, MEDIUM, HARD
    private int preparationTime; // в минутах
    private String alcoholBase; // водка, ром, джин и т.д.
    private double averageRating; // средняя оценка

    public Cocktail(int id, String name, String description, String alcoholBase,
                    String difficulty, int preparationTime, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.alcoholBase = alcoholBase;
        this.difficulty = difficulty;
        this.preparationTime = preparationTime;
        this.imageUrl = imageUrl;
        this.ingredients = new ArrayList<>();
        this.preparationSteps = new ArrayList<>();
        this.averageRating = 0.0;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
    }

    public List<PreparationStep> getPreparationSteps() {
        return preparationSteps;
    }

    public void addPreparationStep(PreparationStep step) {
        this.preparationSteps.add(step);
    }

    public void removePreparationStep(PreparationStep step) {
        this.preparationSteps.remove(step);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getAlcoholBase() {
        return alcoholBase;
    }

    public void setAlcoholBase(String alcoholBase) {
        this.alcoholBase = alcoholBase;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public String toString() {
        return name + " (" + difficulty + ", " + preparationTime + " мин)";
    }
}
