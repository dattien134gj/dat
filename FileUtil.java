package com.taskmanagement.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.taskmanagement.model.Task;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for saving and loading tasks to/from a JSON file.
 * Uses Gson with custom adapters for LocalDate and Enum.
 */
public final class FileUtil {
    private static final String DEFAULT_FILE = "tasks.json";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

    private FileUtil() {
        // Utility class - prevent instantiation
    }

    /**
     * Build Gson instance with LocalDate and Enum support.
     */
    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    /**
     * Save list of tasks to JSON file. Creates file and parent dirs if needed.
     *
     * @param tasks list of tasks to save
     * @param filePath path to JSON file (null = use default "tasks.json")
     * @return true if save succeeded
     */
    public static boolean saveToJson(List<Task> tasks, String filePath) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        String path = filePath != null && !filePath.trim().isEmpty() ? filePath.trim() : DEFAULT_FILE;
        try {
            Path p = Paths.get(path);
            if (p.getParent() != null) {
                Files.createDirectories(p.getParent());
            }
            String json = createGson().toJson(tasks);
            Files.write(p, json.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load list of tasks from JSON file.
     *
     * @param filePath path to JSON file (null = use default "tasks.json")
     * @return list of tasks; empty list if file not found or invalid
     */
    public static List<Task> loadFromJson(String filePath) {
        String path = filePath != null && !filePath.trim().isEmpty() ? filePath.trim() : DEFAULT_FILE;
        Path p = Paths.get(path);
        if (!Files.exists(p) || !Files.isRegularFile(p)) {
            return new ArrayList<>();
        }
        try {
            String json = new String(Files.readAllBytes(p), StandardCharsets.UTF_8);
            Type listType = new TypeToken<ArrayList<Task>>() {}.getType();
            List<Task> list = createGson().fromJson(json, listType);
            return list != null ? list : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Gson TypeAdapter for LocalDate (ISO format: yyyy-MM-dd).
     */
    private static class LocalDateAdapter extends com.google.gson.TypeAdapter<LocalDate> {
        @Override
        public void write(com.google.gson.stream.JsonWriter out, LocalDate value) throws IOException {
            out.value(value == null ? null : value.format(DATE_FORMAT));
        }

        @Override
        public LocalDate read(com.google.gson.stream.JsonReader in) throws IOException {
            String s = in.nextString();
            return s == null || s.isEmpty() ? null : LocalDate.parse(s, DATE_FORMAT);
        }
    }
}
