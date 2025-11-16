package ui;

import models.Cocktail;
import models.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CocktailListPanel extends JPanel {
    private MainWindow mainWindow;

    private JList<Cocktail> cocktailList;
    private DefaultListModel<Cocktail> listModel;
    private JButton viewDetailsButton;
    private JButton searchButton;
    private JButton profileButton;
    private JButton logoutButton;
    private JButton refreshButton;
    private JTextArea detailsArea;

    public CocktailListPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ===== TOP PANEL: MENU BAR =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(33, 33, 33));

        JLabel titleLabel = new JLabel("🍹 Каталог коктейлей");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel);

        topPanel.add(Box.createHorizontalGlue());

        searchButton = new JButton("🔍 Поиск");
        searchButton.addActionListener(e -> mainWindow.showSearchPanel());
        topPanel.add(searchButton);

        profileButton = new JButton("👤 Профиль");
        profileButton.addActionListener(e -> mainWindow.showProfilePanel());
        topPanel.add(profileButton);

        logoutButton = new JButton("Выход");
        logoutButton.setBackground(new Color(244, 67, 54));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.addActionListener(e -> mainWindow.logout());
        topPanel.add(logoutButton);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL: LIST AND DETAILS =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        // Left: List of cocktails
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Список коктейлей"));

        listModel = new DefaultListModel<>();
        cocktailList = new JList<>(listModel);
        cocktailList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cocktailList.setFont(new Font("Arial", Font.PLAIN, 12));
        cocktailList.addListSelectionListener(e -> updateDetailsArea());

        JScrollPane scrollPane = new JScrollPane(cocktailList);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons in left panel
        JPanel leftButtonPanel = new JPanel(new FlowLayout());
        viewDetailsButton = new JButton("Подробнее");
        viewDetailsButton.addActionListener(e -> {
            Cocktail selected = cocktailList.getSelectedValue();
            if (selected != null) {
                new CocktailDetailPanel(mainWindow, selected).setVisible(true);
            }
        });
        leftButtonPanel.add(viewDetailsButton);

        refreshButton = new JButton("Обновить");
        refreshButton.addActionListener(e -> refreshCocktails());
        leftButtonPanel.add(refreshButton);

        leftPanel.add(leftButtonPanel, BorderLayout.SOUTH);

        // Right: Details
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Информация о коктейле"));

        detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);

        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        rightPanel.add(detailsScroll, BorderLayout.CENTER);

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void refreshCocktails() {
        listModel.clear();
        List<Cocktail> cocktails = mainWindow.getCocktailService().getAllCocktails();
        for (Cocktail c : cocktails) {
            listModel.addElement(c);
        }
    }

    private void updateDetailsArea() {
        Cocktail selected = cocktailList.getSelectedValue();
        if (selected != null) {
            StringBuilder sb = new StringBuilder();
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

            detailsArea.setText(sb.toString());
        }
    }
}
