package ui;

import services.CocktailService;
import services.DatabaseService;
import services.SearchService;
import services.UserService;
import models.User;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private DatabaseService databaseService;
    private CocktailService cocktailService;
    private SearchService searchService;
    private UserService userService;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private User currentUser = null;

    // UI Components
    private LoginPanel loginPanel;
    private RegistrationPanel registrationPanel;
    private CocktailListPanel cocktailListPanel;
    private SearchPanel searchPanel;
    private UserProfilePanel userProfilePanel;

    public MainWindow() {
        // Инициализация сервисов
        this.databaseService = new DatabaseService();
        this.cocktailService = new CocktailService(databaseService);
        this.searchService = new SearchService(databaseService);
        this.userService = new UserService(databaseService);

        // Настройка главного окна
        setTitle("🍹 Cocktail Manager - Управление коктейлями");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setResizable(true);

        // Установка внешнего вида
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Создание главной панели с CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Создание и добавление панелей
        loginPanel = new LoginPanel(this);
        registrationPanel = new RegistrationPanel(this);
        cocktailListPanel = new CocktailListPanel(this);
        searchPanel = new SearchPanel(this);
        userProfilePanel = new UserProfilePanel(this);

        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(registrationPanel, "REGISTRATION");
        cardPanel.add(cocktailListPanel, "COCKTAIL_LIST");
        cardPanel.add(searchPanel, "SEARCH");
        cardPanel.add(userProfilePanel, "PROFILE");

        add(cardPanel);

        // Показать экран входа по умолчанию
        cardLayout.show(cardPanel, "LOGIN");

        setVisible(true);
    }

    // ===== NAVIGATION METHODS =====

    public void showLoginPanel() {
        cardLayout.show(cardPanel, "LOGIN");
    }

    public void showRegistrationPanel() {
        cardLayout.show(cardPanel, "REGISTRATION");
    }

    public void showCocktailListPanel() {
        cocktailListPanel.refreshCocktails();
        cardLayout.show(cardPanel, "COCKTAIL_LIST");
    }

    public void showSearchPanel() {
        searchPanel.refresh();
        cardLayout.show(cardPanel, "SEARCH");
    }

    public void showProfilePanel() {
        userProfilePanel.refreshProfile();
        cardLayout.show(cardPanel, "PROFILE");
    }

    // ===== USER AUTHENTICATION =====

    public void login(User user) {
        this.currentUser = user;
        showCocktailListPanel();
    }

    public void logout() {
        this.currentUser = null;
        showLoginPanel();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    // ===== SERVICE GETTERS =====

    public CocktailService getCocktailService() {
        return cocktailService;
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public UserService getUserService() {
        return userService;
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow());
    }
}
