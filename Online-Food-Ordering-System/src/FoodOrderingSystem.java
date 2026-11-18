import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/**
 * Online Food Ordering System (console based)
 *
 * Data structures used:
 *   ArrayList -> menu and cart
 *   Stack     -> undo last cart action + completed order history (latest first)
 *   Queue     -> pending orders processed in FIFO order
 *   Sorting   -> bubble sort (price) and Collections.sort with Comparator (name, rating)
 */
public class FoodOrderingSystem {

    private final List<MenuItem> menu = new ArrayList<>();
    private final List<CartItem> cart = new ArrayList<>();
    private final Stack<MenuItem> undoStack = new Stack<>();
    private final Queue<Order> pendingOrders = new LinkedList<>();
    private final Stack<Order> orderHistory = new Stack<>();
    private final Scanner sc = new Scanner(System.in);

    public FoodOrderingSystem() {
        loadMenu();
    }

    private void loadMenu() {
        menu.add(new MenuItem(1, "Paneer Butter Masala", "Main Course", 220.00, 4.5));
        menu.add(new MenuItem(2, "Veg Biryani", "Main Course", 180.00, 4.2));
        menu.add(new MenuItem(3, "Margherita Pizza", "Pizza", 250.00, 4.4));
        menu.add(new MenuItem(4, "Veg Burger", "Fast Food", 90.00, 3.9));
        menu.add(new MenuItem(5, "French Fries", "Snacks", 80.00, 4.0));
        menu.add(new MenuItem(6, "Masala Dosa", "South Indian", 100.00, 4.6));
        menu.add(new MenuItem(7, "Gulab Jamun", "Dessert", 60.00, 4.7));
        menu.add(new MenuItem(8, "Cold Coffee", "Beverage", 70.00, 4.1));
        menu.add(new MenuItem(9, "Chicken Biryani", "Main Course", 260.00, 4.8));
        menu.add(new MenuItem(10, "Chowmein", "Chinese", 120.00, 4.0));
    }

    // ---------------------------------------------------------------- Menu

    private void showMenu(List<MenuItem> list) {
        System.out.println("\n================= MENU =================");
        System.out.printf("%-4s %-22s %-12s %-11s %s%n", "ID", "Name", "Category", "Price", "Rating");
        System.out.println("----------------------------------------------------------");
        for (MenuItem m : list) {
            System.out.println(m);
        }
    }

    // ------------------------------------------------------------- Sorting

    /** Manual bubble sort by price (ascending) to show the algorithm explicitly. */
    private void sortByPriceLowToHigh() {
        for (int i = 0; i < menu.size() - 1; i++) {
            for (int j = 0; j < menu.size() - i - 1; j++) {
                if (menu.get(j).getPrice() > menu.get(j + 1).getPrice()) {
                    MenuItem temp = menu.get(j);
                    menu.set(j, menu.get(j + 1));
                    menu.set(j + 1, temp);
                }
            }
        }
    }

    private void sortMenu() {
        System.out.println("\nSort menu by:");
        System.out.println("1. Price (Low to High)");
        System.out.println("2. Price (High to Low)");
        System.out.println("3. Name (A to Z)");
        System.out.println("4. Rating (High to Low)");
        System.out.println("5. ID (default order)");
        int choice = readInt("Enter choice: ");

        switch (choice) {
            case 1:
                sortByPriceLowToHigh();
                break;
            case 2:
                Collections.sort(menu, Comparator.comparingDouble(MenuItem::getPrice).reversed());
                break;
            case 3:
                Collections.sort(menu, Comparator.comparing(MenuItem::getName));
                break;
            case 4:
                Collections.sort(menu, Comparator.comparingDouble(MenuItem::getRating).reversed());
                break;
            case 5:
                Collections.sort(menu, Comparator.comparingInt(MenuItem::getId));
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        showMenu(menu);
    }

    // ---------------------------------------------------------------- Cart

    private MenuItem findMenuItem(int id) {
        for (MenuItem m : menu) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    private CartItem findInCart(MenuItem item) {
        for (CartItem c : cart) {
            if (c.getItem().getId() == item.getId()) return c;
        }
        return null;
    }

    private void addToCart() {
        showMenu(menu);
        int id = readInt("\nEnter item ID to add: ");
        MenuItem item = findMenuItem(id);
        if (item == null) {
            System.out.println("Item not found.");
            return;
        }
        int qty = readInt("Enter quantity: ");
        if (qty <= 0) {
            System.out.println("Quantity must be at least 1.");
            return;
        }

        CartItem existing = findInCart(item);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + qty);
        } else {
            cart.add(new CartItem(item, qty));
        }
        for (int i = 0; i < qty; i++) {
            undoStack.push(item);   // one entry per unit so undo removes one at a time
        }
        System.out.println(qty + " x " + item.getName() + " added to cart.");
    }

