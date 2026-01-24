package DesignPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

interface Command {
    void execute();
    void undo();
}

interface Appliance {
    void on();
    void off();
}

class TV implements Appliance {

    @Override
    public void on() {
        System.out.println("TV is ON");
    }

    @Override
    public void off() {
        System.out.println("TV is OFF");
    }
}

class Lights implements Appliance {

    @Override
    public void on() {
        System.out.println("Lights is ON");
    }

    @Override
    public void off() {
        System.out.println("Lights is OFF");
    }
}

class AC implements Appliance {

    @Override
    public void on() {
        System.out.println("AC is ON");
    }

    @Override
    public void off() {
        System.out.println("AC is OFF");
    }
}

class TVCommand implements Command {
    private final TV tv;

    public TVCommand(TV tv) {
        this.tv = tv;
    }

    @Override
    public void execute() {
        tv.on();
    }

    @Override
    public void undo() {
        tv.off();
    }
}

class ACCommand implements Command {
    private final AC ac;

    public ACCommand(AC ac) {
        this.ac = ac;
    }

    @Override
    public void execute() {
        ac.on();
    }

    @Override
    public void undo() {
        ac.off();
    }
}

class LightCommand implements Command {
    private final Lights lights;

    public LightCommand(Lights lights) {
        this.lights = lights;
    }

    @Override
    public void execute() {
        lights.on();
    }

    @Override
    public void undo() {
        lights.off();
    }
}

class RemoteController {
    private final List<Command> applianceCommands;
    private final List<Boolean> applianceState;

    public RemoteController(List<Command> applianceCommands) {
        this.applianceCommands = applianceCommands;
        this.applianceState = new ArrayList<>(applianceCommands.size());

        for (int i = 0; i < applianceCommands.size(); i++) {
            applianceState.add(false); // OFF by default
        }
    }

    void pressButton(Integer buttonNum) {
        if(!Objects.isNull(buttonNum) && buttonNum < applianceState.size()) {
            Boolean appState = applianceState.get(buttonNum);
            if (!appState) {
                applianceCommands.get(buttonNum).execute();
                applianceState.set(buttonNum, true);
            } else {
                applianceCommands.get(buttonNum).undo();
                applianceState.set(buttonNum, false);
            }
        }
    }

    void getRemoteInfo() {
        for (int i = 0; i < applianceCommands.size(); i++) {
            System.out.println("PRESS: " + i + " for " + applianceCommands.get(i).getClass().getName().substring(14).toUpperCase());
        }
    }
}

public class CommandDesignPattern {
    static void main() {
        Command acCommand = new ACCommand(new AC());
        Command lightCommand = new LightCommand(new Lights());
        Command tvCommand = new TVCommand(new TV());
        List<Command> homeApplianceCommands = List.of(acCommand, lightCommand, tvCommand);
        RemoteController homeRemoteController = new RemoteController(homeApplianceCommands);
        homeRemoteController.getRemoteInfo();
        homeRemoteController.pressButton(0);
        homeRemoteController.pressButton(1);
        homeRemoteController.pressButton(2);
        homeRemoteController.pressButton(0);
        homeRemoteController.pressButton(1);
        homeRemoteController.pressButton(2);
        homeRemoteController.pressButton(null);

        System.out.println("------------------------------------");

        Command acCommand1 = new ACCommand(new AC());
        Command lightCommand1 = new LightCommand(new Lights());
        List<Command> schoolApplianceCommands = List.of(acCommand1, lightCommand1);
        RemoteController schoolRemoteController = new RemoteController(schoolApplianceCommands);
        schoolRemoteController.getRemoteInfo();
        schoolRemoteController.pressButton(0);
        schoolRemoteController.pressButton(0);
        schoolRemoteController.pressButton(0);
        schoolRemoteController.pressButton(1);
        schoolRemoteController.pressButton(null);
    }
}
