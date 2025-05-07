package HospitalManagement.service;

import HospitalManagement.model.CanteenItem;
import java.util.*;
import java.io.*;

public class CanteenService {
    private Map<String, List<CanteenItem>> menuByCategory = new HashMap<>();
    private Scanner scanner = new Scanner(System.in);
    // CSV File Path
    private final String MENU_FILE = "data/Menu.csv";

    public CanteenService() {
        initializeMenu();
    }

    private void initializeMenu() {
        File file = new File(MENU_FILE);
        // if (!file.exists()) {
        // System.out.println("CSV file not found, creating default menu.");
        // addDefaultCategory("Snacks", new CanteenItem("Chips", 20.0), new
        // CanteenItem("Samosa", 15.0));
        // addDefaultCategory("Breakfast", new CanteenItem("Poha", 30.0), new
        // CanteenItem("Sandwich", 40.0));
        // addDefaultCategory("Lunch", new CanteenItem("Paneer Curry", 80.0), new
        // CanteenItem("Dal Tadka", 60.0));
        // addDefaultCategory("Dinner", new CanteenItem("Noodles", 50.0), new
        // CanteenItem("Rice", 40.0));
        // saveMenuToCSV(); // save the default menu
        // return;
        // }

        try (BufferedReader br = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length == 3) {
                    String category = parts[0].toLowerCase(); 
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    menuByCategory.putIfAbsent(category, new ArrayList<>());
                    menuByCategory.get(category).add(new CanteenItem(name, price));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading menu CSV: " + e.getMessage());
        }
    }

    private void addDefaultCategory(String category, CanteenItem... items) {
        menuByCategory.putIfAbsent(category, new ArrayList<>());
        for (CanteenItem item : items) {
            menuByCategory.get(category).add(item);
        }
    }

