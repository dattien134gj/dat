package com.taskmanagement.service;

import com.taskmanagement.model.Priority;
import com.taskmanagement.model.Status;
import com.taskmanagement.model.Task;
import com.taskmanagement.repository.TaskRepository;
import com.taskmanagement.util.FileUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service layer: contains business logic and coordinates repository and file util.
 * Separates UI (Main) from data access (Repository) and file I/O (FileUtil).
 */
public class TaskService {
    private final TaskRepository repository;
    private static final String DEFAULT_JSON_FILE = "tasks.json";

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Create a new task with validation (title required).
     */
    public Optional<Task> createTask(String title, String description, Priority priority, LocalDate dueDate) {
        if (title == null || title.trim().isEmpty()) {
            return Optional.empty();
        }
        Task task = new Task(title.trim(), description != null ? description.trim() : "", priority, dueDate);
        repository.add(task);
        return Optional.of(task);
    }

    /**
     * Get all tasks.
     */
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    /**
     * Update task status by ID.
     */
    public boolean updateStatus(String id, Status status) {
        Optional<Task> opt = repository.findById(id);
        if (opt.isEmpty() || status == null) return false;
        opt.get().setStatus(status);
        return true;
    }

    /**
     * Update task priority by ID.
     */
    public boolean updatePriority(String id, Priority priority) {
        Optional<Task> opt = repository.findById(id);
        if (opt.isEmpty() || priority == null) return false;
        opt.get().setPriority(priority);
        return true;
    }

    /**
     * Delete task by ID.
     */
    public boolean deleteTask(String id) {
        return repository.removeById(id);
    }

    /**
     * Search tasks by keyword in title/description.
     */
    public List<Task> searchByKeyword(String keyword) {
        return repository.searchByKeyword(keyword);
    }

    /**
     * Get tasks sorted by priority (HIGH first).
     */
    public List<Task> getSortedByPriority() {
        return repository.findAllSortedByPriority();
    }

    /**
     * Get tasks sorted by due date.
     */
    public List<Task> getSortedByDueDate() {
        return repository.findAllSortedByDueDate();
    }

    /**
     * Get tasks filtered by status.
     */
    public List<Task> getFilteredByStatus(Status status) {
        return repository.findByStatus(status);
    }

    /**
     * Save all tasks to JSON file.
     */
    public boolean saveToFile(String filePath) {
        return FileUtil.saveToJson(repository.findAll(), filePath != null ? filePath : DEFAULT_JSON_FILE);
    }

    /**
     * Load tasks from JSON file and replace current in-memory list.
     */
    public boolean loadFromFile(String filePath) {
        String path = filePath != null ? filePath : DEFAULT_JSON_FILE;
        List<Task> loaded = FileUtil.loadFromJson(path);
        repository.setAll(loaded);
        return true;
    }

    /**
     * Find task by ID (for UI to show details or validate).
     */
    public Optional<Task> findById(String id) {
        return repository.findById(id);
    }
}
