/** Kindla summa allahindlus, nt 5 eurot. Summa ei lähe kunagi alla nulli. */
public class FixedDiscount implements Discount {

    private final double amountOff;

    public FixedDiscount(double amountOff) {
        if (amountOff <= 0) {
            throw new IllegalArgumentException("Allahindluse summa peab olema positiivne.");
        }
        this.amountOff = amountOff;
    }

    @Override
    public double apply(double amount) {
        return Math.max(0, amount - amountOff);
    }

    @Override
    public String getDescription() {
        return String.format("-%.2f €", amountOff);
    }
}
