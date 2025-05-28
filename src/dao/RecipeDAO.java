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
            
            conn.setAutoCommit(false);

            recipeStmt.setString(1, recipe.getName());
            recipeStmt.executeUpdate();
            
            ResultSet generatedKeys = recipeStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                conn.rollback();
                throw new SQLException("Crearea rețetei a eșuat, nu s-a obținut niciun ID.");
            }
            int recipeId = generatedKeys.getInt(1);
            recipe.setId(recipeId);

            if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
                for (Map.Entry<Ingredient, Double> entry : recipe.getIngredients().entrySet()) {
                    if (entry.getKey().getId() == 0) {
                        System.err.println("Avertisment: Ingredientul " + entry.getKey().getName() + " nu are ID. Nu se poate adăuga în rețetă.");
                        continue;
                    }
                    ingredientStmt.setInt(1, recipeId);
                    ingredientStmt.setInt(2, entry.getKey().getId());    // ingredient
                    ingredientStmt.setDouble(3, entry.getValue());       // cantitate
                    ingredientStmt.addBatch();
                }
                ingredientStmt.executeBatch();
            }
            
            conn.commit();
            System.out.println("✅ Rețetă salvată cu ID: " + recipeId);
            
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
            Map<Integer, Recipe> recipeMap = new HashMap<>();  // id: Rețetă

            while (rs.next()) {
                int recipeId = rs.getInt("recipe_id");
                Recipe recipe = recipeMap.get(recipeId);

                if (recipe == null) {
                    recipe = new Recipe(rs.getString("recipe_name"));
                    recipe.setId(recipeId);
                    recipeMap.put(recipeId, recipe);
                }

                if (rs.getObject("id") != null) {
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

        Ingredient ingredient = switch (baseUnit) {
            case "piece" -> new UnitIngredient(name, macros);
            case "g" -> new WeightIngredient(name, macros);
            case "ml" -> new VolumeIngredient(name, macros);
            default -> throw new IllegalArgumentException("Unsupported unit type: " + baseUnit);
        };

        ingredient.setId(id);

        return ingredient;
    }

    public Recipe getRecipeById(int recipeId) {
        Recipe recipe = null;
        String sql = "SELECT r.id AS recipe_id, r.name AS recipe_name, " +
                     "ri.ingredient_id, ri.quantity, i.* " +  // reteta + ingredient
                     "FROM recipes r " +
                     "LEFT JOIN recipe_ingredients ri ON r.id = ri.recipe_id " +
                     "LEFT JOIN ingredients i ON ri.ingredient_id = i.id " +
                     "WHERE r.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recipeId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                if (recipe == null) {
                    recipe = new Recipe(rs.getString("recipe_name"));
                    recipe.setId(rs.getInt("recipe_id"));
                }

                // Verific daca exista ingredient asociat cu reteta
                if (rs.getObject("id") != null) {
                    Ingredient ingredient = createIngredientFromResultSet(rs);
                    double quantity = rs.getDouble("quantity");
                    if (recipe != null) {
                        recipe.addIngredient(ingredient, quantity);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    public void updateRecipe(Recipe recipe) {
        String updateRecipeSql = "UPDATE recipes SET name = ? WHERE id = ?";
        String deleteIngredientsSql = "DELETE FROM recipe_ingredients WHERE recipe_id = ?";
        String insertIngredientsSql = "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement updateRecipeStmt = conn.prepareStatement(updateRecipeSql);
             PreparedStatement deleteIngredientsStmt = conn.prepareStatement(deleteIngredientsSql);
             PreparedStatement insertIngredientsStmt = conn.prepareStatement(insertIngredientsSql)) {

            conn.setAutoCommit(false);

            updateRecipeStmt.setString(1, recipe.getName());
            updateRecipeStmt.setInt(2, recipe.getId());
            updateRecipeStmt.executeUpdate();

            deleteIngredientsStmt.setInt(1, recipe.getId());
            deleteIngredientsStmt.executeUpdate();

            if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
                for (Map.Entry<Ingredient, Double> entry : recipe.getIngredients().entrySet()) {
                    if (entry.getKey().getId() == 0) {
                        System.err.println("Avertisment: Ingredientul " + entry.getKey().getName() + " nu are ID. Nu se poate adăuga în rețetă.");
                        continue;
                    }
                    insertIngredientsStmt.setInt(1, recipe.getId());
                    insertIngredientsStmt.setInt(2, entry.getKey().getId());
                    insertIngredientsStmt.setDouble(3, entry.getValue());
                    insertIngredientsStmt.addBatch();
                }
                insertIngredientsStmt.executeBatch();
            }

            conn.commit();
            System.out.println("✅ Rețetă actualizată cu succes!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteRecipe(int recipeId) {
        String deleteIngredientsSql = "DELETE FROM recipe_ingredients WHERE recipe_id = ?";
        String deleteRecipeSql = "DELETE FROM recipes WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement deleteIngredientsStmt = conn.prepareStatement(deleteIngredientsSql);
             PreparedStatement deleteRecipeStmt = conn.prepareStatement(deleteRecipeSql)) {

            conn.setAutoCommit(false);

            deleteIngredientsStmt.setInt(1, recipeId);
            deleteIngredientsStmt.executeUpdate();

            deleteRecipeStmt.setInt(1, recipeId);
            int affectedRows = deleteRecipeStmt.executeUpdate();

            if (affectedRows > 0) {
                conn.commit();
                System.out.println("✅ Rețetă ștearsă cu succes!");
            } else {
                conn.rollback();
                System.out.println("⚠️ Nu s-a găsit nicio rețetă cu ID-ul specificat.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
