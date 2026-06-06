public class Expense {
    private String description;
    private double amount;
    private String category;
    private String date;
    
    // Constructor
    public Expense(String description, double amount, String category, String date) {
    this.description = description;
    this.amount = amount;
    this.category = category;
    this.date = date;
    }
    
    // Getters (you need these to access private fields)
    public String getDescription() {
        return description;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getCategory() {
        return category;
    }

    public String getDate() {
    return date;
}
    
    // For saving to file (we'll use this later)
    public String toFileString() {
        return description + "," + amount + "," + category + "," + date; }
    
    // For displaying to user
    @Override
    public String toString() {
        return description + " - $" + amount + " (" + category + ") on " + date;
    }
}

