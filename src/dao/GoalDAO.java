package dao;

import util.DBConnection;
import java.sql.*;

public class GoalDAO {
    
    public String getUserGoalType(int userId) {
        String sql = "SELECT goal_type FROM user_goals WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("goal_type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUserGoal(int userId, String goalType) {
        String sql = "INSERT INTO user_goals (user_id, goal_type) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE goal_type = VALUES(goal_type)";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, goalType.toUpperCase());
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Eroare la actualizarea obiectivului: " + e.getMessage());
        }
    }
}