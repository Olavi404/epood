/** Visatakse, kui soovitud kogust tootest pole laos. */
public class OutOfStockException extends Exception {

    public OutOfStockException(String message) {
        super(message);
    }
}
