import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MealPlan {
    private List<Meal> meals;

    public MealPlan() {
        this.meals = new ArrayList<>();
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

    public List<Meal> getMeals() {
        return meals;
    }

    public List<Meal> getSortedMeals() {
        List<Meal> sorted = new ArrayList<>(meals);
        sorted.sort(Comparator.comparingInt(m -> m.getMealType().ordinal()));
        return sorted;
    }

}
