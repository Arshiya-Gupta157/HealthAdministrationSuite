package HospitalManagement.service;

import HospitalManagement.service.*;
import HospitalManagement.model.*;

import java.util.Scanner;

public class AdminServices {

    private final DoctorService doctorService = new DoctorService();
    private final PatientService patientService = new PatientService();
    private final CanteenService canteenService = new CanteenService();

    public AdminServices() {

    }

    public void adminMenuManagement() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Admin Menu =====");
            System.out.println("1. Manage Doctors");
            System.out.println("2. Manage Patients");
            System.out.println("3. Manage Appointments");
            System.out.println("4. Manage Canteen");
            System.out.println("0. Back");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // Load data from CSV files on startup
                    doctorService.loadDoctors();
                    doctorService.loadPatients();
                    doctorService.loadAppointments();
                    doctorService.adminMenu();
                    break;
                case 2:
                    patientService.loadPatients();
                    patientService.loadAppointments();
                    patientService.manageAsAdminPatients();
                    break;
                case 3:
                    // appointmentService.manageAppointments();
                    break;
                case 4:
                    canteenService.manageCanteen();
                    break;
                case 0:
                    System.out.println("Returning to previous menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 0);
    }
}
