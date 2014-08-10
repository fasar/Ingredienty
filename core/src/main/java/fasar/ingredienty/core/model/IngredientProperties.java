package fasar.ingredienty.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabien_s on 10/08/2014.
 */
public class IngredientProperties {

    private final Ingredient ingredient;
    private List<IngredientProperty> properties;

    public IngredientProperties(Ingredient ingredient, List<IngredientProperty> properties) {
        this.ingredient = ingredient;
        this.properties = properties;
    }

    public IngredientProperties(Ingredient ingredient) {
        this.ingredient = ingredient;
        this.properties = new ArrayList<>();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public List<IngredientProperty> getProperties() {
        return properties;
    }
}
