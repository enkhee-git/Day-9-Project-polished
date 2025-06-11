package sales;

public class Customer {
    private String name;
    private String email;
    private String phone;

    public Customer(String name, String email, String phone) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (!phone.matches("^\\d{8}$")) {
            throw new IllegalArgumentException("Phone number must be 8 digits.");
        }

        this.name = name.trim();
        this.email = email.trim();
        this.phone = phone.trim();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return String.format("Customer: %s\nEmail: %s\nPhone: %s", name, email, phone);
    }
}

