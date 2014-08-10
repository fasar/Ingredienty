package fasar.ingredienty.core.model;

/**
 * Created by fabien_s on 10/08/2014.
 */
public class Ingredient {

    private final String id;
    private final String name;
    private final IngredientFamily family;

    public Ingredient(String id, String name, IngredientFamily family) {
        this.id = id;
        this.name = name;
        this.family = family;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public IngredientFamily getFamily() {
        return family;
    }
}
