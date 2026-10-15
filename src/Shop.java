import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * E-pood: tootekataloog, sooduskoodid ja tellimused.
 *
 * Andmestruktuurid:
 *  - catalog (LinkedHashMap): toode leitakse koodi järgi kohe, kood on unikaalne,
 *    lisamise järjekord säilib kuvamisel;
 *  - categories (TreeSet): iga kategooria on ainult üks kord ja need on tähestiku järjekorras;
 *  - promoCodes (HashMap): sooduskood -> allahindlus, järjekord pole oluline;
 *  - orders (ArrayList): tellimused ajalises järjekorras.
 */
public class Shop {

    private final Map<String, Product> catalog = new LinkedHashMap<>();
    private final Set<String> categories = new TreeSet<>();
    private final Map<String, Discount> promoCodes = new HashMap<>();
    private final List<Order> orders = new ArrayList<>();
    private int nextOrderNumber = 1;

    public void addProduct(Product product) {
        if (catalog.containsKey(product.getId())) {
            throw new IllegalArgumentException("Toode koodiga " + product.getId() + " on juba olemas.");
        }
        catalog.put(product.getId(), product);
        categories.add(product.getCategory());
    }

    /** Tagastab toote koodi järgi või null, kui sellist pole. */
    public Product findById(String id) {
        return catalog.get(id.trim().toUpperCase());
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(catalog.values());
    }

    public List<Product> searchByName(String text) {
        String searchText = text.trim().toLowerCase();
        List<Product> result = new ArrayList<>();
        for (Product product : catalog.values()) {
            if (product.getName().toLowerCase().contains(searchText)) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> filterByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product product : catalog.values()) {
            if (product.getCategory().equalsIgnoreCase(category.trim())) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> getProductsSortedByPrice(boolean ascending) {
        List<Product> sorted = getAllProducts();
        Comparator<Product> byPrice = Comparator.comparingDouble(Product::getPrice);
        sorted.sort(ascending ? byPrice : byPrice.reversed());
        return sorted;
    }

    public Set<String> getCategories() {
        return new TreeSet<>(categories);
    }

    public void addPromoCode(String code, Discount discount) {
        promoCodes.put(code.trim().toUpperCase(), discount);
    }

    /** Tagastab sooduskoodile vastava allahindluse või null, kui koodi pole. */
    public Discount findPromoCode(String code) {
        return promoCodes.get(code.trim().toUpperCase());
    }

    /**
     * Vormistab tellimuse: kontrollib laoseisu, vähendab seda, arvutab summa ja tühjendab korvi.
     * Kõigepealt kontrollitakse KÕIK read, alles siis muudetakse laoseisu – nii ei jää
     * pooleli tellimuse korral laoseis osaliselt vähendatuks.
     */
    public Order placeOrder(Customer customer, ShoppingCart cart, Discount discount) throws OutOfStockException {
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Ostukorv on tühi.");
        }
        List<CartItem> items = cart.getItems();
        for (CartItem item : items) {
            if (!item.getProduct().isAvailable(item.getQuantity())) {
                throw new OutOfStockException("Toodet \"" + item.getProduct().getName() + "\" pole enam piisavalt laos.");
            }
        }
        for (CartItem item : items) {
            item.getProduct().removeFromStock(item.getQuantity());
        }

        double subtotal = cart.getSubtotal();
        double discountedSubtotal = discount == null ? subtotal : discount.apply(subtotal);
        String discountDescription = discount == null ? null : discount.getDescription();

        Order order = new Order(nextOrderNumber++, customer, items, subtotal,
                discountedSubtotal, cart.getShippingCost(), discountDescription);
        orders.add(order);
        cart.clear();
        return order;
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }
}
