package dao;

import models.mealprep.User;
import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MealDAO {
    public void insertUser(User userObj) {
        String sql = "INSERT INTO meals (name, age, weight, height, gender, activity_level) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userObj.getName());
            stmt.setInt(2, userObj.getAge());
            stmt.setDouble(3, userObj.getWeight());
            stmt.setDouble(4, userObj.getHeight());
            stmt.setString(5, userObj.getGender());
            stmt.setString(6, userObj.getActivityLevel().name());

            stmt.executeUpdate();
            System.out.println("Utilizatorul a fost adăugat cu succes în baza de date!");

        } catch (SQLException e) {
            System.err.println("Eroare la inserarea utilizatorului: " + e.getMessage());
        }
    }
}
