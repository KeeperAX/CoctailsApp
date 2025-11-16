package ui;

import models.Cocktail;
import models.PreparationStep;

import javax.swing.*;
import java.awt.*;

public class CocktailDetailPanel extends JDialog {
    private Cocktail cocktail;

    public CocktailDetailPanel(MainWindow parent, Cocktail cocktail) {
        super(parent, "Подробно: " + cocktail.getName(), true);
        this.cocktail = cocktail;

        setSize(700, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== HEADER =====
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(cocktail.getName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // ===== CENTER: DETAILS =====
        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Info
        JPanel infoPanel = createInfoPanel();
        tabbedPane.addTab("Информация", infoPanel);

        // Tab 2: Ingredients
        JPanel ingredientsPanel = createIngredientsPanel();
        tabbedPane.addTab("Ингредиенты", ingredientsPanel);

        // Tab 3: Preparation
        JPanel preparationPanel = createPreparationPanel();
        tabbedPane.addTab("Ход приготовления", preparationPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // ===== CLOSE BUTTON =====
        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addLabeledInfo(panel, "Описание:", cocktail.getDescription());
        addLabeledInfo(panel, "Алкогольная основа:", cocktail.getAlcoholBase());
        addLabeledInfo(panel, "Сложность:", cocktail.getDifficulty());
        addLabeledInfo(panel, "Время приготовления:", cocktail.getPreparationTime() + " минут");
        addLabeledInfo(panel, "Средняя оценка:", String.format("%.1f/5", cocktail.getAverageRating()));

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createIngredientsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (var ingredient : cocktail.getIngredients()) {
            sb.append(i).append(". ").append(ingredient).append("\n");
            i++;
        }

        textArea.setText(sb.toString());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPreparationPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        StringBuilder sb = new StringBuilder();
        for (PreparationStep step : cocktail.getPreparationSteps()) {
            sb.append("═════════════════════════════════════\n");
            sb.append("ШАГ ").append(step.getStepNumber()).append("\n");
            sb.append("═════════════════════════════════════\n");
            sb.append(step.getDescription()).append("\n\n");
            sb.append("💡 Совет: ").append(step.getTips()).append("\n");
            sb.append("⏱ Время: ").append(step.getDuration()).append(" сек\n\n");
        }

        textArea.setText(sb.toString());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);

        return panel;
    }

    private void addLabeledInfo(JPanel panel, String label, String value) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(labelComp);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Arial", Font.PLAIN, 11));
        panel.add(valueComp);

        panel.add(Box.createVerticalStrut(10));
    }
}
