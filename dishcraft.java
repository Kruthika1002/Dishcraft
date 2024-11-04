import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.border.EmptyBorder;

public class Dishcraft extends JFrame {
    private JTextArea ingredientInput;
    private JTextArea recipeOutput;
    private ArrayList<Recipe> recipeDatabase;
    private static final String CSV_FILE = "recipes.csv";

    private static class Recipe {
        String ingredients;
        String name;
        String instructions;

        Recipe(String ingredients, String name, String instructions) {
            this.ingredients = ingredients.toLowerCase();
            this.name = name;
            this.instructions = instructions;
        }
    }

    // Custom JPanel for background image
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = new ImageIcon("dishcraft_bg.jpg").getImage();
                setLayout(new BorderLayout(20, 20));
                setBorder(new EmptyBorder(200, 200, 200, 200));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public Dishcraft() {
        setTitle("DishCraft - Recipe Suggestions");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);

        // Initialize recipe database
        recipeDatabase = new ArrayList<>();
        loadRecipesFromCSV();

        // Create main split pane
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setDividerLocation(1000);
        mainSplitPane.setDividerSize(1);
        mainSplitPane.setBorder(null);

        // Left panel with background image and input components
        BackgroundPanel leftPanel = new BackgroundPanel();
        
        // Create input components with transparent background
        JPanel inputPanel = createInputPanel();
        inputPanel.setOpaque(false);
        
        // Add input components to the background panel
        leftPanel.add(inputPanel, BorderLayout.CENTER);

        // Right panel for output
        JPanel rightPanel = createRightPanel();
        rightPanel.setPreferredSize(new Dimension(400, getHeight()));
        rightPanel.setMinimumSize(new Dimension(300, getHeight()));

        // Add panels to main split pane
        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightPanel);

        add(mainSplitPane);
    }


    // Modify the createInputPanel method to remove the Add Recipe button
private JPanel createInputPanel() {
    JPanel inputPanel = new JPanel(new BorderLayout(50, 50));
    inputPanel.setOpaque(false);

    // Header
    JLabel headerLabel = new JLabel("Enter Ingredients");
    headerLabel.setFont(new Font("Script MT Bold", Font.BOLD, 25));
    headerLabel.setHorizontalAlignment(JLabel.CENTER);
    headerLabel.setForeground(Color.darkGray);

    // Input section
    JLabel inputLabel = new JLabel("Available ingredients (one per line):");
    inputLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
    inputLabel.setForeground(Color.BLACK);
    
    ingredientInput = new JTextArea(10, 25);
    ingredientInput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    ingredientInput.setLineWrap(true);
    ingredientInput.setWrapStyleWord(true);
    JScrollPane inputScroll = new JScrollPane(ingredientInput);
    inputScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

    // Make the input area semi-transparent
    ingredientInput.setOpaque(true);
    ingredientInput.setBackground(new Color(255, 255, 255, 220));

    // Button Panel (now with only Find Recipes button)
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
    buttonPanel.setOpaque(false);
    
    JButton findRecipesButton = createStyledButton("Find Recipes");
    findRecipesButton.addActionListener(e -> findRecipes());
    
    buttonPanel.add(findRecipesButton);

    // Add components to panel
    JPanel contentPanel = new JPanel(new BorderLayout(10, 20));
    contentPanel.setOpaque(false);
    contentPanel.add(inputLabel, BorderLayout.NORTH);
    contentPanel.add(inputScroll, BorderLayout.CENTER);
    contentPanel.add(buttonPanel, BorderLayout.SOUTH);

    inputPanel.add(headerLabel, BorderLayout.NORTH);
    inputPanel.add(contentPanel, BorderLayout.CENTER);

    return inputPanel;
}

