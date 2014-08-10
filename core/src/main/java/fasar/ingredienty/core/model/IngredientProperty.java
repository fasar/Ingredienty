package fasar.ingredienty.core.model;

import fasar.ingredienty.common.model.Unit;

/**
 * Created by fabien_s on 10/08/2014.
 */
public class IngredientProperty {
    public final String id;
    public final String name;
    public final String description;
    public final Unit unit;

    public IngredientProperty(String id, String name, String description, Unit unit) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Unit getUnit() {
        return unit;
    }
}
