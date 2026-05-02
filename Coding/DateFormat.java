package Coding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DateFormat {
    static void main() {
        List<String> dates = List.of("1/1/1234", "1/01/1234", "01/01/1234", "12/14/1234");

        for (String date : dates) {
            String formattedDate = formatToMMDDYYYY(date.trim());
            System.out.println("Input date: " + date + " => Formatted date: " + formattedDate);
        }
    }

    public static String formatToMMDDYYYY(String inputDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        LocalDate date = LocalDate.parse(inputDate, inputFormatter);
        return date.format(outputFormatter);
    }
}
