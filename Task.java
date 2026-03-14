package com.taskmanagement.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Entity class representing a task in the system.
 * Uses encapsulation with private fields and public getters/setters.
 */
public class Task {
    private String id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDate createdDate;
    private LocalDate dueDate;

    /**
     * Constructor for creating a new task (generates UUID and sets created date).
     */
    public Task(String title, String description, Priority priority, LocalDate dueDate) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.status = Status.TODO;
        this.priority = priority != null ? priority : Priority.MEDIUM;
        this.createdDate = LocalDate.now();
        this.dueDate = dueDate;
    }

    /**
     * Full constructor used when loading from JSON (preserves id and dates).
     */
    public Task(String id, String title, String description, Status status,
                Priority priority, LocalDate createdDate, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status != null ? status : Status.TODO;
        this.priority = priority != null ? priority : Priority.MEDIUM;
        this.createdDate = createdDate != null ? createdDate : LocalDate.now();
        this.dueDate = dueDate;
    }

    // Getters and setters (encapsulation)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return String.format(
                "Task[id=%s, title='%s', description='%s', status=%s, priority=%s, createdDate=%s, dueDate=%s]",
                id, title, description, status, priority, createdDate, dueDate
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