    /** Undo uses a Stack (LIFO): the most recently added unit is removed first. */
    private void undoLastAdd() {
        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        MenuItem last = undoStack.pop();
        CartItem c = findInCart(last);
        if (c != null) {
            c.setQuantity(c.getQuantity() - 1);
            if (c.getQuantity() == 0) {
                cart.remove(c);
            }
        }
        System.out.println("Removed one " + last.getName() + " from cart.");
    }

    private double cartTotal() {
        double total = 0;
        for (CartItem c : cart) total += c.getSubtotal();
        return total;
    }

    private void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("\nYour cart is empty.");
            return;
        }
        System.out.println("\n--------- YOUR CART ---------");
        for (CartItem c : cart) {
            System.out.println(c);
        }
        System.out.printf("Total: Rs.%.2f%n", cartTotal());
    }

    // -------------------------------------------------------------- Orders

    /** New orders are added to the back of the Queue (enqueue). */
    private void placeOrder() {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Add items first.");
            return;
        }
        viewCart();
        System.out.print("Enter your name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) name = "Guest";

        Order order = new Order(name, cart);
        pendingOrders.offer(order);

        cart.clear();
        undoStack.clear();
        System.out.println("\nOrder placed successfully! Your Order ID is #" + order.getOrderId());
        System.out.println("Orders ahead of you: " + (pendingOrders.size() - 1));
    }

    /** Kitchen takes the order at the front of the Queue (dequeue, FIFO). */
    private void processNextOrder() {
        Order order = pendingOrders.poll();
        if (order == null) {
            System.out.println("No pending orders.");
            return;
        }
        order.setStatus("DELIVERED");
        orderHistory.push(order);   // history is a Stack, so the latest order is on top
        System.out.println("\nProcessed and delivered:");
        order.printDetails();
    }

    private void viewPendingOrders() {
        if (pendingOrders.isEmpty()) {
            System.out.println("\nNo pending orders.");
            return;
        }
        System.out.println("\n----- PENDING ORDERS (first = next to be served) -----");
        for (Order o : pendingOrders) {
            System.out.println(o);
        }
    }

    private void viewOrderHistory() {
        if (orderHistory.isEmpty()) {
            System.out.println("\nNo completed orders yet.");
            return;
        }
        System.out.println("\n----- ORDER HISTORY (latest first) -----");
        for (int i = orderHistory.size() - 1; i >= 0; i--) {
            System.out.println(orderHistory.get(i));
        }
    }

    // ------------------------------------------------------------- Helpers

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n========== ONLINE FOOD ORDERING SYSTEM ==========");
        System.out.println("1.  View Menu");
        System.out.println("2.  Sort Menu");
        System.out.println("3.  Add Item to Cart");
        System.out.println("4.  Undo Last Added Item");
        System.out.println("5.  View Cart");
        System.out.println("6.  Place Order");
        System.out.println("7.  Process Next Order (Kitchen)");
        System.out.println("8.  View Pending Orders");
        System.out.println("9.  View Order History");
        System.out.println("0.  Exit");
    }

    public void run() {
        while (true) {
            printMainMenu();
            int choice = readInt("Enter your choice: ");
            switch (choice) {
                case 1: showMenu(menu); break;
                case 2: sortMenu(); break;
                case 3: addToCart(); break;
                case 4: undoLastAdd(); break;
                case 5: viewCart(); break;
                case 6: placeOrder(); break;
                case 7: processNextOrder(); break;
                case 8: viewPendingOrders(); break;
                case 9: viewOrderHistory(); break;
                case 0:
                    System.out.println("Thank you for visiting. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public static void main(String[] args) {
        new FoodOrderingSystem().run();
    }
}
