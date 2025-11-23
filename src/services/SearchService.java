package services;  // Объявляем, что класс находится в пакете services (бизнес-логика)

import models.Cocktail;                     // Импортируем модель коктейля
import java.util.List;                       // Импортируем интерфейс List для возврата списков
import java.util.stream.Collectors;          // Импортируем Collectors для сбора результатов Stream

public class SearchService {                 // Класс сервиса поиска коктейлей

    private DatabaseService databaseService; // Поле: ссылка на сервис, работающий с данными (JSON-файлы)

    // Конструктор — принимает зависимость от DatabaseService (внедрение зависимости)
    public SearchService(DatabaseService databaseService) {
        this.databaseService = databaseService;  // Сохраняем ссылку на источник данных
    }

    /**
     * Поиск коктейлей по названию (регистронезависимый поиск подстроки)
     * Если запрос пустой — возвращает все коктейли
     */
    public List<Cocktail> searchByName(String query) {
        // Если запрос null или пустой — возвращаем весь список коктейлей
        if (query == null || query.isEmpty()) {
            return databaseService.getAllCocktails();
        }

        String lowerQuery = query.toLowerCase(); // Приводим запрос к нижнему регистру для поиска без учёта регистра

        // Фильтруем все коктейли: оставляем только те, в названии которых есть подстрока
        return databaseService.getAllCocktails().stream()
                .filter(c -> c.getName().toLowerCase().contains(lowerQuery)) // Сравниваем в нижнем регистре
                .collect(Collectors.toList()); // Собираем результат в список
    }

    /**
     * Поиск коктейлей по алкогольной основе (точное совпадение, без учёта регистра)
     */
    public List<Cocktail> searchByAlcoholBase(String alcoholBase) {
        // Если параметр пустой или null — возвращаем все коктейли (фильтр не применяется)
        if (alcoholBase == null || alcoholBase.isEmpty()) {
            return databaseService.getAllCocktails();
        }

        // Фильтруем коктейли по точному совпадению алкогольной основы (игнорируя регистр)
        return databaseService.getAllCocktails().stream()
                .filter(c -> c.getAlcoholBase().equalsIgnoreCase(alcoholBase))
                .collect(Collectors.toList());
    }

    /**
     * Поиск коктейлей, содержащих указанный ингредиент (по подстроке в названии ингредиента)
     */
    public List<Cocktail> searchByIngredient(String ingredientName) {
        // Если запрос пустой — возвращаем все коктейли
        if (ingredientName == null || ingredientName.isEmpty()) {
            return databaseService.getAllCocktails();
        }

        String lowerQuery = ingredientName.toLowerCase(); // Приводим запрос к нижнему регистру

        // Фильтруем коктейли: оставляем те, у которых хотя бы один ингредиент содержит подстроку
        return databaseService.getAllCocktails().stream()
                .filter(c -> c.getIngredients().stream()     // Переходим к потоку ингредиентов коктейля
                        .anyMatch(ing -> ing.getName().toLowerCase().contains(lowerQuery))) // Проверяем совпадение
                .collect(Collectors.toList());
    }

    /**
     * Комбинированный (расширенный) поиск по нескольким параметрам одновременно
     * Любой параметр может быть null или пустым — тогда он игнорируется
     */
    public List<Cocktail> advancedSearch(String name, String alcoholBase, String difficulty) {
        return databaseService.getAllCocktails().stream()
                .filter(c -> {                                   // Фильтруем каждый коктейль
                    // Проверка по названию: совпадение подстроки или параметр не указан
                    boolean nameMatch = name == null || name.isEmpty() ||
                            c.getName().toLowerCase().contains(name.toLowerCase());

                    // Проверка по алкогольной основе: точное совпадение или параметр не указан
                    boolean alcoholMatch = alcoholBase == null || alcoholBase.isEmpty() ||
                            c.getAlcoholBase().equalsIgnoreCase(alcoholBase);

                    // Проверка по сложности: точное совпадение или параметр не указан
                    boolean difficultyMatch = difficulty == null || difficulty.isEmpty() ||
                            c.getDifficulty().equalsIgnoreCase(difficulty);

                    // Коктейль подходит, только если все активные фильтры совпадают
                    return nameMatch && alcoholMatch && difficultyMatch;
                })
                .collect(Collectors.toList()); // Возвращаем отфильтрованный список
    }

    /**
     * Возвращает список всех уникальных алкогольных основ, используемых в коктейлях
     * Отсортировано по алфавиту
     */
    public List<String> getAvailableAlcoholBases() {
        return databaseService.getAllCocktails().stream()
                .map(Cocktail::getAlcoholBase)      // Берём только алкогольную основу
                .distinct()                         // Убираем дубликаты
                .sorted()                           // Сортируем по алфавиту
                .collect(Collectors.toList());     // Собираем в список
    }

    /**
     * Возвращает список всех уникальных уровней сложности коктейлей
     * Без сортировки (обычно их мало: "Лёгкий", "Средний", "Сложный")
     */
    public List<String> getAvailableDifficulties() {
        return databaseService.getAllCocktails().stream()
                .map(Cocktail::getDifficulty)       // Берём только сложность
                .distinct()                         // Убираем повторения
                .collect(Collectors.toList());      // Возвращаем список
    }
}