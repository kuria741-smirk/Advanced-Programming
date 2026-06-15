import Lecture1_adt.*;
import Lecture4_interfaces_abstract_classes.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.List;

/**
 * Client Code - Main class.
 *
 * Existing lecture test functions are preserved unchanged.
 * Q4 adds testDepositTransaction(), testWithdrawalTransaction(),
 * and testPolymorphism() below.
 */
public class Main {

    // ================================================================
    // Existing lecture test functions (unchanged)
    // ================================================================

    public static void testTransaction1() {
        Calendar d1 = new GregorianCalendar();
        Lecture1_adt.Transaction1 t1 = new Lecture1_adt.Transaction1(1000, d1);

        System.out.println(t1.toString());
        System.out.println("Lecture1_adt.TransactionInterface Amount: \t " + t1.amount);
        System.out.println("Lecture1_adt.TransactionInterface Date: \t "   + t1.date);
    }

    public static Transaction2 makeNextPayment(Transaction2 t) {
        Calendar d = t.getDate();
        d.add(Calendar.MONTH, 1);
        return new Transaction2(t.getAmount(), d);
    }

    public static void testTransaction2() {
        Calendar d1 = new GregorianCalendar();
        Lecture1_adt.Transaction2 t       = new Lecture1_adt.Transaction2(1000, d1);
        Lecture1_adt.Transaction2 modified_t = makeNextPayment(t);

        System.out.println("\n\nState of the Object T1 After Client Code Tried to Change the Amount");
        System.out.println("Lecture1_adt.TransactionInterface Amount: \t " + modified_t.getAmount());
        System.out.println("Lecture1_adt.TransactionInterface Date: \t "   + modified_t.getDate().getTime());

        System.out.println("\n\nHow does T2 Look Like?????");
        System.out.println("Lecture1_adt.TransactionInterface Amount: \t " + modified_t.getAmount());
        System.out.println("Lecture1_adt.TransactionInterface Date: \t "   + modified_t.getDate().getTime());
    }

    public static List<Transaction3> makeYearOfPayments(int amount) throws NullPointerException {
        List<Transaction3> listOfTransaction3s = new ArrayList<>();
        Calendar date = new GregorianCalendar(2024, Calendar.JANUARY, 3);
        for (int i = 0; i < 12; i++) {
            listOfTransaction3s.add(new Transaction3(amount, date));
            date.add(Calendar.MONTH, 1);
        }
        return listOfTransaction3s;
    }

    public static void testTransaction3() {
        List<Transaction3> allPaymentsIn2024 = makeYearOfPayments(1000);
        for (Transaction3 t3 : allPaymentsIn2024) {
            for (Transaction3 transact : allPaymentsIn2024) {
                System.out.println("\n\n  ::::::::::::::::::::::::::::::::::::::::::::\n");
                System.out.println("Lecture1_adt.TransactionInterface Amount: \t " + transact.getAmount());
                System.out.println("Lecture1_adt.TransactionInterface Date: \t "   + transact.getDate().getTime());
            }
        }
    }

    public static List<Transaction4> makeYearOfPaymentsFinal(int amount) throws NullPointerException {
        List<Transaction4> listOfTransaction4s = new ArrayList<>();
        Calendar date = new GregorianCalendar(2024, Calendar.JANUARY, 3);
        for (int i = 0; i < 12; i++) {
            listOfTransaction4s.add(new Transaction4(amount, date));
            date.add(Calendar.MONTH, 1);
        }
        return listOfTransaction4s;
    }

    public static void testTransaction4() {
        List<Transaction4> transactionsIn2024 = makeYearOfPaymentsFinal(1200);
        for (Transaction4 transact : transactionsIn2024) {
            System.out.println("\n\n  ::::::::::::::::::::::::::::::::::::::::::::\n");
            System.out.println("Lecture1_adt.TransactionInterface Amount: \t " + transact.getAmount());
            System.out.println("Lecture1_adt.TransactionInterface Date: \t "   + transact.getDate().getTime());
        }
    }


    // ================================================================
    // Q4 – New client code: DepositTransaction tests
    // ================================================================

    public static void testDepositTransaction() {
        System.out.println("\n============================================================");
        System.out.println("  TEST: DepositTransaction");
        System.out.println("============================================================");

        Calendar date       = new GregorianCalendar();
        BankAccount account = new BankAccount(500.00);
        System.out.println("Initial balance: " + account.getBalance());

        // --- Subtype object, subtype reference ---
        DepositTrasaction deposit = new DepositTrasaction(200, date);
        deposit.printTransactionDetails();
        deposit.apply(account);
        System.out.println("Balance after deposit: " + account.getBalance());

        // --- Type-cast subtype to supertype, then call apply() ---
        // The reference is BaseTransaction (supertype), object is DepositTrasaction.
        // Java resolves apply() at RUNTIME (late binding / dynamic dispatch),
        // so DepositTrasaction.apply() is called — not BaseTransaction.apply().
        System.out.println("\n-- Type-casting DepositTrasaction → BaseTransaction --");
        BaseTransaction castedDeposit = (BaseTransaction) new DepositTrasaction(100, date);
        castedDeposit.printTransactionDetails();
        castedDeposit.apply(account);   // late binding → DepositTrasaction.apply()
        System.out.println("Balance after casted deposit: " + account.getBalance());
    }


