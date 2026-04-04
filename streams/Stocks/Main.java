package streams.Stocks;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Stock> stockList = new ArrayList<>();

        stockList.add(new Stock("buy", "infosys", 100));
        stockList.add(new Stock("buy", "infosys", 100));
        stockList.add(new Stock("sell", "infosys", 50));
        stockList.add(new Stock("sell", "infosys", 20));

        stockList.add(new Stock("buy", "TCS", 20));
        stockList.add(new Stock("buy", "TCS", 40));
        stockList.add(new Stock("Sell", "TCS", 20));

        stockList.add(new Stock("Sell", "Wipro", 40));

        // Approach 1
        Map<String, Integer> result = stockList.stream()
                .collect(Collectors.groupingBy(Stock::getStockName, Collectors.collectingAndThen(
                        Collectors.toList(),
                        stocks -> {
                            int netQuantity = 0;
                            for (Stock st : stocks) {
                                if (st.getType().equalsIgnoreCase("buy")) netQuantity += st.getQuantity();
                                else netQuantity -= st.getQuantity();
                            }
                            return netQuantity;
                        }
                )));

        result.forEach((k, v) -> System.out.println(k + " => " + v));

        // Approach 1
        Map<String, Integer> result1 = stockList.stream()
                .collect(Collectors.groupingBy(
                        Stock::getStockName,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .mapToInt(s -> s.getType().equalsIgnoreCase("buy")
                                                ? s.getQuantity()
                                                : -s.getQuantity())
                                        .sum()
                        )
                ));
        result1.forEach((k, v) -> System.out.println(k + " => " + v));
    }
}
