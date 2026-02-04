package Record.serializable.demo9_AssociatedClassShouldImplementsSerialzableInterface;

public class InfluencerUser extends User {

    private static final long serialVersionUID = 2L;
    private int followerCount;
    private boolean isVerified;

    public InfluencerUser(int id, String userName, String password, int followerCount, boolean isVerified, Gadget gadget) {
        super(id, userName, password, gadget);
        this.followerCount = followerCount;
        this.isVerified = isVerified;
    }

    @Override
    public String toString() {
        return super.toString() + ", followerCount=" + followerCount + ", isVerified=" + isVerified;
    }

}
