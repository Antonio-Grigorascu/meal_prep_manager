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

        System.out.println("\nEvaluare plan pentru " + user.getName() + ":");
        System.out.println("Ținta calorică (" + goal.getGoalName() + "): " + targetCalories + " kcal");
        System.out.println("Total calorii în plan: " + totalMacros.getCalories() + " kcal");

        if (totalMacros.getCalories() < targetCalories - 100) {
            System.out.println("⬇️ Sub ținta calorică cu " + (targetCalories - totalMacros.getCalories()) + " kcal");
        } else if (totalMacros.getCalories() > targetCalories + 100) {
            System.out.println("⬆️ Peste ținta calorică cu " + (totalMacros.getCalories() - targetCalories) + " kcal");
        } else {
            System.out.println("✅ Ținta calorică atinsă!");
        }

        System.out.println("Detaliere macronutrienți:");
        System.out.println("Proteine: " + totalMacros.getProteins() + "g");
        System.out.println("Grăsimi: " + totalMacros.getFats() + "g");
        System.out.println("Carbohidrați: " + totalMacros.getCarbs() + "g");
    }
}
