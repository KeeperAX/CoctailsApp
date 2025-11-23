package ui;  // Объявляем, что класс находится в пакете ui

import models.Cocktail;  // Импортируем модель коктейля для отображения в списке и деталях
import models.User;       // Импортируем модель пользователя (не используется напрямую здесь, но может быть нужен позже)

import javax.swing.*;               // Импортируем все компоненты Swing
import java.awt.*;                      // Импортируем AWT: цвета, шрифты, компоновки
import java.util.List;                  // Импортируем List для получения списка всех коктейлей

public class CocktailListPanel extends JPanel {  // Панель со списком коктейлей — основная после входа

    private MainWindow mainWindow;  // Ссылка на главное окно для доступа к сервисам и навигации

    private JList<Cocktail> cocktailList;            // Список всех коктейлей
    private DefaultListModel<Cocktail> listModel;    // Модель данных для динамического обновления списка
    private JButton viewDetailsButton;               // Кнопка «Подробнее» — открывает отдельное окно
    private JButton searchButton;                    // Кнопка перехода на поиск
    private JButton profileButton;                   // Кнопка перехода в профиль
    private JButton logoutButton;                    // Кнопка выхода из аккаунта
    private JButton refreshButton;                   // Кнопка обновления списка
    private JTextArea detailsArea;                   // Область с краткой информацией о выбранном коктейле

    public CocktailListPanel(MainWindow mainWindow) {  // Конструктор — принимает главное окно
        this.mainWindow = mainWindow;                  // Сохраняем ссылку на главное окно
        initComponents();                              // Запускаем создание интерфейса
    }

