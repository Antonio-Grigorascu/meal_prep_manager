package models.plans;

public class Maintenance extends NutritionalGoal {
    @Override
    public int calculateCalorieTarget(User user) {
        return (int)(user.getTDEE());
    }

    @Override
    public String getGoalName() {
        return "Maintenance";
    }
}
