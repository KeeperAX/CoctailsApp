package ui;  // Объявляем, что класс находится в пакете ui

import models.Cocktail;           // Импортируем модель коктейля
import models.PreparationStep;    // Импортируем модель шага приготовления

import javax.swing.*;               // Импортируем все компоненты Swing
import java.awt.*;                      // Импортируем AWT: цвета, шрифты, компоновки

public class CocktailDetailPanel extends JDialog {  // Класс модального окна с подробной информацией о коктейле

    private Cocktail cocktail;  // Поле: хранит коктейль, данные которого отображаются

    // Конструктор: создаёт диалоговое окно как дочернее от MainWindow
    public CocktailDetailPanel(MainWindow parent, Cocktail cocktail) {
        super(parent, "Подробно: " + cocktail.getName(), true); // true — модальное окно (блокирует родителя)
        this.cocktail = cocktail;                                // Сохраняем переданный коктейль

        setSize(700, 600);                                       // Устанавливаем размер окна 700×600
        setLocationRelativeTo(parent);                           // Центрируем окно относительно родителя
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);      // При закрытии — просто освобождаем ресурсы

        initComponents();                                        // Запускаем построение интерфейса
        setVisible(true);                                        // Делаем окно видимым сразу после создания
    }

    private void initComponents() {                              // Основной метод создания всего содержимого окна
        JPanel mainPanel = new JPanel(new BorderLayout());       // Главная панель с BorderLayout
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Отступы по 15 пикселей со всех сторон

        // ─────────────────────── ШАПКА ОКНА ───────────────────────
        JPanel headerPanel = new JPanel();                       // Панель шапки
        headerPanel.setBackground(new Color(33, 150, 243));      // Синий фон
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Внутренние отступы

        JLabel titleLabel = new JLabel(cocktail.getName());      // Название коктейля как заголовок
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));    // Крупный жирный шрифт
        titleLabel.setForeground(Color.WHITE);                   // Белый цвет текста
        headerPanel.add(titleLabel);                             // Добавляем заголовок в шапку

        mainPanel.add(headerPanel, BorderLayout.NORTH);          // Помещаем шапку в верхнюю часть

        // ─────────────────────── ВКЛАДКИ С ИНФОРМАЦИЕЙ ───────────────────────
        JTabbedPane tabbedPane = new JTabbedPane();              // Создаём вкладки

        JPanel infoPanel = createInfoPanel();                    // Вкладка 1 — общая информация
        tabbedPane.addTab("Информация", infoPanel);              // Добавляем вкладку

        JPanel ingredientsPanel = createIngredientsPanel();      // Вкладка 2 — ингредиенты
        tabbedPane.addTab("Ингредиенты", ingredientsPanel);      // Добавляем вкладку

        JPanel preparationPanel = createPreparationPanel();      // Вкладка 3 — шаги приготовления
        tabbedPane.addTab("Ход приготовления", preparationPanel); // Добавляем вкладку

        mainPanel.add(tabbedPane, BorderLayout.CENTER);          // Помещаем вкладки в центр

        // ─────────────────────── КНОПКА ЗАКРЫТИЯ ───────────────────────
        JButton closeButton = new JButton("Закрыть");            // Кнопка закрытия окна
        closeButton.addActionListener(e -> dispose());           // При нажатии — закрываем окно (dispose освобождает ресурсы)
        JPanel buttonPanel = new JPanel();                       // Панель для кнопки
        buttonPanel.add(closeButton);                            // Добавляем кнопку
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);         // Помещаем кнопку в нижнюю часть

        add(mainPanel);                                          // Добавляем главную панель в диалог
    }

    // Создаёт панель с общей информацией о коктейле
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();                              // Новая панель
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Вертикальное расположение элементов
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Отступы

        addLabeledInfo(panel, "Описание:", cocktail.getDescription());           // Добавляем строку с описанием
        addLabeledInfo(panel, "Алкогольная основа:", cocktail.getAlcoholBase()); // Добавляем алкогольную основу
        addLabeledInfo(panel, "Сложность:", cocktail.getDifficulty());           // Добавляем сложность
        addLabeledInfo(panel, "Время приготовления:", cocktail.getPreparationTime() + " минут"); // Время
        addLabeledInfo(panel, "Средняя оценка:", String.format("%.1f/5", cocktail.getAverageRating())); // Средний рейтинг

        panel.add(Box.createVerticalGlue());                     // Добавляем "пружину" — растягивает содержимое вверх
        return panel;                                            // Возвращаем готовую панель
    }

    // Создаёт панель с нумерованным списком ингредиентов
    private JPanel createIngredientsPanel() {
        JPanel panel = new JPanel(new BorderLayout());           // Панель с BorderLayout
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Отступы

        JTextArea textArea = new JTextArea();                    // Текстовое поле для ингредиентов
        textArea.setEditable(false);                             // Запрещаем редактирование
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Моноширинный шрифт для ровного выравнивания

        StringBuilder sb = new StringBuilder();                  // Буфер для формирования текста
        int i = 1;                                               // Счётчик для нумерации
        for (var ingredient : cocktail.getIngredients()) {      // Проходим по всем ингредиентам
            sb.append(i).append(". ").append(ingredient).append("\n"); // Добавляем номер и ингредиент
            i++;                                                 // Увеличиваем счётчик
        }

        textArea.setText(sb.toString());                         // Устанавливаем сформированный текст
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER); // Добавляем прокрутку и помещаем в центр

        return panel;                                            // Возвращаем панель
    }

    // Создаёт панель с пошаговым описанием приготовления
    private JPanel createPreparationPanel() {
        JPanel panel = new JPanel(new BorderLayout());           // Панель с BorderLayout
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Отступы

        JTextArea textArea = new JTextArea();                    // Текстовое поле для шагов
        textArea.setEditable(false);                             // Только чтение
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Моноширинный шрифт
        textArea.setLineWrap(true);                              // Перенос строк
        textArea.setWrapStyleWord(true);                         // Перенос по словам

        StringBuilder sb = new StringBuilder();                  // Буфер для текста
        for (PreparationStep step : cocktail.getPreparationSteps()) { // Проходим по всем шагам
            sb.append("═════════════════════════════════════\n");     // Разделитель
            sb.append("ШАГ ").append(step.getStepNumber()).append("\n"); // Номер шага
            sb.append("═════════════════════════════════════\n");     // Разделитель
            sb.append(step.getDescription()).append("\n\n");           // Основное описание шага
            sb.append("Совет: ").append(step.getTips()).append("\n"); // Полезный совет
            sb.append("Время: ").append(step.getDuration()).append(" сек\n\n"); // Время на шаг
        }

        textArea.setText(sb.toString());                         // Устанавливаем текст
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER); // Добавляем прокрутку

        return panel;                                            // Возвращаем панель
    }

    // Вспомогательный метод: добавляет пару "метка — значение" на панель
    private void addLabeledInfo(JPanel panel, String label, String value) {
        JLabel labelComp = new JLabel(label);                    // Метка (например, "Описание:")
        labelComp.setFont(new Font("Arial", Font.BOLD, 12));     // Жирный шрифт
        panel.add(labelComp);                                    // Добавляем метку

        JLabel valueComp = new JLabel(value);                    // Значение (например, сам текст описания)
        valueComp.setFont(new Font("Arial", Font.PLAIN, 11));    // Обычный шрифт
        panel.add(valueComp);                                    // Добавляем значение

        panel.add(Box.createVerticalStrut(10));                  // Добавляем вертикальный отступ 10 пикселей
    }
}