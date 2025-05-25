package dao;

import models.meals.Recipe;
import models.ingredients.Ingredient;
import models.ingredients.UnitIngredient;
import models.ingredients.WeightIngredient;
import models.ingredients.VolumeIngredient;
import models.ingredients.Macros;
import util.DBConnection;

import java.sql.*;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeDAO {
    
    public void insertRecipe(Recipe recipe) {
        String recipeSql = "INSERT INTO recipes (name) VALUES (?)";
        String ingredientSql = "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement recipeStmt = conn.prepareStatement(recipeSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement ingredientStmt = conn.prepareStatement(ingredientSql)) {
            
            recipeStmt.setString(1, recipe.getName());
            recipeStmt.executeUpdate();
            
            ResultSet generatedKeys = recipeStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Creating recipe failed, no ID obtained.");
            }
            int recipeId = generatedKeys.getInt(1);

            for (Map.Entry<Ingredient, Double> entry : recipe.getIngredients().entrySet()) {
                ingredientStmt.setInt(1, recipeId);
                ingredientStmt.setInt(2, entry.getKey().getId());
                ingredientStmt.setDouble(3, entry.getValue());
                ingredientStmt.addBatch();
            }
            
            ingredientStmt.executeBatch();

            System.out.println("✅ Rețetă salvată.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT r.id AS recipe_id, r.name AS recipe_name, " +
                     "ri.ingredient_id, ri.quantity, i.* " +
                     "FROM recipes r " +
                     "LEFT JOIN recipe_ingredients ri ON r.id = ri.recipe_id " +
                     "LEFT JOIN ingredients i ON ri.ingredient_id = i.id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = pstmt.executeQuery();
            Map<Integer, Recipe> recipeMap = new HashMap<>();

            while (rs.next()) {
                int recipeId = rs.getInt("recipe_id");
                Recipe recipe = recipeMap.get(recipeId);

                if (recipe == null) {
                    recipe = new Recipe(rs.getString("recipe_name"));
                    recipe.setId(recipeId);
                    recipeMap.put(recipeId, recipe);
                }

                if (!rs.wasNull()) { // Daca exista ingrediente
                    Ingredient ingredient = createIngredientFromResultSet(rs);
                    double quantity = rs.getDouble("quantity");
                    recipe.addIngredient(ingredient, quantity);
                }
            }
            recipes.addAll(recipeMap.values());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    private Ingredient createIngredientFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String baseUnit = rs.getString("base_unit");
        String macrosDefinition = rs.getString("macros_definition_unit");
        
        // obiectul Macros din valorile din baza de date
        Macros macros = new Macros(
            rs.getDouble("calories"),
            rs.getDouble("protein"),
            rs.getDouble("fats"),
            rs.getDouble("carbs")
        );

        // tipul de ingredient corespunzator
        Ingredient ingredient = switch (baseUnit) {
            case "piece" -> new UnitIngredient(name, macros);
            case "g" -> new WeightIngredient(name, macros);
            case "ml" -> new VolumeIngredient(name, macros);
            default -> throw new IllegalArgumentException("Unsupported unit type: " + baseUnit);
        };

        // setez ID-ul din baza de date
        ingredient.setId(id);

        return ingredient;
    }
}
