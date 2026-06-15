package Lecture4_interfaces_abstract_classes;

/**
 * Q3 - Custom checked exception for insufficient funds.
 *
 * Extends Exception (not RuntimeException) so it is a proper checked exception
 * and callers are forced to handle or declare it — demonstrating Java's
 * exception hierarchy through inheritance.
 */
public class InsufficientFundsException extends Exception {

    private final double amountRequested;
    private final double amountAvailable;

    public InsufficientFundsException(double amountRequested, double amountAvailable) {
        super(String.format(
                "Insufficient funds: requested %.2f but only %.2f is available.",
                amountRequested, amountAvailable
        ));
        this.amountRequested = amountRequested;
        this.amountAvailable  = amountAvailable;
    }

    public double getAmountRequested() { return amountRequested; }
    public double getAmountAvailable() { return amountAvailable; }

    /** Convenience: how much short the account is. */
    public double getShortfall() { return amountRequested - amountAvailable; }
}