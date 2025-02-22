package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The SignInPage class represents the sign-in window for users.
 * It allows users to enter their email and password for authentication.
 */
public class SignInPage extends JFrame {

    /**
     * Constructs the SignInPage frame and initializes the UI components.
     */
    public SignInPage() {
        setTitle("Sign In");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set light theme background color
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(255, 255, 255)); // Light white background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Back Button with Icon
        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/diwashadhikari/Desktop/back_back.png").getImage()
                .getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(backIcon);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        /**
         * ActionListener for the back button to navigate to the Home screen.
         */
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Home();
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(255, 255, 255)); // Light white background
        topPanel.add(backButton);

        JLabel emailLabel = new JLabel("Email:");
        styleLabel(emailLabel);

        JTextField emailField = new JTextField(20);
        styleTextField(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        styleLabel(passwordLabel);

        JPasswordField passwordField = new JPasswordField(20);
        styleTextField(passwordField);

        JButton loginButton = new JButton("Login");
        styleButton(loginButton, new Color(41, 128, 185));

        /**
         * ActionListener for the login button to validate user credentials.
         */
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                // Validate input fields
                if (email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(SignInPage.this, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!EmailValidiator.isValidEmail(email)) {
                    JOptionPane.showMessageDialog(SignInPage.this, "Please Enter a Valid email", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (password.length() < 6) {
                    JOptionPane.showMessageDialog(SignInPage.this, "Password must be at least 6 characters!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Authenticate user
                ReturnType response = JDBC.checkLogin(email, password);
                if (response.success) {
                    JOptionPane.showMessageDialog(SignInPage.this, response.msg, "Success", JOptionPane.INFORMATION_MESSAGE);
                    if (response.user.getRole().equals("USER")) {
                        dispose();
                        new UserPannelUI(response.user);
                    } else {
                        dispose();
                        new AdminPanelUI(response.user);
                    }
                } else {
                    JOptionPane.showMessageDialog(SignInPage.this, response.msg, "Error", JOptionPane.ERROR_MESSAGE);
                    emailField.setText("");
                    passwordField.setText("");
                }
            }
        });

        gbc.gridy = 0;
        mainPanel.add(emailLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(emailField, gbc);

        gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);

        gbc.gridy = 3;
        mainPanel.add(passwordField, gbc);

        gbc.gridy = 4;
        mainPanel.add(loginButton, gbc);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Styles a JButton with custom font, colors, and padding.
     * @param button The JButton to style.
     * @param bgColor The background color of the button.
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
     * Styles a JLabel with a custom font and color.
     * @param label The JLabel to style.
     */
    private void styleLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(new Color(44, 62, 80)); // Darker text color
    }

    /**
     * Styles a JTextField with a custom font and size.
     * @param field The JTextField to style.
     */
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(250, 40));
        field.setBackground(new Color(245, 245, 245)); // Light gray background
    }
}