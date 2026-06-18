import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:expenses.db";
    private Connection connection;

    // track which expense has which ID
    private HashMap<Integer, Expense> expenseIdMap = new HashMap<>();
    private int nextId = 1; // ID generator

    public DatabaseManager() {
        connect();
        createTable();
        loadAllExpenses(); // Load existing expenses into map
    }

    private void connect() {
        try {
            // Register the SQLite driver explicitly
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("✓ Connected to database");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite driver not found! Make sure the JAR is in classpath.");
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void createTable() {
        String sql = """
                    CREATE TABLE IF NOT EXISTS expenses (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        description TEXT NOT NULL,
                        amount REAL NOT NULL,
                        category TEXT NOT NULL,
                        date TEXT NOT NULL
                    )
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("✓ Expenses table ready");
        } catch (SQLException e) {
            System.out.println("Table creation error: " + e.getMessage());
        }
    }

    // Read all expenses from DB into the map
    private void loadAllExpenses() {
        String sql = "SELECT * FROM expenses ORDER BY id";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String desc = rs.getString("description");
                double amount = rs.getDouble("amount");
                String category = rs.getString("category");
                String date = rs.getString("date");

                Expense expense = new Expense(desc, amount, category, date);
                expenseIdMap.put(id, expense);

                // Track highest ID for nextId
                if (id >= nextId) {
                    nextId = id + 1;
                }
            }
        } catch (SQLException e) {
            System.out.println("Load error: " + e.getMessage());
        }
    }

    // Add expense to database
    public int addExpense(Expense expense) {
        String sql = "INSERT INTO expenses (description, amount, category, date) VALUES (?, ?, ?,?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, expense.getDescription());
            pstmt.setDouble(2, expense.getAmount());
            pstmt.setString(3, expense.getCategory());
            pstmt.setString(4, expense.getDate());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    expenseIdMap.put(id, expense);
                    return id;
                }
            }
        } catch (SQLException e) {
            System.out.println("Add error: " + e.getMessage());
        }
        return -1;
    }

    // Return all expenses from the map
    public ArrayList<Expense> getAllExpenses() {
        return new ArrayList<>(expenseIdMap.values());
    }

    // Delete by Expense object
    public void deleteExpense(Expense expense) {
        // Find which ID belongs to this expense
        int idToDelete = -1;
        for (Map.Entry<Integer, Expense> entry : expenseIdMap.entrySet()) {
            if (entry.getValue().equals(expense)) { // Uses Expense.equals() if overridden
                idToDelete = entry.getKey();
                break;
            }
        }

        if (idToDelete == -1) {
            System.out.println("Expense not found in database");
            return;
        }

        String sql = "DELETE FROM expenses WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idToDelete);
            pstmt.executeUpdate();
            expenseIdMap.remove(idToDelete);
            System.out.println("✓ Removed from database");
        } catch (SQLException e) {
            System.out.println("Delete error: " + e.getMessage());
        }
    }

    // Get total by category (Bonus)
    public void showTotalsByCategory() {
        String sql = "SELECT category, SUM(amount) as total FROM expenses GROUP BY category";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- TOTALS BY CATEGORY ---");
            while (rs.next()) {
                String category = rs.getString("category");
                double total = rs.getDouble("total");
                //System.out.printf("%s: $%.2f%n", category, total);
                System.out.println(category + ": " + total);
            }
        } catch (SQLException e) {
            System.out.println("Total error: " + e.getMessage());
        }
    }

    // Close connection
    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("✓ Database closed");
            }
        } catch (SQLException e) {
            System.out.println("Close error: " + e.getMessage());
        }
    }
}