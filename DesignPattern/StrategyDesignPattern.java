package DesignPattern;

interface Talkable {
    void talk();
}

interface Walkable {
    void walk();
}

interface Flyable {
    void fly();
}

class NormalTalk implements Talkable {
    @Override
    public void talk() {
        System.out.println("Normal TALK");
    }
}

class NoTalk implements Talkable {
    @Override
    public void talk() {
        System.out.println("No TALK");
    }
}

class NormalWalk implements Walkable {
    @Override
    public void walk() {
        System.out.println("Normal WALK");
    }
}

class NoWalk implements Walkable {
    @Override
    public void walk() {
        System.out.println("No WALK");
    }
}

class NormalFlyable implements Flyable {
    @Override
    public void fly() {
        System.out.println("Normal FLY");
    }
}

class NoFlyable implements Flyable {
    @Override
    public void fly() {
        System.out.println("No FLY");
    }
}

abstract class Robot {
    private Talkable talkable;
    private Walkable walkable;
    private Flyable flyable;

    Robot(Talkable t, Walkable w, Flyable f) {
        this.talkable = t;
        this.walkable = w;
        this.flyable = f;
    }

    void talk() {
        this.talkable.talk();
    }

    void walk() {
        this.walkable.walk();
    }

    void fly() {
        this.flyable.fly();
    }

    abstract void projection();
}

class NormalRobot extends Robot {

    public NormalRobot(Talkable t, Walkable w, Flyable f) {
        super(t, w, f);
    }

    @Override
    void projection() {
        System.out.println("DesignPattern.NormalRobot projection");
    }
}

class SpecialRobot extends Robot {

    public SpecialRobot(Talkable t, Walkable w, Flyable f) {
        super(t, w, f);
    }

    @Override
    void projection() {
        System.out.println("DesignPattern.SpecialRobot projection");
    }
}

public class StrategyDesignPattern {
    static void main() {
        NormalTalk normalTalk = new NormalTalk();
        NormalWalk normalWalk = new NormalWalk();
        NormalFlyable normalFlyable = new NormalFlyable();

        NoTalk noTalk = new NoTalk();
        NoWalk noWalk = new NoWalk();
        NoFlyable noFlyable = new NoFlyable();

        Robot robot1 = new NormalRobot(normalTalk, normalWalk, normalFlyable);
        Robot robot2 = new NormalRobot(normalTalk, normalWalk, noFlyable);
        Robot robot3 = new SpecialRobot(noTalk, noWalk, normalFlyable);

        robot1.talk();
        robot1.walk();
        robot1.fly();
        robot1.projection();
        System.out.println("---------------------------");
        robot2.talk();
        robot2.walk();
        robot2.fly();
        robot2.projection();
        System.out.println("---------------------------");
        robot3.talk();
        robot3.walk();
        robot3.fly();
        robot3.projection();
    }
}
