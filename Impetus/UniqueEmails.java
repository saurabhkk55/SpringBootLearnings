package Impetus;

import java.util.*;
import java.util.stream.Collectors;

class User {
    String name;
    List<String> emails;

    public User(String name, List<String> emails) {
        this.name = name;
        this.emails = emails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}

public class UniqueEmails {
    static void main() {
        List<User> users = Arrays.asList(
                new User("A", Arrays.asList("123@example.com", "234@example.com")),
                new User("B", Arrays.asList()),
                new User("C", Arrays.asList("123@example.com")),
                new User("D", Arrays.asList("456@example.com", "jahshshhs")),
                new User("E", null)
        );

        // only unique emails, no null, no empty
        List<String> uniqueEmails =
                users.stream()
                        .map(User::getEmails)          // get email lists
                        .filter(Objects::nonNull)      // remove null lists
                        .flatMap(List::stream)         // flatten
                        .filter(email -> email.contains("@")) // basic validation
                        .distinct()                    // unique
                        .collect(Collectors.toList());

        System.out.println(uniqueEmails);
    }
}
