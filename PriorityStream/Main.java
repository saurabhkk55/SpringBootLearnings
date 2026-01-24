package PriorityStream;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Priority t1 = new Priority("office", 3);
        Priority t2 = new Priority("Bath", 1);
        Priority t3 = new Priority("BWakeup", 0);
        Priority t4 = new Priority("Home", 4);
        Priority t5 = new Priority("Breakfast", 2);
        Priority t6 = new Priority("AWakeup", 0);

        List<Priority> list = Arrays.asList(t1, t2, t3, t4, t5, t6);

        list.stream()
//                .sorted((tt1, tt2) -> tt1.priority - tt2.getPriority())
//                .sorted((tt1, tt2) -> Integer.valueOf(tt1.getPriority()).compareTo(Integer.valueOf(tt2.getPriority())))
//                .sorted((tt1, tt2) -> {
//                    if(tt1.getPriority() > tt2.getPriority()) return 1;
//                    else if (tt1.getPriority() == tt2.getPriority()) {
//                        return tt1.getTask().compareTo(tt2.getTask());
//                    }
//                    else return -1;
//                })
                .sorted()
                .forEach(t -> System.out.println(t.getPriority() + " => " + t.getTask()));
    }
}
