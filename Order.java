package sales;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Order {
    private static final Logger logger = LogManager.getLogger(Order.class);

    private Customer customer;
    private List<OrderItem> items;

    public Order(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }
        this.customer = customer;
        this.items = new ArrayList<>();
    }

    public boolean addItem(Product product, int quantity) {
        if (quantity > product.getStockQuantity()) {
            logger.warn("Not enough stock for product: " + product.getName());
            return false;
        }

        product.reduceStock(quantity);
        items.add(new OrderItem(product, quantity));
        logger.info("Added item to order: " + product.getName() + " x" + quantity);
        return true;
    }

    public List<OrderItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(OrderItem::getSubtotal).sum();
    }

    public Customer getCustomer() {
        return customer;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void saveToFile() {
        try {
            String userHome = System.getProperty("user.home");
            File file = new File(userHome + "/Desktop/order_summary.txt");
            file.getParentFile().mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(customer.toString());
                writer.newLine();
                writer.write("----- Ordered Items -----");
                writer.newLine();
                for (OrderItem item : items) {
                    writer.write(item.toString());
                    writer.newLine();
                }
                writer.write("-------------------------");
                writer.newLine();
                writer.write(String.format("Total: $%.2f", getTotalPrice()));
                writer.newLine();
            }

            logger.info("Order saved to file: " + file.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to save order to file.", e);
        }
    }
}

