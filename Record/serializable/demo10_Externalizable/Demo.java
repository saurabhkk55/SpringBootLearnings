package Record.serializable.demo10_Externalizable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Demo {

    private static final String FILE_NAME = "Record/serializable/demo10_Externalizable/data.ser";

    public static void main(String[] args) {
        Gadget gadget = new Gadget("Lenovo", "Laptop");
        User user = new InfluencerUser(101, "Abhishek Verma", "P@$$word", 17_089, false, gadget);
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
//Writing Data: User [id=101, userName=Abhishek Verma, password=P@$$word, platformName=YouTube, gadget=Gadget [brand=Lenovo, type=Laptop]], followerCount=17089, isVerified=false
//Reading Data
//Reading Data: User [id=101, userName=Abhishek Verma, password=P@$$word, platformName=YouTube, gadget=Gadget [brand=Lenovo, type=Laptop]], followerCount=17089, isVerified=false
