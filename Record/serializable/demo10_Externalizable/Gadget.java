package Record.serializable.demo10_Externalizable;

import java.io.*;

public class Gadget implements Externalizable {

    private String brand;
    private String type;

    public Gadget() {
        super();
    }

    public Gadget(String brand, String type) {
        super();
        this.brand = brand;
        this.type = type;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(brand);
        out.writeUTF(type);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.brand = in.readUTF();
        this.type = in.readUTF();
    }

    @Override
    public String toString() {
        return "Gadget [brand=" + brand + ", type=" + type + "]";
    }

}
