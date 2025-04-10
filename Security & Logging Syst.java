// Main.java

import java.util.*;

// Class to represent a User
class User {
    String username;
    String password;
    List<String> parkingHistory = new ArrayList<>();

    User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

// Class to represent a log entry (entry/exit time)
class LogEntry {
    String username;
    Date entryTime;
    Date exitTime;

    LogEntry(String username) {
        this.username = username;
        this.entryTime = new Date();
    }

    void logExit() {
        this.exitTime = new Date();
    }

    @Override
    public String toString() {
        return "User: " + username + ", Entry: " + entryTime + ", Exit: " + (exitTime != null ? exitTime : "Still Inside");
    }
}

// Main system class
class SecurityLoggingSystem {
    Map<String, User> users = new HashMap<>();
    Map<String, LogEntry> activeLogs = new HashMap<>();
    List<LogEntry> logHistory = new ArrayList<>();
    String adminPassword = "admin123";

    void registerUser(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password));
            System.out.println("User registered successfully.");
        } else {
            System.out.println("Username already exists.");
        }
    }

    boolean authenticate(String username, String password) {
        User user = users.get(username);
        return user != null && user.password.equals(password);
    }

    void logEntry(String username) {
        if (!activeLogs.containsKey(username)) {
            LogEntry entry = new LogEntry(username);
            activeLogs.put(username, entry);
            users.get(username).parkingHistory.add("Entered at: " + entry.entryTime);
            System.out.println("Entry logged.");
        } else {
            System.out.println("User already inside.");
        }
    }

    void logExit(String username) {
        LogEntry entry = activeLogs.get(username);
        if (entry != null) {
            entry.logExit();
            logHistory.add(entry);
            users.get(username).parkingHistory.add("Exited at: " + entry.exitTime);
            activeLogs.remove(username);
            System.out.println("Exit logged.");
        } else {
            System.out.println("No active entry found.");
        }
    }

    void viewAdminDashboard(String password) {
        if (password.equals(adminPassword)) {
            System.out.println("--- Admin Dashboard ---");
            System.out.println("Active Users:");
            for (String user : activeLogs.keySet()) {
                System.out.println(user);
            }
        } else {
            System.out.println("Invalid admin password.");
        }
    }

    void viewUserHistory(String username) {
        User user = users.get(username);
        if (user != null) {
            System.out.println("--- Parking History for " + username + " ---");
            for (String record : user.parkingHistory) {
                System.out.println(record);
            }
        } else {
            System.out.println("User not found.");
        }
    }

    void generateReport() {
        System.out.println("--- Full Entry/Exit Report ---");
        for (LogEntry log : logHistory) {
            System.out.println(log);
        }
    }

    void sendSecurityNotification(String username, String message) {
        System.out.println("Security Alert for " + username + ": " + message);
    }
}

// Main class with main() function
public class Main {
    public static void main(String[] args) {
        SecurityLoggingSystem system = new SecurityLoggingSystem();

        // Register users
        system.registerUser("john", "1234");
        system.registerUser("alice", "abcd");

        // Authenticate and log entry
        if (system.authenticate("john", "1234")) {
            system.logEntry("john");
        }

        if (system.authenticate("alice", "abcd")) {
            system.logEntry("alice");
        }

        // Log exit
        system.logExit("john");

        // View admin dashboard
        system.viewAdminDashboard("admin123");

        // View user history
        system.viewUserHistory("john");

        // Generate report
        system.generateReport();

        // Send security notification
        system.sendSecurityNotification("alice", "You forgot to log out!");
    }
}