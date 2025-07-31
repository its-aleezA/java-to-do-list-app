package todolist;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ToDoListSystem {
    private List<Tasks> taskList;
    
    public ToDoListSystem() {
        taskList = new ArrayList<>();
    }
    
    public void addTask(Tasks t) {
        taskList.add(t);
        updateTaskStatuses();
    }
    
    public boolean deleteTask(Tasks t) {
        return taskList.remove(t);
    }
    
    public void toggleTaskCompletion(Tasks t) {
        t.setCompleted(!t.isCompleted());
        updateTaskStatuses();
    }
    
    public List<Tasks> getAllTasks() {
        return new ArrayList<>(taskList);
    }
    
    public List<Tasks> getTasksByStatus(Tasks.Status status) {
        List<Tasks> result = new ArrayList<>();
        for (Tasks t : taskList) {
            if (t.getStatus() == status) {
                result.add(t);
            }
        }
        return result;
    }
    
    public void updateTaskStatuses() {
        LocalDate currentDate = LocalDate.now();
        for (Tasks t : taskList) {
            t.updateStatus(currentDate);
        }
    }
    
    // Other methods remain similar but updated for LocalDate
    public void editName(Tasks t, String n) {
        t.setName(n);
        updateTaskStatuses();
    }
    
    public void editCourse(Tasks t, String c) {
        t.setCourse(c);
    }
    
    public void editInfo(Tasks t, String i) {
        t.setInfo(i);
    }
    
    public void editDueDate(Tasks t, LocalDate d) {
        t.setDueDate(d);
        updateTaskStatuses();
    }
}