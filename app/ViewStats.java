package app;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * ViewStats class displays the statistics of competitors in a table format.
 * It retrieves data from the MySQL database and calculates key statistics.
 */
public class ViewStats extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel totalPlayersLabel, highestMarkLabel, lowestMarkLabel, meanMarkLabel, totalScoresLabel;

    /**
     * Constructor to initialize and display the ViewStats UI.
     * 
     * @param user The competitor user currently logged in.
     */
    public ViewStats(Compitetor user) {
        setTitle("Competitor Stats");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel titleLabel = new JLabel("Competitor Statistics", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        styleButton(backButton, new Color(231, 76, 60), Color.WHITE);
        backButton.addActionListener(e -> {
            dispose();
            new AdminPanelUI(user);
        });
        headerPanel.add(backButton, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(30);
        loadTableData();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        statsPanel.setBackground(new Color(236, 240, 241));

        totalPlayersLabel = createStyledLabel("Total Players: ");
        highestMarkLabel = createStyledLabel("Highest Mark: ");
        lowestMarkLabel = createStyledLabel("Lowest Mark: ");
        meanMarkLabel = createStyledLabel("Mean Mark: ");
        totalScoresLabel = createStyledLabel("Total Scores: ");

        statsPanel.add(totalPlayersLabel);
        statsPanel.add(highestMarkLabel);
        statsPanel.add(lowestMarkLabel);
        statsPanel.add(meanMarkLabel);
        statsPanel.add(totalScoresLabel);

        add(statsPanel, BorderLayout.SOUTH);

        updateStats();
        setVisible(true);
    }

    /**
     * Styles a JButton with custom colors and font.
     * 
     * @param button  The JButton to style.
     * @param bgColor Background color of the button.
     * @param fgColor Foreground (text) color of the button.
     */
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    /**
     * Creates a JLabel with a custom font and color.
     * 
     * @param text The text to display in the label.
     * @return A styled JLabel.
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(new Color(44, 62, 80));
        return label;
    }

    /**
     * Loads competitor data from the database and populates the table.
     */
    private void loadTableData() {
        String url = "jdbc:mysql://localhost:3306/quiz";
        String user = "root";
        String password = "diwash12345";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT id, name, email, level, score1, score2, score3, score4, score5 FROM users WHERE role = 'USER' "
                       + "ORDER BY (score1 + score2 + score3 + score4 + score5) DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            tableModel.setRowCount(0);
            tableModel.setColumnIdentifiers(new String[]{
                    "Competitor ID", "Name", "Email", "Level", "Score1", "Score2", "Score3", "Score4", "Score5", "Overall Score"
            });

            while (rs.next()) {
                int overallScore = rs.getInt("score1") + rs.getInt("score2") + rs.getInt("score3") +
                                   rs.getInt("score4") + rs.getInt("score5");
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("level"),
                        rs.getInt("score1"),
                        rs.getInt("score2"),
                        rs.getInt("score3"),
                        rs.getInt("score4"),
                        rs.getInt("score5"),
                        overallScore
                });
            }

            // Adjust column widths
            table.getColumnModel().getColumn(2).setPreferredWidth(250); // Email column wider
            table.getColumnModel().getColumn(3).setPreferredWidth(150); // Increased Level column width
            table.getColumnModel().getColumn(4).setPreferredWidth(60);  // Score1
            table.getColumnModel().getColumn(5).setPreferredWidth(60);  // Score2
            table.getColumnModel().getColumn(6).setPreferredWidth(60);  // Score3
            table.getColumnModel().getColumn(7).setPreferredWidth(60);  // Score4
            table.getColumnModel().getColumn(8).setPreferredWidth(60);  // Score5
            table.getColumnModel().getColumn(9).setPreferredWidth(100); // Overall Score

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retrieves and updates competitor statistics from the database.
     */
    private void updateStats() {
        String url = "jdbc:mysql://localhost:3306/quiz";
        String user = "root";
        String password = "diwash12345";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String sql = "SELECT COUNT(*) AS totalPlayers, MAX(score1 + score2 + score3 + score4 + score5) AS highest, " +
                         "MIN(score1 + score2 + score3 + score4 + score5) AS lowest, AVG(score1 + score2 + score3 + score4 + score5) AS mean, " +
                         "SUM(score1 + score2 + score3 + score4 + score5) AS totalScores " +
                         "FROM users WHERE role = 'USER'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                totalPlayersLabel.setText("Total Players: " + rs.getInt("totalPlayers"));
                highestMarkLabel.setText("Highest Mark: " + rs.getInt("highest"));
                lowestMarkLabel.setText("Lowest Mark: " + rs.getInt("lowest"));
                meanMarkLabel.setText("Mean Mark: " + rs.getDouble("mean"));
                totalScoresLabel.setText("Total Scores: " + rs.getInt("totalScores"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error retrieving stats!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}