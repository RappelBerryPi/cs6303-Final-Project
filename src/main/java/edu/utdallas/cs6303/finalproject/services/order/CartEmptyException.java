package edu.utdallas.cs6303.finalproject.services.order;

public class CartEmptyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CartEmptyException() {
        super("The cart is empty");
    }

    public CartEmptyException(String message) {
        super(message);
    }

    public CartEmptyException(Throwable cause) {
        super(cause);
    }

    public CartEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
