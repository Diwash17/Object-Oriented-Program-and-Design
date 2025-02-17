package app;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * The DashBoardUI class represents the main dashboard UI that displays a set of buttons for admin actions.
 * Each button is associated with a specific action that can be performed by the admin.
 */
public class DashBoardUI {

    /**
     * Constructor to create and display the dashboard UI with a set of buttons for different actions.
     *
     * @param title   The title of the dashboard, typically a welcome message for the admin.
     * @param actions A map containing button labels and their associated actions. The keys are button labels,
     *                and the values are the actions to be performed when the buttons are clicked.
     */
    public DashBoardUI(String title, Map<String, Runnable> actions) {
        // Create the frame for the dashboard UI
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        // Create the main panel to hold the buttons and title
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(255, 255, 255)); // Light background color

        // Set up GridBagConstraints for button layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create and set the title label for the dashboard
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.BLACK); // Black text for better contrast on light background

        // Create a panel for the title to ensure proper spacing
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(255, 255, 255)); // Light background color
        titlePanel.add(titleLabel);

        // Add the title label to the frame
        frame.add(titlePanel, BorderLayout.NORTH);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(255, 255, 255)); // Light background for buttons as well

        // Add action buttons to the panel
        int row = 0;
        for (String buttonText : actions.keySet()) {
            JButton button = createButton(buttonText, frame, actions.get(buttonText));
            gbc.gridy = row++;
            buttonPanel.add(button, gbc);
        }

        // Add the button panel to the frame
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * Creates a button with the given label and associates it with a specific action.
     *
     * @param text  The label to be displayed on the button.
     * @param frame The JFrame to dispose when the button is clicked.
     * @param action The action to be performed when the button is clicked.
     * @return A JButton with the specified label and action.
     */
    private JButton createButton(String text, JFrame frame, Runnable action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setBackground(new Color(241, 241, 241)); // Lighter background for buttons
        button.setForeground(Color.BLACK); // Dark text for visibility
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(600, 90));
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // Set the action for the button when clicked
        button.addActionListener(e -> {
            frame.dispose(); // Close the current frame
            try {
                action.run(); // Execute the associated action
            } catch (Exception ex) {
                ex.printStackTrace(); // Handle any exceptions that occur during action execution
            }
        });
        return button;
    }
}