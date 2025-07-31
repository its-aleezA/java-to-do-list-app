package todolist;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import org.eclipse.swt.graphics.Point;

public class ToDoListGUI {
    protected Shell shell;
    private ToDoListSystem system;
    private Table table;
    private Text nameText, courseText, infoText, dateText;
    private Button checkButton;
    
    public static void main(String[] args) {
            try {
                ToDoListGUI window = new ToDoListGUI();
                window.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public void open() {
        Display display = Display.getDefault();
        system = new ToDoListSystem();
        createContents();
        shell.open();
        shell.layout();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    protected void createContents() {
    	shell = new Shell();
        shell.setSize(900, 600);
        shell.setText("Enhanced To-Do List");
        shell.setLayout(new GridLayout(2, false));

        // Left panel - Input fields
        Composite inputComposite = new Composite(shell, SWT.NONE);
        inputComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        inputComposite.setLayout(new GridLayout(2, false));

        // Task Name
        Label nameLabel = new Label(inputComposite, SWT.NONE);
        nameLabel.setText("Task Name:");
        nameText = new Text(inputComposite, SWT.BORDER);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // Course
        Label courseLabel = new Label(inputComposite, SWT.NONE);
        courseLabel.setText("Course:");
        courseText = new Text(inputComposite, SWT.BORDER);
        courseText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // Info
        Label infoLabel = new Label(inputComposite, SWT.NONE);
        infoLabel.setText("Details:");
        infoText = new Text(inputComposite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        GridData infoGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        infoGridData.heightHint = 100;
        infoText.setLayoutData(infoGridData);

        // Date
        Label dateLabel = new Label(inputComposite, SWT.NONE);
        dateLabel.setText("Due Date (YYYY-MM-DD):");
        dateText = new Text(inputComposite, SWT.BORDER);
        dateText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        dateText.setText(LocalDate.now().toString()); // Default to today's date

        // Checkbox for completion
        checkButton = new Button(inputComposite, SWT.CHECK);
        checkButton.setText("Completed");
        checkButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

        // Buttons
        Composite buttonComposite = new Composite(inputComposite, SWT.NONE);
        buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        buttonComposite.setLayout(new GridLayout(3, true));

        Button addButton = new Button(buttonComposite, SWT.PUSH);
        addButton.setText("Add Task");
        addButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        addButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addTask();
            }
        });

        Button updateButton = new Button(buttonComposite, SWT.PUSH);
        updateButton.setText("Update Task");
        updateButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        updateButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                updateTask();
            }
        });

        Button deleteButton = new Button(buttonComposite, SWT.PUSH);
        deleteButton.setText("Delete Task");
        deleteButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        deleteButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                deleteTask();
            }
        });

        // Right panel - Task list
        Composite listComposite = new Composite(shell, SWT.NONE);
        listComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        listComposite.setLayout(new GridLayout(1, false));

        table = new Table(listComposite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.CHECK);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        // Create columns
        TableColumn checkColumn = new TableColumn(table, SWT.CENTER);
        checkColumn.setText("");
        checkColumn.setWidth(30);

        String[] columns = {"Task Name", "Course", "Details", "Due Date", "Status"};
        for (String col : columns) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(col);
            column.setWidth(col.equals("Details") ? 200 : 120);
        }

        // Refresh button
        Button refreshButton = new Button(listComposite, SWT.PUSH);
        refreshButton.setText("Refresh List");
        refreshButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        refreshButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                refreshTaskList();
            }
        });

        // Add selection listener to table
        table.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                loadSelectedTask();
            }
        });

        // Add checkbox listener
        table.addListener(SWT.Selection, event -> {
            if (event.detail == SWT.CHECK) {
                TableItem item = (TableItem) event.item;
                Tasks task = (Tasks) item.getData();
                system.toggleTaskCompletion(task);
                refreshTaskList(); // Refresh to update visual feedback
            }
        });
        
        table.addListener(SWT.EraseItem, event -> {
            if (event.index == 0) return; // Skip checkbox column
            
            Tasks task = (Tasks) event.item.getData();
            if (task != null && task.isCompleted()) {
                event.detail &= ~SWT.HOT; // Clear HOT to prevent selection highlight
            }
        });

        table.addListener(SWT.PaintItem, event -> {
            Tasks task = (Tasks) event.item.getData();
            if (task != null && task.isCompleted() && event.index > 0) {
                Rectangle bounds = event.getBounds();
                GC gc = event.gc;
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                int midY = bounds.y + bounds.height / 2;
                gc.drawLine(bounds.x, midY, bounds.x + bounds.width, midY);
            }
        });
        
        // Add dispose listener to clean up resources
        shell.addDisposeListener(e -> {
            // Other disposal code if needed
        });

        // Initial refresh
        refreshTaskList();
    }

    private void addTask() {
        try {
            String name = nameText.getText();
            String course = courseText.getText();
            String info = infoText.getText();
            LocalDate date = LocalDate.parse(dateText.getText());
            boolean completed = checkButton.getSelection();

            Tasks task = new Tasks(name, info, course, date);
            task.setCompleted(completed);
            system.addTask(task);
            refreshTaskList();
            clearFields();
        } catch (DateTimeParseException e) {
            showError("Invalid date format", "Please enter date in YYYY-MM-DD format");
        }
    }

    private void updateTask() {
        int selectionIndex = table.getSelectionIndex();
        if (selectionIndex >= 0) {
            try {
                Tasks selectedTask = (Tasks) table.getItem(selectionIndex).getData();
                selectedTask.setName(nameText.getText());
                selectedTask.setCourse(courseText.getText());
                selectedTask.setInfo(infoText.getText());
                selectedTask.setDueDate(LocalDate.parse(dateText.getText()));
                selectedTask.setCompleted(checkButton.getSelection());
                system.updateTaskStatuses();
                refreshTaskList();
            } catch (DateTimeParseException e) {
                showError("Invalid date format", "Please enter date in YYYY-MM-DD format");
            }
        } else {
            showWarning("No task selected", "Please select a task to update");
        }
    }

    private void deleteTask() {
        int selectionIndex = table.getSelectionIndex();
        if (selectionIndex >= 0) {
            Tasks selectedTask = (Tasks) table.getItem(selectionIndex).getData();
            system.deleteTask(selectedTask);
            refreshTaskList();
            clearFields();
        } else {
            showWarning("No task selected", "Please select a task to delete");
        }
    }

    private void refreshTaskList() {
        table.removeAll();
        system.updateTaskStatuses();
        
        for (Tasks task : system.getAllTasks()) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setChecked(task.isCompleted());
            item.setText(1, task.getName());
            item.setText(2, task.getCourse());
            item.setText(3, task.getInfo());
            item.setText(4, task.getFormattedDate());
            item.setText(5, task.getStatus().toString());
            item.setData(task);
            
            if (task.isCompleted()) {
                for (int i = 1; i <= 5; i++) {
                    item.setForeground(i, Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
                }
            }
            
            if (task.getStatus() == Tasks.Status.LATE && !task.isCompleted()) {
                item.setBackground(5, Display.getCurrent().getSystemColor(SWT.COLOR_RED));
            } else if (task.getStatus() == Tasks.Status.FINISHED) {
                item.setBackground(5, Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
            }
        }
        
        for (TableColumn column : table.getColumns()) {
            column.pack();
        }
    }

    private void loadSelectedTask() {
        int selectionIndex = table.getSelectionIndex();
        if (selectionIndex >= 0) {
            Tasks selectedTask = (Tasks) table.getItem(selectionIndex).getData();
            nameText.setText(selectedTask.getName());
            courseText.setText(selectedTask.getCourse());
            infoText.setText(selectedTask.getInfo());
            dateText.setText(selectedTask.getDueDate().toString());
            checkButton.setSelection(selectedTask.isCompleted());
        }
    }

    private void clearFields() {
        nameText.setText("");
        courseText.setText("");
        infoText.setText("");
        dateText.setText(LocalDate.now().toString());
        checkButton.setSelection(false);
    }

    private void showError(String title, String message) {
        MessageBox mb = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
        mb.setText(title);
        mb.setMessage(message);
        mb.open();
    }

    private void showWarning(String title, String message) {
        MessageBox mb = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
        mb.setText(title);
        mb.setMessage(message);
        mb.open();
    }
}