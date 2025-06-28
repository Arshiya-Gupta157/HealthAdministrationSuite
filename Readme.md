Instructions for running the project:
1) First move to the directory where HospitalManagement folder is stored. You can do this by using 'cd ../' (But mke sure you are in HospitalManagement directory).
2) then implement the following command to compile all the files present in the HospitalManagement directory
javac -d . HospitalManagement/model/*.java HospitalManagement/service/*.java HospitalManagement/Main.java
3) Then execute command 'cd HospitalManagement' in the terminal. Make sure you enter the HospitalManagement directory after executing the command.
4) Then execute 'java Main.java' command in terminal to run project.

Structure of Project:
HOSPITALMANAGEMENT/
│
├── data/
│   ├── Appointments.csv
│   ├── Doctors.csv
│   ├── Menu.csv
│   ├── Patients.csv
│   └── Pharmacy.csv
|
├── model/
│   ├── CanteenItem.java
│   ├── Patient.java
│   ├── Appointment.java
│   ├── Doctor.java
│   └── Pharmacy.java
│
├── service/
│   ├── AdminServices.java
│   ├── CanteenService.java
│   ├── DoctorService.java
│   ├── PharmacyServices.java
│   └── PatientService.java
│
└── Main.java