    private void saveMenuToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MENU_FILE))) {
            bw.write("Category,ItemName,Price\n");
            for (Map.Entry<String, List<CanteenItem>> entry : menuByCategory.entrySet()) {
                String category = entry.getKey();
                for (CanteenItem item : entry.getValue()) {
                    bw.write(category + "," + item.getName() + "," + item.getPrice() + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Error writing menu CSV: " + e.getMessage());
        }
    }

    public void manageCanteen() {
        System.out.println("\n--- Welcome to the Canteen ---");
        System.out.print("Are you a:\n1. Manager\n2. Customer\nEnter choice (1 or 2): ");
        int role = scanner.nextInt();
        scanner.nextLine();

        if (role == 1) {
            manageCanteenAsManager();
        } else if (role == 2) {
            manageCanteenAsCustomer();
        } else {
            System.out.println("Invalid role selected.");
        }
    }

    // ---------- Manager Flow ----------
    private void manageCanteenAsManager() {
        int choice;
        do {
            System.out.println("\n--- CANTEEN MANAGER MENU ---");
            System.out.println("1. Add Item");
            System.out.println("2. Delete Item");
            System.out.println("3. Update Item");
            System.out.println("4. Show Menu");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addItem();
                case 2 -> deleteItem();
                case 3 -> updateItem();
                case 4 -> showFullMenu();
                case 0 -> System.out.println("Returning to main menu.");
                default -> System.out.println("Invalid option!");
            }
        } while (choice != 0);
    }

    private void addItem() {
        System.out.print("Enter category (Snacks/Breakfast/Lunch/Dinner): ");
        String category = scanner.nextLine();

        System.out.print("Enter item name: ");
        String name = scanner.nextLine();

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        menuByCategory.putIfAbsent(category, new ArrayList<>());
        menuByCategory.get(category).add(new CanteenItem(name, price));
        System.out.println("Item added to " + category);
        saveMenuToCSV();
    }

    private void deleteItem() {
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        if (!menuByCategory.containsKey(category)) {
            System.out.println("Category not found.");
            return;
        }

        showCategoryMenu(category);
        System.out.print("Enter item name to delete: ");
        String name = scanner.nextLine();

        boolean removed = menuByCategory.get(category).removeIf(item -> item.getName().equalsIgnoreCase(name));
        if (removed) {
            System.out.println("Item deleted.");
        } else {
            System.out.println("Item not found.");
        }
        saveMenuToCSV();
    }

    private void updateItem() {
        System.out.print("Enter category: ");
        String category = scanner.nextLine();
        if (!menuByCategory.containsKey(category)) {
            System.out.println("Category not found.");
            return;
        }

        showCategoryMenu(category);
        System.out.print("Enter item name to update: ");
        String name = scanner.nextLine();

        for (CanteenItem item : menuByCategory.get(category)) {
            if (item.getName().equalsIgnoreCase(name)) {
                System.out.print("Enter new name: ");
                String newName = scanner.nextLine();
                System.out.print("Enter new price: ");
                double newPrice = scanner.nextDouble();
                scanner.nextLine();
                menuByCategory.get(category).remove(item);
                menuByCategory.get(category).add(new CanteenItem(newName, newPrice));
                System.out.println("Item updated.");
                saveMenuToCSV();
                return;
            }
        }
        System.out.println("Item not found.");
    }

    private void showFullMenu() {
        if (menuByCategory.isEmpty()) {
            System.out.println("Menu is empty.");
            return;
        }
        for (String category : menuByCategory.keySet()) {
            System.out.println("\nCategory: " + category);
            for (CanteenItem item : menuByCategory.get(category)) {
                System.out.println(item.getName() + " - Rs" + item.getPrice());
            }
        }
    }

    private void showCategoryMenu(String category) {
        if (menuByCategory.containsKey(category)) {
            System.out.println("Items in " + category + ":");
            for (CanteenItem item : menuByCategory.get(category)) {
                System.out.println(item.getName() + " - Rs" + item.getPrice());
            }
        } else {
            System.out.println("No items found.");
        }
    }

    // ---------- Customer Flow ----------
    private void manageCanteenAsCustomer() {
        List<CanteenItem> order = new ArrayList<>();
        int choice;
        
        do {
            System.out.println("\nAvailable Categories:");
            for (String category : menuByCategory.keySet()) {
                System.out.println("- " + category);
            }
    
            System.out.print("Choose a category (or 0 to confirm order): ");
            String category = scanner.nextLine();
    
            if (category.equals("0")) {
                break;  // Exit and confirm the order
            }
    
            if (!menuByCategory.containsKey(category)) {
                System.out.println("Category not found. Please choose again.");
                continue;
            }
    
            List<CanteenItem> items = menuByCategory.get(category);
            
            // Add items from the selected category
            do {
                System.out.println("\n--- " + category + " MENU ---");
                for (int i = 0; i < items.size(); i++) {
                    CanteenItem item = items.get(i);
                    System.out.println((i + 1) + ". " + item.getName() + " - Rs" + item.getPrice());
                }
                System.out.println("0. Back to category selection");
    
                System.out.print("Select item number to order (0 to go back): ");
                choice = scanner.nextInt();
                scanner.nextLine();  // consume newline
    
                if (choice > 0 && choice <= items.size()) {
                    order.add(items.get(choice - 1));
                    System.out.println("Item added to order.");
                } else if (choice != 0) {
                    System.out.println("Invalid choice.");
                }
    
            } while (choice != 0);  // Allow switching to another category
        } while (true);  // Allow going back and choosing another category
    
        // Show the final order summary
        if (order.isEmpty()) {
            System.out.println("No items were selected.");
            return;
        }
    
        double total = 0;
        System.out.println("\nYour Order:");
        for (CanteenItem item : order) {
            System.out.println(item.getName() + " - Rs" + item.getPrice());
            total += item.getPrice();
        }
        System.out.println("Total: Rs" + total);
    
        // Ask for confirmation
        System.out.print("Confirm order? (yes/no): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("yes")) {
            System.out.println("Order placed successfully!");
        } else {
            System.out.println("Order cancelled.");
        }
    }
    
}
