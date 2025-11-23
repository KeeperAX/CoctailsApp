package ui;  // Объявляем, что класс находится в пакете ui

import models.Cocktail;  // Импортируем модель коктейля, чтобы работать с объектами Cocktail

import javax.swing.*;  // Импортируем все классы из пакета javax.swing (JPanel, JButton, JList и т.д.)
import java.awt.*;  // Импортируем все классы из java.awt (BorderLayout, GridBagLayout, Color и т.д.)
import java.util.List;  // Импортируем интерфейс List для хранения списка найденных коктейлей

public class SearchPanel extends JPanel {  // Объявляем публичный класс SearchPanel, который наследуется от JPanel

    private MainWindow mainWindow;  // Поле для хранения ссылки на главное окно приложения

    private JTextField searchField;  // Поле ввода для поиска по названию коктейля
    private JComboBox<String> alcoholBaseCombo;  // Выпадающий список для выбора алкогольной основы
    private JComboBox<String> difficultyCombo;  // Выпадающий список для выбора сложности приготовления
    private JButton searchButton;   // Кнопка «Поиск»
    private JButton backButton;     // Кнопка «Назад»
    private JButton clearButton;    // Кнопка «Очистить»
    private DefaultListModel<Cocktail> resultListModel;  // Модель данных для динамического списка результатов
    private JList<Cocktail> resultList;                  // Список, в котором отображаются найденные коктейли
    private JTextArea detailsArea;                       // Текстовое поле для подробного описания выбранного коктейля

    public SearchPanel(MainWindow mainWindow) {  // Конструктор класса, принимает главное окно
        this.mainWindow = mainWindow;            // Сохраняем переданную ссылку на MainWindow
        initComponents();                        // Вызываем метод, который создаёт весь интерфейс
    }

    private void initComponents() {              // Метод, отвечающий за построение всего GUI
        setLayout(new BorderLayout());           // Устанавливаем основной менеджер компоновки — BorderLayout
        setBackground(new Color(245, 245, 245)); // Задаём светло-серый фон всей панели

        // ─────────────────────── ВЕРХНЯЯ ПАНЕЛЬ: фильтры поиска ───────────────────────
        JPanel topPanel = new JPanel(new GridBagLayout()); // Создаём панель с сеткой для элементов фильтров
        topPanel.setBackground(new Color(240, 240, 240));  // Чуть темнее фон у панели фильтров
        topPanel.setBorder(BorderFactory.createTitledBorder("Поиск коктейлей")); // Добавляем рамку с заголовком

        GridBagConstraints gbc = new GridBagConstraints(); // Объект настроек расположения в GridBagLayout
        gbc.insets = new Insets(8, 8, 8, 8);      // Отступы 8 пикселей со всех сторон
        gbc.fill = GridBagConstraints.HORIZONTAL; // Элементы растягиваются по горизонтали

        // Метка «По названию:»
        JLabel nameLabel = new JLabel("По названию:");
        gbc.gridx = 0;          // Столбец 0
        gbc.gridy = 0;          // Строка 0
        gbc.weightx = 0;        // Не растягивать метку
        topPanel.add(nameLabel, gbc); // Добавляем метку на панель

        // Поле ввода названия
        searchField = new JTextField(15);
        gbc.gridx = 1;          // Столбец 1
        gbc.weightx = 0.3;      // Растягивать поле
        topPanel.add(searchField, gbc);

        // Метка «По основе:»
        JLabel alcoholLabel = new JLabel("По основе:");
        gbc.gridx = 2;
        gbc.weightx = 0;
        topPanel.add(alcoholLabel, gbc);

        // Выпадающий список алкогольных основ
        alcoholBaseCombo = new JComboBox<>();
        alcoholBaseCombo.addItem("Все"); // Первый пункт — без фильтра
        for (String base : mainWindow.getSearchService().getAvailableAlcoholBases()) {
            alcoholBaseCombo.addItem(base); // Заполняем реальными основами из сервиса
        }
        gbc.gridx = 3;
        gbc.weightx = 0.3;
        topPanel.add(alcoholBaseCombo, gbc);

        // Метка «По сложности:»
        JLabel difficultyLabel = new JLabel("По сложности:");
        gbc.gridx = 4;
        gbc.weightx = 0;
        topPanel.add(difficultyLabel, gbc);

        // Выпадающий список сложности
        difficultyCombo = new JComboBox<>();
        difficultyCombo.addItem("Все");
        for (String difficulty : mainWindow.getSearchService().getAvailableDifficulties()) {
            difficultyCombo.addItem(difficulty);
        }
        gbc.gridx = 5;
        gbc.weightx = 0.3;
        topPanel.add(difficultyCombo, gbc);

        // Кнопка «Поиск»
        searchButton = new JButton("Поиск");
        searchButton.setBackground(new Color(76, 175, 80)); // Зелёный фон
        searchButton.setForeground(Color.WHITE);           // Белый текст
        searchButton.addActionListener(e -> performSearch()); // При нажатии вызываем метод поиска
        gbc.gridx = 6;
        gbc.weightx = 0;
        topPanel.add(searchButton, gbc);

        // Кнопка «Очистить»
        clearButton = new JButton("Очистить");
        clearButton.addActionListener(e -> clearFilters()); // При нажатии очищаем все фильтры
        gbc.gridx = 7;
        topPanel.add(clearButton, gbc);

        // Кнопка «Назад»
        backButton = new JButton("Назад");
        backButton.setBackground(new Color(158, 158, 158));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> mainWindow.showCocktailListPanel()); // Возврат к списку коктейлей
        gbc.gridx = 8;
        topPanel.add(backButton, gbc);

