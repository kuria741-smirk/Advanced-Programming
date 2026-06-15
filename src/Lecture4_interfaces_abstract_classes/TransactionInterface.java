package Lecture4_interfaces_abstract_classes;

import java.util.Calendar;

/**
 * Interface for Transactions.
 * Any class that defines a transaction is expected to implement this interface.
 *
 * Q1: Extended with printTransactionDetails() and apply() as required by the assignment.
 */
public interface TransactionInterface {

    // Method to get the transaction amount
    double getAmount();

    // Method to get the transaction date
    Calendar getDate();

    // Method to get a unique identifier for the transaction
    String getTransactionID();

    // Method to print transaction details (Q1)
    void printTransactionDetails();

    // Method to apply the transaction to a BankAccount (Q1)
    void apply(BankAccount ba);
}