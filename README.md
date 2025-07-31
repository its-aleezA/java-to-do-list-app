# ✅ Java To-Do List with Smart Deadline Tracking

A feature-rich desktop to-do list application built with Java and Eclipse SWT, featuring automatic deadline status detection and interactive task management.

---

## 📌 Features

- Task management with name, course, details, and deadline
- Automatic status detection (Pending/Late/Finished)
- Interactive checkbox completion system
- Deadline-aware color coding (red for late tasks)
- Strikethrough visualization for completed tasks
- Full CRUD operations (Create, Read, Update, Delete)
- Responsive SWT GUI with native look-and-feel

---

## 🏗 Technologies Used

- Java 8+
- Eclipse SWT for native GUI components
- `LocalDate` for deadline tracking
- Custom table painting for visual effects
- MVC architecture pattern
- Thread-safe collections for task management

---

## 📁 Project Structure

| File                   | Description                                |
|------------------------|--------------------------------------------|
| `Tasks.java`           | Task entity with status logic              |
| `ToDoListSystem.java`  | Core task management system                |
| `ToDoListGUI.java`     | Main GUI application class                 |
| `.gitignore`           | Standard Java/Eclipse ignores              |

---

## 🖥️ Graphical Interface

The SWT GUI provides:

- Interactive task table with checkboxes
- Automatic status coloring:
  - 🟢 Finished tasks (green)
  - 🔴 Late tasks (red)
  - ⚪ Pending tasks (default)
- Form for adding/editing tasks
- Real-time deadline tracking
- Strikethrough visualization for completed items

---

### 🚀 How to Run

1. Import into Eclipse with SWT support
2. Run `ToDoListGUI.java` as Java Application
3. Or compile with:
   ```bash
   javac -cp swt.jar todolist/*.java
   java -cp .:swt.jar todolist.ToDoListGUI
   ```

---

### 🤝 Collaborators

Developed by [Aleeza Rizwan](https://github.com/its-aleezA) demonstrating:
- Desktop GUI development with SWT
- Date-aware application logic
- Custom widget painting
- Task management patterns

---

### ✅ License
This project is under the **MIT License**
See [LICENSE](LICENSE) for details.
