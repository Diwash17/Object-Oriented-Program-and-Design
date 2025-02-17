package unitTest;

 
import org.junit.jupiter.api.Test;

import app.JDBC;

import static org.junit.jupiter.api.Assertions.*;

public class AdminFeaturesTest {

    @Test
    public void updateQuestion_Success() {
        int questionId = 1; // Replace with an existing question ID
        String newQuestionText = "What is AI?";
        String newLevel = "Medium";
        String newOption1 = "Option A";
        String newOption2 = "Option B";
        String newOption3 = "Option C";
        String newOption4 = "Option D";
        String newAnswer = "Option B";

        boolean result = JDBC.updateQuestion(questionId, newQuestionText, newLevel, newOption1, newOption2, newOption3, newOption4, newAnswer);
        assertTrue(result, "Failed: Unable to update question");
    }

    @Test
    public void updateQuestion_Failure() {
        int questionId = 999999999; // Replace with a non-existent question ID
        String newQuestionText = "What is AI?";
        String newLevel = "Medium";
        String newOption1 = "Option A";
        String newOption2 = "Option B";
        String newOption3 = "Option C";
        String newOption4 = "Option D";
        String newAnswer = "Option B";

        boolean result = JDBC.updateQuestion(questionId, newQuestionText, newLevel, newOption1, newOption2, newOption3, newOption4, newAnswer);
        assertFalse(result, "Update should fail for a non-existent question ID");
    }
}
