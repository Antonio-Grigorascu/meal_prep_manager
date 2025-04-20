public enum ActivityLevel {
    SEDENTARY("Sedentary", 1.2),
    LIGHTLY_ACTIVE("Lightly Active", 1.375),
    MODERATELY_ACTIVE("Moderately Active", 1.55),
    VERY_ACTIVE("Very Active", 1.725),
    EXTRA_ACTIVE("Extra Active", 1.9);

    private final String description;
    private final double multiplier;

    ActivityLevel(String description, double multiplier) {
        this.description = description;
        this.multiplier = multiplier;
    }

    public String getDescription() {
        return description;
    }

    public double getMultiplier() {
        return multiplier;
    }

    @Override
    public String toString() {
        return description + " (Multiplier: " + multiplier + ")";
    }
}