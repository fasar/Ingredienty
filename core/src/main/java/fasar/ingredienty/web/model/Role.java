package fasar.ingredienty.web.model;

/**
 * Created by fabien_s on 10/08/2014.
 */
public class Role {
    private final String name;
    private final String description;

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
