package ui;  // Объявляем, что класс находится в пакете ui

import models.Cocktail;  // Импортируем модель коктейля для выбора и отображения
import models.User;       // Импортируем модель пользователя для доступа к его данным

import javax.swing.*;               // Импортируем все компоненты Swing
import javax.swing.event.ChangeEvent;   // Импортируем событие изменения значения (для слайдера)
import java.awt.*;                      // Импортируем AWT: цвета, шрифты, компоновки

public class UserProfilePanel extends JPanel {  // Класс панели профиля пользователя, наследуется от JPanel

    private MainWindow mainWindow;  // Ссылка на главное окно приложения

    private JLabel usernameLabel;   // Метка для отображения имени пользователя
    private JLabel emailLabel;      // Метка для отображения email
    private JComboBox<Cocktail> cocktailCombo;  // Выпадающий список для выбора коктейля
    private JSlider ratingSlider;   // Слайдер для выбора оценки от 1 до 5
    private JButton rateButton;     // Кнопка "Оценить"
    private JButton backButton;     // Кнопка "Назад"
    private JTextArea ratingsArea;  // Текстовое поле для списка оценок пользователя

    public UserProfilePanel(MainWindow mainWindow) {  // Конструктор — принимает главное окно
        this.mainWindow = mainWindow;                 // Сохраняем ссылку на главное окно
        initComponents();                             // Запускаем создание интерфейса
    }

