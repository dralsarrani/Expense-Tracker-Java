import java.util.*;

public class ExpenseManager {
    private ArrayList<Expense> expenses;
    private DatabaseManager db;

    public ExpenseManager() {
        db = new DatabaseManager();
        expenses = db.getAllExpenses(); // Load from database
    }

    public void addExpense(String description, double amount, String category, String date) {
        Expense newExpense = new Expense(description, amount, category, date);
        int id = db.addExpense(newExpense);  // Gets ID from DB
        
        // The expense is already in the map (inside DatabaseManager)
        // We just need it in our local list too
        expenses.add(newExpense);
        System.out.println("✓ Expense added! (ID: " + id + ")");
    }

    public void viewAllExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }

        System.out.println("\n--- ALL EXPENSES ---");
        for (int i = 0; i < expenses.size(); i++) {
            System.out.println((i + 1) + ". " + expenses.get(i));
        }
    }

    public void showTotalByCategory() {
        db.showTotalsByCategory(); 
    }

    public void showGrandTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        //System.out.printf("\nGrand Total: $%.2f%n", total);
        System.out.println("\nGrand Total: " + total);
    }

    public int getExpenseCount() {
        return expenses.size();
    }

    // uses the Expense object directly
    public void deleteExpense(int index) {
        if (index >= 1 && index <= expenses.size()) {
            Expense toDelete = expenses.remove(index - 1);
            db.deleteExpense(toDelete);  // Pass the object
            System.out.println("✓ Deleted: " + toDelete.getDescription());
        } else {
            System.out.println("Invalid number.");
        }
    }

    public boolean hasNoExpenses() {
        return expenses.isEmpty();
    }

    public void close() {
        db.close();
    }
}