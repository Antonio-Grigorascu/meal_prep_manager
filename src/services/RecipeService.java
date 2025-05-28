package services;

import dao.RecipeDAO;
import models.meals.Recipe;

import java.util.List;

public class RecipeService {

    private static RecipeService instance;
    private final RecipeDAO recipeDAO;

    private RecipeService() {
        this.recipeDAO = new RecipeDAO();
    }

    public static synchronized RecipeService getInstance() {
        if (instance == null) {
            instance = new RecipeService();
        }
        return instance;
    }

    public void insertRecipe(Recipe Recipe) {
        recipeDAO.insertRecipe(Recipe);
    }

    public List<Recipe> getAllRecipes(){
        return recipeDAO.getAllRecipes();
    }

    public Recipe getRecipeById(int recipeId){
        return recipeDAO.getRecipeById(recipeId);
    }

    public void updateRecipe(Recipe Recipe){
        recipeDAO.updateRecipe(Recipe);
    }

    public void deleteRecipe(int id){
        recipeDAO.deleteRecipe(id);
    }


}