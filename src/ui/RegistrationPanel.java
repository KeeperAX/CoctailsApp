package ui;

import utils.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegistrationPanel extends JPanel {
    private MainWindow mainWindow;

    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;
    private JLabel messageLabel;

    public RegistrationPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Заголовок
        JLabel titleLabel = new JLabel("Регистрация нового пользователя");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Username
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

        // Password
        JLabel passwordLabel = new JLabel("Пароль:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Confirm Password
        JLabel confirmLabel = new JLabel("Подтвердить пароль:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(confirmLabel, gbc);

        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(confirmPasswordField, gbc);

        // Message Label
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        add(messageLabel, gbc);

        // Register Button
        registerButton = new JButton("Зарегистрироваться");
        registerButton.setBackground(new Color(76, 175, 80));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(this::handleRegister);
        gbc.gridy = 6;
        add(registerButton, gbc);

        // Back Button
        backButton = new JButton("Назад");
        backButton.setBackground(new Color(158, 158, 158));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> mainWindow.showLoginPanel());
        gbc.gridy = 7;
        add(backButton, gbc);
    }

    private void handleRegister(ActionEvent e) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Валидация
        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(email) ||
                !ValidationUtil.isNotEmpty(password)) {
            messageLabel.setText("Заполните все поля");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Пароли не совпадают");
            return;
        }

        if (!ValidationUtil.isValidUsername(username)) {
            messageLabel.setText("Username: 3-20 символов, буквы/цифры/_");
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            messageLabel.setText("Некорректный email");
            return;
        }

        if (!ValidationUtil.isValidPassword(password)) {
            messageLabel.setText("Пароль: минимум 6 символов");
            return;
        }

        // Регистрация
        if (mainWindow.getUserService().registerUser(username, email, password)) {
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Регистрация успешна! Перенаправление...");
            Timer timer = new Timer(2000, e1 -> mainWindow.showLoginPanel());
            timer.setRepeats(false);
            timer.start();
        } else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Такой пользователь уже существует");
        }
    }
}
