package sales;

public class Product {
    private String name;
    private double price;
    private int stockQuantity;

    public Product(String name, double price, int stockQuantity) {
        if (!name.matches("[a-zA-Z0-9\\s]+")) {
            throw new IllegalArgumentException("Product name must contain only letters and numbers.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive.");
        }
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void reduceStock(int qty) {
        if (qty < 0 || qty > stockQuantity) {
            throw new IllegalArgumentException("Invalid quantity.");
        }
        stockQuantity -= qty;
    }

    public void increaseStock(int qty) {
        if (qty < 0) {
            throw new IllegalArgumentException("Cannot add negative stock.");
        }
        stockQuantity += qty;
    }

    @Override
    public String toString() {
        return name + " - $" + price;
    }
}

