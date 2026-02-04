package Record.serializable.demo2_SaveObjectWithoutSerializableInterface;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Demo {

    private static final String FILE_NAME = "Record/serializable/demo2_SaveObjectWithoutSerializableInterface/data.ser";

    public static void main(String[] args) {
        User user = new User(101, "Saurabh Kardam", "P@$$word");
        System.out.println("Writing Data");
        saveData(user);
        System.out.println("Reading Data");
        loadData();
    }

    public static void saveData(User user) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            objectOutputStream.writeObject(user);
            System.out.println("Writing Data: " + user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadData() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            User user = (User) objectInputStream.readObject();
            System.out.println("Reading Data: " + user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//This is an expected behaviour because we are trying to save an Object of User class which does not implements Serializable interface
// =========== OUTPUT
//Writing Data
//java.io.NotSerializableException: Record.serializable.demo2.User
//at java.base/java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1085)
//at java.base/java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:325)
//at Record.serializable.demo2.Demo.saveData(Demo.java:22)
//at Record.serializable.demo2.Demo.main(Demo.java:15)
//java.io.WriteAbortedException: writing aborted; java.io.NotSerializableException: Record.serializable.demo2.User
//at java.base/java.io.ObjectInputStream.readObject0(ObjectInputStream.java:1627)
//at java.base/java.io.ObjectInputStream.readObject(ObjectInputStream.java:487)
//at java.base/java.io.ObjectInputStream.readObject(ObjectInputStream.java:445)
//at Record.serializable.demo2.Demo.loadData(Demo.java:31)
//at Record.serializable.demo2.Demo.main(Demo.java:17)
//Caused by: java.io.NotSerializableException: Record.serializable.demo2.User
//at java.base/java.io.ObjectOutputStream.writeObject0(ObjectOutputStream.java:1085)
//at java.base/java.io.ObjectOutputStream.writeObject(ObjectOutputStream.java:325)
//at Record.serializable.demo2.Demo.saveData(Demo.java:22)
//at Record.serializable.demo2.Demo.main(Demo.java:15)
//Reading Data
