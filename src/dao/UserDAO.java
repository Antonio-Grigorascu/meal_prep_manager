package dao;

import enums.ActivityLevel;
import models.plans.User;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

//    private static UserDAO instance;
//
//    private UserDAO() {}
//
//    public static synchronized UserDAO getInstance() {
//        if (instance == null) {
//            instance = new UserDAO();
//        }
//        return instance;
//    }


    public boolean insertUser(User userObj) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String userSql = "INSERT INTO users (name, age, weight, height, gender, activity_level) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement userStmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {

                userStmt.setString(1, userObj.getName());
                userStmt.setInt(2, userObj.getAge());
                userStmt.setDouble(3, userObj.getWeight());
                userStmt.setDouble(4, userObj.getHeight());
                userStmt.setString(5, userObj.getGender());
                userStmt.setString(6, userObj.getActivityLevel().name());

                int affectedRows = userStmt.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet rs = userStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        userObj.setId(rs.getInt(1));
                    }
                }
            }

            // Adaugare in weight_history
            String historySql = "INSERT INTO weight_history (user_id, weight) VALUES (?, ?)";
            try (PreparedStatement historyStmt = conn.prepareStatement(historySql)) {
                historyStmt.setInt(1, userObj.getId());
                historyStmt.setDouble(2, userObj.getWeight());
                historyStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Resetez auto-commit
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

    public boolean updateUserWeight(int userId, double newWeight){
        String updateUserSql = "UPDATE users SET weight = ? WHERE id = ?";
        String insertHistorySql = "INSERT INTO weight_history (user_id, weight) VALUES (?, ?)";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement updateStmt = conn.prepareStatement(updateUserSql);
            PreparedStatement historyStmt = conn.prepareStatement(insertHistorySql)) {

            // Actualizare greutate
            updateStmt.setDouble(1, newWeight);
            updateStmt.setInt(2, userId);
            int updated = updateStmt.executeUpdate();

            // Adaugare in tabelul weight_history
            historyStmt.setInt(1, userId);
            historyStmt.setDouble(2, newWeight);
            historyStmt.executeUpdate();

            return updated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Double> getWeightHistory(int userId){
        List<Double> history = new ArrayList<>();
        String sql = "SELECT weight FROM weight_history WHERE user_id = ? ORDER BY recorded_at ASC";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                history.add(rs.getDouble("weight"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE users SET name = ?, age = ?, weight = ?, height = ?, gender = ?, activity_level = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setInt(2, user.getAge());
            stmt.setDouble(3, user.getWeight());
            stmt.setDouble(4, user.getHeight());
            stmt.setString(5, user.getGender());
            stmt.setString(6, user.getActivityLevel().name());
            stmt.setInt(7, user.getId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int userId) {
        String deleteHistorySql = "DELETE FROM weight_history WHERE user_id = ?";
        String deleteUserSql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deleteHistoryStmt = conn.prepareStatement(deleteHistorySql);
                 PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserSql)) {

                // Sterg istoricul de greutate
                deleteHistoryStmt.setInt(1, userId);
                deleteHistoryStmt.executeUpdate();

                // Sterg user-ul
                deleteUserStmt.setInt(1, userId);
                int rowsDeleted = deleteUserStmt.executeUpdate();

                conn.commit();
                return rowsDeleted > 0;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
