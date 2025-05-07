package HospitalManagement.model;

import java.util.Arrays;

public class Doctor {
    private int id;
    private String name;
    private String qualification;
    private String specialization;
    private String contact;
    private int experience;
    private String[] availability; // Days like {"Mon", "Wed", "Fri"}

    public Doctor(int id, String name, String qualification, String specialization, String contact, int experience, String[] availability) {
        this.id = id;
        this.name = name;
        this.qualification = qualification;
        this.specialization = specialization;
        this.contact = contact;
        this.experience = experience;
        this.availability = availability;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String[] getAvailability() { return availability; }
    public void setAvailability(String[] availability) { this.availability = availability; }

    public boolean isAvailableOn(String day) {
        if (availability == null || day == null) return false;
        for (String availableDay : availability) {
            if (availableDay.equalsIgnoreCase(day)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Doctor ID: " + id +
               ", Name: " + name +
               ", Qualification: " + qualification +
               ", Specialization: " + specialization +
               ", Contact: " + contact +
               ", Experience: " + experience + " years" +
               ", Available Days: " + Arrays.toString(availability);
    }
}
