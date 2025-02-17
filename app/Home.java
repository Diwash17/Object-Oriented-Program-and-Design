package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Home class represents the main landing screen of the MyQuiz application.
 * It provides options for users to either sign up for a new account or sign in to an existing one.
 * The UI is designed with a modern and user-friendly layout.
 */
public class Home extends JFrame {
    
    /**
     * Constructor to initialize the home screen UI with a welcome message and buttons for navigation.
     */
    public Home() {
        setTitle("MyQuiz App"); // Set window title
        setSize(600, 500); // Set window size
        setLocationRelativeTo(null); // Center the window on the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close application on exit

        // Create the main panel with BorderLayout for structured placement
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        // Header panel for the title
        JLabel welcomeLabel = new JLabel("Welcome to MyQuiz App", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 32)); // Set font style
        welcomeLabel.setForeground(new Color(52, 73, 94)); // Dark blue text
        mainPanel.add(welcomeLabel, BorderLayout.NORTH); // Add label to the top

        // Panel to hold buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 20, 20)); // Grid layout for buttons
        buttonPanel.setBackground(new Color(245, 245, 245)); // Match background color
        
        // Create and style buttons
        JButton signUpButton = new JButton("Sign Up");
        JButton signInButton = new JButton("Sign In");
        styleButton(signUpButton);
        styleButton(signInButton);
        
        buttonPanel.add(signUpButton);
        buttonPanel.add(signInButton);
        
        // Center the button panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(buttonPanel);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER); // Add buttons to the center

        // Footer panel
        JLabel footerLabel = new JLabel("Enhance Your Knowledge with Fun Quizzes!", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        footerLabel.setForeground(new Color(100, 100, 100)); // Soft gray
        mainPanel.add(footerLabel, BorderLayout.SOUTH); // Add footer to the bottom

        // Add action listeners for buttons
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new SignUpPage(); // Open sign-up page
            }
        });

        signInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                new SignInPage(); // Open sign-in page
            }
        });

        add(mainPanel); // Add the main panel to the frame
        setVisible(true); // Make the frame visible
    }

    /**
     * Styles a JButton to match the modern UI theme with enhanced visuals.
     *
     * @param button The JButton to style.
     */
    public static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18)); // Set font style
        button.setBackground(new Color(52, 152, 219)); // Blue background
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false); // Remove focus border
        button.setPreferredSize(new Dimension(250, 50)); // Set button size
        button.setOpaque(true); // Make the background solid
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding inside button
    }

    /**
     * The main method to launch the Home screen.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        new Home();
    }
}
