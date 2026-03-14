package com.taskmanagement.repository;

import com.taskmanagement.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository layer: handles in-memory storage of tasks using ArrayList.
 * Abstracts data access from the service layer.
 */
public class TaskRepository {
    private final List<Task> tasks = new ArrayList<>();

    /**
     * Add a new task to the repository.
     */
    public void add(Task task) {
        if (task != null) {
            tasks.add(task);
        }
    }

    /**
     * Get all tasks (returns a copy to avoid external modification).
     */
    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    /**
     * Find task by ID using Optional for safe handling.
     */
    public Optional<Task> findById(String id) {
        return tasks.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    /**
     * Remove task by ID. Returns true if removed.
     */
    public boolean removeById(String id) {
        return tasks.removeIf(t -> t.getId().equals(id));
    }

    /**
     * Replace current in-memory list with loaded tasks (e.g. from JSON).
     */
    public void setAll(List<Task> taskList) {
        tasks.clear();
        if (taskList != null) {
            tasks.addAll(taskList);
        }
    }

    /**
     * Get current number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Search tasks by keyword in title or description (case-insensitive).
     */
    public List<Task> searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findAll();
        }
        String lower = keyword.trim().toLowerCase();
        return tasks.stream()
                .filter(t -> (t.getTitle() != null && t.getTitle().toLowerCase().contains(lower))
                        || (t.getDescription() != null && t.getDescription().toLowerCase().contains(lower)))
                .collect(Collectors.toList());
    }

    /**
     * Return a new list sorted by priority (HIGH first). Uses Collections and Comparator.
     */
    public List<Task> findAllSortedByPriority() {
        List<Task> copy = findAll();
        Collections.sort(copy, (a, b) -> b.getPriority().ordinal() - a.getPriority().ordinal());
        return copy;
    }

    /**
     * Return a new list sorted by due date (earliest first). Null due dates last.
     */
    public List<Task> findAllSortedByDueDate() {
        List<Task> copy = findAll();
        Collections.sort(copy, (a, b) -> {
            if (a.getDueDate() == null && b.getDueDate() == null) return 0;
            if (a.getDueDate() == null) return 1;
            if (b.getDueDate() == null) return -1;
            return a.getDueDate().compareTo(b.getDueDate());
        });
        return copy;
    }

    /**
     * Filter tasks by status.
     */
    public List<Task> findByStatus(com.taskmanagement.model.Status status) {
        if (status == null) return findAll();
        return tasks.stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }
}
