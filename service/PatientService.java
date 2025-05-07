package HospitalManagement.service;

import HospitalManagement.model.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.Consumer;

//self made classes being used
class AppointmentLinkedList implements Iterable<Appointment> {

    private class Node {
        Appointment data;
        Node next;

        Node(Appointment data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;

    // Enqueue: Add to rear
    public void enqueue(Appointment appointment) {
        Node newNode = new Node(appointment);
        if (tail == null) {
            head = tail = newNode;
            return;
        }
        tail.next = newNode;
        tail = newNode;
    }

    // Dequeue: Remove from front
    public Appointment dequeue() {
        if (head == null) {
            throw new NoSuchElementException("Queue is empty");
        }
        Appointment result = head.data;
        head = head.next;
        if (head == null) {
            tail = null; // reset tail when queue becomes empty
        }
        return result;
    }

    // Peek at front (next appointment)
    public Appointment peek() {
        if (head == null)
            return null;
        return head.data;
    }

    // Check if empty
    public boolean isEmpty() {
        return head == null;
    }

    // Print all appointments
    public void printAll() {
        Node current = head;
        while (current != null) {
            System.out.println(current.data); // Ensure Appointment overrides toString()
            current = current.next;
        }
    }

    // Iterator to loop through appointments
    @Override
    public Iterator<Appointment> iterator() {
        return new Iterator<>() {
            private Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Appointment next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                Appointment data = current.data;
                current = current.next;
                return data;
            }
        };
    }
}

class PatientNode {
    Patient data;
    PatientNode left, right;

    public PatientNode(Patient data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}

class PatientBinaryST {
    private PatientNode root;

    public void insert(Patient data) {
        root = insertRecursive(root, data);
    }

    private PatientNode insertRecursive(PatientNode node, Patient data) {
        if (node == null)
            return new PatientNode(data);

        if (data.getPatientId() < node.data.getPatientId()) {
            node.left = insertRecursive(node.left, data);
        } else {
            node.right = insertRecursive(node.right, data);
        }
        return node;
    }

    public Patient searchByName(String name) {
        return searchByNameRecursive(root, name);
    }

    private Patient searchByNameRecursive(PatientNode node, String name) {
        if (node == null)
            return null;

        int cmp = name.compareToIgnoreCase(node.data.getName());
        if (cmp == 0)
            return node.data;
        else if (cmp < 0)
            return searchByNameRecursive(node.left, name);
        else
            return searchByNameRecursive(node.right, name);
    }

    public Patient searchById(int id) {
        return searchByIdRecursive(root, id);
    }

    private Patient searchByIdRecursive(PatientNode node, int id) {
        if (node == null)
            return null;

        if (node.data.getPatientId() == id)
            return node.data;

        Patient found = searchByIdRecursive(node.left, id);
        if (found != null)
            return found;

        return searchByIdRecursive(node.right, id);
    }

    public void traverseInOrder(Consumer<Patient> action) {
        traverseInOrderRecursive(root, action);
    }

    private void traverseInOrderRecursive(PatientNode node, Consumer<Patient> action) {
        if (node == null)
            return;

        // Traverse left
        traverseInOrderRecursive(node.left, action);

        // Apply action on current node
        action.accept(node.data);

        // Traverse right
        traverseInOrderRecursive(node.right, action);
    }
}

// Main execution
public class PatientService {
    // Variables
    private static final String PATIENTS_CSV_FILE = "data/Patients.csv";
    private static final String APPOINTMENTS_CSV_FILE = "data/Appointments.csv";
    private static PatientBinaryST patientTree = new PatientBinaryST();
    private static Scanner scanner = new Scanner(System.in);
    private static AppointmentLinkedList appointments = new AppointmentLinkedList();

    // Constructor to initialize
    public PatientService() {
        loadPatients();
        loadAppointments();
    }

