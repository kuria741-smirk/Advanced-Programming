package Lecture4_interfaces_abstract_classes;

import org.jetbrains.annotations.NotNull;
import java.util.Calendar;

/**
 * Q1 - Concrete class that implements TransactionInterface.
 *
 * The starter code made this abstract; the assignment asks us to make it
 * a CONCRETE class that fully implements the interface, while ensuring
 * its apply() behaves differently from the subclasses.
 *
 * Fields match the starter code exactly (int amount, Calendar date, String transactionID).
 */
public class BaseTransaction implements TransactionInterface {

    // --- Fields (preserved from starter code) ---
    private final int amount;
    private final Calendar date;
    private final String transactionID;

    /**
     * Constructor - mirrors the starter code signature.
     *
     * @param amount integer transaction amount
     * @param date   must not be null; defensively copied to preserve invariants
     */
    public BaseTransaction(int amount, @NotNull Calendar date) {
        this.amount        = amount;
        this.date          = (Calendar) date.clone();   // defensive copy (from lecture)
        int uniq           = (int)(Math.random() * 10000);
        this.transactionID = date.toString() + uniq;
    }

    // ---------------------------------------------------------------
    // Q1 – Getter implementations (from TransactionInterface)
    // ---------------------------------------------------------------

    @Override
    public double getAmount() {
        return amount;  // int → double widening; value type, safe to return directly
    }

    @Override
    public Calendar getDate() {
        return (Calendar) date.clone();  // defensive / judicious copy (from lecture)
    }

    @Override
    public String getTransactionID() {
        return transactionID;
    }

    // ---------------------------------------------------------------
    // Q1 – printTransactionDetails()
    // ---------------------------------------------------------------

    @Override
    public void printTransactionDetails() {
        System.out.println("=== Base Transaction Details ===");
        System.out.println("  Transaction ID : " + transactionID);
        System.out.printf ("  Amount         : %.2f%n", (double) amount);
        System.out.printf ("  Date           : %tF%n", date);
        System.out.println("  Type           : BaseTransaction");
    }

    // ---------------------------------------------------------------
    // Q1 – apply()
    //
    // BaseTransaction.apply() is intentionally DIFFERENT from the
    // subclass overrides: it is a neutral / informational application
    // that logs the call but does NOT modify the account balance.
    // This makes the base behaviour clearly distinct from Deposit
    // (which credits) and Withdrawal (which debits).
    // ---------------------------------------------------------------

    @Override
    public void apply(BankAccount ba) {
        System.out.printf(
                "[BaseTransaction] apply() called — Amount: %.2f | Balance unchanged at: %.2f%n",
                (double) amount, ba.getBalance()
        );
    }
}