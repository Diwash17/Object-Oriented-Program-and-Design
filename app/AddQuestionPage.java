package app;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AddQuestionPage extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField textFieldQuestion, textFieldAnswer, textFieldOption1, textFieldOption2, textFieldOption3, textFieldOption4;
    private JComboBox<String> comboBoxLevel;

    public AddQuestionPage(Compitetor admin) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(new Color(245, 245, 245));
        setContentPane(contentPane);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));

        JLabel lblWelcome = new JLabel("Welcome, " + admin.getName() + "!", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 24));
        lblWelcome.setForeground(Color.WHITE);
        headerPanel.add(lblWelcome, BorderLayout.CENTER);
        
        JButton backButton = createBackButton(admin);
        headerPanel.add(backButton, BorderLayout.WEST);

        contentPane.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        contentPane.add(centerPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        addFormField(centerPanel, gbc, "Question:", textFieldQuestion = new JTextField(30));
        addFormField(centerPanel, gbc, "Level:", comboBoxLevel = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"}));
        addFormField(centerPanel, gbc, "Answer:", textFieldAnswer = new JTextField(30));
        addFormField(centerPanel, gbc, "Option 1:", textFieldOption1 = new JTextField(20));
        addFormField(centerPanel, gbc, "Option 2:", textFieldOption2 = new JTextField(20));
        addFormField(centerPanel, gbc, "Option 3:", textFieldOption3 = new JTextField(20));
        addFormField(centerPanel, gbc, "Option 4:", textFieldOption4 = new JTextField(20));

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton btnAddQuestion = new JButton("Add Question");
        styleButton(btnAddQuestion, new Color(46, 204, 113), Color.WHITE);
        btnAddQuestion.addActionListener(new AddQuestionAction());
        buttonPanel.add(btnAddQuestion);

        centerPanel.add(buttonPanel, gbc);

        JButton btnLogout = new JButton("Logout");
        styleButton(btnLogout, new Color(231, 76, 60), Color.WHITE);
        btnLogout.addActionListener(e -> {
            dispose();
            new Home();
        });
        contentPane.add(btnLogout, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createBackButton(Compitetor admin) {
        JButton backButton = new JButton("← Back");
        styleButton(backButton, new Color(52, 152, 219), Color.WHITE);
        backButton.addActionListener(e -> {
            new AdminPanelUI(admin);
            dispose();
        });
        return backButton;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.BLACK);
        panel.add(label, gbc);

        gbc.gridx = 1;
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(250, 40));
        panel.add(field, gbc);
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setOpaque(true);
    }

    private class AddQuestionAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String question = textFieldQuestion.getText().trim();
            String answer = textFieldAnswer.getText().trim();
            String option1 = textFieldOption1.getText().trim();
            String option2 = textFieldOption2.getText().trim();
            String option3 = textFieldOption3.getText().trim();
            String option4 = textFieldOption4.getText().trim();
            String level = ((String) comboBoxLevel.getSelectedItem()).toUpperCase();

            if (question.isEmpty() || answer.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty()) {
                JOptionPane.showMessageDialog(AddQuestionPage.this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ReturnType response = JDBC.addQuestion(question, level, answer, option1, option2, option3, option4);
            if (response.success) {
                JOptionPane.showMessageDialog(AddQuestionPage.this, response.msg, "Success", JOptionPane.INFORMATION_MESSAGE);
                textFieldQuestion.setText("");
                textFieldAnswer.setText("");
                textFieldOption1.setText("");
                textFieldOption2.setText("");
                textFieldOption3.setText("");
                textFieldOption4.setText("");
                comboBoxLevel.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(AddQuestionPage.this, response.msg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
