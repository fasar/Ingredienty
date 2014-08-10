package fasar.ingredienty.core.model;

/**
 * Created by fabien_s on 10/08/2014.
 */
public class IngredientFamily {

    private final String id;
    private final String name;

    public IngredientFamily(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
