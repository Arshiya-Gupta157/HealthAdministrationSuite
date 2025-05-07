package HospitalManagement.model;
import java.time.LocalDateTime;
public class Appointment {
    private static int idCounter = 0; // This will be used to generate unique IDs
    private int appointmentId;
    private int doctorId;
    private String patientName;
    private LocalDateTime dateTime;
    private String reason;
    private int patientId;

    public Appointment(int appointmentId, int patientId, String patientName, String reason, LocalDateTime dateTime, int doctorId){
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.patientName = patientName;
        this.reason = reason;
        this.dateTime = dateTime;
        this.doctorId = doctorId;

        // Update idCounter if needed
        if (appointmentId > idCounter) {
            idCounter = appointmentId;
        }
    }

    // Method to generate the next unique appointment ID
    public static int getNextAppointmentId() {
        return ++idCounter; // Increment idCounter for the next unique ID
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getPatientId() {
        return patientId;
    }

    public LocalDateTime getAppointmentDate() {
        return this.dateTime;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public String getPatientName() {
        return patientName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setAppointmentDate(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "AppointmentID=" + appointmentId +
                ", Patient='" + patientName + '\'' +
                ", Reason='" + reason + '\'' +
                ", DateTime=" + dateTime +
                ", DoctorID=" + doctorId +
                '}';
    }
}
