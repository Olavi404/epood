import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/** Vormistatud tellimus. Pärast loomist tellimust muuta ei saa (kõik väljad on final). */
public class Order {

    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final int orderNumber;
    private final Customer customer;
    private final List<CartItem> items;
    private final double subtotal;
    private final double discountedSubtotal;
    private final double shippingCost;
    private final String discountDescription;
    private final LocalDateTime createdAt;

    public Order(int orderNumber, Customer customer, List<CartItem> items, double subtotal,
                 double discountedSubtotal, double shippingCost, String discountDescription) {
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.items = new ArrayList<>(items);
        this.subtotal = subtotal;
        this.discountedSubtotal = discountedSubtotal;
        this.shippingCost = shippingCost;
        this.discountDescription = discountDescription;
        this.createdAt = LocalDateTime.now();
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotal() {
        return discountedSubtotal + shippingCost;
    }

    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Tellimus #%d  %s  %s%n", orderNumber, createdAt.format(TIME_FORMAT), customer));
        for (CartItem item : items) {
            sb.append("   ").append(item).append(System.lineSeparator());
        }
        sb.append(String.format("   Vahesumma: %.2f €%n", subtotal));
        if (discountDescription != null) {
            sb.append(String.format("   Allahindlus (%s): -%.2f €%n", discountDescription, subtotal - discountedSubtotal));
        }
        sb.append(String.format("   Saatmine: %.2f €%n", shippingCost));
        sb.append(String.format("   KOKKU: %.2f €", getTotal()));
        return sb.toString();
    }
}
