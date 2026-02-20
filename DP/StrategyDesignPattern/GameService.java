package DP.StrategyDesignPattern;

import java.util.Objects;

// ================= Attack Strategy =================
interface AttackStrategy {
    void attack();
}

class SwordAttack implements AttackStrategy {
    @Override
    public void attack() {
        System.out.println("Sword Attack");
    }
}

class GunAttack implements AttackStrategy {
    @Override
    public void attack() {
        System.out.println("Gun Attack");
    }
}

class MagicAttack implements AttackStrategy {
    @Override
    public void attack() {
        System.out.println("Magic Attack");
    }
}

// Default Strategy (Null Object Pattern)
class NoAttack implements AttackStrategy {
    @Override
    public void attack() {
        System.out.println("No Attack Mode Selected");
    }
}

// ================= Move Strategy =================
interface MoveStrategy {
    void move();
}

class WalkMove implements MoveStrategy {
    @Override
    public void move() {
        System.out.println("Walking");
    }
}

class FlyMove implements MoveStrategy {
    @Override
    public void move() {
        System.out.println("Flying");
    }
}

class SwimMove implements MoveStrategy {
    @Override
    public void move() {
        System.out.println("Swimming");
    }
}

// Default Strategy
class NoMove implements MoveStrategy {
    @Override
    public void move() {
        System.out.println("No Move Mode Selected");
    }
}

// ================= Context Class =================
class GameCharacter {
    private AttackStrategy attackStrategy;
    private MoveStrategy moveStrategy;

    // Constructor Injection
    public GameCharacter(AttackStrategy attackStrategy, MoveStrategy moveStrategy) {
        this.attackStrategy = Objects.requireNonNullElse(attackStrategy, new NoAttack());
        this.moveStrategy = Objects.requireNonNullElse(moveStrategy, new NoMove());
    }

    // Runtime Strategy Change
    public void setAttackStrategy(AttackStrategy attackStrategy) {
        this.attackStrategy = Objects.requireNonNull(attackStrategy, "Attack strategy cannot be null");
    }

    public void setMoveStrategy(MoveStrategy moveStrategy) {
        this.moveStrategy = Objects.requireNonNull(moveStrategy, "Move strategy cannot be null");
    }

    public void performAttack() {
        attackStrategy.attack();
    }

    public void performMove() {
        moveStrategy.move();
    }
}

// ================= Main Class =================
public class GameService {
    public static void main(String[] args) {

        AttackStrategy gun = new GunAttack();
        AttackStrategy sword = new SwordAttack();
        MoveStrategy fly = new FlyMove();
        MoveStrategy swim = new SwimMove();

        GameCharacter character = new GameCharacter(gun, fly);

        character.performAttack(); // Gun Attack
        character.performMove(); // Flying

        System.out.println("---- Changing Strategy at Runtime ----");

        character.setAttackStrategy(sword); // Sword Attack
        character.setMoveStrategy(swim); // Swimming

        character.performAttack(); // No Attack Mode Selected
        character.performMove(); // No Move Mode Selected

        GameCharacter character1 = new GameCharacter(null, null);
        character1.performAttack();
        character1.performMove();
    }
}
