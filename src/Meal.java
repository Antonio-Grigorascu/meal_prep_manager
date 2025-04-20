public class Meal {
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

    public int getCalories() {
        return getMacros().getCalories();
    }

    @Override
    public String toString() {
        return mealType + ": " + recipe;
    }
}