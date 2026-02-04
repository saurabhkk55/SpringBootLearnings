package Record.serializable.demo2_SaveObjectWithoutSerializableInterface;

public class User {

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
