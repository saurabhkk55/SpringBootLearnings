package Stocks;

public class Stock {
    String type;
    String stockName;
    int quantity;

    public Stock(String type, String stockName, int quantity) {
        this.type = type;
        this.stockName = stockName;
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public String getStockName() {
        return stockName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "type='" + type + '\'' +
                ", stockName='" + stockName + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
