package services;

import models.Cocktail;
import java.util.*;
import java.util.stream.Collectors;

public class CocktailService {
    private DatabaseService databaseService;

    public CocktailService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<Cocktail> getAllCocktails() {
        return databaseService.getAllCocktails();
    }

    public Cocktail getCocktailById(int id) {
        return databaseService.getCocktailById(id);
    }

    public void addNewCocktail(Cocktail cocktail) {
        if (cocktail.getName() != null && !cocktail.getName().isEmpty()) {
            databaseService.addCocktail(cocktail);
        }
    }

    public void updateCocktail(Cocktail cocktail) {
        databaseService.updateCocktail(cocktail);
    }

    public void deleteCocktail(int id) {
        databaseService.deleteCocktail(id);
    }

    public int getNextCocktailId() {
        return databaseService.getAllCocktails().stream()
                .mapToInt(Cocktail::getId)
                .max()
                .orElse(0) + 1;
    }

    // Сортировка
    public List<Cocktail> sortByDifficulty() {
        return databaseService.getAllCocktails().stream()
                .sorted(Comparator.comparing(Cocktail::getDifficulty))
                .collect(Collectors.toList());
    }

    public List<Cocktail> sortByPreparationTime() {
        return databaseService.getAllCocktails().stream()
                .sorted(Comparator.comparingInt(Cocktail::getPreparationTime))
                .collect(Collectors.toList());
    }

    public List<Cocktail> sortByRating() {
        return databaseService.getAllCocktails().stream()
                .sorted((c1, c2) -> Double.compare(c2.getAverageRating(), c1.getAverageRating()))
                .collect(Collectors.toList());
    }

    // Фильтрация
    public List<Cocktail> filterByDifficulty(String difficulty) {
        return databaseService.getAllCocktails().stream()
                .filter(c -> c.getDifficulty().equalsIgnoreCase(difficulty))
                .collect(Collectors.toList());
    }

    public List<Cocktail> filterByAlcoholBase(String alcoholBase) {
        return databaseService.getAllCocktails().stream()
                .filter(c -> c.getAlcoholBase().equalsIgnoreCase(alcoholBase))
                .collect(Collectors.toList());
    }

    public List<Cocktail> filterByMaxPreparationTime(int maxTime) {
        return databaseService.getAllCocktails().stream()
                .filter(c -> c.getPreparationTime() <= maxTime)
                .collect(Collectors.toList());
    }
}