    // methods to initialize structures
    public void loadPatients() {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATIENTS_CSV_FILE))) {
            String line;
            reader.readLine(); // skip header

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                int id = Integer.parseInt(data[0]);
                String name = data[1];
                int age = Integer.parseInt(data[2]);
                String gender = data[3];
                String illness = data[4];
                String contact = data[5];
                boolean vegetarian = Boolean.parseBoolean(data[6]);
                int visits = Integer.parseInt(data[7]);
                boolean admittedBefore = Boolean.parseBoolean(data[8]);

                patientTree.insert(
                        new Patient(id, name, age, gender, illness, contact, vegetarian, visits, admittedBefore));
            }
        } catch (IOException e) {
            System.out.println("Error loading patients: " + e.getMessage());
        }
    }

    public void loadAppointments() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENTS_CSV_FILE))) {
            String line = reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length >= 6) {
                    int appointmentId = Integer.parseInt(fields[0].trim());
                    int patientId = Integer.parseInt(fields[1].trim());
                    String patientName = fields[2].trim();
                    String reason = fields[3].trim();
                    LocalDateTime dateTime = LocalDateTime.parse(fields[4].trim(), formatter);
                    int doctorId = Integer.parseInt(fields[5].trim());

                    Appointment appointment = new Appointment(appointmentId, patientId, patientName, reason, dateTime,
                            doctorId);
                    appointments.enqueue(appointment);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading appointments: " + e.getMessage());
        }
    }

    // Main execution method
    // public void patientManagement() {
    // int choice;
    // do {
    // System.out.println("\n==== Patient Administration Menu ====");
    // System.out.println("1. Login as Administrator");
    // System.out.println("2. Login as Patient");
    // choice = scanner.nextInt();
    // scanner.nextLine();
    // if (choice == 1) {
    // manageAsAdmin();
    // } else if (choice == 2) {
    // managePatients();
    // } else {
    // System.out.println("Invalid Choice. Please try again.");
    // }
    // } while (choice != 0);
    // }

    public void managePatients() {
        int choice;
        do {

            System.out.println("\n==== Appointment MENU ====");
            System.out.println("1. Book an Appointment");
            System.out.println("2. Check My Appointments");
            System.out.println("3. Update My Appointments");
            System.out.println("4. Delete My Appointments");
            System.out.println("5. View Doctors List");
            System.out.println("6. View your appointments");
            System.out.println("0. Back to Main Menu");
            System.out.println("\n==== PATIENT MENU ====");
            System.out.println("7. Update Patient Information");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer

            switch (choice) {
                case 1:
                    bookAppointment();
                    break;
                case 2:
                    checkAppointments();
                    break;
                case 3:
                    updateAppointments();
                    break;
                case 4:
                    deleteAppointments();
                    break;
                case 5:
                    viewDoctors();
                    break;
                case 6:
                    viewMyAppointments();
                    break;
                case 7:
                    updatePatient();
                    break;
                case 0:
                    System.out.println("Returning to main menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    // Book an appointment
    private void bookAppointment() {
        System.out.println("Enter your name: ");
        String patientName = scanner.nextLine();

        System.out.println("Enter illness details: ");
        String illness = scanner.nextLine();

        System.out.println("Enter Contact details: ");
        String contactNo = scanner.nextLine();

        System.out.println("==================================  Doctors' List:  ===================================");
        viewDoctors();

        System.out.println("Choose preferred date and time for the appointment (format: yyyy-MM-dd HH:mm): ");
        String dateTimeInput = scanner.nextLine();

        LocalDateTime dateTime;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            dateTime = LocalDateTime.parse(dateTimeInput, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date-time format. Please use yyyy-MM-dd HH:mm");
            return;
        }

        System.out.println("Enter preferred doctor ID: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        // Find the doctor
        Doctor doctor = findDoctorById(doctorId);
        if (doctor == null) {
            System.out.println("Doctor not found.");
            return;
        }

        // Generate appointmentId (can be incremental or randomized)
        int appointmentId = Appointment.getNextAppointmentId();

        int patientId = getOrRegisterPatientId(patientName, contactNo);

        // Create the Appointment object
        Appointment appointment = new Appointment(appointmentId, patientId, patientName, illness, dateTime, doctorId);

        // Check if the appointment time is available
        if (checkAvailability(dateTime, doctorId)) {
            appointments.enqueue(appointment); // Add appointment to the list
            System.out.println("Appointment booked successfully!");
        } else {
            // Suggest next available slot or another doctor
            System.out.println("Requested slot is not available. Suggesting next available slot...");
            // suggestAlternateSlotOrDoctor(dateTime, doctorId);
        }
        saveAppointmentsToCSV();
    }

    // Check the appointments of the patient
    private void checkAppointments() {
        System.out.print("Enter your Patient ID: ");
        int patientId;

        try {
            patientId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Patient ID. Please enter a numeric value.");
            return;
        }

        boolean found = false;
        System.out.println("Your Appointments:");
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId() == patientId) {
                System.out.println(appointment);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments found for Patient ID: " + patientId);
        }
    }

    private void updateAppointments() {
        if (!viewMyAppointments()) {
            return; // Exit if no appointments found
        }

        System.out.print("Enter Appointment ID to update: ");

        int targetId;
        try {
            targetId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid appointment ID. Please enter a valid number.");
            return;
        }

        boolean found = false;

        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId() == targetId) {
                System.out.println("Appointment found. Enter new details (press Enter to keep previous):");

                int newDoctorId = appointment.getDoctorId(); // default
                System.out.print("Enter new doctor ID [" + appointment.getDoctorId() + "]: ");
                String doctorInput = scanner.nextLine().trim();
                if (!doctorInput.isEmpty()) {
                    try {
                        newDoctorId = Integer.parseInt(doctorInput);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid doctor ID. Update aborted.");
                        return;
                    }
                }

                System.out.print("Enter new reason [" + appointment.getReason() + "]: ");
                String reasonInput = scanner.nextLine().trim();
                if (!reasonInput.isEmpty()) {
                    appointment.setReason(reasonInput);
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime newDateTime = appointment.getAppointmentDate(); // default
                System.out.print(
                        "Enter new date and time [" + appointment.getAppointmentDate().format(formatter) + "]: ");
                String dateTimeStr = scanner.nextLine().trim();
                if (!dateTimeStr.isEmpty()) {
                    try {
                        newDateTime = LocalDateTime.parse(dateTimeStr, formatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date and time format. Update aborted.");
                        return;
                    }
                }

                // Check doctor's day-wise availability
                if (!checkAvailability(newDateTime, newDoctorId)) {
                    System.out.println("Doctor is not available on the selected date. Update aborted.");
                    return;
                }

                // Apply updates
                appointment.setDoctorId(newDoctorId);
                appointment.setAppointmentDate(newDateTime);

                found = true;
                break;
            }
        }

        if (found) {
            saveAppointmentsToCSV();
            System.out.println("Appointment updated successfully.");
        } else {
            System.out.println("Appointment ID not found.");
        }
    }

    private void deleteAppointments() {
        if (!viewMyAppointments()) {
            return; // Exit if no appointments found
        }

        System.out.print("Enter Appointment ID to delete: ");

        int targetId;
        try {
            targetId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid appointment ID. Please enter a number.");
            return;
        }

        boolean removed = false;
        AppointmentLinkedList updatedAppointments = new AppointmentLinkedList();

        while (!appointments.isEmpty()) {
            Appointment appointment = appointments.dequeue(); // Assuming this is how you remove from
                                                              // AppointmentLinkedList
            if (appointment.getAppointmentId() == targetId) {
                removed = true;
                continue; // Do not add back the deleted appointment
            }
            updatedAppointments.enqueue(appointment); // Add back others
        }

        appointments = updatedAppointments; // Replace with updated list

        if (removed) {
            saveAppointmentsToCSV();
            System.out.println("Appointment deleted successfully.");
        } else {
            System.out.println("Appointment ID not found.");
        }
    }

    // View the list of doctors
    private void viewDoctors() {
        String filePath = "data/Doctors.csv"; // Ensure path is correct if in a different folder
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Read header
            if (line == null) {
                System.out.println("No doctor records found.");
                return;
            }

            // Print table headers
            System.out.printf("%-5s %-25s %-10s %-20s %-15s %-10s %-15s%n",
                    "ID", "Name", "Qualif.", "Specialization", "Contact", "Exp(yrs)", "Availability");
            System.out.println(
                    "-----------------------------------------------------------------------------------------------------------");

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length < 7)
                    continue;

                System.out.printf("%-5s %-25s %-10s %-20s %-15s %-10s %-15s%n",
                        fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6]);
            }

        } catch (IOException e) {
            System.out.println("Error reading Doctors.csv: " + e.getMessage());
        }
    }

    // View the medical history of the patient
    private boolean viewMyAppointments() {
        System.out.println("Enter Patient ID: ");
        int patientId;

        try {
            patientId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Patient ID.");
            return false;
        }

        boolean found = false;
        System.out.println("\n------------------------------------------");
        System.out.println("        Your Appointments are:");
        System.out.println("------------------------------------------");

        for (Appointment appointment : appointments) {
            if (appointment.getPatientId() == patientId) {
                System.out.println(appointment);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments found.");
        }

        return found;
    }

    private void updatePatient() {
        System.out.print("Enter Patient ID to update: ");
        int patientId = Integer.parseInt(scanner.nextLine().trim());

        Patient targetPatient = patientTree.searchById(patientId);

        if (targetPatient == null) {
            System.out.println("Patient ID not found.");
            return;
        }

        System.out.println("Updating details for: " + targetPatient.getName());

        System.out.print("New Emergency Contact (leave blank to skip): ");
        String emergency = scanner.nextLine();
        if (!emergency.isEmpty())
            targetPatient.setEmergencyContact(emergency);

        System.out.print("Is Vegetarian? (true/false or leave blank): ");
        String veg = scanner.nextLine();
        if (!veg.isEmpty())
            targetPatient.setVegetarian(Boolean.parseBoolean(veg));

        savePatientsToCSV();
        System.out.println("Patient information updated.");
    }

    private int getOrRegisterPatientId(String patientName, String contactNo) {
        String filePath = "data/Patients.csv";
        int lastId = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length < 6)
                    continue;

                int id = Integer.parseInt(fields[0].trim());
                String name = fields[1].trim();
                String contact = fields[5].trim();

                // Check both name and contactNo
                if (name.equalsIgnoreCase(patientName) && contact.equals(contactNo)) {
                    return id;
                }

                if (id > lastId) {
                    lastId = id;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Patients.csv: " + e.getMessage());
        }

        // Patient not found – register new
        Scanner scanner = new Scanner(System.in);
        System.out.println("Patient not found. Registering new patient.");

        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter gender (M/F/O): ");
        String gender = scanner.nextLine().trim();

        System.out.print("Enter illness: ");
        String illness = scanner.nextLine().trim();

        System.out.print("Is the patient vegetarian? (true/false): ");
        boolean vegetarian = Boolean.parseBoolean(scanner.nextLine().trim());

        int numberOfVisits = 1;

        System.out.print("Was the patient admitted before? (true/false): ");
        boolean wasAdmittedBefore = Boolean.parseBoolean(scanner.nextLine().trim());

        int newId = lastId + 1;

        // Create patient record and write to file
        writePatientToCSV(newId, patientName, age, gender, illness, contactNo, vegetarian, numberOfVisits,
                wasAdmittedBefore);

        return newId;
    }

    // save changes to appointment
    private void saveAppointmentsToCSV() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENTS_CSV_FILE))) {
            // Write header
            writer.write("AppointmentID,PatientID,PatientName,Reason,DateTime,DoctorID");
            writer.newLine();

            for (Appointment appointment : appointments) {
                writer.write(appointment.getAppointmentId() + "," +
                        appointment.getPatientId() + "," +
                        appointment.getPatientName() + "," +
                        appointment.getReason() + "," +
                        appointment.getDateTime().format(formatter) + "," +
                        appointment.getDoctorId());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving appointments: " + e.getMessage());
        }
    }

    private void writePatientToCSV(Patient p) {
        String filePath = "data/Patients.csv";
        String newPatientData = String.format("%d,%s,%d,%s,%s,%s,%b,%d,%b",
                p.getPatientId(),
                p.getName(),
                p.getAge(),
                p.getGender(),
                p.getIllness(),
                p.getEmergencyContact(),
                p.isVegetarian(),
                p.getNumberOfVisits(),
                p.isWasAdmittedBefore());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.newLine();
            writer.write(newPatientData);
        } catch (IOException e) {
            System.out.println("Error writing to Patients.csv: " + e.getMessage());
        }
    }

    private void savePatientsToCSV() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data/Patients.csv"))) {
            writer.println(
                    "PatientID,Name,Age,Gender,Illness,EmergencyContact,Vegetarian,NumberOfVisits,WasAdmittedBefore");

            // Use the traverseInOrder method to loop through each patient in the tree
            patientTree.traverseInOrder(patient -> {
                writer.printf("%d,%s,%d,%s,%s,%s,%b,%d,%b\n",
                        patient.getPatientId(),
                        patient.getName(),
                        patient.getAge(),
                        patient.getGender(),
                        patient.getIllness(),
                        patient.getEmergencyContact(),
                        patient.isVegetarian(),
                        patient.getNumberOfVisits(),
                        patient.isWasAdmittedBefore());
            });

            System.out.println("Patients saved to CSV successfully.");
        } catch (IOException e) {
            System.out.println("Error saving to CSV: " + e.getMessage());
        }
    }

    // Helper method to find doctor by ID
    private Doctor findDoctorById(int doctorId) {
        String filePath = "data/Doctors.csv";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length < 7)
                    continue;

                int id = Integer.parseInt(fields[0].trim());
                if (id == doctorId) {
                    // Found the doctor, create and return the Doctor object
                    String name = fields[1].trim();
                    String qualification = fields[2].trim();
                    String specialization = fields[3].trim();
                    String contact = fields[4].trim();
                    int experience = Integer.parseInt(fields[5].trim());
                    String[] availability = fields[6].trim().split("-");
                    return new Doctor(id, name, qualification, specialization, contact, experience, availability);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading from Doctors.csv: " + e.getMessage());
        }

        return null; // Doctor not found
    }

    private boolean checkAvailability(LocalDateTime dateTime, int doctorId) {
        String filePath = "data/Doctors.csv"; // File path for the doctor's CSV file
        boolean isAvailable = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip the header
            reader.readLine();

            // Loop through the file and check the doctor's availability
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length < 7)
                    continue; // Skip invalid lines

                int doctorIdFromFile = Integer.parseInt(fields[0].trim()); // Assuming ID is the first column
                String availability = fields[6].trim(); // Availability is the last column

                // Check if the current doctor is the one we're looking for
                if (doctorIdFromFile == doctorId) {
                    // Split availability into individual days (Mon, Tue, etc.)
                    String[] availableDays = availability.split("-");

                    // Extract the day of the week from the appointment's date
                    String dayOfWeek = dateTime.getDayOfWeek().toString().substring(0, 3); // "Mon", "Tue", etc.

                    // Check if the doctor is available on that day
                    for (String day : availableDays) {
                        if (day.equalsIgnoreCase(dayOfWeek)) {
                            isAvailable = true;
                            break;
                        }
                    }
                    break; // Once we find the doctor, we don't need to process the file further
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading from Doctors.csv: " + e.getMessage());
        }

        return isAvailable; // If the doctor is available on the selected day
    }

    public void manageAsAdminPatients() {
        Scanner scanner = new Scanner(System.in); // Define scanner
        int choice;

        do {
            System.out.println("\n==== Patient Administration Menu ====");
            System.out.println("1. Add Patients");
            System.out.println("2. Delete Patients");
            System.out.println("3. Update Patients");
            System.out.println("4. View All Patients");
            System.out.println("5. Search Patients");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Clear newline

            switch (choice) {
                case 1:
                    addPatient(); // Implement this
                    break;
                case 2:
                    // deletePatient(); // Implement this
                    break;
                case 3:
                    updatePatient(); // Implement this
                    break;
                case 4:
                    // viewAllPatients(); // Implement this
                    break;
                case 5:
                    // searchPatient(); // Implement this
                    break;
                case 0:
                    System.out.println("Returning to previous menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    public void addPatient() {
        System.out.print("Enter Patient ID (numeric): ");
        int id = Integer.parseInt(scanner.nextLine()); // convert to int

        System.out.print("Enter Patient Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();

        System.out.print("Enter Emergency Contact Number: ");
        String emergencyContact = scanner.nextLine();

        System.out.print("Enter Blood Group: ");
        String bloodGroup = scanner.nextLine(); // add if constructor expects it

        System.out.print("Is the patient vegetarian? (true/false): ");
        boolean vegetarian = scanner.nextBoolean();

        System.out.print("Number of previous visits: ");
        int numberOfVisits = scanner.nextInt();

        System.out.print("Was the patient admitted before? (true/false): ");
        boolean wasAdmittedBefore = scanner.nextBoolean();
        scanner.nextLine(); // consume newline if any

        // Create patient object
        Patient newPatient = new Patient(id, name, age, gender, emergencyContact, bloodGroup, vegetarian,
                numberOfVisits, wasAdmittedBefore);

        // Insert into Binary Search Tree
        patientTree.insert(newPatient);

        // Save to CSV
        writePatientToCSV(newPatient);

        System.out.println("Patient added successfully and saved to records.");
    }
}