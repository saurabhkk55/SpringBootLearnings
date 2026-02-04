package Record.serializable.demo10_Externalizable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class User implements Externalizable {

    private int id;
    private String userName;
    private String password;
    private static String platformName;
    private Gadget gadget;

    public User() {
        super();
    }

    public User(int id, String userName, String password, Gadget gadget) {
        super();
        this.id = id;
        this.userName = userName;
        this.password = password;
        platformName = "YouTube";
        this.gadget = gadget;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(id);
        out.writeUTF(userName);
        out.writeUTF(encryptPassword(password));
        out.writeObject(gadget);
    }

    private String encryptPassword(String password) {
        return new StringBuilder(password).reverse().toString();
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.id = in.readInt();
        this.userName = in.readUTF();
        this.password = decryptPassword(in.readUTF());
        this.gadget = (Gadget) in.readObject();
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
