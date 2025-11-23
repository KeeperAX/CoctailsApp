package ui;  // Определение пакета: класс относится к пользовательскому интерфейсу

import utils.ValidationUtil;  // Импорт утилиты для проверки корректности ввода (пустые поля, формат email, пароля и т.д.)

import javax.swing.*;  // Импорт всех компонентов Swing
import java.awt.*;        // Импорт базовых классов AWT (цвета, шрифты, компоновка и т.д.)
import java.awt.event.ActionEvent;  // Импорт класса события нажатия на кнопку

public class RegistrationPanel extends JPanel {  // Класс панели регистрации, наследуется от JPanel

    private MainWindow mainWindow;  // Ссылка на главное окно приложения — нужна для переключения панелей

    // Поля ввода
    private JTextField usernameField;           // Поле для имени пользователя
    private JTextField emailField;              // Поле для email
    private JPasswordField passwordField;       // Поле для пароля
    private JPasswordField confirmPasswordField; // Поле для подтверждения пароля

    // Кнопки
    private JButton registerButton;  // Кнопка «Зарегистрироваться»
    private JButton backButton;      // Кнопка «Назад»

    // Метка для вывода сообщений об ошибках/успехе
    private JLabel messageLabel;

    // Конструктор — вызывается при создании панели
    public RegistrationPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;   // Сохраняем ссылку на главное окно
        initComponents();               // Инициализируем все элементы интерфейса
    }

    private void initComponents() {  // Метод создания и размещения всех компонентов
        setLayout(new GridBagLayout());                 // Используем гибкую сетку GridBagLayout
        setBackground(new Color(245, 245, 245));        // Светло-серый фон

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);        // Отступы вокруг всех элементов
        gbc.fill = GridBagConstraints.HORIZONTAL;      // Растягивать элементы по горизонтали

        // Заголовок
        JLabel titleLabel = new JLabel("Регистрация нового пользователя");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;                              // Занимает два столбца
        add(titleLabel, gbc);

        // Имя пользователя
        JLabel usernameLabel = new JLabel("Имя пользователя:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        add(usernameField, gbc);

        // Email
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(emailLabel, gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        add(emailField, gbc);

        // Пароль
        JLabel passwordLabel = new JLabel("Пароль:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Подтверждение пароля
        JLabel confirmLabel = new JLabel("Подтвердить пароль:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(confirmLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(confirmPasswordField, gbc);

        // Метка для сообщений
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(messageLabel, gbc);

        // Кнопка «Зарегистрироваться»
        registerButton = new JButton("Зарегистрироваться");
        registerButton.setBackground(new Color(76, 175, 80));   // Зелёный
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(this::handleRegister); // Привязываем обработчик
        gbc.gridy = 6;
        add(registerButton, gbc);

        // Кнопка «Назад»
        backButton = new JButton("Назад");
        backButton.setBackground(new Color(158, 158, 158));    // Серый
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> mainWindow.showLoginPanel()); // Возврат на экран входа
        gbc.gridy = 7;
        add(backButton, gbc);
    }

    // Обработчик нажатия кнопки «Зарегистрироваться»
    private void handleRegister(ActionEvent e) {
        // Получаем и обрезаем введённые данные
        String username = usernameField.getText().trim();
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Проверка на пустые поля
        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(email) ||
                !ValidationUtil.isNotEmpty(password)) {
            messageLabel.setText("Заполните все поля");
            return;
        }

        // Проверка совпадения паролей
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Пароли не совпадают");
            return;
        }

        // Валидация имени пользователя
        if (!ValidationUtil.isValidUsername(username)) {
            messageLabel.setText("Username: 3-20 символов, буквы/цифры/_");
            return;
        }

        // Валидация email
        if (!ValidationUtil.isValidEmail(email)) {
            messageLabel.setText("Некорректный email");
            return;
        }

        // Валидация сложности пароля
        if (!ValidationUtil.isValidPassword(password)) {
            messageLabel.setText("Пароль: минимум 6 символов");
            return;
        }

        // Попытка зарегистрировать пользователя через UserService
        boolean success = mainWindow.getUserService().registerUser(username, email, password);

        if (success) {
            // Успешная регистрация
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Регистрация успешна! Перенаправление...");

            // Автоматический переход на экран входа через 2 секунды
            Timer timer = new Timer(2000, e1 -> mainWindow.showLoginPanel());
            timer.setRepeats(false);   // Выполнится только один раз
            timer.start();
        } else {
            // Пользователь с таким именем уже существует
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Такой пользователь уже существует");
        }
    }
}