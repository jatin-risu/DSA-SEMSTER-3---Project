import java.util.ArrayList;
import java.util.List;

/**
 * A placed order. It waits in the queue until the kitchen processes it.
 */
public class Order {
    private static int counter = 1000;

    private final int orderId;
    private final String customerName;
    private final List<CartItem> items;
    private final double total;
    private String status;

    public Order(String customerName, List<CartItem> cartItems) {
        this.orderId = ++counter;
        this.customerName = customerName;
        this.items = new ArrayList<>(cartItems);   // copy so later cart changes don't affect the order
        double sum = 0;
        for (CartItem c : items) {
            sum += c.getSubtotal();
        }
        this.total = sum;
        this.status = "PENDING";
    }

    public int getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void printDetails() {
        System.out.println("Order #" + orderId + " | Customer: " + customerName + " | Status: " + status);
        for (CartItem c : items) {
            System.out.println("   - " + c);
        }
        System.out.printf("   Total: Rs.%.2f%n", total);
    }

    @Override
    public String toString() {
        return String.format("Order #%d | %-12s | Rs.%.2f | %s", orderId, customerName, total, status);
    }
}
