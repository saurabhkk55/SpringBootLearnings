package DesignPattern;

interface GreetingService {
    void greet();
}

class EveningGreetingService implements GreetingService {

    @Override
    public void greet() {
        System.out.println("Evening");
    }
}

class EveningAndRainingGreetingService implements GreetingService {

    @Override
    public void greet() {
        System.out.println("Evening + Rain");
    }
}

class GreetingServiceProxy implements GreetingService {

    private final GreetingService realGreetingService;

    public GreetingServiceProxy(GreetingService realGreetingService) {
        this.realGreetingService = realGreetingService;
    }

    @Override
    public void greet() {
        System.out.println("Morning");   // pre-processing
        realGreetingService.greet();     // delegation
        System.out.println("Night");     // post-processing
    }
}

public class ProxyPattern {

    public static void main(String[] args) {

        GreetingService greetingService = new GreetingServiceProxy(new EveningGreetingService());

        greetingService.greet();
    }
}
