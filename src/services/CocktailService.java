package services;                                            // Объявление пакета — класс относится к сервисам бизнес-логики

import models.Cocktail;                                      // Импорт модели коктейля
import java.util.*;                                          // Импорт всех коллекций и утилит Java (List, Comparator и др.)
import java.util.stream.Collectors;                          // Импорт Collectors для сбора результатов Stream в список

public class CocktailService {                               // Публичный класс-сервис для работы с коктейлями

    private DatabaseService databaseService;                // Поле: зависимость от сервиса, который работает с JSON-файлами

    // Конструктор — внедряет зависимость DatabaseService (внедрение зависимостей)
    public CocktailService(DatabaseService databaseService) {
        this.databaseService = databaseService;              // Сохраняем ссылку на сервис базы данных
    }

    // Возвращает полный список всех коктейлей
    public List<Cocktail> getAllCocktails() {
        return databaseService.getAllCocktails();            // Делегируем вызов в DatabaseService
    }

    // Находит коктейль по его уникальному ID
    public Cocktail getCocktailById(int id) {
        return databaseService.getCocktailById(id);          // Делегируем поиск в DatabaseService
    }

    // Добавляет новый коктейль (с базовой проверкой на корректность названия)
    public void addNewCocktail(Cocktail cocktail) {
        if (cocktail.getName() != null && !cocktail.getName().isEmpty()) {  // Проверяем, что название не null и не пустое
            databaseService.addCocktail(cocktail);           // Если проверка пройдена — сохраняем коктейль
        }
        // Если название пустое — коктейль не сохраняется (можно расширить проверками)
    }

    // Обновляет существующий коктейль (по ID)
    public void updateCocktail(Cocktail cocktail) {
        databaseService.updateCocktail(cocktail);            // Делегируем обновление в DatabaseService
    }

    // Удаляет коктейль по его ID
    public void deleteCocktail(int id) {
        databaseService.deleteCocktail(id);                  // Делегируем удаление в DatabaseService
    }

    // Генерирует следующий доступный ID для нового коктейля
    public int getNextCocktailId() {
        return databaseService.getAllCocktails().stream()    // Получаем поток всех коктейлей
                .mapToInt(Cocktail::getId)                   // Преобразуем в поток ID (int)
                .max()                                       // Находим максимальный ID
                .orElse(0)                                   // Если коктейлей нет — возвращаем 0
                + 1;                                         // Увеличиваем на 1 — это будет новый ID
    }

    // ===== МЕТОДЫ СОРТИРОВКИ =====

    // Сортирует все коктейли по сложности (по алфавиту: EASY → MEDIUM → HARD)
    public List<Cocktail> sortByDifficulty() {
        return databaseService.getAllCocktails().stream()    // Берём поток коктейлей
                .sorted(Comparator.comparing(Cocktail::getDifficulty))  // Сортируем по сложности
                .collect(Collectors.toList());               // Собираем результат в список
    }

    // Сортирует коктейли по времени приготовления (от меньшего к большему)
    public List<Cocktail> sortByPreparationTime() {
        return databaseService.getAllCocktails().stream()
                .sorted(Comparator.comparingInt(Cocktail::getPreparationTime))  // Сортировка по времени (int)
                .collect(Collectors.toList());
    }

    // Сортирует коктейли по среднему рейтингу (от большего к меньшему — лучшие сверху)
    public List<Cocktail> sortByRating() {
        return databaseService.getAllCocktails().stream()
                .sorted((c1, c2) -> Double.compare(c2.getAverageRating(), c1.getAverageRating()))  // Обратная сортировка по рейтингу
                .collect(Collectors.toList());
    }

    // ===== МЕТОДЫ ФИЛЬТРАЦИИ =====

    // Фильтрует коктейли по уровню сложности (регистронезависимо)
    public List<Cocktail> filterByDifficulty(String difficulty) {
        return databaseService.getAllCocktails().stream()
                .filter(c -> c.getDifficulty().equalsIgnoreCase(difficulty))  // Сравниваем без учёта регистра
                .collect(Collectors.toList());
    }

    // Фильтрует коктейли по алкогольной основе (регистронезависимо)
    public List<Cocktail> filterByAlcoholBase(String alcoholBase) {
        return databaseService.getAllCocktails().stream()
                .filter(c -> c.getAlcoholBase().equalsIgnoreCase(alcoholBase))
                .collect(Collectors.toList());
    }

    // Фильтрует коктейли, которые готовятся не дольше указанного времени (в минутах)
    public List<Cocktail> filterByMaxPreparationTime(int maxTime) {
        return databaseService.getAllCocktails().stream()
                .filter(c -> c.getPreparationTime() <= maxTime)  // Оставляем только быстрые в приготовлении
                .collect(Collectors.toList());
    }
}