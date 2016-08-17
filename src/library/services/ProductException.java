package library.services;

/**
 * Created by IntelliJ IDEA.
 * User: grigo
 * Date: Mar 18, 2009
 * Time: 1:32:58 PM
 */
public class ProductException extends Exception{
    public ProductException() {
    }

    public ProductException(String message) {
        super(message);
    }

    public ProductException(String message, Throwable cause) {
        super(message, cause);
    }
}
