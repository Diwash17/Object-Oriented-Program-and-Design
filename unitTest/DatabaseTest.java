package unitTest;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import app.JDBC;
import app.PasswordHash;
import app.ReturnType;


public class DatabaseTest {
	

	
	@BeforeEach
	public void establishDbConnection() {
		assertNotNull(JDBC.getConnection(),"There must be a valid database connection: please check the database credentials");
	}
	
	
	@Test
	public void checkUniqueEmail() {
		
        String name = "Diwash Adhikari";
        String email = "ram@gmail.com"; //this user already exists so this must fail
        String password = "Abcefg123!"; 
        String level = "BEGINNER";
        
        String hash =  PasswordHash.hashPassword(password);

        ReturnType response = JDBC.registerUser(name, email, hash, level);
        
        assertTrue(response.success);
 
    }
	

    
    @AfterAll
    public static void removeUser() {
        try (Connection conn = JDBC.getConnection()) {
            if (conn == null) {
                System.err.println("Failed to connect to database during cleanup");
                return;
            }

            // Clean up test question
            try (PreparedStatement questionStmt = conn.prepareStatement("DELETE FROM questions WHERE question = ? AND answer = ?")) {
                questionStmt.setString(1, "Knok Knok!");
                questionStmt.setString(2, "Okay");
                questionStmt.executeUpdate();
            }

            // Clean up test admin
            try (PreparedStatement adminStmt = conn.prepareStatement("DELETE FROM users WHERE email = ? AND role = 'ADMIN'")) {
                adminStmt.setString(1, "diwash.adhi4@gmail.com");
                adminStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
          
       
    }


