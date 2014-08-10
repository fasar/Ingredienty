package fasar.ingredienty.web.model;

import fasar.ingredienty.core.model.Ingredient;

import java.util.Map;

/**
 * Created by fabien_s on 10/08/2014.
 */
public class RecipeIngredients {

    private final Recipe recipe;
    private final Map<Ingredient, Double> ingredients;

    public RecipeIngredients(Recipe recipe, Map<Ingredient, Double> ingredients) {
        this.recipe = recipe;
        this.ingredients = ingredients;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public Map<Ingredient, Double> getIngredients() {
        return ingredients;
    }
}
