package fasar.ingredienty.web.model;

/**
 * Created by fabien_s on 10/08/2014.
 */
public class Recipe {

    private final String id;
    private final String name;
    private final String instructions;
    private final String authorId;
    private final String description;
    private final Integer preparationSec;
    private final Integer cookSec;

    public Recipe(String id, String name, String instructions, String authorId, String description, Integer preparationSec, Integer cookSec) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.authorId = authorId;
        this.description = description;
        this.preparationSec = preparationSec;
        this.cookSec = cookSec;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getAuthorId() {
        return authorId;
    }

    public String getDescription() {
        return description;
    }

    public Integer getPreparationSec() {
        return preparationSec;
    }

    public Integer getCookSec() {
        return cookSec;
    }
}
