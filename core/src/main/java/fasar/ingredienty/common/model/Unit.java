package fasar.ingredienty.common.model;

/**
 * Created by fabien_s on 10/08/2014.
 */
public class Unit {

    private final String id;
    private final String name;
    private final String nameAbrv;
    private final String plural;
    private final String pluralAbrv;

    public Unit(String id, String name, String nameAbrv, String plural, String pluralAbrv) {
        this.id = id;
        this.name = name;
        this.nameAbrv = nameAbrv;
        this.plural = plural;
        this.pluralAbrv = pluralAbrv;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameAbrv() {
        return nameAbrv;
    }

    public String getPlural() {
        return plural;
    }

    public String getPluralAbrv() {
        return pluralAbrv;
    }
}
