package com.taskmanagement.main;

import com.taskmanagement.model.Priority;
import com.taskmanagement.model.Status;
import com.taskmanagement.model.Task;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.service.TaskService;
import com.taskmanagement.util.InputUtil;

import java.util.List;

/**
 * Entry point and console menu (UI layer).
 * Delegates business logic to TaskService and input to InputUtil.
 */
public class Main {
    private static final String JSON_FILE = "tasks.json";
    private final TaskService taskService;

    public Main(TaskService taskService) {
        this.taskService = taskService;
    }

    public static void main(String[] args) {
        TaskRepository repository = new TaskRepository();
        TaskService service = new TaskService(repository);
        Main app = new Main(service);
        app.run();
    }

    private void run() {
        while (true) {
            printMenu();
            int choice = InputUtil.readInt("Enter your choice (1-12): ", 1, 12);
            boolean exit = handleChoice(choice);
            if (exit) break;
        }
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n===== TASK MANAGEMENT SYSTEM =====");
        System.out.println("1.  Add Task");
        System.out.println("2.  Show All Tasks");
        System.out.println("3.  Update Task Status");
        System.out.println("4.  Update Task Priority");
        System.out.println("5.  Delete Task");
        System.out.println("6.  Search Task");
        System.out.println("7.  Sort by Priority");
        System.out.println("8.  Sort by Due Date");
        System.out.println("9.  Filter by Status");
        System.out.println("10. Save Tasks");
        System.out.println("11. Load Tasks");
        System.out.println("12. Exit");
        System.out.println("=================================\n");
    }

    private boolean handleChoice(int choice) {
        switch (choice) {
            case 1:  addTask();       break;
            case 2:  showAllTasks();  break;
            case 3:  updateStatus();  break;
            case 4:  updatePriority(); break;
            case 5:  deleteTask();    break;
            case 6:  searchTask();    break;
            case 7:  sortByPriority(); break;
            case 8:  sortByDueDate(); break;
            case 9:  filterByStatus(); break;
            case 10: saveTasks();     break;
            case 11: loadTasks();     break;
            case 12: return true;
            default: break;
        }
        return false;
    }

    private void addTask() {
        String title = InputUtil.readLine("Enter task title: ", true);
        String description = InputUtil.readLine("Enter description (optional): ", false);
        System.out.println("Select priority:");
        Priority priority = InputUtil.readPriority("Choice (1-3): ");
        System.out.println("Enter due date (yyyy-MM-dd) or leave blank:");
        var dueDate = InputUtil.readDate("Due date: ");
        var task = taskService.createTask(title, description, priority, dueDate);
        if (task.isPresent()) {
            System.out.println("Task added: " + task.get());
        } else {
            System.out.println("Failed to add task (title required).");
        }
        InputUtil.pressEnterToContinue();
    }

    private void showAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        printTaskList(tasks, "All Tasks");
        InputUtil.pressEnterToContinue();
    }

    private void updateStatus() {
        String id = InputUtil.readLine("Enter task ID: ", true);
        if (taskService.findById(id).isEmpty()) {
            System.out.println("Task not found.");
            InputUtil.pressEnterToContinue();
            return;
        }
        System.out.println("Select new status:");
        Status status = InputUtil.readStatus("Choice (1-3): ");
        boolean ok = taskService.updateStatus(id, status);
        System.out.println(ok ? "Status updated." : "Update failed.");
        InputUtil.pressEnterToContinue();
    }

    private void updatePriority() {
        String id = InputUtil.readLine("Enter task ID: ", true);
        if (taskService.findById(id).isEmpty()) {
            System.out.println("Task not found.");
            InputUtil.pressEnterToContinue();
            return;
        }
        System.out.println("Select new priority:");
        Priority priority = InputUtil.readPriority("Choice (1-3): ");
        boolean ok = taskService.updatePriority(id, priority);
        System.out.println(ok ? "Priority updated." : "Update failed.");
        InputUtil.pressEnterToContinue();
    }

    private void deleteTask() {
        String id = InputUtil.readLine("Enter task ID to delete: ", true);
        boolean ok = taskService.deleteTask(id);
        System.out.println(ok ? "Task deleted." : "Task not found.");
        InputUtil.pressEnterToContinue();
    }

    private void searchTask() {
        String keyword = InputUtil.readLine("Enter search keyword: ", false);
        List<Task> tasks = taskService.searchByKeyword(keyword);
        printTaskList(tasks, "Search results for: " + keyword);
        InputUtil.pressEnterToContinue();
    }

    private void sortByPriority() {
        List<Task> tasks = taskService.getSortedByPriority();
        printTaskList(tasks, "Tasks sorted by Priority (HIGH first)");
        InputUtil.pressEnterToContinue();
    }

    private void sortByDueDate() {
        List<Task> tasks = taskService.getSortedByDueDate();
        printTaskList(tasks, "Tasks sorted by Due Date");
        InputUtil.pressEnterToContinue();
    }

    private void filterByStatus() {
        System.out.println("Filter by status:");
        Status status = InputUtil.readStatus("Choice (1-3): ");
        List<Task> tasks = taskService.getFilteredByStatus(status);
        printTaskList(tasks, "Tasks with status: " + status);
        InputUtil.pressEnterToContinue();
    }

    private void saveTasks() {
        boolean ok = taskService.saveToFile(JSON_FILE);
        System.out.println(ok ? "Tasks saved to " + JSON_FILE : "Save failed.");
        InputUtil.pressEnterToContinue();
    }

    private void loadTasks() {
        taskService.loadFromFile(JSON_FILE);
        System.out.println("Tasks loaded from " + JSON_FILE + ". Total: " + taskService.getAllTasks().size());
        InputUtil.pressEnterToContinue();
    }

    private void printTaskList(List<Task> tasks, String header) {
        System.out.println("\n--- " + header + " ---");
        if (tasks == null || tasks.isEmpty()) {
            System.out.println("(No tasks)");
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }
}
