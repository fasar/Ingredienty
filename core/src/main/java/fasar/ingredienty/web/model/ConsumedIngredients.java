package fasar.ingredienty.web.model;

import fasar.ingredienty.core.model.Ingredient;

import java.util.Date;
import java.util.Map;

/**
 * Created by fabien_s on 10/08/2014.
 */
public class ConsumedIngredients {

    private final User user;
    private final Map<Ingredient, Double> ingredients;
    private final Date date;

    public ConsumedIngredients(User user, Map<Ingredient, Double> ingredients, Date date) {
        this.user = user;
        this.ingredients = ingredients;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public Map<Ingredient, Double> getIngredients() {
        return ingredients;
    }

    public Date getDate() {
        return date;
    }
}