        add(topPanel, BorderLayout.NORTH); // Добавляем всю верхнюю панель в северную часть основной панели

        // ─────────────────────── ЦЕНТРАЛЬНАЯ ЧАСТЬ: результаты ───────────────────────
        JPanel centerPanel = new JPanel(new GridLayout(1, 2)); // Делим центр на два столбца

        // Левая часть — список результатов
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Результаты поиска"));

        resultListModel = new DefaultListModel<>();   // Создаём модель, которую можно динамически менять
        resultList = new JList<>(resultListModel);    // Список, использующий эту модель
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Разрешаем выбирать только один элемент
        resultList.addListSelectionListener(e -> updateDetailsArea());     // При выборе — обновляем детали справа

        JScrollPane scrollPane = new JScrollPane(resultList); // Добавляем прокрутку к списку
        leftPanel.add(scrollPane, BorderLayout.CENTER); // Помещаем список в центр левой панели

        // Правая часть — подробности о коктейле
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Информация"));

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);                     // Запрещаем редактировать текст
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 11)); // Моноширинный шрифт для выравнивания
        detailsArea.setLineWrap(true);                      // Перенос строк
        detailsArea.setWrapStyleWord(true);                 // Перенос по словам

        JScrollPane detailsScroll = new JScrollPane(detailsArea); // Прокрутка для текста
        rightPanel.add(detailsScroll, BorderLayout.CENTER);

        centerPanel.add(leftPanel);   // Добавляем левую часть
        centerPanel.add(rightPanel);  // Добавляем правую часть

        add(centerPanel, BorderLayout.CENTER); // Помещаем всю центральную часть в центр основной панели
    }

    private void performSearch() {                     // Метод, выполняющий поиск
        String name = searchField.getText().trim();    // Получаем текст из поля (без пробелов по краям)
        String alcoholBase = (String) alcoholBaseCombo.getSelectedItem(); // Текущая выбранная основа
        String difficulty = (String) difficultyCombo.getSelectedItem();   // Текущая выбранная сложность

        if ("Все".equals(alcoholBase)) alcoholBase = "";   // "Все" → пустая строка (отключаем фильтр)
        if ("Все".equals(difficulty)) difficulty = "";     // Аналогично для сложности

        List<Cocktail> results = mainWindow.getSearchService() // Обращаемся к сервису поиска
                .advancedSearch(name, alcoholBase, difficulty); // Выполняем расширенный поиск

        resultListModel.clear();                          // Очищаем предыдущие результаты
        for (Cocktail c : results) {                       // Проходим по всем найденным коктейлям
            resultListModel.addElement(c);                 // Добавляем каждый в список
        }

        if (results.isEmpty()) {                           // Если ничего не найдено
            detailsArea.setText("По вашему запросу ничего не найдено"); // Показываем сообщение
        }
    }

    private void clearFilters() {                      // Метод очистки всех фильтров
        searchField.setText("");                       // Очищаем поле названия
        alcoholBaseCombo.setSelectedIndex(0);          // Ставим "Все" в выпадающих списках
        difficultyCombo.setSelectedIndex(0);
        resultListModel.clear();                       // Удаляем все результаты из списка
        detailsArea.setText("");                        // Очищаем область с деталями
    }

    private void updateDetailsArea() {                 // Обновляет правую часть при выборе коктейля
        Cocktail selected = resultList.getSelectedValue(); // Получаем выбранный коктейль
        if (selected != null) {                        // Если что-то выбрано
            StringBuilder sb = new StringBuilder();    // Создаём строковый буфер
            sb.append("═══════════════════════════════════\n");
            sb.append("НАЗВАНИЕ: ").append(selected.getName()).append("\n");
            sb.append("═══════════════════════════════════\n\n");
            sb.append("Описание:\n").append(selected.getDescription()).append("\n\n");
            sb.append("Алкогольная основа: ").append(selected.getAlcoholBase()).append("\n");
            sb.append("Сложность: ").append(selected.getDifficulty()).append("\n");
            sb.append("Время приготовления: ").append(selected.getPreparationTime()).append(" мин\n");
            sb.append("Средняя оценка: ").append(String.format("%.1f/5", selected.getAverageRating())).append("\n\n");

            sb.append("Ингредиенты:\n");
            selected.getIngredients().forEach(ing -> sb.append("  • ").append(ing).append("\n"));

            detailsArea.setText(sb.toString());        // Выводим весь сформированный текст
        }
    }

    public void refresh() {                            // Метод для обновления панели при возврате на неё
        clearFilters();                                // Сначала очищаем всё
        alcoholBaseCombo.removeAllItems();             // Удаляем старые пункты списка основ
        alcoholBaseCombo.addItem("Все");               // Добавляем пункт «Все»
        for (String base : mainWindow.getSearchService().getAvailableAlcoholBases()) {
            alcoholBaseCombo.addItem(base);            // Заново заполняем актуальными основами
        }
        // При необходимости можно обновить и список сложности аналогично
    }
}