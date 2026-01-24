package Stocks;

import java.util.*;
import java.util.stream.Collectors;

//public class Main {
//    public static void main(String[] args) {
//        List<Stock> stockList = new ArrayList<>();
//
//        stockList.add(new Stock("buy", "infosys", 100));
//        stockList.add(new Stock("buy", "infosys", 100));
//        stockList.add(new Stock("sell", "infosys", 50));
//        stockList.add(new Stock("sell", "infosys", 20));
//
//        stockList.add(new Stock("buy", "TCS", 20));
//        stockList.add(new Stock("buy", "TCS", 40));
//        stockList.add(new Stock("Sell", "TCS", 20));
//
//        stockList.add(new Stock("Sell", "Wipro", 40));
//
//        Map<String, List<Stock>> collect = stockList.stream()
//                .collect(Collectors.groupingBy(stk -> stk.getStockName()));
//
//        // collect.forEach((x, y) -> System.out.println(x + " => " + y));
//
//        List<Map<String, Integer>> collect1 = collect.entrySet().stream()
//                .map(stk -> {
//                    String stockName = stk.getKey();
//                    int net = 0;
//                    for (Stock s : stk.getValue()) {
//                        if (s.getType() == "buy") net += s.getQuantity();
//                        else net -= s.getQuantity();
//                    }
//                    Map<String, Integer> StockAndItsNetQuantity = new HashMap<>();
//                    StockAndItsNetQuantity.put(stockName, net);
//                    return StockAndItsNetQuantity;
//                })
//                .collect(Collectors.toList());
//
//        collect1.forEach(x -> System.out.println(x));
//    }
//}

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

        Map<String, Integer> result = stockList.stream()
                .collect(Collectors.groupingBy(
                        Stocks.Stock::getStockName,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .mapToInt(s -> s.getType().equalsIgnoreCase("buy")
                                                ? s.getQuantity()
                                                : -s.getQuantity())
                                        .sum()
                        )
                ));

        result.forEach((k, v) -> System.out.println(k + " => " + v));
    }
}