// Modify the createRightPanel method to add the Add Recipe button at the bottom
private JPanel createRightPanel() {
    JPanel rightPanel = new JPanel(new BorderLayout(20, 20));
    rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    rightPanel.setBackground(new Color(255, 255, 255, 220));

    // Header
    JLabel headerLabel = new JLabel("Suggested Recipes");
    headerLabel.setFont(new Font("Script MT Bold", Font.BOLD, 32));
    headerLabel.setHorizontalAlignment(JLabel.CENTER);
    headerLabel.setForeground(new Color(70, 130, 180));

    // Output area
    recipeOutput = new JTextArea(20, 30);
    recipeOutput.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    recipeOutput.setEditable(false);
    recipeOutput.setLineWrap(true);
    recipeOutput.setWrapStyleWord(true);
    JScrollPane outputScroll = new JScrollPane(recipeOutput);
    outputScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

    // Create Add Recipe button panel
    JPanel addRecipePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
    addRecipePanel.setBackground(new Color(255, 255, 255, 220));
    
    JButton addRecipeButton = createStyledButton("Add New Recipe");
    addRecipeButton.addActionListener(e -> showAddRecipeDialog());
    addRecipePanel.add(addRecipeButton);

    // Create a panel for output area and button
    JPanel centerPanel = new JPanel(new BorderLayout());
    centerPanel.setOpaque(false);
    centerPanel.add(outputScroll, BorderLayout.CENTER);
    centerPanel.add(addRecipePanel, BorderLayout.SOUTH);

    rightPanel.add(headerLabel, BorderLayout.NORTH);
    rightPanel.add(centerPanel, BorderLayout.CENTER);

    return rightPanel;
}


    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.BLUE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadRecipesFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    recipeDatabase.add(new Recipe(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            showError("Error loading recipes: " + e.getMessage());
        }
    }

    private void saveRecipe(String ingredients, String name, String instructions) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_FILE, true))) {
            writer.println(ingredients + ";" + name + ";" + instructions);
            recipeDatabase.add(new Recipe(ingredients, name, instructions));
        } catch (IOException e) {
            showError("Error saving recipe: " + e.getMessage());
        }
    }

    private void showAddRecipeDialog() {
        JDialog dialog = new JDialog(this, "Add New Recipe", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel nameLabel = new JLabel("Recipe Name:");
        JTextField nameField = new JTextField(30);

        JLabel ingredientsLabel = new JLabel("Ingredients (comma-separated):");
        JTextField ingredientsField = new JTextField(30);
        
        JLabel instructionsLabel = new JLabel("Cooking Instructions:");
        JTextArea instructionsArea = new JTextArea(10, 30);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(instructionsArea);

        JButton saveButton = createStyledButton("Save Recipe");
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String ingredients = ingredientsField.getText().trim().toLowerCase();
            String instructions = instructionsArea.getText().trim();
            
            if (!name.isEmpty() && !ingredients.isEmpty() && !instructions.isEmpty()) {
                saveRecipe(ingredients, name, instructions);
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Recipe added successfully!");
            } else {
                showError("Please fill in all fields");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        gbc.gridy = 1;
        panel.add(nameField, gbc);

        gbc.gridy = 2;
        panel.add(ingredientsLabel, gbc);

        gbc.gridy = 3;
        panel.add(ingredientsField, gbc);

        gbc.gridy = 4;
        panel.add(instructionsLabel, gbc);

        gbc.gridy = 5;
        panel.add(scrollPane, gbc);

        gbc.gridy = 6;
        panel.add(saveButton, gbc);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void findRecipes() {
        String[] userIngredients = ingredientInput.getText().toLowerCase().split("\n");
        Set<String> availableIngredients = new HashSet<>();
        for (String ingredient : userIngredients) {
            ingredient = ingredient.trim();
            if (!ingredient.isEmpty()) {
                availableIngredients.add(ingredient);
            }
        }

        if (availableIngredients.isEmpty()) {
            recipeOutput.setText("Please enter at least one ingredient!");
            return;
        }

        StringBuilder suggestions = new StringBuilder();
        for (Recipe recipe : recipeDatabase) {
            for (String ingredient : availableIngredients) {
                if (recipe.ingredients.contains(ingredient)) {
                    suggestions.append("Recipe: ").append(recipe.name)
                             .append("\n\nRequired Ingredients:\n");
                    
                    String[] recipeIngredients = recipe.ingredients.split(",");
                    for (String ri : recipeIngredients) {
                        suggestions.append("- ").append(ri.trim())
                                 .append(availableIngredients.contains(ri.trim()) ? " (✓)\n" : " (✗)\n");
                    }
                    
                    suggestions.append("\nInstructions:\n").append(recipe.instructions)
                             .append("\n\n----------------------------\n\n");
                    break;
                }
            }
        }
        
        if (suggestions.length() == 0) {
            recipeOutput.setText("No recipes found with these ingredients. Try different ingredients!");
        } else {
            recipeOutput.setText(suggestions.toString());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new Dishcraft().setVisible(true);
        });
    }
}
