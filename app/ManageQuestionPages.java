package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * The ManageQuestionPages class provides an interface for the admin to manage quiz questions.
 * Admins can view, update, and delete quiz questions from the database.
 * This UI follows a modern, clean theme with contrasting colors to enhance usability.
 */
public class ManageQuestionPages {

    // Define theme colors for UI elements
    private static final Color DARK_BACKGROUND = new Color(245, 245, 245);
    private static final Color PANEL_BACKGROUND = new Color(245, 245, 245);
    private static final Color TEXT_COLOR = new Color(0, 0, 0);

    private static final Color UPDATE_COLOR = new Color(80, 250, 123); // Green update button
    private static final Color DELETE_COLOR = new Color(255, 85, 85); // Red delete button

    private JFrame frame;
    private JPanel contentPanel;
    private ArrayList<JPanel> questionPanels;

    /**
     * Constructor to initialize and display the Manage Questions UI.
     *
     * @param admin The admin user managing the questions.
     */
    public ManageQuestionPages(Compitetor admin) {
        frame = new JFrame("Manage Questions");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(DARK_BACKGROUND);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS)); // Changed layout orientation
        contentPanel.setBackground(DARK_BACKGROUND);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        questionPanels = new ArrayList<>();

        // Back button with custom styling
        JButton backButton = createIconButton("/Users/diwashadhikari/Desktop/back_back.png");
        backButton.addActionListener(e -> {
            frame.dispose();
            new AdminPanelUI(admin); // Navigate back to Admin Panel
        });

        // Top panel with back button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(DARK_BACKGROUND);
        topPanel.add(backButton);
        contentPanel.add(topPanel);

        // Title label for the screen
        JLabel lblTitle = new JLabel("Manage Questions", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(TEXT_COLOR);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(lblTitle);

        // Fetch all questions and render them
        ArrayList<Questions> questions = JDBC.fetchAllQuestions();
        for (Questions question : questions) {
            renderQuestion(question, admin);
        }

        // Scroll pane to make content scrollable
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.getViewport().setBackground(DARK_BACKGROUND);
        scrollPane.setBorder(null);
        frame.add(scrollPane);

        frame.setVisible(true);
    }

    /**
     * Renders a single question panel with options to update or delete.
     *
     * @param question The question to be displayed.
     * @param admin The admin managing the questions.
     */
    private void renderQuestion(Questions question, Compitetor admin) {
        JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        questionPanel.setBackground(PANEL_BACKGROUND);
        questionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblQuestion = new JLabel(question.getQuestionText());
        lblQuestion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblQuestion.setForeground(TEXT_COLOR);
        questionPanel.add(lblQuestion);

        // Difficulty level label with dynamic text color
        JLabel difficulty = new JLabel(question.getLevel());
        difficulty.setFont(new Font("Arial", Font.PLAIN, 16));
        difficulty.setOpaque(true);
        difficulty.setForeground(getDifficultyColor(question.getLevel()));

        questionPanel.add(difficulty);

        // Buttons for updating and deleting questions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(PANEL_BACKGROUND);

        JButton btnUpdate = createStyledButton("Update", UPDATE_COLOR);
        btnUpdate.addActionListener(e -> new UpdateQuestionPage(question, admin));
        buttonPanel.add(btnUpdate);

        JButton btnDelete = createStyledButton("Delete", DELETE_COLOR);
        btnDelete.addActionListener(e -> handleDelete(question, questionPanel));
        buttonPanel.add(btnDelete);

        questionPanel.add(buttonPanel);
        questionPanels.add(questionPanel);
        contentPanel.add(questionPanel);
    }

    /**
     * Handles the deletion of a question with confirmation.
     *
     * @param question The question to delete.
     * @param questionPanel The panel displaying the question.
     */
    private void handleDelete(Questions question, JPanel questionPanel) {
        int confirmDelete = JOptionPane.showConfirmDialog(
            frame, "Are you sure you want to delete this question?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirmDelete == JOptionPane.YES_OPTION) {
            if (JDBC.deleteQuestion(question.getId())) {
                JOptionPane.showMessageDialog(frame, "Deleted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                contentPanel.remove(questionPanel);
                questionPanels.remove(questionPanel);
                contentPanel.revalidate();
                contentPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "Deletion Failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Returns the text color for the difficulty level.
     *
     * @param level The difficulty level.
     * @return The color for the text of the difficulty level.
     */
    private Color getDifficultyColor(String level) {
        switch (level.toUpperCase()) {
            case "ADVANCE":
                return Color.BLUE;
            case "INTERMEDIATE":
                return Color.GREEN;
            case "BEGINNER":
                return Color.PINK;
            default:
                return Color.GRAY;
        }
    }

    /**
     * Creates a styled button with a custom background color.
     *
     * @param text The text to display on the button.
     * @param bgColor The background color of the button.
     * @return A styled JButton.
     */
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    /**
     * Creates an icon button for navigation.
     *
     * @param imagePath The path to the icon image.
     * @return A JButton with an image icon.
     */
    private JButton createIconButton(String imagePath) {
        ImageIcon icon = new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton button = new JButton(icon);
        button.setPreferredSize(new Dimension(40, 40));
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setBackground(DARK_BACKGROUND);
        return button;
    }
}