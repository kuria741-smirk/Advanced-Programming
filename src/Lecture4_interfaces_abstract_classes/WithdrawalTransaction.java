package Lecture4_interfaces_abstract_classes;

import org.jetbrains.annotations.NotNull;
import java.util.Calendar;

/**
 * Q1, Q2 & Q3 - Concrete class for withdrawal transactions.
 *
 * Withdrawals are REVERSIBLE by design (assignment spec, Q2).
 *
 * Q1  – Overrides apply() to debit the BankAccount.
 * Q2  – Implements reverse() to restore the account to its pre-withdrawal balance.
 * Q3  – Overloaded apply(BankAccount, boolean) uses the 'throws' keyword and a
 *        try { } catch { } finally { } block; handles the partial-withdrawal case.
 */
public class WithdrawalTransaction extends BaseTransaction {

    // Tracks the account this transaction was applied to (needed for reverse())
    private BankAccount appliedAccount    = null;

    // Records any amount that could NOT be withdrawn in a partial withdrawal
    private double      amountNotWithdrawn = 0.0;

    // Guards against reversing a transaction that was never successfully applied
    private boolean     isApplied          = false;

    // --- Constructor (matches starter code signature) ---
    public WithdrawalTransaction(int amount, @NotNull Calendar date) {
        super(amount, date);
    }

    // Helper preserved from starter code
    private boolean checkDepositAmount(int amt) {
        return amt >= 0;
    }

    // ---------------------------------------------------------------
    // Q1 – Override apply()
    //
    // Simple debit; delegates to the overloaded version.
    // Any InsufficientFundsException is caught here and reported so
    // that this override still satisfies the void / no-throws signature
    // required by the interface.
    // ---------------------------------------------------------------

    @Override
    public void apply(BankAccount ba) {
        try {
            apply(ba, false);
        } catch (InsufficientFundsException e) {
            System.err.println("[WithdrawalTransaction] apply() blocked: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Q3 – Overloaded apply() with full exception handling
    //
    // @param ba           the account to debit
    // @param allowPartial if true: when 0 < balance < amount, withdraw
    //                     all available balance and record the shortfall.
    //                     if false: throw InsufficientFundsException.
    //
    // Uses:  throws keyword (method signature)
    //        try / catch / finally block (inside the method body)
    // ---------------------------------------------------------------

    public void apply(BankAccount ba, boolean allowPartial) throws InsufficientFundsException {

        try {
            double balance = ba.getBalance();

            // Case A: account is empty or overdrawn
            if (balance <= 0) {
                throw new InsufficientFundsException(getAmount(), balance);
            }

            // Case B: partial withdrawal allowed and 0 < balance < amount
            if (allowPartial && balance < getAmount()) {
                amountNotWithdrawn = getAmount() - balance;
                ba.debit(balance);          // withdraw everything available
                appliedAccount     = ba;
                isApplied          = true;

                System.out.printf(
                        "[WithdrawalTransaction] Partial withdrawal: debited %.2f. "
                                + "Amount not withdrawn: %.2f. New balance: %.2f%n",
                        balance, amountNotWithdrawn, ba.getBalance()
                );

            } else if (balance < getAmount()) {
                // Case C: insufficient funds, partial NOT allowed → throw
                throw new InsufficientFundsException(getAmount(), balance);

            } else {
                // Case D: sufficient funds → normal full debit
                ba.debit(getAmount());
                appliedAccount = ba;
                isApplied      = true;

                System.out.printf(
                        "[WithdrawalTransaction] Debited %.2f. New balance: %.2f%n",
                        getAmount(), ba.getBalance()
                );
            }

        } catch (InsufficientFundsException e) {
            // Log and re-throw so the caller can decide how to handle it
            System.err.println("[WithdrawalTransaction] Caught: " + e.getMessage());
            throw e;

        } finally {
            // Always runs — suitable for audit/logging regardless of outcome
            System.out.println("[WithdrawalTransaction] apply() finished for account balance: "
                    + ba.getBalance());
        }
    }

    // ---------------------------------------------------------------
    // Q2 – reverse()
    //
    // Reverses a previously applied withdrawal by crediting the exact
    // amount that was debited back to the original BankAccount.
    // Returns true on success, false if the transaction was never applied.
    // ---------------------------------------------------------------

    public boolean reverse() {
        if (!isApplied || appliedAccount == null) {
            System.out.println("[WithdrawalTransaction] reverse() failed: transaction not yet applied.");
            return false;
        }

        // Amount actually debited = requested amount minus whatever was not withdrawn
        double actuallyDebited = getAmount() - amountNotWithdrawn;
        appliedAccount.credit(actuallyDebited);

        System.out.printf(
                "[WithdrawalTransaction] Reversed: credited %.2f back. Restored balance: %.2f%n",
                actuallyDebited, appliedAccount.getBalance()
        );

        // Reset state so the transaction cannot be reversed twice
        isApplied      = false;
        appliedAccount = null;

        return true;
    }

    // ---------------------------------------------------------------
    // Q1 – Override printTransactionDetails()
    // ---------------------------------------------------------------

    @Override
    public void printTransactionDetails() {
        System.out.println("=== Withdrawal Transaction Details ===");
        System.out.println("  Transaction ID       : " + getTransactionID());
        System.out.printf ("  Amount               : -%.2f%n", getAmount());
        System.out.printf ("  Date                 : %tF%n",   getDate());
        System.out.printf ("  Amount Not Withdrawn : %.2f%n",  amountNotWithdrawn);
        System.out.println("  Type                 : Withdrawal (reversible)");
    }

    public double getAmountNotWithdrawn() { return amountNotWithdrawn; }
}