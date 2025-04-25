public class UnitIngredient extends Ingredient {
    private final String unit = "buc";

    public UnitIngredient(String name, Macros macros) {
        super(name, macros);
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public double getNutritionFactor(double quantity) {
        return (int) quantity;
    }
}
