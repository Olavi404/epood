import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * E-pood: tootekataloog koos otsingu, filtreerimise ja sorteerimisega.
 *
 * Andmestruktuurid:
 *  - catalog (LinkedHashMap): toode leitakse koodi järgi kohe, kood on unikaalne,
 *    lisamise järjekord säilib kuvamisel;
 *  - categories (TreeSet): iga kategooria on ainult üks kord ja need on tähestiku järjekorras.
 */
public class Shop {

    private final Map<String, Product> catalog = new LinkedHashMap<>();
    private final Set<String> categories = new TreeSet<>();

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
}
