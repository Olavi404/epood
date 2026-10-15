import java.util.List;

/** Programmi käivituspunkt ja konsoolimenüü. */
public class ShopApp {

    private final Shop shop = new Shop();
    private final ShoppingCart cart = new ShoppingCart();
    private final ConsoleInput input = new ConsoleInput();

    public static void main(String[] args) {
        new ShopApp().run();
    }

    private void run() {
        addSampleData();
        boolean running = true;
        while (running) {
            printMenu();
            int choice = input.readInt("Vali: ", 0, 9);
            try {
                switch (choice) {
                    case 1 -> printProducts(shop.getAllProducts());
                    case 2 -> showSortedByPrice();
                    case 3 -> filterByCategory();
                    case 4 -> searchByName();
                    case 5 -> addToCart();
                    case 6 -> showCart();
                    case 7 -> removeFromCart();
                    case 8 -> checkout();
                    case 9 -> showOrders();
                    case 0 -> running = false;
                }
            } catch (OutOfStockException | IllegalArgumentException e) {
                System.out.println("Viga: " + e.getMessage());
            }
        }
        System.out.println("Aitäh, head aega!");
    }

    private void printMenu() {
        System.out.println();
        System.out.println("========= E-POOD =========");
        System.out.println("1 - Kõik tooted");
        System.out.println("2 - Tooted hinna järgi");
        System.out.println("3 - Filtreeri kategooria järgi");
        System.out.println("4 - Otsi nime järgi");
        System.out.println("5 - Lisa toode ostukorvi");
        System.out.println("6 - Vaata ostukorvi");
        System.out.println("7 - Eemalda toode ostukorvist");
        System.out.println("8 - Vormista tellimus");
        System.out.println("9 - Tellimuste ajalugu");
        System.out.println("0 - Välju");
    }

    private void addSampleData() {
        shop.addProduct(new PhysicalProduct("P1", "Java raamat", "Raamatud", 34.90, 5, 0.9));
        shop.addProduct(new PhysicalProduct("P2", "Juhtmevaba hiir", "Elektroonika", 24.90, 10, 0.2));
        shop.addProduct(new PhysicalProduct("P3", "USB-C kaabel", "Elektroonika", 9.99, 0, 0.1));
        shop.addProduct(new DigitalProduct("D1", "Java e-raamat", "Raamatud", 19.90, 15));
        shop.addProduct(new DigitalProduct("D2", "Fototöötlus", "Tarkvara", 49.00, 850));

        shop.addPromoCode("SOODUS10", new PercentageDiscount(10));
        shop.addPromoCode("KINK5", new FixedDiscount(5));
    }

    private void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Tooteid ei leitud.");
            return;
        }
        System.out.printf("%-5s %-18s %-12s %10s   %s%n", "Kood", "Nimi", "Kategooria", "Hind", "Info");
        for (Product product : products) {
            // getDetails() käitub PhysicalProduct ja DigitalProduct puhul erinevalt (polümorfism)
            System.out.println(product);
        }
    }

    private void showSortedByPrice() {
        int order = input.readInt("1 - odavamad enne, 2 - kallimad enne: ", 1, 2);
        printProducts(shop.getProductsSortedByPrice(order == 1));
    }

    private void filterByCategory() {
        System.out.println("Kategooriad: " + String.join(", ", shop.getCategories()));
        String category = input.readNonEmpty("Kategooria: ");
        printProducts(shop.filterByCategory(category));
    }

    private void searchByName() {
        String text = input.readNonEmpty("Otsingusõna: ");
        printProducts(shop.searchByName(text));
    }

    private void addToCart() throws OutOfStockException {
        String id = input.readNonEmpty("Toote kood: ");
        Product product = shop.findById(id);
        if (product == null) {
            System.out.println("Toodet koodiga " + id + " ei ole.");
            return;
        }
        int quantity = input.readInt("Kogus: ", 1, 100);
        cart.addProduct(product, quantity);
        System.out.println("Lisatud korvi: " + product.getName() + " x " + quantity);
    }

    private void showCart() {
        if (cart.isEmpty()) {
            System.out.println("Ostukorv on tühi.");
            return;
        }
        for (CartItem item : cart.getItems()) {
            System.out.println(item);
        }
        System.out.printf("Vahesumma: %.2f €, saatmine: %.2f €%n", cart.getSubtotal(), cart.getShippingCost());
    }

    private void removeFromCart() {
        String id = input.readNonEmpty("Eemaldatava toote kood: ");
        if (cart.removeProduct(id)) {
            System.out.println("Eemaldatud.");
        } else {
            System.out.println("Seda toodet pole korvis.");
        }
    }

    private void checkout() throws OutOfStockException {
        if (cart.isEmpty()) {
            System.out.println("Ostukorv on tühi.");
            return;
        }
        showCart();
        String name = input.readNonEmpty("Sinu nimi: ");
        String email = input.readNonEmpty("E-post: ");
        Customer customer = new Customer(name, email);

        Discount discount = null;
        String code = input.readLine("Sooduskood (Enter, kui pole): ");
        if (!code.isEmpty()) {
            discount = shop.findPromoCode(code);
            if (discount == null) {
                System.out.println("Sellist sooduskoodi pole, jätkame ilma allahindluseta.");
            }
        }

        Order order = shop.placeOrder(customer, cart, discount);
        System.out.println();
        System.out.println(order.getSummary());
    }

    private void showOrders() {
        List<Order> orders = shop.getOrders();
        if (orders.isEmpty()) {
            System.out.println("Tellimusi veel pole.");
            return;
        }
        for (Order order : orders) {
            System.out.println(order.getSummary());
            System.out.println();
        }
    }
}
