import java.util.Scanner;

/** Kasutaja sisendi lugemine. Vigase sisendi korral küsitakse uuesti. */
public class ConsoleInput {

    private final Scanner scanner = new Scanner(System.in);

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public String readNonEmpty(String prompt) {
        while (true) {
            String input = readLine(prompt);
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Väärtus ei tohi olla tühi.");
        }
    }

    /** Loeb täisarvu vahemikus min..max. */
    public int readInt(String prompt, int min, int max) {
        while (true) {
            String input = readLine(prompt);
            try {
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Arv peab olema vahemikus " + min + "–" + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("\"" + input + "\" ei ole täisarv.");
            }
        }
    }
}
