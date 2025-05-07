package HospitalManagement.service;

import HospitalManagement.model.Appointment;
import HospitalManagement.model.Patient;
import HospitalManagement.model.Doctor;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * DoctorService.java
 * 
 * A service class for managing doctors, appointments, and patient searches in a
 * Hospital Management System.
 * Implements doctor login (by ID), appointment viewing, patient search, and an
 * administrator menu
 * for CRUD operations on doctors with CSV file persistence.
 */
class PatientBST {
    private class Node {
        Patient patient;
        Node left, right;

        Node(Patient patient) {
            this.patient = patient;
        }
    }

    private Node root;

    public void insert(Patient patient) {
        root = insertRec(root, patient);
    }

    private Node insertRec(Node root, Patient patient) {
        if (root == null)
            return new Node(patient);
        if (patient.getPatientId() < root.patient.getPatientId())
            root.left = insertRec(root.left, patient);
        else
            root.right = insertRec(root.right, patient);
        return root;
    }

    public Patient searchById(int id) {
        Node node = searchRec(root, id);
        return node == null ? null : node.patient;
    }

    private Node searchRec(Node root, int id) {
        if (root == null || root.patient.getPatientId() == id)
            return root;
        if (id < root.patient.getPatientId())
            return searchRec(root.left, id);
        return searchRec(root.right, id);
    }

    public void inOrder(List<Patient> list) {
        inOrderRec(root, list);
    }

    private void inOrderRec(Node node, List<Patient> list) {
        if (node != null) {
            inOrderRec(node.left, list);
            list.add(node.patient);
            inOrderRec(node.right, list);
        }
    }

    public void delete(int id) {
        root = deleteRec(root, id);
    }

    private Node deleteRec(Node root, int id) {
        if (root == null) return root;

        if (id < root.patient.getPatientId())
            root.left = deleteRec(root.left, id);
        else if (id > root.patient.getPatientId())
            root.right = deleteRec(root.right, id);
        else {
            if (root.left == null) return root.right;
            else if (root.right == null) return root.left;

            root.patient = minValue(root.right);
            root.right = deleteRec(root.right, root.patient.getPatientId());
        }

        return root;
    }

    private Patient minValue(Node root) {
        Patient minv = root.patient;
        while (root.left != null) {
            minv = root.left.patient;
            root = root.left;
        }
        return minv;
    }
}

class DoctorBST {
    private class Node {
        Doctor doctor;
        Node left, right;

        Node(Doctor doctor) {
            this.doctor = doctor;
        }
    }

    private Node root;

    public void insert(Doctor doctor) {
        root = insertRec(root, doctor);
    }

    private Node insertRec(Node root, Doctor doctor) {
        if (root == null)
            return new Node(doctor);
        if (doctor.getId() < root.doctor.getId())
            root.left = insertRec(root.left, doctor);
        else
            root.right = insertRec(root.right, doctor);
        return root;
    }

    public Doctor searchById(int id) {
        Node node = searchRec(root, id);
        return node == null ? null : node.doctor;
    }

    private Node searchRec(Node root, int id) {
        if (root == null || root.doctor.getId() == id)
            return root;
        if (id < root.doctor.getId())
            return searchRec(root.left, id);
        return searchRec(root.right, id);
    }

    public void inOrder(List<Doctor> doctors) {
        inOrderRec(root, doctors);
    }

    private void inOrderRec(Node node, List<Doctor> doctors) {
        if (node != null) {
            inOrderRec(node.left, doctors);
            doctors.add(node.doctor);
            inOrderRec(node.right, doctors);
        }
    }

    public void delete(int id) {
        root = deleteRec(root, id);
    }

    private Node deleteRec(Node root, int id) {
        if (root == null)
            return root;

        if (id < root.doctor.getId()) {
            root.left = deleteRec(root.left, id);
        } else if (id > root.doctor.getId()) {
            root.right = deleteRec(root.right, id);
        } else {
            if (root.left == null)
                return root.right;
            else if (root.right == null)
                return root.left;

            root.doctor = minValue(root.right);
            root.right = deleteRec(root.right, root.doctor.getId());
        }

        return root;
    }

    private Doctor minValue(Node root) {
        Doctor minv = root.doctor;
        while (root.left != null) {
            minv = root.left.doctor;
            root = root.left;
        }
        return minv;
    }
}

public class DoctorService {
    // Data lists for doctors, patients, and appointments
    private static DoctorBST doctorTree = new DoctorBST();
    private static PatientBST patientBST = new PatientBST();
    private static List<Appointment> appointments = new ArrayList<>();

