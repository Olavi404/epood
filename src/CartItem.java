/** Üks rida ostukorvis: toode ja selle kogus. */
public class CartItem {

    private final Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        setQuantity(quantity);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Kogus peab olema vähemalt 1.");
        }
        this.quantity = quantity;
    }

    public double getLineTotal() {
        return product.getPrice() * quantity;
    }

    public double getShippingCost() {
        return product.getShippingCost(quantity);
    }

    @Override
    public String toString() {
        return String.format("%-5s %-18s %3d x %7.2f € = %8.2f €",
                product.getId(), product.getName(), quantity, product.getPrice(), getLineTotal());
    }
}
