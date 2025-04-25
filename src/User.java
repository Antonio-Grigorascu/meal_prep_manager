import java.util.ArrayList;
import java.util.List;

public class User implements Trackable{
    private String name;
    private int age;
    private double weight; // in kg
    private double height; // in cm
    private String gender; // "male" or "female"
    private ActivityLevel activityLevel;
    private List<Double> weightHistory;

    public User(String name, int age, double weight, double height, String gender, ActivityLevel activityLevel) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.gender = gender.toLowerCase();
        this.activityLevel = activityLevel;
        this.weightHistory = new ArrayList<>();
        this.weightHistory.add(weight);
    }

    @Override
    public void updateWeight(double newWeight) {
        this.weight = newWeight;
        this.weightHistory.add(newWeight);
    }

    @Override
    public double getCurrentWeight() {
        return this.weight;
    }

    @Override
    public List<Double> getWeightHistory() {
        return new ArrayList<>(this.weightHistory);
    }

    public double getBMR() {
        if (gender.equals("male")) {
            return 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            return 10 * weight + 6.25 * height - 5 * age - 161;
        }
    }

    public double getTDEE() {
        return getBMR() * activityLevel.getMultiplier();
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
    public ActivityLevel getActivityLevel() { return activityLevel; }

    @Override
    public void printWeightProgress() {
        System.out.println("📉 Progresul greutății pentru " + name + ":");
        List<Double> history = getWeightHistory();
        for (int i = 0; i < history.size(); i++) {
            double w = history.get(i);
            int bars = (int) (w / 2); // o bara = 2kg
            String graph = "█".repeat(bars);
            System.out.printf("Etapa %2d: %5.1f kg | %s%n", i + 1, w, graph);
        }
    }

}
