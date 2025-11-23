package models;                                              // Объявление пакета — класс относится к моделям данных приложения

public class PreparationStep {                               // Публичный класс, описывающий один шаг приготовления коктейля

    private int stepNumber;                                  // Поле: порядковый номер шага (1, 2, 3 и т.д.)
    private String description;                              // Поле: основное описание действия на этом шаге
    private String tips;                                     // Поле: полезный совет или лайфхак для выполнения шага
    private int duration;                                    // Поле: примерное время выполнения шага в секундах

    // Конструктор — создаёт объект шага с полным набором данных
    public PreparationStep(int stepNumber, String description, String tips, int duration) {
        this.stepNumber = stepNumber;                        // Инициализируем номер шага
        this.description = description;                      // Инициализируем основное описание
        this.tips = tips;                                    // Инициализируем совет
        this.duration = duration;                            // Инициализируем длительность в секундах
    }

    // ===== ГЕТТЕРЫ И СЕТТЕРЫ =====

    // Геттер: возвращает номер шага
    public int getStepNumber() {
        return stepNumber;                                   // Возвращаем значение поля stepNumber
    }

    // Сеттер: позволяет изменить номер шага (например, при редактировании рецепта)
    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;                        // Присваиваем новое значение полю stepNumber
    }

    // Геттер: возвращает текстовое описание действия
    public String getDescription() {
        return description;                                  // Возвращаем описание шага
    }

    // Сеттер: изменяет описание шага
    public void setDescription(String description) {
        this.description = description;                      // Присваиваем новое описание
    }

    // Геттер: возвращает совет по выполнению шага
    public String getTips() {
        return tips;                                         // Возвращаем текст совета
    }

    // Сеттер: изменяет совет
    public void setTips(String tips) {
        this.tips = tips;                                    // Присваиваем новый совет
    }

    // Геттер: возвращает время выполнения шага в секундах
    public int getDuration() {
        return duration;                                     // Возвращаем длительность
    }

    // Сеттер: изменяет время выполнения шага
    public void setDuration(int duration) {
        this.duration = duration;                            // Присваиваем новое значение длительности
    }

    // Переопределяем toString() для удобного отображения шага в интерфейсе и логах
    @Override
    public String toString() {
        return "Шаг " + stepNumber + ": " + description;     // Возвращаем строку вида: "Шаг 1: Охладить бокал"
    }
}