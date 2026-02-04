package Record.serializable.demo8_SerializableInheritance;

public class InfluencerUser extends User { // No need for child to implements Serializable if parent (User class) is already doing so.

    // INCORRECT: But it better for child class to use the same default serialVersionUID as parent is using
    // CORRECT: Child class should define its own serialVersionUID to control its version compatibility
    private static final long serialVersionUID = 2L;
    private int followerCount;
    private boolean isVerified;

    public InfluencerUser(int id, String userName, String password, int followerCount, boolean isVerified) {
        super(id, userName, password);
        this.followerCount = followerCount;
        this.isVerified = isVerified;
    }

    @Override
    public String toString() {
        return super.toString() + ", followerCount=" + followerCount + ", isVerified=" + isVerified;
    }
}
