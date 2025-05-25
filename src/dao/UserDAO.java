package dao;

import enums.ActivityLevel;
import models.plans.User;
import util.DBConnection;

import java.sql.*;

public class UserDAO {

    public boolean insertUser(User userObj) {
        String sql = "INSERT INTO users (name, age, weight, height, gender, activity_level) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, userObj.getName());
            stmt.setInt(2, userObj.getAge());
            stmt.setDouble(3, userObj.getWeight());
            stmt.setDouble(4, userObj.getHeight());
            stmt.setString(5, userObj.getGender());
            stmt.setString(6, userObj.getActivityLevel().name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        userObj.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByName(String name) {
        String sql = "SELECT * FROM users WHERE name = ?";
        User user = null;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = new User(
                    rs.getString("name"),
                    rs.getInt("age"),
                    rs.getDouble("weight"),
                    rs.getDouble("height"),
                    rs.getString("gender"),
                    ActivityLevel.valueOf(rs.getString("activity_level"))
                );
                user.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
