package dao;

import models.meals.Meal;
import models.meals.Recipe;
import enums.MealType;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealDAO {
    public static void insertMeal(Meal meal, int userId) {
        String sql = "INSERT INTO meals (recipe_id, meal_type) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, meal.getRecipe().getId());
            pstmt.setString(2, meal.getMealType().name());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int mealId = rs.getInt(1);
                    linkMealToUser(userId, mealId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void linkMealToUser(int userId, int mealId) {
        String sql = "INSERT INTO user_meal_plan_entries (user_id, meal_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, mealId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Meal> getMealsByUserId(int userId) {
        List<Meal> meals = new ArrayList<>();
        String sql = "SELECT m.id AS meal_id, m.meal_type, r.id AS recipe_id, r.name AS recipe_name " +
                     "FROM user_meal_plan_entries umpe " +
                     "JOIN meals m ON umpe.meal_id = m.id " +
                     "JOIN recipes r ON m.recipe_id = r.id " +
                     "WHERE umpe.user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Recipe recipe = new Recipe(rs.getString("recipe_name"));
                recipe.setId(rs.getInt("recipe_id"));

                MealType mealType = MealType.valueOf(rs.getString("meal_type"));
                Meal meal = new Meal(mealType, recipe);
                meal.setId(rs.getInt("meal_id"));
                
                meals.add(meal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meals;
    }
}