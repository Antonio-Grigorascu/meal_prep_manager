package models.meals;

import enums.MealType;
import models.ingredients.Macros;

public class Meal implements Comparable<Meal> {
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
        return recipe.getTotalMacros();
    }

    public double getCalories() {
        return getMacros().getCalories();
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