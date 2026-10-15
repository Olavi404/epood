/**
 * Abstraktne toode e-poe kataloogis.
 * Ühised andmed (kood, nimi, kategooria, hind) on siin, tootetüübist sõltuv
 * käitumine (laoseis, saatmiskulu) on alamklassides.
 */
public abstract class Product {

    private final String id;
    private final String name;
    private final String category;
    private double price;

    protected Product(String id, String name, String category, double price) {
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("Toote kood ja nimi on kohustuslikud.");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Toote kategooria on kohustuslik.");
        }
        this.id = id.trim().toUpperCase();
        this.name = name.trim();
        this.category = category.trim();
        setPrice(price);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Hind ei tohi olla negatiivne.");
        }
        this.price = price;
    }

    /** Kas seda toodet saab antud koguses osta? */
    public abstract boolean isAvailable(int quantity);

    /** Vähendab laoseisu pärast tellimuse vormistamist. */
    public abstract void removeFromStock(int quantity);

    /** Saatmiskulu antud koguse eest. */
    public abstract double getShippingCost(int quantity);

    /** Tootetüübile omane lisainfo kuvamiseks. */
    public abstract String getDetails();

    @Override
    public String toString() {
        return String.format("%-5s %-18s %-12s %8.2f €   %s", id, name, category, price, getDetails());
    }
}
