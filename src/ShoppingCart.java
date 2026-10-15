import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Ostukorv. Read on ArrayList-is, sest korvis on vähe ridu, nende järjekord on oluline
 * (kuvatakse lisamise järjekorras) ja neid käiakse alati kõik läbi (summa arvutamine).
 */
public class ShoppingCart {

    private static final double FREE_SHIPPING_FROM = 50.0;

    private final List<CartItem> items = new ArrayList<>();

    /** Lisab toote korvi. Kui toode on juba korvis, suurendatakse olemasoleva rea kogust. */
    public void addProduct(Product product, int quantity) throws OutOfStockException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Kogus peab olema vähemalt 1.");
        }
        CartItem existing = findItem(product.getId());
        int newQuantity = existing == null ? quantity : existing.getQuantity() + quantity;

        if (!product.isAvailable(newQuantity)) {
            throw new OutOfStockException("Toodet \"" + product.getName() + "\" pole laos " + newQuantity + " tk.");
        }
        if (existing == null) {
            items.add(new CartItem(product, quantity));
        } else {
            existing.setQuantity(newQuantity);
        }
    }

    /** Eemaldab toote korvist. Tagastab false, kui toodet korvis polnud. */
    public boolean removeProduct(String productId) {
        // Iterator, sest listist ei tohi elemente eemaldada tavalise for-each tsükli sees
        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getProduct().getId().equalsIgnoreCase(productId.trim())) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items);
    }

    public double getSubtotal() {
        double sum = 0;
        for (CartItem item : items) {
            sum += item.getLineTotal();
        }
        return sum;
    }

    /** Saatmiskulu: iga toode arvutab oma osa ise, alates 50 eurost on saatmine tasuta. */
    public double getShippingCost() {
        if (getSubtotal() >= FREE_SHIPPING_FROM) {
            return 0;
        }
        double shipping = 0;
        for (CartItem item : items) {
            shipping += item.getShippingCost();
        }
        return shipping;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    private CartItem findItem(String productId) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equalsIgnoreCase(productId)) {
                return item;
            }
        }
        return null;
    }
}