    private void initComponents() {                     // Основной метод построения всего интерфейса
        setLayout(new BorderLayout());                  // Устанавливаем BorderLayout как основной layout
        setBackground(new Color(245, 245, 245));        // Светло-серый фон панели

        // ─────────────────────── ВЕРХНЯЯ ПАНЕЛЬ: меню и кнопки навигации ───────────────────────
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Панель с горизонтальным выравниванием слева
        topPanel.setBackground(new Color(33, 33, 33));                  // Тёмно-серый фон (как у навигационной панели)

        JLabel titleLabel = new JLabel("Каталог коктейлей");           // Заголовок страницы
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));           // Крупный жирный шрифт
        titleLabel.setForeground(Color.WHITE);                         // Белый текст
        topPanel.add(titleLabel);                                       // Добавляем заголовок

        topPanel.add(Box.createHorizontalGlue());                      // Добавляем "пружину" — растягивает всё вправо

        searchButton = new JButton("Поиск");                            // Кнопка перехода на панель поиска
        searchButton.addActionListener(e -> mainWindow.showSearchPanel()); // При нажатии — показываем поиск
        topPanel.add(searchButton);                                    // Добавляем кнопку

        profileButton = new JButton("Профиль");                         // Кнопка перехода в профиль
        profileButton.addActionListener(e -> mainWindow.showProfilePanel()); // Переход в профиль
        topPanel.add(profileButton);                                    // Добавляем кнопку

        logoutButton = new JButton("Выход");                            // Кнопка выхода из аккаунта
        logoutButton.setBackground(new Color(244, 67, 54));             // Красный фон
        logoutButton.setForeground(Color.WHITE);                        // Белый текст
        logoutButton.addActionListener(e -> mainWindow.logout());       // Вызываем метод logout() в главном окне
        topPanel.add(logoutButton);                                     // Добавляем кнопку

        add(topPanel, BorderLayout.NORTH);                              // Помещаем верхнюю панель в север

        // ─────────────────────── ЦЕНТРАЛЬНАЯ ЧАСТЬ: список и детали ───────────────────────
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));          // Делим центр на два столбца

        // Левая часть — список коктейлей
        JPanel leftPanel = new JPanel(new BorderLayout());              // Панель с BorderLayout
        leftPanel.setBorder(BorderFactory.createTitledBorder("Список коктейлей")); // Рамка с заголовком

        listModel = new DefaultListModel<>();                           // Создаём модель для динамического списка
        cocktailList = new JList<>(listModel);                          // Создаём JList на основе модели
        cocktailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Разрешаем выбирать только один коктейль
        cocktailList.setFont(new Font("Arial", Font.PLAIN, 12));        // Устанавливаем шрифт
        cocktailList.addListSelectionListener(e -> updateDetailsArea()); // При выборе — обновляем детали справа

        JScrollPane scrollPane = new JScrollPane(cocktailList);         // Добавляем прокрутку к списку
        leftPanel.add(scrollPane, BorderLayout.CENTER);                 // Помещаем список в центр левой панели

        // Панель с кнопками под списком
        JPanel leftButtonPanel = new JPanel(new FlowLayout());          // Панель с горизонтальным расположением
        viewDetailsButton = new JButton("Подробнее");                   // Кнопка открытия детального окна
        viewDetailsButton.addActionListener(e -> {                      // Обработчик нажатия
            Cocktail selected = cocktailList.getSelectedValue();        // Получаем выбранный коктейль
            if (selected != null) {                                     // Если что-то выбрано
                new CocktailDetailPanel(mainWindow, selected).setVisible(true); // Открываем новое окно с деталями
            }
        });
        leftButtonPanel.add(viewDetailsButton);                         // Добавляем кнопку "Подробнее"

        refreshButton = new JButton("Обновить");                        // Кнопка принудительного обновления списка
        refreshButton.addActionListener(e -> refreshCocktails());       // При нажатии — перезагружаем данные
        leftButtonPanel.add(refreshButton);                             // Добавляем кнопку "Обновить"

        leftPanel.add(leftButtonPanel, BorderLayout.SOUTH);             // Помещаем кнопки в низ левой панели

        // Правая часть — краткая информация о выбранном коктейле
        JPanel rightPanel = new JPanel(new BorderLayout());             // Панель с BorderLayout
        rightPanel.setBorder(BorderFactory.createTitledBorder("Информация о коктейле")); // Рамка

        detailsArea = new JTextArea();                                  // Текстовое поле для деталей
        detailsArea.setEditable(false);                                 // Только чтение
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));    // Моноширинный шрифт для выравнивания
        detailsArea.setLineWrap(true);                                  // Перенос по строкам
        detailsArea.setWrapStyleWord(true);                             // Перенос по словам

        JScrollPane detailsScroll = new JScrollPane(detailsArea);       // Прокрутка для длинного текста
        rightPanel.add(detailsScroll, BorderLayout.CENTER);             // Помещаем в центр

        centerPanel.add(leftPanel);                                     // Добавляем левую часть
        centerPanel.add(rightPanel);                                    // Добавляем правую часть

        add(centerPanel, BorderLayout.CENTER);                          // Добавляем центральную панель в центр
    }

    public void refreshCocktails() {                                    // Метод обновления списка коктейлей
        listModel.clear();                                              // Очищаем текущий список
        List<Cocktail> cocktails = mainWindow.getCocktailService().getAllCocktails(); // Получаем свежие данные из сервиса
        for (Cocktail c : cocktails) {                                  // Проходим по всем коктейлям
            listModel.addElement(c);                                    // Добавляем каждый в модель
        }
    }

    private void updateDetailsArea() {                                  // Обновляет правую часть при выборе коктейля
        Cocktail selected = cocktailList.getSelectedValue();            // Получаем выбранный коктейль
        if (selected != null) {                                         // Если что-то выбрано
            StringBuilder sb = new StringBuilder();                     // Создаём буфер для текста
            sb.append("═══════════════════════════════════\n");        // Верхняя линия
            sb.append("НАЗВАНИЕ: ").append(selected.getName()).append("\n"); // Название
            sb.append("═══════════════════════════════════\n\n");        // Нижняя линия

            sb.append("Описание:\n").append(selected.getDescription()).append("\n\n"); // Описание
            sb.append("Алкогольная основа: ").append(selected.getAlcoholBase()).append("\n"); // Основа
            sb.append("Сложность: ").append(selected.getDifficulty()).append("\n");         // Сложность
            sb.append("Время приготовления: ").append(selected.getPreparationTime()).append(" мин\n"); // Время
            sb.append("Средняя оценка: ").append(String.format("%.1f/5", selected.getAverageRating())).append("\n\n"); // Рейтинг

            sb.append("Ингредиенты:\n");                                // Заголовок ингредиентов
            selected.getIngredients().forEach(ing -> sb.append("  • ").append(ing).append("\n")); // Каждый ингредиент с точкой

            detailsArea.setText(sb.toString());                         // Выводим весь текст в правую область
        }
    }
}