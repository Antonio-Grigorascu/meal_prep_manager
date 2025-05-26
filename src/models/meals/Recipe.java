package models.meals;

import models.ingredients.Ingredient;
import models.ingredients.Macros;

import java.util.HashMap;
import java.util.Map;

public class Recipe {
    private int id;
    private String name;
    private Map<Ingredient, Double> ingredients;

    public Recipe(String name) {
        this.name = name;
        this.ingredients = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        double totalCalories = 0.0;
        double totalProteins = 0.0;
        double totalFats = 0.0;
        double totalCarbs = 0.0;

        if (ingredients == null) {
            System.err.println("DEBUG [Recipe.getTotalMacros] for recipe '" + this.name + "': Ingredients map is null!");
            return new Macros(0,0,0,0);
        }
        if (ingredients.isEmpty()){
            System.out.println("DEBUG [Recipe.getTotalMacros] for recipe '" + this.name + "': No ingredients in this recipe.");
        }

        //System.out.println("DEBUG [Recipe.getTotalMacros] Calculating for recipe: '" + this.name + "' (ID: " + this.id + ") with " + ingredients.size() + " ingredient(s).");

        for (Map.Entry<Ingredient, Double> entry : ingredients.entrySet()) {
            Ingredient ingredient = entry.getKey();
            double quantity = entry.getValue();

            if (ingredient == null) {
                //System.err.println("DEBUG [Recipe.getTotalMacros] Null ingredient found in recipe: '" + this.name + "'. Skipping.");
                continue;
            }
            Macros ingredientMacros = ingredient.getMacros(); 
            if (ingredientMacros == null) {
                //System.err.println("DEBUG [Recipe.getTotalMacros] Null macros for ingredient: '" + ingredient.getName() + "' in recipe: '" + this.name + "'. Skipping.");
                continue;
            }

            double calsToAdd = ingredientMacros.getCalories() * quantity;
            double protToAdd = ingredientMacros.getProteins() * quantity;
            double fatsToAdd = ingredientMacros.getFats() * quantity;
            double carbsToAdd = ingredientMacros.getCarbs() * quantity;

            totalCalories += calsToAdd;
            totalProteins += protToAdd;
            totalFats += fatsToAdd;
            totalCarbs += carbsToAdd;
        }
        //System.out.println("DEBUG [Recipe.getTotalMacros] Finished for recipe: '" + this.name + "'. Totals: Cals=" +totalCalories + ", Prot=" + totalProteins + ", Fats=" + totalFats + ", Carbs=" + totalCarbs);
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