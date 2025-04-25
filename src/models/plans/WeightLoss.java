package models.plans;

public class WeightLoss extends NutritionalGoal {
    @Override
    public int calculateCalorieTarget(User user) {
        double tdee = user.getTDEE();
        return (int)(tdee - 500); // deficit caloric moderat (~0.5kg/saptamana)
    }

    @Override
    public String getGoalName() {
        return "Weight Loss";
    }
}
