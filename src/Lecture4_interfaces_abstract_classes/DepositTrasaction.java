package Lecture4_interfaces_abstract_classes;

import org.jetbrains.annotations.NotNull;
import java.util.Calendar;

/**
 * Q1 & Q2 - Concrete class for deposit transactions.
 *
 * Deposits are IRREVERSIBLE by design (assignment spec, Q2).
 * Overrides apply() to credit the BankAccount.
 *
 * Note: class name intentionally matches starter code spelling ("Trasaction").
 */
public class DepositTrasaction extends BaseTransaction {

    // --- Constructor ---
    public DepositTrasaction(int amount, @NotNull Calendar date) {
        super(amount, date);
    }

    // Helper preserved from starter code
    private boolean checkDepositAmount(int amt) {
        return amt >= 0;
    }

    // ---------------------------------------------------------------
    // Q1 – Override apply()
    // Credits the account; clearly different from BaseTransaction.apply()
    // which makes no balance change, and from WithdrawalTransaction
    // which debits.
    // ---------------------------------------------------------------

    @Override
    public void apply(BankAccount ba) {
        ba.credit(getAmount());
        System.out.printf(
                "[DepositTransaction] Credited %.2f → New balance: %.2f%n",
                getAmount(), ba.getBalance()
        );
    }

    // ---------------------------------------------------------------
    // Q1 – Override printTransactionDetails()
    // ---------------------------------------------------------------

    @Override
    public void printTransactionDetails() {
        System.out.println("=== Deposit Transaction Details ===");
        System.out.println("  Transaction ID : " + getTransactionID());
        System.out.printf ("  Amount         : +%.2f%n", getAmount());
        System.out.printf ("  Date           : %tF%n",   getDate());
        System.out.println("  Type           : Deposit (irreversible)");
    }
}