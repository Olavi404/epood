/** Füüsiline toode – on piiratud laoseisuga ja selle saatmine maksab. */
public class PhysicalProduct extends Product {

    private static final double SHIPPING_PRICE_PER_KG = 0.80;

    private int stock;
    private final double weightKg;

    public PhysicalProduct(String id, String name, String category, double price, int stock, double weightKg) {
        super(id, name, category, price);
        if (stock < 0) {
            throw new IllegalArgumentException("Laoseis ei tohi olla negatiivne.");
        }
        if (weightKg <= 0) {
            throw new IllegalArgumentException("Kaal peab olema positiivne.");
        }
        this.stock = stock;
        this.weightKg = weightKg;
    }

    public int getStock() {
        return stock;
    }

    public double getWeightKg() {
        return weightKg;
    }

    @Override
    public boolean isAvailable(int quantity) {
        return quantity > 0 && quantity <= stock;
    }

    @Override
    public void removeFromStock(int quantity) {
        if (!isAvailable(quantity)) {
            throw new IllegalArgumentException("Tootel " + getName() + " pole laos " + quantity + " tk.");
        }
        stock -= quantity;
    }

    @Override
    public double getShippingCost(int quantity) {
        return weightKg * quantity * SHIPPING_PRICE_PER_KG;
    }

    @Override
    public String getDetails() {
        return stock == 0 ? "LÕPPENUD" : "Laos " + stock + " tk, " + weightKg + " kg";
    }
}
