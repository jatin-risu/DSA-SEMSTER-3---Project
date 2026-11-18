/**
 * Represents one dish on the restaurant menu.
 */
public class MenuItem {
    private final int id;
    private final String name;
    private final String category;
    private final double price;
    private final double rating;

    public MenuItem(int id, String name, String category, double price, double rating) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.rating = rating;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public double getRating() { return rating; }

    @Override
    public String toString() {
        return String.format("%-4d %-22s %-12s Rs.%-8.2f %.1f*", id, name, category, price, rating);
    }
}
