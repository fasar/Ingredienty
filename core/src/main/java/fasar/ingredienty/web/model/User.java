package fasar.ingredienty.web.model;

/**
 * Created by fabien_s on 10/08/2014.
 */
public class User {

    private final String id;
    private final String email;
    private final String name;
    private final String password;
    private final Role role;

    public User(String id, String email, String name, String password, Role role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
