package models.ingredients;

import java.util.Objects;

public abstract class Ingredient{
    private String name;
    private Macros macros;

    public Ingredient(String name, Macros macros) {
        this.name = name;
        this.macros = macros;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name) && Objects.equals(macros, that.macros);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, macros);
    }

    @Override
    public String toString() {
        return "Ingredient: " + name + " | Macros: [" + macros + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Macros getMacros() {
        return macros;
    }

    public void setMacros(Macros macros) {
        this.macros = macros;
    }

    public double getNutritionFactor(double quantity) {
        return quantity / 100.0;
    }

    public abstract String getUnit();

}