package HospitalManagement.service;

import HospitalManagement.model.Pharmacy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.function.Predicate;

class PharmacyLinkedList<T> {
    public class Node {
        public T data;
        public Node next;

        public Node(T data) {
            this.data = data;
        }
    }

    private Node head;

    // Add item to end
    public void add(T data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
        } else {
            Node curr = head;
            while (curr.next != null)
                curr = curr.next;
            curr.next = newNode;
        }
    }

    // Remove items matching predicate
    public boolean removeIf(Predicate<T> predicate) {
        boolean removed = false;

        while (head != null && predicate.test(head.data)) {
            head = head.next;
            removed = true;
        }

        Node curr = head;
        while (curr != null && curr.next != null) {
            if (predicate.test(curr.next.data)) {
                curr.next = curr.next.next;
                removed = true;
            } else {
                curr = curr.next;
            }
        }

        return removed;
    }

    // Clear list
    public void clear() {
        head = null;
    }

    // Find first item matching predicate
    public T find(Predicate<T> predicate) {
        Node curr = head;
        while (curr != null) {
            if (predicate.test(curr.data))
                return curr.data;
            curr = curr.next;
        }
        return null;
    }

    // Get the head node to manually traverse
    public Node getHead() {
        return head;
    }

    // Check if list is empty
    public boolean isEmpty() {
        return head == null;
    }
}

public class PharmacyServices {
    private PharmacyLinkedList<Pharmacy> itemList = new PharmacyLinkedList<>();
    private final String FILE_PATH = "data/Pharmacy.csv";

    public PharmacyServices() {
        loadItemsFromCSV();
    }

    // ======================================== CSV Methods ====================================================

