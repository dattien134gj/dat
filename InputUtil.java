package com.taskmanagement.util;

import com.taskmanagement.model.Priority;
import com.taskmanagement.model.Status;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utility class for safe console input handling with basic validation.
 */
public final class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    private InputUtil() {
        // Utility class
    }

    /**
     * Read a non-empty string line. Re-prompts until non-blank or null for optional.
     */
    public static String readLine(String prompt, boolean required) {
        while (true) {
            if (prompt != null && !prompt.isEmpty()) {
                System.out.print(prompt);
            }
            String line = scanner.nextLine();
            if (line == null) line = "";
            line = line.trim();
            if (required && line.isEmpty()) {
                System.out.println("This field is required. Please try again.");
                continue;
            }
            return line;
        }
    }

    /**
     * Read integer in range [min, max]. Re-prompts on invalid input.
     */
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            if (prompt != null && !prompt.isEmpty()) {
                System.out.print(prompt);
            }
            String line = scanner.nextLine();
            if (line == null) line = "";
            line = line.trim();
            try {
                int value = Integer.parseInt(line.trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Please try again.");
            }
        }
    }

    /**
     * Read a valid LocalDate in yyyy-MM-dd format.
     */
    public static LocalDate readDate(String prompt) {
        while (true) {
            if (prompt != null && !prompt.isEmpty()) {
                System.out.print(prompt);
            }
            String line = scanner.nextLine();
            if (line != null) line = line.trim();
            if (line == null || line.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(line);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use format yyyy-MM-dd (e.g. 2025-12-31). Try again.");
            }
        }
    }

    /**
     * Parse status from user input: 1=TODO, 2=IN_PROGRESS, 3=DONE.
     */
    public static Status readStatus(String prompt) {
        System.out.println("1. TODO  2. IN_PROGRESS  3. DONE");
        int choice = readInt(prompt, 1, 3);
        switch (choice) {
            case 1: return Status.TODO;
            case 2: return Status.IN_PROGRESS;
            case 3: return Status.DONE;
            default: return Status.TODO;
        }
    }

    /**
     * Parse priority from user input: 1=LOW, 2=MEDIUM, 3=HIGH.
     */
    public static Priority readPriority(String prompt) {
        System.out.println("1. LOW  2. MEDIUM  3. HIGH");
        int choice = readInt(prompt, 1, 3);
        switch (choice) {
            case 1: return Priority.LOW;
            case 2: return Priority.MEDIUM;
            case 3: return Priority.HIGH;
            default: return Priority.MEDIUM;
        }
    }

    /**
     * Pause and wait for Enter (e.g. after showing a list).
     */
    public static void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}
