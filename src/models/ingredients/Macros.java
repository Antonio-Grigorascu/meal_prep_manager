package models.ingredients;

import java.util.Objects;

public class Macros {
    private double calories;
    private double proteins;
    private double fats;
    private double carbs;

    public Macros(double calories, double proteins, double fats, double carbs) {
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public double getCarbs() {
        return carbs;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Macros macros = (Macros) o;
        return calories == macros.calories && proteins == macros.proteins && fats == macros.fats && carbs == macros.carbs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(calories, proteins, fats, carbs);
    }

    @Override
    public String toString() {
        return "Calorii: " + calories +
                " kcal, Proteine: " + proteins +
                " g, Grăsimi: " + fats +
                " g, Carbohidrați: " + carbs + " g";
    }

    public Macros add(Macros macros) {
        double totalCalories = this.calories + macros.calories;
        double totalProtein = this.proteins + macros.proteins;
        double totalFat = this.fats + macros.fats;
        double totalCarbs = this.carbs + macros.carbs;

        return new Macros(totalCalories, totalProtein, totalFat, totalCarbs);
    }

}