    private void loadItemsFromCSV() {
        itemList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header
                    continue;
                }
                String[] data = line.split(",");
                if (data.length == 6) {
                    Pharmacy p = new Pharmacy(
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            Integer.parseInt(data[4].trim()),
                            Double.parseDouble(data[5].trim()));
                    itemList.add(p);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading pharmacy data: " + e.getMessage());
        }
    }

    private void saveItemsToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            PharmacyLinkedList<Pharmacy>.Node current = itemList.getHead();
            while (current != null) {
                writer.println(current.data.toString());
                current = current.next;
            }
        } catch (IOException e) {
            System.out.println("Error saving pharmacy data: " + e.getMessage());
        }
    }

    // ========================================|Admin|====================================================

    public void adminMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n------ Pharmacy Administrator Menu ------");
            System.out.println("1. View All Items");
            System.out.println("2. Add Item");
            System.out.println("3. Update Item");
            System.out.println("4. Delete Item");
            System.out.println("5. Search Item");
            System.out.println("0. Back to Main Menu");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewAllItems();
                    break;
                case 2:
                    System.out.print("Enter Item ID: ");
                    String id = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Category: ");
                    String category = scanner.nextLine();
                    System.out.print("Enter Manufacturer: ");
                    String manufacturer = scanner.nextLine();
                    System.out.print("Enter Quantity: ");
                    int quantity = scanner.nextInt();
                    System.out.print("Enter Price: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine();

                    Pharmacy p = new Pharmacy(id, name, category, manufacturer, quantity, price);
                    addItem(p);
                    break;
                case 3:
                    System.out.print("Enter Item ID to update: ");
                    id = scanner.nextLine();
                    updateItem(id);
                    break;
                case 4:
                    System.out.print("Enter Item ID to delete: ");
                    id = scanner.nextLine();
                    deleteItem(id);
                    break;
                case 5:
                    searchItem();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 0);
    }

    public void addItem(Pharmacy p) {
        itemList.add(p);
        saveItemsToCSV();
        System.out.println("Item added successfully.");
    }

    public void deleteItem(String id) {
        boolean removed = itemList.removeIf(p -> p.getItemId().equalsIgnoreCase(id));
        if (removed) {
            saveItemsToCSV();
            System.out.println("Item deleted.");
        } else {
            System.out.println("Item ID not found.");
        }
    }

    // Update item by ID
    public void updateItem(String id) {
        Scanner sc = new Scanner(System.in);
        PharmacyLinkedList<Pharmacy>.Node current = itemList.getHead();

        while (current != null) {
            Pharmacy p = current.data;
            if (p.getItemId().equalsIgnoreCase(id)) {
                System.out.println("Updating item: " + id);

                System.out.print("Enter new name [" + p.getName() + "]: ");
                String name = sc.nextLine().trim();
                if (!name.isEmpty()) {
                    p.setName(name);
                }

                System.out.print("Enter new category [" + p.getCategory() + "]: ");
                String category = sc.nextLine().trim();
                if (!category.isEmpty()) {
                    p.setCategory(category);
                }

                System.out.print("Enter new manufacturer [" + p.getManufacturer() + "]: ");
                String manufacturer = sc.nextLine().trim();
                if (!manufacturer.isEmpty()) {
                    p.setManufacturer(manufacturer);
                }

                System.out.print("Enter new quantity [" + p.getQuantity() + "]: ");
                String qtyInput = sc.nextLine().trim();
                if (!qtyInput.isEmpty()) {
                    try {
                        int qty = Integer.parseInt(qtyInput);
                        p.setQuantity(qty);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity. Keeping the previous value.");
                    }
                }

                System.out.print("Enter new price [" + p.getPrice() + "]: ");
                String priceInput = sc.nextLine().trim();
                if (!priceInput.isEmpty()) {
                    try {
                        double price = Double.parseDouble(priceInput);
                        p.setPrice(price);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price. Keeping the previous value.");
                    }
                }

                saveItemsToCSV();
                System.out.println("Item updated successfully.");
                return;
            }
            current = current.next;
        }

        System.out.println("Item ID not found.");
    }

    // View all items in the pharmacy
    public void viewAllItems() {
        System.out.println("\nAll Pharmacy Items:");
        System.out.printf("%-10s %-25s %-15s %-20s %-10s %-10s%n",
                "Item ID", "Name", "Category", "Manufacturer", "Quantity", "Price");
        System.out.println(
                "-------------------------------------------------------------------------------------------------------");

        PharmacyLinkedList<Pharmacy>.Node current = itemList.getHead();
        while (current != null) {
            Pharmacy p = current.data;
            System.out.printf("%-10s %-25s %-15s %-20s %-10d Rs.%-8.2f%n",
                    p.getItemId(), p.getName(), p.getCategory(), p.getManufacturer(), p.getQuantity(), p.getPrice());
            current = current.next;
        }
    }

    // Search items
    public void searchItem() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Search Item:");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Item ID");
        System.out.print("Enter your choice (1 or 2): ");
        String choice = sc.nextLine().trim();

        PharmacyLinkedList<Pharmacy>.Node current = itemList.getHead();
        boolean found = false;

        switch (choice) {
            case "1":
                System.out.print("Enter item name to search: ");
                String name = sc.nextLine().trim();

                while (current != null) {
                    Pharmacy p = current.data;
                    if (p.getName().equalsIgnoreCase(name)) {
                        printPharmacyDetails(p);
                        found = true;
                        break;
                    }
                    current = current.next;
                }
                break;

            case "2":
                System.out.print("Enter item ID to search: ");
                String id = sc.nextLine().trim();

                while (current != null) {
                    Pharmacy p = current.data;
                    if (p.getItemId().equalsIgnoreCase(id)) {
                        printPharmacyDetails(p);
                        found = true;
                        break;
                    }
                    current = current.next;
                }
                break;

            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (!found) {
            System.out.println("Item not found.");
        }
    }

    // Helper function to print item details in table format
    private void printPharmacyDetails(Pharmacy p) {
        System.out.printf("%-10s %-20s %-15s %-20s %-10s %-10s%n",
                "Item ID", "Name", "Category", "Manufacturer", "Qty", "Price");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.printf("%-10s %-20s %-15s %-20s %-10d Rs.%-9.2f%n",
                p.getItemId(), p.getName(), p.getCategory(), p.getManufacturer(), p.getQuantity(), p.getPrice());
    }

    // ======================================== Customer
    // ====================================================

    public void customerMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n------ Customer Menu ------");
            System.out.println("1. View Available Items");
            System.out.println("2. Search Item by Name");
            System.out.println("3. Order Item");
            System.out.println("0. Back to Main Menu");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    viewAvailableItems();
                    break;
                case 2:
                    System.out.print("Enter Item Name to search: ");
                    String name = scanner.nextLine();
                    searchItemCustomer(name);
                    break;
                case 3:
                    orderItem();
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 0); // The loop will repeat until the user chooses to exit
    }

    public void viewAvailableItems() {
        System.out.println("Available Items:");
        System.out.printf("%-20s %-25s %-20s %-10s %-10s%n",
                "Name", "Category", "Manufacturer", "Price", "Quantity");
        System.out.println("-----------------------------------------------------------------------------------------");

        PharmacyLinkedList<Pharmacy>.Node current = itemList.getHead();
        while (current != null) {
            Pharmacy p = current.data;
            if (p.getQuantity() > 0) {
                System.out.printf("%-20s %-25s %-20s Rs %-8.2f %-10d%n",
                        p.getName(), p.getCategory(), p.getManufacturer(), p.getPrice(), p.getQuantity());
            }
            current = current.next;
        }
    }

    // Order item by name and quantity
    public void orderItem() {
        Scanner sc = new Scanner(System.in);
        double totalPrice = 0.0;

        while (true) {
            // Display items
            System.out.println("\nAvailable Items:");
            System.out.printf("%-20s %-25s %-20s %-10s %-10s%n",
                    "Name", "Category", "Manufacturer", "Price", "Quantity");
            System.out.println("------------------------------------------------------------------------------");
            PharmacyLinkedList<Pharmacy>.Node current = itemList.getHead();
            while (current != null) {
                Pharmacy p = current.data;
                if (p.getQuantity() > 0) {
                    System.out.printf("%-20s %-25s %-20s Rs.%-8.2f %-10d%n",
                            p.getName(), p.getCategory(), p.getManufacturer(), p.getPrice(), p.getQuantity());
                }
                current = current.next;
            }

            System.out.print("\nEnter item name to order (or type 'confirm' to finish): ");
            String name = sc.nextLine().trim();
            if (name.equalsIgnoreCase("confirm")) {
                break;
            }

            System.out.print("Enter quantity: ");
            int qty;
            try {
                qty = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Try again.");
                continue;
            }

            // Process order
            current = itemList.getHead();
            boolean found = false;
            while (current != null) {
                Pharmacy p = current.data;
                if (p.getName().equalsIgnoreCase(name)) {
                    found = true;
                    if (p.getQuantity() >= qty) {
                        p.setQuantity(p.getQuantity() - qty);
                        double cost = qty * p.getPrice();
                        totalPrice += cost;
                        System.out.println(
                                "Added " + qty + " units of " + name + " to your order (Subtotal: Rs." + cost + ")");
                    } else {
                        System.out.println("Only " + p.getQuantity() + " units available.");
                    }
                    break;
                }
                current = current.next;
            }

            if (!found) {
                System.out.println("Item not found.");
            }
        }

        saveItemsToCSV(); // Save after all orders

        if (totalPrice > 0) {
            System.out.printf("\nTotal amount to be paid: Rs.%.2f%n", totalPrice);
        } else {
            System.out.println("\nNo items were ordered.");
        }
    }

    public void searchItemCustomer(String name) {
        PharmacyLinkedList<Pharmacy>.Node current = itemList.getHead();
        boolean found = false;

        while (current != null) {
            Pharmacy p = current.data;
            if (p.getName().equalsIgnoreCase(name)) {
                // Print header once
                System.out.printf("%-10s %-20s %-15s %-20s %-10s %-10s%n",
                        "Item ID", "Name", "Category", "Manufacturer", "Qty", "Price");
                System.out.println("----------------------------------------------------------------------------------------");
                // Print item
                System.out.printf("%-10s %-20s %-15s %-20s %-10d Rs %-9.2f%n",
                        p.getItemId(), p.getName(), p.getCategory(), p.getManufacturer(),
                        p.getQuantity(), p.getPrice());
                found = true;
                break;
            }
            current = current.next;
        }

        if (!found) {
            System.out.println("Item not found.");
        }
    }
}
