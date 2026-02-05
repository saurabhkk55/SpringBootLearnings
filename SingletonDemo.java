import java.util.concurrent.SynchronousQueue;

//Double-Checked Locking
class Singleton {
    private static Singleton singleton;

    private Singleton () {}

    public static Singleton getInstance() {
        if(singleton == null) {
            synchronized (Singleton.class) {
                if(singleton == null) {
                    singleton = new Singleton();
                }
            }
        }

        return singleton;
    }
}

public class SingletonDemo {
    static void main() {
        System.out.println(Singleton.getInstance().hashCode());
        System.out.println(Singleton.getInstance().hashCode());
    }
}

//=======OUTPUT
//149928006
//149928006
