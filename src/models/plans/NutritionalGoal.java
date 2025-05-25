package models.plans;

public abstract class NutritionalGoal {
    abstract int calculateCalorieTarget(User user);
    public abstract String getGoalName();
}
