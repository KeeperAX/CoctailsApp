package ui;

import models.User;
import utils.ValidationUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {
    private MainWindow mainWindow;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;

    public LoginPanel(MainWindow mainWindow) {
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
        JLabel titleLabel = new JLabel("🍹 Вход в Cocktail Manager");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Username Label
        JLabel usernameLabel = new JLabel("Имя пользователя:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(usernameLabel, gbc);

        // Username Field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        add(usernameField, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Пароль:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        // Password Field
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Message Label
        messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(messageLabel, gbc);

        // Login Button
        loginButton = new JButton("Войти");
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.setBackground(new Color(76, 175, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this::handleLogin);
        gbc.gridy = 4;
        add(loginButton, gbc);

        // Register Button
        registerButton = new JButton("Создать аккаунт");
        registerButton.setFont(new Font("Arial", Font.BOLD, 12));
        registerButton.setBackground(new Color(33, 150, 243));
        registerButton.setForeground(Color.WHITE);
        registerButton.addActionListener(e -> mainWindow.showRegistrationPanel());
        gbc.gridy = 5;
        add(registerButton, gbc);
    }

    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (!ValidationUtil.isNotEmpty(username) || !ValidationUtil.isNotEmpty(password)) {
            messageLabel.setText("Заполните все поля");
            return;
        }

        User user = mainWindow.getUserService().loginUser(username, password);
        if (user != null) {
            messageLabel.setText("");
            clearFields();
            mainWindow.login(user);
        } else {
            messageLabel.setText("Неверное имя пользователя или пароль");
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}
