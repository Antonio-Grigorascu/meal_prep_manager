package models.plans;

public class WeightGain extends NutritionalGoal {
    @Override
    public int calculateCalorieTarget(User user) {
        double tdee = user.getTDEE();
        return (int)(tdee + 300); // surplus caloric moderat (~0.25-0.5kg/saptamana)
    }

    @Override
    public String getGoalName() {
        return "Weight Gain";
    }
}
