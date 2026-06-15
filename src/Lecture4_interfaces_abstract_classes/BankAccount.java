package Lecture4_interfaces_abstract_classes;

/**
 * Represents a simple bank account with a balance.
 * Starter code preserved; credit() and debit() helpers added
 * so transaction classes don't need to call setBalance() directly.
 */
public class BankAccount {
    private double balance;

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    /** Adds amount to the balance (used by DepositTransaction). */
    public void credit(double amount) {
        this.balance += amount;
    }

    /** Subtracts amount from the balance (used by WithdrawalTransaction). */
    public void debit(double amount) {
        this.balance -= amount;
    }

    @Override
    public String toString() {
        return String.format("BankAccount[Balance=%.2f]", balance);
    }
}