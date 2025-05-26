package dao;

import models.ingredients.*;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO {
    public void insertIngredient(Ingredient ingredient) {
        String sql = "INSERT INTO ingredients (name, base_unit, macros_definition_unit, calories, protein, carbs, fats) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            String baseUnit = "";
            String macrosDefinition = "PER_100_UNITS";
            if (ingredient instanceof UnitIngredient) {
                baseUnit = "piece";
                macrosDefinition = "PER_PIECE";
            } else if (ingredient instanceof WeightIngredient) {
                baseUnit = "g";
            } else if (ingredient instanceof VolumeIngredient) {
                baseUnit = "ml";
            }

            pstmt.setString(1, ingredient.getName());
            pstmt.setString(2, baseUnit);
            pstmt.setString(3, macrosDefinition);
            pstmt.setDouble(4, ingredient.getMacros().getCalories());
            pstmt.setDouble(5, ingredient.getMacros().getProteins());
            pstmt.setDouble(6, ingredient.getMacros().getCarbs());
            pstmt.setDouble(7, ingredient.getMacros().getFats());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        ingredient.setId(rs.getInt(1));  // setez ID-ul generat
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM ingredients";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Ingredient ingredient = createIngredientFromResultSet(rs);
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    private Ingredient createIngredientFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String baseUnit = rs.getString("base_unit");
        String macrosUnit = rs.getString("macros_definition_unit");
        
        Macros macros = new Macros(
            rs.getDouble("calories"),
            rs.getDouble("protein"),
            rs.getDouble("fats"),
            rs.getDouble("carbs")
        );

        Ingredient ingredient = switch (baseUnit) {
            case "piece" -> new UnitIngredient(name, macros);
            case "g" -> new WeightIngredient(name, macros);
            case "ml" -> new VolumeIngredient(name, macros);
            default -> throw new IllegalArgumentException("Unitate de masura invalida: " + baseUnit);
        };
        ingredient.setId(id);
        return ingredient;
    }
}