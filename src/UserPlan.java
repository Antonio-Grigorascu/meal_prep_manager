public class UserPlan {
    private User user;
    private MealPlan mealPlan;
    private NutritionalGoal goal;

    public UserPlan(User user, MealPlan mealPlan, NutritionalGoal goal) {
        this.user = user;
        this.mealPlan = mealPlan;
        this.goal = goal;
    }

    public void evaluatePlan() {
        int targetCalories = goal.calculateCalorieTarget(user);
        Macros totalMacros = mealPlan.getTotalMacros();

        System.out.println("\nEvaluating plan for " + user.getName() + ":");
        System.out.println("Calorie target (" + goal.getGoalName() + "): " + targetCalories + " kcal");
        System.out.println("Total calories in plan: " + totalMacros.getCalories() + " kcal");

        if (totalMacros.getCalories() < targetCalories - 100) {
            System.out.println("⚠️  Sub caloric target by " + (targetCalories - totalMacros.getCalories()) + " kcal");
        } else if (totalMacros.getCalories() > targetCalories + 100) {
            System.out.println("⚠️  Over caloric target by " + (totalMacros.getCalories() - targetCalories) + " kcal");
        } else {
            System.out.println("✅  Caloric target met!");
        }

        // Optionally: Evaluate protein, fat, carbs
        System.out.println("Macros breakdown:");
        System.out.println("Protein: " + totalMacros.getProteins() + "g");
        System.out.println("Fat: " + totalMacros.getFats() + "g");
        System.out.println("Carbs: " + totalMacros.getCarbs() + "g");
    }
}
