package services;

import dao.IngredientDAO;
import models.ingredients.Ingredient;

import java.util.List;

public class IngredientService {

    private static IngredientService instance;
    private final IngredientDAO ingredientDAO;

    private IngredientService() {
        this.ingredientDAO = new IngredientDAO();
    }

    public static synchronized IngredientService getInstance() {
        if (instance == null) {
            instance = new IngredientService();
        }
        return instance;
    }

    public void insertIngredient(Ingredient ingredient) {
        ingredientDAO.insertIngredient(ingredient);
    }

    public List<Ingredient> getAllIngredients(){
        return ingredientDAO.getAllIngredients();
    }

    public void updateIngredient(Ingredient ingredient){
        ingredientDAO.updateIngredient(ingredient);
    }

    public void deleteIngredient(int id){
        ingredientDAO.deleteIngredient(id);
    }


}