package models.meals;

import dao.RecipeDAO;
import enums.MealType;
import models.ingredients.Macros;

public class Meal implements Comparable<Meal> {
    private int id;
    private MealType mealType;
    private Recipe recipe;

    public Meal(MealType mealType, Recipe recipe) {
        this.mealType = mealType;
        this.recipe = recipe;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Macros getMacros() {
        if (this.recipe != null && (this.recipe.getIngredients() == null || this.recipe.getIngredients().isEmpty()) && this.recipe.getId() != 0) {
            RecipeDAO recipeDAO = new RecipeDAO();
            Recipe fullRecipe = recipeDAO.getRecipeById(this.recipe.getId());
            if (fullRecipe != null) {
                this.recipe = fullRecipe;
            } else {
                return new Macros(0,0,0,0);
            }
        }
        return this.recipe != null ? this.recipe.getTotalMacros() : new Macros(0,0,0,0);
    }

    public double getCalories() {
        return getMacros().getCalories();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return mealType + ": " + recipe;
    }

    @Override
    public int compareTo(Meal other) {
        return this.mealType.ordinal() - other.mealType.ordinal();
    }
}