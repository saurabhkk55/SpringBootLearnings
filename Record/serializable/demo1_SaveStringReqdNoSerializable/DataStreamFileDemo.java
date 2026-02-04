package Record.serializable.demo1_SaveStringReqdNoSerializable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class DataStreamFileDemo {

    private static final String DATA_FILE_NAME = "Record/serializable/demo1_SaveStringReqdNoSerializable/data.txt";

    public static void main(String[] args) {

        String messageToWrite = "Like, Share and Subscribe";

        System.out.println("Writing Data...");
        writeMessageToFile(messageToWrite);

        System.out.println("Reading Data...");
        readMessageFromFile();
    }

    private static void writeMessageToFile(String messageToWrite) {

        try (DataOutputStream dataOut = new DataOutputStream(new FileOutputStream(DATA_FILE_NAME))) {

            dataOut.writeUTF(messageToWrite);
            System.out.println("Written Message: " + messageToWrite);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private static void readMessageFromFile() {

        try (DataInputStream dataIn = new DataInputStream(new FileInputStream(DATA_FILE_NAME))) {

            String readMessage = dataIn.readUTF();
            System.out.println("Read Message: " + readMessage);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
