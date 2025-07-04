package HospitalManagement;

import HospitalManagement.model.*;
import HospitalManagement.service.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PatientService patientService = new PatientService();
        CanteenService canteenService = new CanteenService();
        PharmacyServices pharmacyServices = new PharmacyServices();

        int choice;
        do {
            System.out.println("\n==== HEALTHCARE ADMINISTRATION SUITE MENU ====");
            System.out.println("1. Administrator");
            System.out.println("2. Doctor");
            System.out.println("3. Patient");
            System.out.println("4. Canteen");
            System.out.println("5. Pharmacy");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    AdminServices adminServices = new AdminServices();
                    adminServices.adminMenuManagement();
                    break;

                case 2:
                    DoctorService doctorService = new DoctorService();
                    doctorService.manageStaff();
                    break;
                case 3:
                    patientService.managePatients();
                    break;
                case 4:
                    canteenService.manageCanteen();
                    break;
                case 5:
                    pharmacyServices.customerMenu();
                    break;
                case 0:
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
