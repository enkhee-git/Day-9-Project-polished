package sales;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SalesSystemGUI extends JFrame {
    private static final Logger logger = LogManager.getLogger(SalesSystemGUI.class);

    private List<Product> products;
    private Customer customer;
    private Order order;

    private JComboBox<Product> productCombo;
    private JTextField qtyField;
    private JTextArea orderArea;
    private JLabel totalLabel;

    public SalesSystemGUI() {
        logger.info("Application started.");

        setTitle("Sales System");
        setSize(550, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initProducts();
        initComponents();
        askCustomerInfo();
    }

    private void initProducts() {
        products = List.of(
            new Product("Mouse", 10.0, 50),
            new Product("Keyboard", 30.0, 30),
            new Product("Laptop", 900.0, 10)
        );
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Top panel
        JPanel inputPanel = new JPanel(new FlowLayout());

        productCombo = new JComboBox<>(products.toArray(new Product[0]));
        qtyField = new JTextField(5);
        JButton addButton = new JButton("Add");

        inputPanel.add(new JLabel("Product:"));
        inputPanel.add(productCombo);
        inputPanel.add(new JLabel("Quantity:"));
        inputPanel.add(qtyField);
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        // Center panel
        orderArea = new JTextArea();
        orderArea.setEditable(false);
        add(new JScrollPane(orderArea), BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Total: $0.00");
        JButton saveButton = new JButton("Place Order");

        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(saveButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        // Add actions
        addButton.addActionListener(e -> addProductToOrder());
        saveButton.addActionListener(e -> saveOrder());
    }

    private void askCustomerInfo() {
        try {
            String name = JOptionPane.showInputDialog(this, "Enter your name:");
            String email = JOptionPane.showInputDialog(this, "Enter your email:");
            String phone = JOptionPane.showInputDialog(this, "Enter your phone number:");

            customer = new Customer(name, email, phone);
            order = new Order(customer);

            logger.info("Customer registered: " + name);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
            askCustomerInfo(); // try again
        }
    }

    private void addProductToOrder() {
        try {
            Product selectedProduct = (Product) productCombo.getSelectedItem();
            int qty = Integer.parseInt(qtyField.getText());

            boolean success = order.addItem(selectedProduct, qty);
            if (success) {
                updateOrderDisplay();
                qtyField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Not enough stock.", "Stock Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateOrderDisplay() {
        orderArea.setText("");
        for (OrderItem item : order.getItems()) {
            orderArea.append(item.toString() + "\n");
        }
        totalLabel.setText(String.format("Total: $%.2f", order.getTotalPrice()));
    }

    private void saveOrder() {
        if (order.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in the order.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        order.saveToFile();
        JOptionPane.showMessageDialog(this, "Order saved to Desktop!", "Success", JOptionPane.INFORMATION_MESSAGE);
        logger.info("Order placed by: " + customer.getName());
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SalesSystemGUI().setVisible(true));
    }
}

