package app;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.List;

/**
 * LeaderBoardUI displays high scores for different difficulty levels in a tabular format.
 * It fetches the data from the database and presents it with a modern UI design using a light theme.
 */
public class LeaderBoardUI {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quiz";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "diwash12345";

    /**
     * Constructs and displays the Leaderboard UI.
     * The UI includes a back button and difficulty panels for displaying rankings in Beginner, Intermediate, and Advanced levels.
     *
     * @param user The user accessing the leaderboard.
     */
    public LeaderBoardUI(Compitetor user) {
        JFrame frame = new JFrame("High Scores");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        // Header Panel: Contains title and back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 255, 255)); // Light background for header

        JLabel label = new JLabel("High Scores Page", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(Color.BLACK); // Black text for contrast

        JButton backButton = createBackButton(frame, user);
        
        headerPanel.add(backButton, BorderLayout.WEST);
        headerPanel.add(label, BorderLayout.CENTER);
        frame.add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel for displaying leaderboards
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));  // FlowLayout to arrange difficulty panels in a single row
        mainPanel.setBackground(new Color(255, 255, 255));  // Light background for main content
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Adding difficulty panels
        mainPanel.add(createDifficultyPanel("Beginner", fetchLeaderboardData("BEGINNER")));
        mainPanel.add(createDifficultyPanel("Intermediate", fetchLeaderboardData("INTERMEDIATE")));
        mainPanel.add(createDifficultyPanel("Advanced", fetchLeaderboardData("ADVANCED")));
        
        // Wrap the main panel in a JScrollPane to make it horizontally scrollable
        JScrollPane scrollPane = new JScrollPane(mainPanel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * Creates a back button that returns the user to the previous screen.
     *
     * @param frame The parent frame.
     * @param user The current user.
     * @return A styled JButton for navigation.
     */
    private JButton createBackButton(JFrame frame, Compitetor user) {
        ImageIcon backIcon = new ImageIcon(new ImageIcon("/Users/diwashadhikari/Desktop/back_back.png")
                .getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        JButton backButton = new JButton(backIcon);
        styleButton(backButton);
        backButton.addActionListener(e -> {
            frame.dispose();
            new UserPannelUI(user);
        });
        return backButton;
    }

    /**
     * Creates a leaderboard panel for a specific difficulty level.
     *
     * @param difficulty The difficulty level.
     * @param entries The leaderboard entries.
     * @return A JPanel containing the leaderboard table.
     */
    private JPanel createDifficultyPanel(String difficulty, List<LeaderboardEntry> entries) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 255, 255));  // Light background for each panel
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(211, 211, 211), 2),
                difficulty + " Level Rankings",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 18),
                Color.BLACK)); // Title with black text

        // Table Model
        String[] columnNames = {"Rank", "Player", "Score"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Populate table model
        for (int i = 0; i < entries.size(); i++) {
            LeaderboardEntry entry = entries.get(i);
            model.addRow(new Object[]{i + 1, entry.username, entry.score});
        }

        JTable table = new JTable(model);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Fetches leaderboard data from the database.
     *
     * @param difficulty The difficulty level.
     * @return A list of leaderboard entries.
     */
    private List<LeaderboardEntry> fetchLeaderboardData(String difficulty) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        String query = "SELECT email, overallScore FROM users WHERE level = ? ORDER BY overallScore DESC";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, difficulty.toUpperCase());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                entries.add(new LeaderboardEntry(rs.getString("email"), rs.getInt("overallScore")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching leaderboard data: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return entries;
    }

    /**
     * Styles a button to align with the UI theme.
     *
     * @param button The button to style.
     */
    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 122, 255));  // Set background color to blue
        button.setForeground(Color.WHITE);  // Set text color to white
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setFocusPainted(false);
        button.setOpaque(true);
    }

    /**
     * Styles a JTable to maintain UI consistency with a light theme.
     *
     * @param table The JTable to style.
     */
    private void styleTable(JTable table) {
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);
        table.getTableHeader().setBackground(new Color(0, 122, 255));  // Set table header color to blue
        table.getTableHeader().setForeground(Color.WHITE);  // Set header text color to white
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
    }

    /**
     * Represents a leaderboard entry containing a player's username and score.
     */
    private static class LeaderboardEntry {
        String username;
        int score;

        LeaderboardEntry(String username, int score) {
            this.username = username;
            this.score = score;
        }
    }
}