    public static void manageStaff() {
        // Load data from CSV files on startup
        loadDoctors();
        // List<Doctor> loadedDoctors = new ArrayList<>();
        // doctorTree.inOrder(loadedDoctors);

        // System.out.println("Doctors loaded into tree:");
        // for (Doctor d : loadedDoctors) {
        //     System.out.println(d.getId() + " - " + d.getName());
        // }

        loadPatients();
        loadAppointments();

        Scanner scanner = new Scanner(System.in);
        doctorLogin(scanner);
    }

    // ****************** CSV LOADING METHODS ******************

    /** Load doctors from Doctors.csv into the doctors list */
    public static void loadDoctors() {
        String fileName = "data/Doctors.csv";
        File file = new File(fileName);
        // System.out.println("Loading doctors from: " + file.getAbsolutePath());
        // System.out.println("File exists: " + file.exists());

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines or header
                // System.out.println("Line read: " + line);
                if (line.trim().isEmpty() || line.toLowerCase().startsWith("id,")) {
                    continue;
                }

                String[] data = line.split(",", -1); // -1 includes trailing empty strings
                // System.out.println("Split into: " + Arrays.toString(data));
                if (data.length < 7) {
                    continue; // expecting exactly 7 columns
                }
                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                String qualification = data[2].trim();
                String specialization = data[3].trim();
                String contact = data[4].trim();

                int experience;
                try {
                    experience = Integer.parseInt(data[5].trim());
                } catch (NumberFormatException e) {
                    experience = 0;
                }

                String[] availabilityDays = data[6].trim().split("-");

                // Use updated Doctor constructor
                Doctor doctor = new Doctor(id, name, qualification, specialization, contact, experience,
                        availabilityDays);
                doctorTree.insert(doctor);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Doctors' data not found. A new file will be created on save.");
        } catch (IOException e) {
            System.out.println("Error reading Doctors.csv: " + e.getMessage());
        }
    }

    /** Load patients from Patients.csv into the patients list */
    public static void loadPatients() {
        String fileName = "data/Patients.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip header or empty lines
                if (line.trim().isEmpty() || line.toLowerCase().startsWith("patientid")) {
                    continue;
                }

                String[] data = line.split(",", -1); // -1 to include trailing empty fields
                if (data.length < 9) {
                    continue; // Expecting 9 columns
                }

                int patientId = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                int age = Integer.parseInt(data[2].trim());
                String gender = data[3].trim();
                String illness = data[4].trim();
                String emergencyContact = data[5].trim();
                boolean vegetarian = Boolean.parseBoolean(data[6].trim());
                int numberOfVisits = Integer.parseInt(data[7].trim());
                boolean wasAdmittedBefore = Boolean.parseBoolean(data[8].trim());

                Patient patient = new Patient(patientId, name, age, gender, illness, emergencyContact,
                        vegetarian, numberOfVisits, wasAdmittedBefore);

                patientBST.insert(patient);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Patients.csv not found. Patient search may be limited.");
        } catch (IOException e) {
            System.out.println("Error reading Patients.csv: " + e.getMessage());
        }
    }

    /** Load appointments from Appointments.csv into the appointments list */
    public static void loadAppointments() {
        String fileName = "data/Appointments.csv";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip header or empty lines
                if (line.trim().isEmpty() || line.toLowerCase().startsWith("appointmentid")) {
                    continue;
                }

                String[] data = line.split(",", -1); // -1 to avoid skipping empty columns
                if (data.length < 6) // Expecting 6 fields: appointmentId, patientId, patientName, reason, dateTime,
                                     // doctorId
                    continue;

                int appointmentId = Integer.parseInt(data[0]);
                int patientId = Integer.parseInt(data[1]);
                String patientName = data[2];
                String reason = data[3];
                LocalDateTime dateTime = LocalDateTime.parse(data[4], formatter);
                int doctorId = Integer.parseInt(data[5]);

                Appointment appt = new Appointment(appointmentId, patientId, patientName, reason, dateTime, doctorId);
                appointments.add(appt);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Appointments.csv not found. No appointments loaded.");
        } catch (IOException e) {
            System.out.println("Error reading Appointments.csv: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing appointment data: " + e.getMessage());
        }
    }

    // ****************** CSV SAVING METHODS ******************

    /** Save the current list of doctors to Doctors.csv */
    public static void saveDoctors() {
        List<Doctor> doctorList = new ArrayList<>();
        doctorTree.inOrder(doctorList);
        String filename = "data/Doctors.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("id,name,qualification,specialization,contact,experience,availability\n");
            for (Doctor d : doctorList) {
                writer.write(d.getId() + "," + d.getName() + "," + d.getQualification() + "," +
                        d.getSpecialization() + "," + d.getContact() + "," + d.getExperience() + "," +
                        String.join("-", d.getAvailability()) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving doctors: " + e.getMessage());
        }
    }

    // ****************** DOCTOR FUNCTIONS ******************

    /** Doctor login and menu */
    public static void doctorLogin(Scanner scanner) {
        System.out.print("\nEnter Doctor ID: ");
        String docId = scanner.nextLine().trim();
        System.out.println(docId);

        Doctor doctor = findDoctorById(docId); // Make sure the doctor is found first

        if (doctor == null) { // Check for null before proceeding
            System.out.println("Doctor ID not found. Returning to main menu.");
            return; // Exit the method if doctor is not found
        }

        System.out.println(doctor.toString()); // Now safe to call toString()
        System.out.println("Welcome, " + doctor.getName() + "!");

        // Doctor menu loop
        while (true) {
            System.out.println("\n==== Doctor Menu: ====");
            System.out.println("1. View My Appointments");
            System.out.println("2. Search Patient by ID");
            System.out.println("3. Search Patient by Name");
            System.out.println("0. Logout");

            int choice = getIntInput(scanner, "Choose an option: ");
            if (choice == 1) {
                viewAppointments(doctor);
            } else if (choice == 2) {
                searchPatientById(scanner, patientBST);
            } else if (choice == 3) {
                searchPatientByName(scanner, patientBST);
            } else if (choice == 0) {
                System.out.println("Logging out of doctor portal.");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /** Display all appointments for the given doctor */
    public static void viewAppointments(Doctor doctor) {
        boolean found = false;
        System.out.println("\nAppointments for Dr. " + doctor.getName() + ":");
        for (Appointment appt : appointments) {
            if (appt.getDoctorId() == doctor.getId()) {
                Patient pat = findPatientById(appt.getPatientId(), patientBST);
                String patientName = (pat != null) ? pat.getName() : "Unknown Patient";
                System.out.println("Appointment ID: " + appt.getAppointmentId()
                        + ", Patient: " + patientName
                        + ", Date: " + appt.getAppointmentDate());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No appointments found for Dr. " + doctor.getName() + ".");
        }
    }

    /** Search for a patient by ID (doctor function) */
    public static void searchPatientById(Scanner scanner, PatientBST patientBST) {
        System.out.print("Enter Patient ID to search: ");
        int patId = Integer.parseInt(scanner.nextLine().trim());
    
        // Use the searchById method from the PatientBST class
        Patient pat = patientBST.searchById(patId);
    
        if (pat != null) {
            System.out.println("Patient found: " + pat.getName() + " (ID: " + pat.getPatientId() + ")");
            // Optionally display more patient details here
        } else {
            System.out.println("No patient found with ID " + patId + ".");
        }
    }    

    /** Search for patients by name (doctor function) */
    public static void searchPatientByName(Scanner scanner, PatientBST patientBST) {
        System.out.print("Enter Patient Name to search: ");
        String name = scanner.nextLine().trim();
    
        List<Patient> patients = new ArrayList<>();
        patientBST.inOrder(patients);  // Perform in-order traversal to get all patients in sorted order
    
        boolean found = false;
        System.out.println("Search results for name '" + name + "':");
        for (Patient pat : patients) {
            if (pat.getName().equalsIgnoreCase(name)) {
                System.out.println("- " + pat.getName() + " (ID: " + pat.getPatientId() + ")");
                found = true;
            }
        }
    
        if (!found) {
            System.out.println("No patients found with name '" + name + "'.");
        }
    }
    

    /** Helper to find a Doctor object by ID */
    public static Doctor findDoctorById(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            return doctorTree.searchById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Helper to find a Patient object by ID */
    public static Patient findPatientById(int id, PatientBST patientBST) {
        List<Patient> allPatients = new ArrayList<>();
        patientBST.inOrder(allPatients);  // fill the list using in-order traversal
    
        for (Patient p : allPatients) {
            if (p.getPatientId() == id) {
                return p;
            }
        }
        return null;
    }
    

    // ****************** ADMINISTRATOR FUNCTIONS ******************

    /** Administrator menu for managing doctors */
    public static void adminMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nAdministrator Menu:");
        while (true) {
            System.out.println("1. Add Doctor");
            System.out.println("2. Delete Doctor");
            System.out.println("3. Update Doctor");
            System.out.println("4. View All Doctors (sorted by name)");
            System.out.println("5. Search Doctor by ID");
            System.out.println("6. Search Doctor by Name");
            System.out.println("7. Search Doctor by Specialization");
            System.out.println("8. Search Doctor by Experience");
            System.out.println("0. Return to Main Menu");
            int choice = scanner.nextInt();
            // int choice = getIntInput(ch, "Choose an option: ");
            switch (choice) {
                case 1:
                    addDoctor(scanner);
                    break;
                case 2:
                    deleteDoctor(scanner);
                    break;
                case 3:
                    updateDoctor();
                    break;
                case 4:
                    viewDoctors();
                    break;
                case 5:
                    System.out.println("Enter Doctor Id: ");
                    scanner.nextLine();
                    String id = scanner.nextLine();
                    searchDoctorById(id);
                    break;
                case 6:
                    System.out.println("Enter Doctor Name: ");
                    scanner.nextLine();
                    String name = scanner.nextLine();
                    searchDoctorByName(name);
                    break;
                case 7:
                    System.out.println("Enter Doctor Specialization: ");
                    scanner.nextLine();
                    String spe = scanner.nextLine();
                    searchDoctorBySpecialization(spe);
                    break;
                case 8:
                    System.out.println("Enter Doctor Experience: ");
                    int exp = scanner.nextInt();
                    searchDoctorByExperience(exp);
                    break;
                case 0:
                    System.out.println("Returning to main menu.");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /** Add a new doctor and persist to CSV */
    public static void addDoctor(Scanner scanner) {
        scanner.nextLine();
        int id = getIntInput(scanner, "Enter new Doctor ID: "); // Change to int to match constructor
        if (findDoctorById(String.valueOf(id)) != null) { // Assuming findDoctorById accepts a String for id
            System.out.println("Doctor with this ID already exists.");
            return;
        }

        System.out.print("Enter Doctor Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Specialization: ");
        String specialization = scanner.nextLine().trim();

        System.out.print("Enter Contact: ");
        String contact = scanner.nextLine().trim();

        int experience = getIntInput(scanner, "Enter Experience (years): ");

        System.out.print("Enter Availability (e.g., Mon-Wed-Fri): ");
        String availabilityInput = scanner.nextLine().trim();

        // Split the input availability into an array
        String[] availability = availabilityInput.split("-");

        // Create a new Doctor object with the provided data
        Doctor newDoctor = new Doctor(id, name, null, specialization, contact, experience, availability);

        // Assuming doctors is a BST
        doctorTree.insert(newDoctor);

        // Save doctors list to some storage (file, database, etc.)
        saveDoctors();

        System.out.println("Doctor added successfully.");
    }

    /** Delete an existing doctor by ID */
    public static void deleteDoctor(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter Doctor ID to delete: ");
        String id = scanner.nextLine().trim();

        Doctor doc = findDoctorById(id);
        if (doc == null) {
            System.out.println("No doctor found with ID " + id + ".");
            return;
        }

        // Parse ID again to get int
        int parsedId = Integer.parseInt(id); // safe because findDoctorById didn't return null

        doctorTree.delete(parsedId); // now it's an int, matches method signature
        saveDoctors();
        System.out.println("Doctor with ID " + parsedId + " deleted.");
    }

    /** Update an existing doctor's details */
    public static void updateDoctor() {
        Scanner scanner =  new Scanner(System.in);
        System.out.print("Enter Doctor ID to update: ");
        String id = scanner.nextLine().trim();
        Doctor doc = findDoctorById(id);
        if (doc == null) {
            System.out.println("No doctor found with ID " + id + ".");
            return;
        }
        System.out.println("Updating details for Dr. " + doc.getName() + ". Press Enter to keep current value.");
        System.out.print("Enter new name (current: " + doc.getName() + "): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty())
            doc.setName(name);
        System.out.print("Enter new specialization (current: " + doc.getSpecialization() + "): ");
        String specialization = scanner.nextLine().trim();
        if (!specialization.isEmpty())
            doc.setSpecialization(specialization);
        System.out.print("Enter new contact (current: " + doc.getContact() + "): ");
        String contact = scanner.nextLine().trim();
        if (!contact.isEmpty())
            doc.setContact(contact);
        System.out.print("Enter new experience (current: " + doc.getExperience() + "): ");
        String expInput = scanner.nextLine().trim();
        if (!expInput.isEmpty()) {
            try {
                int exp = Integer.parseInt(expInput);
                doc.setExperience(exp);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Experience not changed.");
            }
        }
        saveDoctors();
        System.out.println("Doctor details updated.");
    }

    /** View all doctors, sorted by name */
    public static void viewDoctors() {
        List<Doctor> doctorList = new ArrayList<>();
        doctorTree.inOrder(doctorList); // In-order = sorted by ID

        if (doctorList.isEmpty()) {
            System.out.println("No doctor records found.");
            return;
        }

        System.out.printf("%-5s %-20s %-15s %-20s %-15s %-10s %-20s%n",
                "ID", "Name", "Qualification", "Specialization", "Contact", "Exp", "Availability");
        System.out
                .println("------------------------------------------------------------------------------------------");

        for (Doctor doc : doctorList) {
            System.out.printf("%-5d %-20s %-15s %-20s %-15s %-10d %-20s%n",
                    doc.getId(), doc.getName(), doc.getQualification(), doc.getSpecialization(),
                    doc.getContact(), doc.getExperience(), String.join("-", doc.getAvailability()));
        }
    }

    /** Search and display a doctor by ID */
    public static void searchDoctorById(String id) {
        List<Doctor> allDoctors = new ArrayList<>();
        doctorTree.inOrder(allDoctors);
        int inputId = Integer.parseInt(id); // wrap in try-catch in real usage

        for (Doctor d : allDoctors) {
            if (d.getId() == inputId) {
                displayDoctors(Collections.singletonList(d));
                return;
            }
        }

        System.out.println("No doctor found with ID: " + id);
    }

    /** Search and display doctors by name */
    public static void searchDoctorByName(String name) {
        List<Doctor> matched = new ArrayList<>();
        List<Doctor> allDoctors = new ArrayList<>();
        doctorTree.inOrder(allDoctors); // populate from BST

        for (Doctor d : allDoctors) {
            if (d.getName().toLowerCase().contains(name.toLowerCase())) {
                matched.add(d);
            }
        }

        if (matched.isEmpty()) {
            System.out.println("No doctors found with name containing: " + name);
        } else {
            displayDoctors(matched);
        }
    }

    /** Search and display doctors by specialization */
    public static void searchDoctorBySpecialization(String specialization) {
        List<Doctor> matched = new ArrayList<>();
        List<Doctor> allDoctors = new ArrayList<>();
        doctorTree.inOrder(allDoctors);

        for (Doctor d : allDoctors) {
            if (d.getSpecialization().toLowerCase().contains(specialization.toLowerCase())) {
                matched.add(d);
            }
        }

        if (matched.isEmpty()) {
            System.out.println("No doctors found with specialization: " + specialization);
        } else {
            displayDoctors(matched);
        }
    }

    // /** Search and display doctors by contact */
    // private static void searchDoctorByContact(String contact) {
    // List<Doctor> matched = new ArrayList<>();
    // List<Doctor> allDoctors = new ArrayList<>();
    // doctorTree.inOrder(allDoctors);

    // for (Doctor d : allDoctors) {
    // if (d.getContact().equalsIgnoreCase(contact)) {
    // matched.add(d);
    // }
    // }

    // if (matched.isEmpty()) {
    // System.out.println("No doctors found with contact: " + contact);
    // } else {
    // displayDoctors(matched);
    // }
    // }

    /** Search and display doctors by experience */
    public static void searchDoctorByExperience(int exp) {
        List<Doctor> matched = new ArrayList<>();
        List<Doctor> allDoctors = new ArrayList<>();
        doctorTree.inOrder(allDoctors);

        for (Doctor d : allDoctors) {
            if (d.getExperience() == exp) {
                matched.add(d);
            }
        }

        if (matched.isEmpty()) {
            System.out.println("No doctors found with " + exp + " years of experience.");
        } else {
            displayDoctors(matched);
        }
    }

    public static void displayDoctors(List<Doctor> doctors) {
        if (doctors == null || doctors.isEmpty()) {
            System.out.println("No doctor records to display.");
            return;
        }

        System.out.printf("%-10s %-20s %-20s %-15s %-10s%n", "ID", "Name", "Specialization", "Contact", "Experience");
        System.out
                .println("------------------------------------------------------------------------------------------");

        for (Doctor d : doctors) {
            System.out.printf("%-10s %-20s %-20s %-15s %-10d%n",
                    d.getId(), d.getName(), d.getSpecialization(), d.getContact(), d.getExperience());
        }
    }

    // ****************** INPUT VALIDATION HELPERS ******************

    /** Utility method to safely parse an integer from input */
    public static int getIntInput(Scanner scanner, String prompt) {
        int num;
        while (true) {
            try {
                if (!prompt.isEmpty()) {
                    System.out.print(prompt);
                }
                String line = scanner.nextLine().trim();
                num = Integer.parseInt(line);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid integer.");
            }
        }
        return num;
    }
}