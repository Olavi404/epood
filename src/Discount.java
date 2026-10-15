/**
 * Allahindlus, mida saab sooduskoodiga tellimusele rakendada.
 * Liides määrab ainult, mida allahindlus teeb – kuidas, otsustab iga klass ise.
 */
public interface Discount {

    /** Tagastab summa pärast allahindlust. */
    double apply(double amount);

    /** Allahindluse kirjeldus kasutajale, nt "-10%". */
    String getDescription();
}
