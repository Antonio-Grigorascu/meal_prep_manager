package models.ingredients;

public class VolumeIngredient extends Ingredient {
    private final String unit = "ml";

    public VolumeIngredient(String name, Macros macros) {
        super(name, macros);
    }

    @Override
    public String getUnit() {
        return unit;
    }
}
