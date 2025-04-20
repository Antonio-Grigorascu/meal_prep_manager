public class WeightGain implements NutritionalGoal {
    @Override
    public int calculateCalorieTarget(User user) {
        double tdee = user.getTDEE();
        return (int)(tdee + 300); // surplus caloric moderat (~0.25-0.5kg/săptămână)
    }

    @Override
    public String getGoalName() {
        return "Weight Gain";
    }
}
