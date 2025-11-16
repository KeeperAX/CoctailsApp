package ui;

import models.Cocktail;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SearchPanel extends JPanel {
    private MainWindow mainWindow;

    private JTextField searchField;
    private JComboBox<String> alcoholBaseCombo;
    private JComboBox<String> difficultyCombo;
    private JButton searchButton;
    private JButton backButton;
    private JButton clearButton;
    private DefaultListModel<Cocktail> resultListModel;
    private JList<Cocktail> resultList;
    private JTextArea detailsArea;

    public SearchPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ===== TOP PANEL: SEARCH CRITERIA =====
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.setBorder(BorderFactory.createTitledBorder("Поиск коктейлей"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Search by name
        JLabel nameLabel = new JLabel("По названию:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        topPanel.add(nameLabel, gbc);

        searchField = new JTextField(15);
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        topPanel.add(searchField, gbc);

        // Search by alcohol base
        JLabel alcoholLabel = new JLabel("По основе:");
        gbc.gridx = 2;
        gbc.weightx = 0;
        topPanel.add(alcoholLabel, gbc);

        alcoholBaseCombo = new JComboBox<>();
        alcoholBaseCombo.addItem("Все");
        for (String base : mainWindow.getSearchService().getAvailableAlcoholBases()) {
            alcoholBaseCombo.addItem(base);
        }
        gbc.gridx = 3;
        gbc.weightx = 0.3;
        topPanel.add(alcoholBaseCombo, gbc);

        // Search by difficulty
        JLabel difficultyLabel = new JLabel("По сложности:");
        gbc.gridx = 4;
        gbc.weightx = 0;
        topPanel.add(difficultyLabel, gbc);

        difficultyCombo = new JComboBox<>();
        difficultyCombo.addItem("Все");
        for (String difficulty : mainWindow.getSearchService().getAvailableDifficulties()) {
            difficultyCombo.addItem(difficulty);
        }
        gbc.gridx = 5;
        gbc.weightx = 0.3;
        topPanel.add(difficultyCombo, gbc);

        // Buttons
        searchButton = new JButton("Поиск");
        searchButton.setBackground(new Color(76, 175, 80));
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> performSearch());
        gbc.gridx = 6;
        gbc.weightx = 0;
        topPanel.add(searchButton, gbc);

        clearButton = new JButton("Очистить");
        clearButton.addActionListener(e -> clearFilters());
        gbc.gridx = 7;
        topPanel.add(clearButton, gbc);

        backButton = new JButton("Назад");
        backButton.setBackground(new Color(158, 158, 158));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> mainWindow.showCocktailListPanel());
        gbc.gridx = 8;
        topPanel.add(backButton, gbc);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL: RESULTS =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        // Left: Results list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Результаты поиска"));

        resultListModel = new DefaultListModel<>();
        resultList = new JList<>(resultListModel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultList.addListSelectionListener(e -> updateDetailsArea());

        JScrollPane scrollPane = new JScrollPane(resultList);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        // Right: Details
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Информация"));

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

    private void performSearch() {
        String name = searchField.getText();
        String alcoholBase = (String) alcoholBaseCombo.getSelectedItem();
        String difficulty = (String) difficultyCombo.getSelectedItem();

        // Convert "Все" to empty strings for search
        if ("Все".equals(alcoholBase)) alcoholBase = "";
        if ("Все".equals(difficulty)) difficulty = "";

        List<Cocktail> results = mainWindow.getSearchService()
                .advancedSearch(name, alcoholBase, difficulty);

        resultListModel.clear();
        for (Cocktail c : results) {
            resultListModel.addElement(c);
        }

        if (results.isEmpty()) {
            detailsArea.setText("По вашему запросу ничего не найдено");
        }
    }

    private void clearFilters() {
        searchField.setText("");
        alcoholBaseCombo.setSelectedIndex(0);
        difficultyCombo.setSelectedIndex(0);
        resultListModel.clear();
        detailsArea.setText("");
    }

    private void updateDetailsArea() {
        Cocktail selected = resultList.getSelectedValue();
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

    public void refresh() {
        clearFilters();
        alcoholBaseCombo.removeAllItems();
        alcoholBaseCombo.addItem("Все");
        for (String base : mainWindow.getSearchService().getAvailableAlcoholBases()) {
            alcoholBaseCombo.addItem(base);
        }
    }
}
