package HospitalManagement.model;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private static int idCounter = 100;
    private int patientId;
    private String name;
    private int age;
    private String gender;
    private String illness;
    private String emergencyContact;
    private boolean vegetarian;
    private int numberOfVisits;
    private boolean wasAdmittedBefore;

    public Patient(int patientId, String name, int age, String gender, String illness, String emergencyContact,
                   boolean vegetarian, int numberOfVisits, boolean wasAdmittedBefore) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.illness = illness;
        this.emergencyContact = emergencyContact;
        this.vegetarian = vegetarian;
        this.numberOfVisits = numberOfVisits;
        this.wasAdmittedBefore = wasAdmittedBefore;
        idCounter = Math.max(idCounter, patientId); // update counter
    }

    public int getPatientId() { return patientId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getIllness() { return illness; }
    public String getEmergencyContact() { return emergencyContact; }
    public boolean isVegetarian() { return vegetarian; }
    public int getNumberOfVisits() { return numberOfVisits; }
    public boolean isWasAdmittedBefore() { return wasAdmittedBefore; }
    public void setEmergencyContact(String emergencyContact){
        this.emergencyContact = emergencyContact;
    }
        
    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }
    

    @Override
    public String toString() {
        return "Patient ID: " + patientId + ", Name: " + name + ", Illness: " + illness;
    }
}