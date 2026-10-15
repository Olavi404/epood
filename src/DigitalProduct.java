/** Digitaalne toode (e-raamat, tarkvara) – laoseis on piiramatu ja saatmist pole. */
public class DigitalProduct extends Product {

    private final int fileSizeMb;

    public DigitalProduct(String id, String name, String category, double price, int fileSizeMb) {
        super(id, name, category, price);
        if (fileSizeMb <= 0) {
            throw new IllegalArgumentException("Faili suurus peab olema positiivne.");
        }
        this.fileSizeMb = fileSizeMb;
    }

    public int getFileSizeMb() {
        return fileSizeMb;
    }

    @Override
    public boolean isAvailable(int quantity) {
        return quantity > 0;
    }

    @Override
    public void removeFromStock(int quantity) {
        // Digitaalset toodet saab müüa piiramatult, laoseisu pole vaja muuta.
    }

    @Override
    public double getShippingCost(int quantity) {
        return 0;
    }

    @Override
    public String getDetails() {
        return "Allalaaditav, " + fileSizeMb + " MB";
    }
}
