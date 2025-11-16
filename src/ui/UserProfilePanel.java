package ui;

import models.Cocktail;
import models.User;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;

public class UserProfilePanel extends JPanel {
    private MainWindow mainWindow;

    private JLabel usernameLabel;
    private JLabel emailLabel;
    private JComboBox<Cocktail> cocktailCombo;
    private JSlider ratingSlider;
    private JButton rateButton;
    private JButton backButton;
    private JTextArea ratingsArea;

    public UserProfilePanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // ===== TOP PANEL: USER INFO =====
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(new Color(33, 150, 243));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel profileLabel = new JLabel("👤 Профиль пользователя");
        profileLabel.setFont(new Font("Arial", Font.BOLD, 20));
        profileLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(profileLabel, gbc);

        JLabel usernameLabelTitle = new JLabel("Имя пользователя:");
        usernameLabelTitle.setFont(new Font("Arial", Font.BOLD, 12));
        usernameLabelTitle.setForeground(Color.WHITE);
        gbc.gridy = 1;
        topPanel.add(usernameLabelTitle, gbc);

        usernameLabel = new JLabel("");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        topPanel.add(usernameLabel, gbc);

        JLabel emailLabelTitle = new JLabel("Email:");
        emailLabelTitle.setFont(new Font("Arial", Font.BOLD, 12));
        emailLabelTitle.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        topPanel.add(emailLabelTitle, gbc);

        emailLabel = new JLabel("");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        emailLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        topPanel.add(emailLabel, gbc);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL: RATING SECTION =====
        JPanel centerPanel = new JPanel(new GridLayout(1, 2));

        // Left: Rate cocktails
        JPanel ratingPanel = new JPanel(new GridBagLayout());
        ratingPanel.setBorder(BorderFactory.createTitledBorder("Оценить коктейль"));
        ratingPanel.setBackground(new Color(250, 250, 250));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel selectCocktailLabel = new JLabel("Выберите коктейль:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        ratingPanel.add(selectCocktailLabel, gbc);

        cocktailCombo = new JComboBox<>();
        gbc.gridy = 1;
        ratingPanel.add(cocktailCombo, gbc);

        JLabel ratingLabel = new JLabel("Ваша оценка:");
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        ratingPanel.add(ratingLabel, gbc);

        ratingSlider = new JSlider(1, 5, 3);
        ratingSlider.setMajorTickSpacing(1);
        ratingSlider.setPaintTicks(true);
        ratingSlider.setPaintLabels(true);
        ratingSlider.addChangeListener(this::onRatingChanged);
        gbc.gridx = 1;
        ratingPanel.add(ratingSlider, gbc);

        rateButton = new JButton("Оценить");
        rateButton.setBackground(new Color(76, 175, 80));
        rateButton.setForeground(Color.WHITE);
        rateButton.addActionListener(e -> submitRating());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        ratingPanel.add(rateButton, gbc);

        ratingPanel.add(Box.createVerticalGlue());

        // Right: My ratings
        JPanel myRatingsPanel = new JPanel(new BorderLayout());
        myRatingsPanel.setBorder(BorderFactory.createTitledBorder("Мои оценки"));
        myRatingsPanel.setBackground(new Color(250, 250, 250));

        ratingsArea = new JTextArea();
        ratingsArea.setEditable(false);
        ratingsArea.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(ratingsArea);
        myRatingsPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(ratingPanel);
        centerPanel.add(myRatingsPanel);

        add(centerPanel, BorderLayout.CENTER);

        // ===== BOTTOM PANEL: BUTTONS =====
        JPanel bottomPanel = new JPanel(new FlowLayout());

        backButton = new JButton("Назад");
        backButton.setBackground(new Color(158, 158, 158));
        backButton.setForeground(Color.WHITE);
        backButton.addActionListener(e -> mainWindow.showCocktailListPanel());
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void onRatingChanged(ChangeEvent e) {
        // Может использоваться для предпросмотра оценки
    }

    private void submitRating() {
        Cocktail selected = (Cocktail) cocktailCombo.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Выберите коктейль для оценивания");
            return;
        }

        User currentUser = mainWindow.getCurrentUser();
        int rating = ratingSlider.getValue();

        mainWindow.getUserService().rateCocktail(currentUser.getId(), selected.getId(), rating);

        JOptionPane.showMessageDialog(this, "Оценка сохранена!");
        refreshRatings();
    }

    public void refreshProfile() {
        User currentUser = mainWindow.getCurrentUser();
        if (currentUser != null) {
            usernameLabel.setText(currentUser.getUsername());
            emailLabel.setText(currentUser.getEmail());
        }

        // Обновить комбобокс с коктейлями
        cocktailCombo.removeAllItems();
        for (Cocktail c : mainWindow.getCocktailService().getAllCocktails()) {
            cocktailCombo.addItem(c);
        }

        refreshRatings();
    }

    private void refreshRatings() {
        User currentUser = mainWindow.getCurrentUser();
        StringBuilder sb = new StringBuilder();

        if (currentUser == null || currentUser.getRatings().isEmpty()) {
            sb.append("Вы ещё не оценивали коктейли");
        } else {
            sb.append("Мои оценки:\n");
            sb.append("═══════════════════════════════════\n");
            for (var entry : currentUser.getRatings().entrySet()) {
                Cocktail c = mainWindow.getCocktailService().getCocktailById(entry.getKey());
                if (c != null) {
                    sb.append(c.getName()).append(": ");
                    sb.append("⭐".repeat(entry.getValue()));
                    sb.append(" (").append(entry.getValue()).append("/5)\n");
                }
            }
        }

        ratingsArea.setText(sb.toString());
    }
}
