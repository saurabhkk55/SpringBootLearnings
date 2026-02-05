import java.util.HashMap;

// Rules to create a Immutable class:
//1. Declare the class as final to prevent subclassing.
//2. Make all fields private and final to ensure they cannot be modified.
//3. Do not provide setters or methods that modify fields.
//4. Initialize all fields in the constructor.
//5. Ensure deep copying for mutable fields or collections to prevent external modification.
//6. Return deep copies of mutable fields in getter methods.

class Immutable {
    private final String name;
    private final int age;
    private final HashMap<String, String> metadata;

    // deep copy
    public Immutable(String name, int age, HashMap<String, String> metadata) {
        this.name = name;
        this.age = age;
        this.metadata = new HashMap<>(metadata);
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // return deep copy
    public HashMap<String, String> getMetadata() {
        return new HashMap<>(metadata);
    }

    @Override
    public String toString() {
        return "Immutable[name=" + name +
                ", age=" + age +
                ", metadata=" + metadata + "]";
    }
}

public class TestImmutableClass {
    static void main() {
        HashMap<String, String> metadata1 = new HashMap<>();
        metadata1.put("key1", "value1");
        metadata1.put("key2", "value2");

        Immutable immutable = new Immutable("Saurabh", 27, metadata1);

        System.out.println(immutable);

        // Modify the original map (metada1) to test if it affects the immutable object
        metadata1.put("key3", "value3");
        // Display the modified original map (metadata1)
        System.out.println("Original map (metadata1) after modification: " + metadata1);

        // the immutable object's state shouldn't change, showing that internal state is not affected
        System.out.println("After modifying original map (metadata1), immutable object remains unchanged: " + immutable);

        // Attempt to modify the return map
        // When the metadata field (HashMap) is accessed via the getMetadata() method, a deep copy is returned.
        // Modifying the returned map does not affect the original state of the object
        immutable.getMetadata().put("key4", "value4");
        System.out.println("After modifying returned map, immutable object remains unchanged: " + immutable);
    }
}

//=========OUTPUT
//Immutable[name=Saurabh, age=27, metadata={key1=value1, key2=value2}]
//Original map (metadata1) after modification: {key1=value1, key2=value2, key3=value3}
//After modifying original map (metadata1), immutable object remains unchanged: Immutable[name=Saurabh, age=27, metadata={key1=value1, key2=value2}]
//After modifying returned map, immutable object remains unchanged: Immutable[name=Saurabh, age=27, metadata={key1=value1, key2=value2}]