    private void initComponents() {                    // Основной метод построения интерфейса
        setLayout(new BorderLayout());                 // Устанавливаем основной layout — BorderLayout
        setBackground(new Color(245, 245, 245));       // Светло-серый фон панели

        // ─────────────────────── ВЕРХНЯЯ ПАНЕЛЬ: информация о пользователе ───────────────────────
        JPanel topPanel = new JPanel(new GridBagLayout());           // Панель с сеткой для аккуратного размещения
        topPanel.setBackground(new Color(33, 150, 243));             // Синий фон (как у шапки)
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Отступы по краям

        GridBagConstraints gbc = new GridBagConstraints();          // Настройки размещения элементов
        gbc.insets = new Insets(10, 10, 10, 10);                     // Отступы между элементами
        gbc.anchor = GridBagConstraints.WEST;                        // Выравнивание по левому краю

        JLabel profileLabel = new JLabel("Профиль пользователя");    // Заголовок профиля
        profileLabel.setFont(new Font("Arial", Font.BOLD, 20));      // Крупный жирный шрифт
        profileLabel.setForeground(Color.WHITE);                     // Белый текст
        gbc.gridx = 0;                                               // Столбец 0
        gbc.gridy = 0;                                               // Строка 0
        topPanel.add(profileLabel, gbc);                             // Добавляем заголовок

        JLabel usernameLabelTitle = new JLabel("Имя пользователя:"); // Подпись к имени
        usernameLabelTitle.setFont(new Font("Arial", Font.BOLD, 12)); // Жирный шрифт
        usernameLabelTitle.setForeground(Color.WHITE);                // Белый цвет
        gbc.gridy = 1;                                                // Строка 1
        topPanel.add(usernameLabelTitle, gbc);                        // Добавляем подпись

        usernameLabel = new JLabel("");                               // Поле для отображения имени (пока пустое)
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));     // Обычный шрифт
        usernameLabel.setForeground(Color.WHITE);                     // Белый текст
        gbc.gridx = 1;                                                // Столбец 1
        topPanel.add(usernameLabel, gbc);                             // Добавляем значение имени

        JLabel emailLabelTitle = new JLabel("Email:");                // Подпись к email
        emailLabelTitle.setFont(new Font("Arial", Font.BOLD, 12));    // Жирный шрифт
        emailLabelTitle.setForeground(Color.WHITE);                   // Белый цвет
        gbc.gridx = 0;                                                // Столбец 0
        gbc.gridy = 2;                                                // Строка 2
        topPanel.add(emailLabelTitle, gbc);                           // Добавляем подпись

        emailLabel = new JLabel("");                                 // Поле для отображения email
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));        // Обычный шрифт
        emailLabel.setForeground(Color.WHITE);                        // Белый текст
        gbc.gridx = 1;                                                // Столбец 1
        topPanel.add(emailLabel, gbc);                                // Добавляем значение email

        add(topPanel, BorderLayout.NORTH);                            // Добавляем верхнюю панель в север

        // ─────────────────────── ЦЕНТРАЛЬНАЯ ПАНЕЛЬ: оценка коктейлей ───────────────────────
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));        // Делим центр на два столбца

        // Левая часть — форма оценки коктейля
        JPanel ratingPanel = new JPanel(new GridBagLayout());         // Панель с сеткой
        ratingPanel.setBorder(BorderFactory.createTitledBorder("Оценить коктейль")); // Рамка с заголовком
        ratingPanel.setBackground(new Color(250, 250, 250));          // Светлый фон

        gbc = new GridBagConstraints();                               // Пересоздаём gbc для новой панели
        gbc.insets = new Insets(10, 10, 10, 10);                      // Отступы
        gbc.fill = GridBagConstraints.HORIZONTAL;                     // Растягивание по горизонтали

        JLabel selectCocktailLabel = new JLabel("Выберите коктейль:"); // Подпись к комбобоксу
        gbc.gridx = 0;                                                // Столбец 0
        gbc.gridy = 0;                                                // Строка 0
        gbc.gridwidth = 2;                                            // Занимает два столбца
        ratingPanel.add(selectCocktailLabel, gbc);                     // Добавляем подпись

        cocktailCombo = new JComboBox<>();                            // Выпадающий список коктейлей
        gbc.gridy = 1;                                                // Строка 1
        ratingPanel.add(cocktailCombo, gbc);                           // Добавляем комбобокс

        JLabel ratingLabel = new JLabel("Ваша оценка:");              // Подпись к слайдеру
        gbc.gridy = 2;                                                // Строка 2
        gbc.gridwidth = 1;                                            // Один столбец
        ratingPanel.add(ratingLabel, gbc);                             // Добавляем подпись

        ratingSlider = new JSlider(1, 5, 3);                          // Слайдер: от 1 до 5, начальное значение 3
        ratingSlider.setMajorTickSpacing(1);                          // Деления через 1
        ratingSlider.setPaintTicks(true);                             // Показывать деления
        ratingSlider.setPaintLabels(true);                            // Показывать цифры под делениями
        ratingSlider.addChangeListener(this::onRatingChanged);        // Слушатель изменения значения
        gbc.gridx = 1;                                                // Столбец 1
        ratingPanel.add(ratingSlider, gbc);                            // Добавляем слайдер

        rateButton = new JButton("Оценить");                          // Кнопка отправки оценки
        rateButton.setBackground(new Color(76, 175, 80));             // Зелёный фон
        rateButton.setForeground(Color.WHITE);                        // Белый текст
        rateButton.addActionListener(e -> submitRating());            // При нажатии — отправляем оценку
        gbc.gridx = 0;                                                // Столбец 0
        gbc.gridy = 3;                                                // Строка 3
        gbc.gridwidth = 2;                                            // Занимает два столбца
        ratingPanel.add(rateButton, gbc);                              // Добавляем кнопку

        ratingPanel.add(Box.createVerticalGlue());                    // Добавляем "пружину" для выравнивания

        // Правая часть — список моих оценок
        JPanel myRatingsPanel = new JPanel(new BorderLayout());       // Панель с BorderLayout
        myRatingsPanel.setBorder(BorderFactory.createTitledBorder("Мои оценки")); // Рамка
        myRatingsPanel.setBackground(new Color(250, 250, 250));       // Светлый фон

        ratingsArea = new JTextArea();                                // Текстовое поле для списка оценок
        ratingsArea.setEditable(false);                               // Только чтение
        ratingsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));  // Моноширинный шрифт

        JScrollPane scrollPane = new JScrollPane(ratingsArea);        // Добавляем прокрутку
        myRatingsPanel.add(scrollPane, BorderLayout.CENTER);          // Помещаем в центр

        centerPanel.add(ratingPanel);                                 // Добавляем левую часть
        centerPanel.add(myRatingsPanel);                              // Добавляем правую часть

        add(centerPanel, BorderLayout.CENTER);                        // Добавляем центральную панель

        // ─────────────────────── НИЖНЯЯ ПАНЕЛЬ: кнопка "Назад" ───────────────────────
        JPanel bottomPanel = new JPanel(new FlowLayout());            // Панель с горизонтальным выравниванием

        backButton = new JButton("Назад");                            // Кнопка возврата
        backButton.setBackground(new Color(158, 158, 158));           // Серый фон
        backButton.setForeground(Color.WHITE);                        // Белый текст
        backButton.addActionListener(e -> mainWindow.showCocktailListPanel()); // Возврат к списку коктейлей
        bottomPanel.add(backButton);                                  // Добавляем кнопку

        add(bottomPanel, BorderLayout.SOUTH);                        // Добавляем нижнюю панель
    }

    private void onRatingChanged(ChangeEvent e) {                  // Метод вызывается при движении слайдера
        // Пока пустой — можно добавить предпросмотр оценки в реальном времени
    }

    private void submitRating() {                                 // Метод отправки оценки
        Cocktail selected = (Cocktail) cocktailCombo.getSelectedItem(); // Получаем выбранный коктейль
        if (selected == null) {                                   // Проверка: выбран ли коктейль
            JOptionPane.showMessageDialog(this, "Выберите коктейль для оценивания"); // Сообщение об ошибке
            return;                                               // Прерываем выполнение
        }

        User currentUser = mainWindow.getCurrentUser();           // Получаем текущего пользователя
        int rating = ratingSlider.getValue();                     // Получаем значение слайдера

        mainWindow.getUserService().rateCocktail(currentUser.getId(), selected.getId(), rating); // Сохраняем оценку

        JOptionPane.showMessageDialog(this, "Оценка сохранена!"); // Уведомление об успехе
        refreshRatings();                                         // Обновляем список оценок
    }

    public void refreshProfile() {                                // Метод обновления данных профиля (вызывается при открытии)
        User currentUser = mainWindow.getCurrentUser();           // Получаем текущего пользователя
        if (currentUser != null) {                                // Если пользователь авторизован
            usernameLabel.setText(currentUser.getUsername());     // Обновляем имя
            emailLabel.setText(currentUser.getEmail());           // Обновляем email
        }

        cocktailCombo.removeAllItems();                           // Очищаем список коктейлей
        for (Cocktail c : mainWindow.getCocktailService().getAllCocktails()) { // Перебираем все коктейли
            cocktailCombo.addItem(c);                             // Добавляем каждый в комбобокс
        }

        refreshRatings();                                         // Обновляем список оценок
    }

    private void refreshRatings() {                               // Обновление правой части — список оценок пользователя
        User currentUser = mainWindow.getCurrentUser();           // Получаем текущего пользователя
        StringBuilder sb = new StringBuilder();                   // Создаём строковый буфер

        if (currentUser == null || currentUser.getRatings().isEmpty()) { // Если нет оценок
            sb.append("Вы ещё не оценивали коктейли");            // Сообщение по умолчанию
        } else {
            sb.append("Мои оценки:\n");                           // Заголовок
            sb.append("═══════════════════════════════════\n");   // Разделитель
            for (var entry : currentUser.getRatings().entrySet()) { // Перебираем все оценки
                Cocktail c = mainWindow.getCocktailService().getCocktailById(entry.getKey()); // Находим коктейль по ID
                if (c != null) {                                  // Если коктейль найден
                    sb.append(c.getName()).append(": ");          // Название коктейля
                    sb.append("(".repeat(entry.getValue()));     // Звёздочки по количеству баллов
                    sb.append(" (").append(entry.getValue()).append("/5)\n"); // Числовое значение
                }
            }
        }

        ratingsArea.setText(sb.toString());                       // Выводим результат в текстовое поле
    }
}