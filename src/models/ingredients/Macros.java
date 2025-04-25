package models.ingredients;

import java.util.Objects;

public class Macros {
    private int calories;
    private int proteins;
    private int fats;
    private int carbs;

    public Macros(int calories, int proteins, int fats, int carbs) {
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProteins() {
        return proteins;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
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
        int totalCalories = this.calories + macros.calories;
        int totalProtein = this.proteins + macros.proteins;
        int totalFat = this.fats + macros.fats;
        int totalCarbs = this.carbs + macros.carbs;

        return new Macros(totalCalories, totalProtein, totalFat, totalCarbs);
    }

}
