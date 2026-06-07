import java.util.*;
import java.io.*;

public class ExpenseManager {
    private ArrayList<Expense> expenses;
    private final String FILE_NAME = "expenses.txt";

    // Constructor - loads existing expenses when program starts
    public ExpenseManager() {
        expenses = new ArrayList<>();
        loadFromFile();
    }

    // Add a new expense
    public void addExpense(String description, double amount, String category, String date) {
        Expense newExpense = new Expense(description, amount, category, date);
        expenses.add(newExpense);
        saveToFile(); // Save immediately after adding
        System.out.println("✓ Expense added!");
    }

    // Show all expenses
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

    public void deleteExpense() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses to delete.");
            return;
        }

        viewAllExpenses(); // Show numbered list first

        System.out.print("Enter expense number to delete: ");
        Scanner scanner = new Scanner(System.in);
        int index = scanner.nextInt();
        scanner.nextLine(); // Clear buffer

        if (index >= 1 && index <= expenses.size()) {
            Expense removed = expenses.remove(index - 1); // Convert to 0-based index
            saveToFile(); // Save changes to file
            System.out.println("✓ Deleted: " + removed.getDescription() + " - $" + removed.getAmount());
        } else {
            System.out.println("Invalid number. No expense deleted.");
        }
    }

    public boolean hasNoExpenses() {
        return expenses.isEmpty();
    }

    // Show total by category
    public void showTotalByCategory() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses to total.");
            return;
        }

        HashMap<String, Double> categoryTotals = new HashMap<>();

        for (Expense e : expenses) {
            String cat = e.getCategory();
            double currentTotal = categoryTotals.getOrDefault(cat, 0.0);
            categoryTotals.put(cat, currentTotal + e.getAmount());
        }

        System.out.println("\n--- TOTALS BY CATEGORY ---");
        for (String category : categoryTotals.keySet()) {
            double total = categoryTotals.get(category);
            System.out.println(category + ": " + total);
        }
    }

    // Show grand total
    public void showGrandTotal() {
        double total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        System.out.println("\nGrand Total: " + total);
    }

    // Save to file
    private void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                writer.println(e.toFileString());
            }
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

    // Load from file
    private void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return; // First time running, no file yet
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String desc = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    String category = parts[2];
                    String date = parts[3];
                    expenses.add(new Expense(desc, amount, category, date));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading: " + e.getMessage());
        }
    }
}