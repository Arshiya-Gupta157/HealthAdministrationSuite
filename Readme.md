Command to compile the code:
javac -d . HospitalManagement/model/*.java HospitalManagement/service/*.java HospitalManagement/Main.java

Structure of Project:
HOSPITALMANAGEMENT/
│
├── data/
│   ├── Appointments.csv
│   ├── Doctors.csv
│   ├── Menu.csv
│   └── Patients.csv
|
├── model/
│   ├── CanteenItem.java
│   ├── Patient.java
│   ├── Pharmacy.java
│   ├── Appointment.java
│   ├── Room.java
│   ├── Staff.java
│   └── Vehicle.java
│
├── service/
│   ├── CanteenService.java
│   ├── DoctorService.java
│   ├── HospitalService.java
│   └── PatientService.java
│
└── Main.java