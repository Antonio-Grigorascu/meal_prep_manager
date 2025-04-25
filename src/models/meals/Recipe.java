package models.meals;

import models.ingredients.Ingredient;
import models.ingredients.Macros;

import java.util.HashMap;
import java.util.Map;

public class Recipe {
    private String name;
    private Map<Ingredient, Double> ingredients;

    public Recipe(String name) {
        this.name = name;
        this.ingredients = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Ingredient, Double> getIngredients() {
        return ingredients;
    }

    public void addIngredient(Ingredient ingredient, double quantity) {
        if (this.ingredients.containsKey(ingredient)) {
            this.ingredients.put(ingredient, this.ingredients.get(ingredient) + quantity);
        } else {
            this.ingredients.put(ingredient, quantity);
        }
    }

    public void removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
    }

    public Macros getTotalMacros() {
        int totalCalories = 0;
        int totalProteins = 0;
        int totalFats = 0;
        int totalCarbs = 0;

        for (Map.Entry<Ingredient, Double> entry : ingredients.entrySet()) {
            Ingredient ingredient = entry.getKey();
            double quantity = entry.getValue();
            Macros macros = ingredient.getMacros();
            double factor = ingredient.getNutritionFactor(quantity);


            totalCalories += (int) (macros.getCalories() * factor);
            totalProteins += (int) (macros.getProteins() * factor);
            totalFats += (int) (macros.getFats() * factor);
            totalCarbs += (int) (macros.getCarbs() * factor);
        }

        return new Macros(totalCalories, totalProteins, totalFats, totalCarbs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("models.meals.Recipe: ").append(name).append("\nIngredients:\n");
        for (Map.Entry<Ingredient, Double> entry : ingredients.entrySet()) {
            sb.append("- ").append(entry.getKey().getName())
                    .append(" (").append(entry.getValue()).append("g): ")
                    .append(entry.getKey().getMacros()).append("\n");
        }
        sb.append("Total models.ingredients.Macros: ").append(getTotalMacros());
        return sb.toString();
    }

}