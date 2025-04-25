public class WeightIngredient extends Ingredient {
    private final String unit = "g";

    public WeightIngredient(String name, Macros macros) {
        super(name, macros);
    }

    @Override
    public String getUnit() {
        return unit;
    }

}
