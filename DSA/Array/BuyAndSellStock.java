package DSA.Array;

public class BuyAndSellStock {
    static void main() {
        int[] prices = {7, 1, 5, 3, 6, 4}; // OUTPUT: 5

        int maxProfitObtained = maxProfit(prices);

        System.out.println(maxProfitObtained);
    }

    static int maxProfit(int[] prices) {
        int buyIdx = 0, sellIdx = 0, profit = 0, maxProfit = 0, arrLength = prices.length;

        while (sellIdx < arrLength) {
            profit = prices[sellIdx] - prices[buyIdx];
            maxProfit = Math.max(profit, maxProfit);

            if (profit >= 0) {
                sellIdx++;
            } else {
                buyIdx = sellIdx;
                sellIdx++;
            }
        }

        return maxProfit;
    }
}
