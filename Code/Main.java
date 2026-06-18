import java.util.Scanner;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        ExpenseManager manager = new ExpenseManager();
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US); // FIXES DECIMAL PROBLEM
        int choice;

        do {
            // Display menu
            System.out.println("\n=== EXPENSE TRACKER ===");
            System.out.println("1. Add Expense");
            System.out.println("2. View All Expenses");
            System.out.println("3. View Total by Category");
            System.out.println("4. View Grand Total");
            System.out.println("5. Delete Expense");
            System.out.println("6. Exit");
            System.out.print("Choose (1-6): ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Clear the newline character

            switch (choice) {
                case 1:
                    // Add expense
                    System.out.print("Description: ");
                    String desc = scanner.nextLine();

                    System.out.print("Amount: $");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.print("Category (Food/Transport/Entertainment/Bills/Other): ");
                    String category = scanner.nextLine();

                    System.out.print("Date (YYYY-MM-DD): ");
                    String date = scanner.nextLine();

                    manager.addExpense(desc, amount, category, date);
                    break;

                case 2:
                    // View all expenses
                    manager.viewAllExpenses();
                    break;

                case 3:
                    // View by category
                    manager.showTotalByCategory();
                    break;

                case 4:
                    // View grand total
                    manager.showGrandTotal();
                    break;

                case 5:
                    if (manager.hasNoExpenses()) {
                        System.out.println("No expenses to delete.");
                    } else {
                        manager.viewAllExpenses();
                        System.out.print("Enter expense number to delete: ");
                        int deleteIndex = scanner.nextInt();
                        scanner.nextLine();
                        manager.deleteExpense(deleteIndex); 
                    }
                    break;

                case 6:
                    System.out.println("Goodbye! Expenses saved.");
                    break;

                default:
                    System.out.println("Invalid choice. Pick 1-6.");
            }

        } while (choice != 6);

        scanner.close();
        manager.close(); // Close database connection
    }
}