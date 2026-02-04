package Record.serializable.demo9_AssociatedClassShouldImplementsSerialzableInterface;

import java.io.Serializable;

public class Gadget implements Serializable {

    private static final long serialVersionUID = 1L;
    private String brand;
    private String type;

    public Gadget(String brand, String type) {
        super();
        this.brand = brand;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Gadget [brand=" + brand + ", type=" + type + "]";
    }

}