    // ================================================================
    // Q4 – New client code: WithdrawalTransaction tests
    // ================================================================

    public static void testWithdrawalTransaction() {
        System.out.println("\n============================================================");
        System.out.println("  TEST: WithdrawalTransaction");
        System.out.println("============================================================");

        Calendar date       = new GregorianCalendar();
        BankAccount account = new BankAccount(1000.00);
        System.out.println("Initial balance: " + account.getBalance());

        // --- Normal withdrawal (sufficient funds) ---
        WithdrawalTransaction w1 = new WithdrawalTransaction(300, date);
        w1.printTransactionDetails();
        w1.apply(account);
        System.out.println("Balance after withdrawal: " + account.getBalance());

        // --- Reverse the withdrawal (Q2) ---
        System.out.println("\n-- Reversing withdrawal --");
        boolean reversed = w1.reverse();
        System.out.println("Reversal successful: " + reversed);
        System.out.println("Balance after reversal: " + account.getBalance());

        // --- Insufficient funds, allowPartial = false (Q3, throws) ---
        System.out.println("\n-- Withdrawal exceeding balance (allowPartial=false) --");
        WithdrawalTransaction w2 = new WithdrawalTransaction(9999, date);
        try {
            w2.apply(account, false);
        } catch (InsufficientFundsException e) {
            System.out.println("Caught InsufficientFundsException: " + e.getMessage());
            System.out.printf ("Shortfall: %.2f%n", e.getShortfall());
        }
        System.out.println("Balance unchanged: " + account.getBalance());

        // --- Partial withdrawal: 0 < balance < amount (Q3) ---
        System.out.println("\n-- Partial withdrawal (allowPartial=true, balance < amount) --");
        BankAccount smallAccount = new BankAccount(80.00);
        WithdrawalTransaction w3 = new WithdrawalTransaction(200, date);
        try {
            w3.apply(smallAccount, true);
        } catch (InsufficientFundsException e) {
            System.err.println("Unexpected exception: " + e.getMessage());
        }
        w3.printTransactionDetails();
        System.out.println("Balance after partial withdrawal: " + smallAccount.getBalance());
    }


    // ================================================================
    // Q4 – New client code: Polymorphism (early vs. late binding)
    // ================================================================

    public static void testPolymorphism() {
        System.out.println("\n============================================================");
        System.out.println("  TEST: Polymorphism — Early vs. Late Binding");
        System.out.println("============================================================");

        Calendar date       = new GregorianCalendar();
        BankAccount account = new BankAccount(500.00);

        /*
         * EARLY BINDING (compile-time / static dispatch):
         * The reference type AND the object type are both BaseTransaction.
         * The compiler resolves apply() to BaseTransaction.apply() at
         * compile time because there is no subtype to dispatch to.
         * Result: no balance change (BaseTransaction.apply() is informational).
         */
        System.out.println("-- Early Binding: BaseTransaction reference, BaseTransaction object --");
        BaseTransaction earlyBound = new BaseTransaction(50, date);
        earlyBound.apply(account);
        System.out.println("Balance (unchanged): " + account.getBalance());

        /*
         * LATE BINDING (runtime / dynamic dispatch):
         * The reference type is BaseTransaction (supertype), but the actual
         * object at runtime is DepositTrasaction (subtype).
         * The JVM looks up the method in the actual object's class at runtime
         * and dispatches to DepositTrasaction.apply() → balance increases.
         */
        System.out.println("\n-- Late Binding: BaseTransaction reference, DepositTrasaction object --");
        BaseTransaction lateDeposit = new DepositTrasaction(150, date);
        lateDeposit.apply(account);   // JVM dispatches to DepositTrasaction.apply()
        System.out.println("Balance after late-bound deposit: " + account.getBalance());

        /*
         * LATE BINDING with WithdrawalTransaction:
         * Same principle — the supertype reference holds a WithdrawalTransaction.
         * apply() resolves to WithdrawalTransaction.apply() at runtime.
         */
        System.out.println("\n-- Late Binding: BaseTransaction reference, WithdrawalTransaction object --");
        BaseTransaction lateWithdraw = new WithdrawalTransaction(100, date);
        lateWithdraw.apply(account);  // JVM dispatches to WithdrawalTransaction.apply()
        System.out.println("Balance after late-bound withdrawal: " + account.getBalance());
    }


    // ================================================================
    // main()
    // ================================================================

    public static void main(String[] args) {
        // Existing lecture tests (uncomment as needed)
        // testTransaction1();
        // testTransaction2();
        // testTransaction3();
        // testTransaction4();

        // Q4 – Assignment tests
        testDepositTransaction();
        testWithdrawalTransaction();
        testPolymorphism();
    }
}