public class User {
    private String name;
    private int age;
    private double weight; // in kg
    private double height; // in cm
    private String gender; // "male" or "female"
    private ActivityLevel activityLevel;

    public User(String name, int age, double weight, double height, String gender, ActivityLevel activityLevel) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.gender = gender.toLowerCase();
        this.activityLevel = activityLevel;
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
}
