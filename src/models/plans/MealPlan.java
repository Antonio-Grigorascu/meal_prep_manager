package models.plans;

import models.ingredients.Macros;
import models.meals.Meal;

import java.util.Set;
import java.util.TreeSet;

public class MealPlan {
    private Set<Meal> meals;

    public MealPlan() {
        this.meals = new TreeSet<>();
    }

    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public Macros getTotalMacros() {
        Macros total = new Macros(0, 0, 0, 0);
        for (Meal meal : meals) {
            total = total.add(meal.getMacros());
        }
        return total;
    }

    public Set<Meal> getMeals() {
        return meals;
    }
}
