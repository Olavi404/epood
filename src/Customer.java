/** Klient, kes tellimuse esitab. */
public class Customer {

    private final String name;
    private final String email;

    public Customer(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Kliendi nimi on kohustuslik.");
        }
        if (email == null || !email.matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new IllegalArgumentException("Vigane e-posti aadress: " + email);
        }
        this.name = name.trim();
        this.email = email.trim();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return name + " <" + email + ">";
    }
}
