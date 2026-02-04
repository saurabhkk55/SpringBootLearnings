package Record.serializable.demo4_serialVersionUID;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // default serialVersionUID
    private final int id;
    private final String name;
    private final String password;
    private final String platformName;

    public User(int id, String name, String password) {
        super();
        this.id = id;
        this.name = name;
        this.password = password;
        this.platformName = "YouTube";
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", password=" + password + ", platformName=" + platformName + "]";
    }
}

// always define default serialVersionUID
// if a developer don't define it then it will be generated dynamically on its own.
