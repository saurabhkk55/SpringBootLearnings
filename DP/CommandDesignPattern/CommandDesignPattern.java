package DP.CommandDesignPattern;

import java.util.List;

interface Command {
    void execute();
    void undo();
}

interface Appliance {
    void on();
    void off();
}

class TV implements Appliance {
    public void on() {
        System.out.println("TV is ON");
    }

    public void off() {
        System.out.println("TV is OFF");
    }
}

class Lights implements Appliance {
    public void on() {
        System.out.println("Lights is ON");
    }

    public void off() {
        System.out.println("Lights is OFF");
    }
}

class AC implements Appliance {
    public void on() {
        System.out.println("AC is ON");
    }

    public void off() {
        System.out.println("AC is OFF");
    }
}

class ApplianceCommand implements Command {

    private final Appliance appliance;
    private boolean isOn = false;

    public ApplianceCommand(Appliance appliance) {
        this.appliance = appliance;
    }

    @Override
    public void execute() {
        if (!isOn) {
            appliance.on();
            isOn = true;
        } else {
            undo();
        }
    }

    @Override
    public void undo() {
        appliance.off();
        isOn = false;
    }
}

class RemoteController {

    private final List<Command> commands;

    public RemoteController(List<Command> commands) {
        this.commands = commands;
    }

    void pressButton(Integer buttonNum) {
        if (buttonNum != null && buttonNum >= 0 && buttonNum < commands.size()) {
            commands.get(buttonNum).execute();
        }
    }

    void getRemoteInfo() {
        for (int i = 0; i < commands.size(); i++) {
            System.out.println("PRESS: " + i);
        }
    }
}

public class CommandDesignPattern {

    public static void main(String[] args) {

        Command acCommand = new ApplianceCommand(new AC());
        Command lightCommand = new ApplianceCommand(new Lights());
        Command tvCommand = new ApplianceCommand(new TV());

        List<Command> homeCommands = List.of(acCommand, lightCommand, tvCommand);
        RemoteController homeRemote = new RemoteController(homeCommands);

        homeRemote.getRemoteInfo();

        homeRemote.pressButton(0);
        homeRemote.pressButton(1);
        homeRemote.pressButton(2);

        homeRemote.pressButton(0);
        homeRemote.pressButton(1);
        homeRemote.pressButton(2);

        System.out.println("---------------");

        Command ac = new ApplianceCommand(new AC());
        Command light = new ApplianceCommand(new Lights());

        RemoteController schoolRemote = new RemoteController(List.of(ac, light));

        schoolRemote.pressButton(0);
        schoolRemote.pressButton(0);
        schoolRemote.pressButton(1);
    }
}
