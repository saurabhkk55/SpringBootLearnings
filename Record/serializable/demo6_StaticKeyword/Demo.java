package Record.serializable.demo6_StaticKeyword;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Demo {

    private static final String FILE_NAME = "Record/serializable/demo6_StaticKeyword/data.ser";

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

//========OUTPUT
//Writing Data
//Writing Data: User [id=101, name=Saurabh Kardam, password=P@$$word, platformName=YouTube]
//Reading Data
//Reading Data: User [id=101, name=Saurabh Kardam, password=null, platformName=YouTube]

//Note: In the file we won't be having platformName=YouTube