package app;

import javax.swing.*;
import java.awt.*;

/**
 * The SignUpPage class represents the sign-up page for user registration.
 * It allows users to enter their name, email, password, and select a difficulty level.
 */
public class SignUpPage extends JFrame {
    /**
     * Constructs the SignUpPage UI.
     * Sets up the layout, components, and event listeners.
     */
    public SignUpPage() {
        setTitle("Sign Up");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/diwashadhikari/Desktop/back_back.png").getImage()
                .getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(backIcon);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.addActionListener(e -> {
            dispose();
            new Home();
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(backButton);

        JLabel nameLabel = new JLabel("Name:");
        styleLabel(nameLabel);
        JTextField nameField = new JTextField(20);
        styleTextField(nameField);

        JLabel emailLabel = new JLabel("Email:");
        styleLabel(emailLabel);
        JTextField emailField = new JTextField(20);
        styleTextField(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        styleLabel(passwordLabel);
        JPasswordField passwordField = new JPasswordField(20);
        styleTextField(passwordField);

        JLabel levelLabel = new JLabel("Select Level:");
        styleLabel(levelLabel);
        String[] levels = {"BEGINNER", "INTERMEDIATE", "ADVANCE"};
        JComboBox<String> levelDropdown = new JComboBox<>(levels);
        levelDropdown.setFont(new Font("Arial", Font.PLAIN, 18));
        levelDropdown.setPreferredSize(new Dimension(250, 40));

        JButton signUpButton = new JButton("Sign Up");
        styleButton(signUpButton, new Color(41, 128, 185));

        /**
         * Action listener for the sign-up button.
         * Validates user input and registers the user.
         */
        signUpButton.addActionListener(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String level = (String) levelDropdown.getSelectedItem();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(SignUpPage.this, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!EmailValidiator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(SignUpPage.this, "Please Enter a Valid email", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(SignUpPage.this, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String hashedPassword = PasswordHash.hashPassword(password);
            ReturnType response = JDBC.registerUser(name, email, hashedPassword, level.toUpperCase());

            if (response.success) {
                JOptionPane.showMessageDialog(SignUpPage.this, response.msg, "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new SignInPage();
            } else {
                JOptionPane.showMessageDialog(SignUpPage.this, response.msg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridy = 0; mainPanel.add(nameLabel, gbc);
        gbc.gridy = 1; mainPanel.add(nameField, gbc);
        gbc.gridy = 2; mainPanel.add(emailLabel, gbc);
        gbc.gridy = 3; mainPanel.add(emailField, gbc);
        gbc.gridy = 4; mainPanel.add(passwordLabel, gbc);
        gbc.gridy = 5; mainPanel.add(passwordField, gbc);
        gbc.gridy = 6; mainPanel.add(levelLabel, gbc);
        gbc.gridy = 7; mainPanel.add(levelDropdown, gbc);
        gbc.gridy = 8; mainPanel.add(signUpButton, gbc);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Styles a button with a specific background color.
     * @param button the button to style
     * @param bgColor the background color
     */
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 50));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    /**
     * Styles a label with font and color.
     * @param label the label to style
     */
    private void styleLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.BLACK);
    }

    /**
     * Styles a text field with font and size.
     * @param field the text field to style
     */
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(250, 40));
    }
}
