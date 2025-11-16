package services;

import models.Cocktail;
import java.util.List;
import java.util.stream.Collectors;

public class SearchService {
    private DatabaseService databaseService;

    public SearchService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Поиск коктейлей по названию (регистронезависимый поиск подстроки)
     */
    public List<Cocktail> searchByName(String query) {
        if (query == null || query.isEmpty()) {
            return databaseService.getAllCocktails();
        }

        String lowerQuery = query.toLowerCase();
        return databaseService.getAllCocktails().stream()
                .filter(c -> c.getName().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    /**
     * Поиск коктейлей по алкогольной основе
     */
    public List<Cocktail> searchByAlcoholBase(String alcoholBase) {
        if (alcoholBase == null || alcoholBase.isEmpty()) {
            return databaseService.getAllCocktails();
        }

        return databaseService.getAllCocktails().stream()
                .filter(c -> c.getAlcoholBase().equalsIgnoreCase(alcoholBase))
                .collect(Collectors.toList());
    }

    /**
     * Поиск коктейлей по ингредиентам
     */
    public List<Cocktail> searchByIngredient(String ingredientName) {
        if (ingredientName == null || ingredientName.isEmpty()) {
            return databaseService.getAllCocktails();
        }

        String lowerQuery = ingredientName.toLowerCase();
        return databaseService.getAllCocktails().stream()
                .filter(c -> c.getIngredients().stream()
                        .anyMatch(ing -> ing.getName().toLowerCase().contains(lowerQuery)))
                .collect(Collectors.toList());
    }

    /**
     * Комбинированный поиск по нескольким параметрам
     */
    public List<Cocktail> advancedSearch(String name, String alcoholBase, String difficulty) {
        return databaseService.getAllCocktails().stream()
                .filter(c -> {
                    boolean nameMatch = name == null || name.isEmpty() ||
                            c.getName().toLowerCase().contains(name.toLowerCase());
                    boolean alcoholMatch = alcoholBase == null || alcoholBase.isEmpty() ||
                            c.getAlcoholBase().equalsIgnoreCase(alcoholBase);
                    boolean difficultyMatch = difficulty == null || difficulty.isEmpty() ||
                            c.getDifficulty().equalsIgnoreCase(difficulty);
                    return nameMatch && alcoholMatch && difficultyMatch;
                })
                .collect(Collectors.toList());
    }

    /**
     * Получить список всех уникальных алкогольных оснований
     */
    public List<String> getAvailableAlcoholBases() {
        return databaseService.getAllCocktails().stream()
                .map(Cocktail::getAlcoholBase)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Получить список всех уровней сложности
     */
    public List<String> getAvailableDifficulties() {
        return databaseService.getAllCocktails().stream()
                .map(Cocktail::getDifficulty)
                .distinct()
                .collect(Collectors.toList());
    }
}
