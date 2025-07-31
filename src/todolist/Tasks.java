package todolist;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Tasks {
    public enum Status {
        PENDING, LATE, FINISHED
    }
    
    private String name;
    private String course;
    private String info;
    private LocalDate dueDate;
    private Status status;
    private boolean completed;
    
    public Tasks(String n, String i, String c, LocalDate d) {
        name = n;
        info = i;
        course = c;
        dueDate = d;
        status = Status.PENDING;
        completed = false;
    }
    
    // Update status based on current date
    public void updateStatus(LocalDate currentDate) {
        if (completed) {
            status = Status.FINISHED;
        } else if (currentDate.isAfter(dueDate)) {
            status = Status.LATE;
        } else {
            status = Status.PENDING;
        }
    }
    
    // Setters and getters
    public void setName(String n) { name = n; }
    public void setInfo(String i) { info = i; }
    public void setCourse(String c) { course = c; }
    public void setDueDate(LocalDate d) { dueDate = d; }
    public void setCompleted(boolean c) { 
        completed = c; 
        if (completed) {
            status = Status.FINISHED;
        }
    }
    
    public String getName() { return name; }
    public String getInfo() { return info; }
    public String getCourse() { return course; }
    public LocalDate getDueDate() { return dueDate; }
    public String getFormattedDate() {
        return dueDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }
    public Status getStatus() { return status; }
    public boolean isCompleted() { return completed; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Tasks other = (Tasks) obj;
        return dueDate.equals(other.dueDate) &&
               name.equals(other.name) &&
               course.equals(other.course) &&
               info.equals(other.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, course, info, dueDate);
    }
    
    @Override
    public String toString() {
        return name + " (" + course + ") - Due: " + getFormattedDate() + " - " + status;
    }
}