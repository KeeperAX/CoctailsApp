package models;                                              // Объявление пакета — класс относится к моделям данных приложения

public class Ingredient {                                    // Публичный класс, представляющий один ингредиент коктейля

    private String name;                                     // Поле: название ингредиента (например, "Водка", "Лимонный сок")
    private double quantity;                                 // Поле: количество ингредиента (например, 50.0)
    private String unit;                                     // Поле: единица измерения (мл, гр, шт, капля, щепотка и т.д.)

    // Конструктор — создаёт объект ингредиента с заданными параметрами
    public Ingredient(String name, double quantity, String unit) {
        this.name = name;                                    // Инициализируем название ингредиента
        this.quantity = quantity;                            // Инициализируем количество
        this.unit = unit;                                    // Инициализируем единицу измерения
    }

    // ===== ГЕТТЕРЫ И СЕТТЕРЫ =====

    // Геттер: возвращает название ингредиента
    public String getName() {
        return name;                                         // Возвращаем значение поля name
    }

    // Сеттер: устанавливает новое название ингредиента
    public void setName(String name) {
        this.name = name;                                    // Присваиваем новое значение полю name
    }

    // Геттер: возвращает количество ингредиента
    public double getQuantity() {
        return quantity;                                     // Возвращаем значение поля quantity
    }

    // Сеттер: изменяет количество ингредиента
    public void setQuantity(double quantity) {
        this.quantity = quantity;                            // Присваиваем новое значение полю quantity
    }

    // Геттер: возвращает единицу измерения
    public String getUnit() {
        return unit;                                         // Возвращаем значение поля unit
    }

    // Сеттер: изменяет единицу измерения
    public void setUnit(String unit) {
        this.unit = unit;                                    // Присваиваем новое значение полю unit
    }

    // Переопределяем метод toString() для красивого вывода ингредиента
    @Override
    public String toString() {
        return quantity + " " + unit + " " + name;           // Возвращаем строку вида: "50.0 мл Водка"
    }
}