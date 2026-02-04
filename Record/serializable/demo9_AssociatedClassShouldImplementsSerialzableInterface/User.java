package Record.serializable.demo9_AssociatedClassShouldImplementsSerialzableInterface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String userName;
    private transient String password;
    private static String platformName;
    private Gadget gadget;

    public User(int id, String userName, String password, Gadget gadget) {
        super();
        this.id = id;
        this.userName = userName;
        this.password = password;
        platformName = "YouTube";
        this.gadget = gadget;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(encryptPassword(password));
    }

    private String encryptPassword(String password) {
        return new StringBuilder(password).reverse().toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        String encryptedPassword = (String) objectInputStream.readObject();
        password = decryptPassword(encryptedPassword);
    }

    private String decryptPassword(String password) {
        return new StringBuilder(password).reverse().toString();
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", platformName=" + platformName
                + ", gadget=" + gadget + "]";
    }

}