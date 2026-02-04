package Record.serializable.demo6_StaticKeyword;

import java.io.Serial;
import java.io.Serializable;

public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // demo4: default serialVersionUID
    private int id;
    private String name;
    private transient String password;  // demo5: using transient
    private static String platformName; // demo6: using static

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

// demo4:
// always define default serialVersionUID
// if a developer don't define it then it will be generated dynamically on its own.
