package unitTest;

import app.Compitetor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CompitetorsTest {

    @Test
    public void testConstructor() {
        int[] scores = {10, 20, 30};
        Compitetor competitor = new Compitetor(1, "Diwash", "diwash.adhi4@gmail.com", "USER", "ADVANCE", scores);
        
        assertEquals(1, competitor.getId());
        assertEquals("Diwash", competitor.getName());
        assertEquals("diwash.adhi4@gmail.com", competitor.getEmail());
        assertEquals("USER", competitor.getRole());
        assertEquals("ADVANCE", competitor.getLevel());
        assertArrayEquals(scores, competitor.getScores());
    }

    @Test
    public void testOverallScore() {
        int[] scores = {10, 20, 30};
        Compitetor competitor = new Compitetor(1, "Diwash", "diwash.adhi4@gmail.com", "USER", "ADVANCE", scores);
        
        double expectedScore = 60.0; // 10 + 20 + 30 = 60
        assertEquals(expectedScore, competitor.getOverallScore(),"Failed: calculation error ");
    }

}
