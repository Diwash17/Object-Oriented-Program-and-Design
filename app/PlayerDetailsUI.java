package app;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * This class provides the user interface for viewing the details of a player.
 * It allows searching for a player by their ID (email) and displays detailed
 * information about the player in a well-structured format.
 */
class PlayerDetailsUI {
    private JLabel nameLabel, emailLabel, levelLabel, scoreLabel, fullDetailsLabel;
    private JPanel detailsPanel;

    /**
     * Constructor that initializes the PlayerDetailsUI frame and components.
     *
     * @param user The currently logged-in user (Competitor object).
     */
    public PlayerDetailsUI(Compitetor user) {
        // Create the main frame for the player details UI
        JFrame frame = new JFrame("Player Details");
        frame.setSize(900, 500); // Increased size for better visibility
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(245,245,245)); // Dark blue background for frame

        // Panel for input (player ID search)
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(245,245,245)); // Dark blue background for input panel
        inputPanel.setLayout(new FlowLayout());

        // Create search field and button
        JTextField searchField = new JTextField(15);
        JButton seeResultButton = new JButton("See Result");
        styleButton(seeResultButton); // Style the button

        // Add components to the input panel
        inputPanel.add(new JLabel("Enter Player ID: ")); // Label for search field
        inputPanel.add(searchField);
        inputPanel.add(seeResultButton);

        // Panel to display player details
        detailsPanel = new JPanel();
        detailsPanel.setBackground(new Color(245,245,245)); // Dark blue background for details panel
        detailsPanel.setLayout(new GridLayout(5, 1, 10, 10)); // Increased spacing for readability
        detailsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK), 
                "Player Details", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 18), 
                Color.BLACK)); // Titled border for player details section

        // Initialize labels to display player details
        nameLabel = createStyledLabel("Name: " + user.getName());
        emailLabel = createStyledLabel("Email: " + user.getEmail());
        levelLabel = createStyledLabel("Level: " + user.getLevel());
        scoreLabel = createStyledLabel("Overall Score: " + user.getOverallScore() + "/25");
        fullDetailsLabel = createStyledLabel(user.getFullDetails());

        // Add labels to the details panel
        detailsPanel.add(nameLabel);
        detailsPanel.add(emailLabel);
        detailsPanel.add(levelLabel);
        detailsPanel.add(scoreLabel);
        detailsPanel.add(fullDetailsLabel);

        // Action listener for "See Result" button
        seeResultButton.addActionListener(e -> {
            String inputId = searchField.getText(); // Get the input ID (email)
            ReturnType response = JDBC.getUserByEmail(inputId);
            
            // If user not found or there is an error
            if (!response.success) {
                JOptionPane.showMessageDialog(frame, response.msg, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // If user found, update the labels with the new player's data
                Compitetor newUser = response.user;
                nameLabel.setText("Name: " + newUser.getName());
                emailLabel.setText("Email: " + newUser.getEmail());
                levelLabel.setText("Level: " + newUser.getLevel());
                scoreLabel.setText("Overall Score: " + newUser.getOverallScore() + "/25");
                fullDetailsLabel.setText(newUser.getFullDetails());
                detailsPanel.revalidate(); // Revalidate panel to reflect updated data
                detailsPanel.repaint(); // Repaint panel
            }
        });

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.NORTH); // Input panel at the top
        frame.add(detailsPanel, BorderLayout.CENTER); // Details panel in the center

        // Create a JScrollPane to make the panel scrollable
        JScrollPane scrollPane = new JScrollPane(detailsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane, BorderLayout.CENTER); // Add scrollable details panel to the frame

        // Window listener to return to the user panel when closed
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose(); // Close current frame
                new UserPannelUI(user); // Return to the user panel
            }
        });

        // Set frame visibility
        frame.setVisible(true);
    }

    /**
     * Helper method to create styled labels.
     *
     * @param text The text to display in the label.
     * @return A JLabel with custom styling.
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20)); // Set font to Arial, bold, size 20
        label.setForeground(Color.BLACK); // Set text color to white
        return label;
    }

    /**
     * Helper method to style buttons with custom fonts and colors.
     *
     * @param button The JButton to style.
     */
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Set font to Arial, bold, size 16
        button.setBackground(new Color(52, 152, 219)); // Set background color to light blue
        button.setForeground(Color.BLACK); // Set text color to white
        button.setFocusPainted(false); // Remove focus paint when clicked
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Set padding for button
    }
}