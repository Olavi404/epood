/** Protsentuaalne allahindlus, nt 10% kogu summast. */
public class PercentageDiscount implements Discount {

    private final int percent;

    public PercentageDiscount(int percent) {
        if (percent <= 0 || percent > 100) {
            throw new IllegalArgumentException("Protsent peab olema vahemikus 1–100.");
        }
        this.percent = percent;
    }

    @Override
    public double apply(double amount) {
        return amount * (100 - percent) / 100.0;
    }

    @Override
    public String getDescription() {
        return "-" + percent + "%";
    }
}
