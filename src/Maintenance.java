public class Maintenance implements NutritionalGoal {
    @Override
    public int calculateCalorieTarget(User user) {
        return (int)(user.getTDEE());
    }

    @Override
    public String getGoalName() {
        return "Maintenance";
    }
}
