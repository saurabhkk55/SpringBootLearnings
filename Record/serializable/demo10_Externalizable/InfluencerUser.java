package Record.serializable.demo10_Externalizable;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class InfluencerUser extends User {

    private int followerCount;
    private boolean isVerified;

    public InfluencerUser() {
        super();
    }

    public InfluencerUser(int id, String userName, String password, int followerCount, boolean isVerified, Gadget gadget) {
        super(id, userName, password, gadget);
        this.followerCount = followerCount;
        this.isVerified = isVerified;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeInt(followerCount);
        out.writeBoolean(isVerified);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        this.followerCount = in.readInt();
        this.isVerified = in.readBoolean();
    }

    @Override
    public String toString() {
        return super.toString() + ", followerCount=" + followerCount + ", isVerified=" + isVerified;
    }

}
