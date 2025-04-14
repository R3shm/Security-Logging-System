import java.util.*;
import java.time.*;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static AuthSystem authSystem = new AuthSystem();
    static LogSystem logSystem = new LogSystem();

    public static void main(String[] args) {
        System.out.println("=== Security & Logging System ===");
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        String role = authSystem.authenticate(username, password);

        if (role == null) {
            System.out.println("Invalid login. Access denied.");
            return;
        }

        logSystem.logEntry(username);

        switch (role) {
            case "admin":
                new AdminDashboard(username, logSystem);
                break;
            case "faculty":
                new FacultyDashboard(username, logSystem);
                break;
            case "student":
                new StudentDashboard(username, logSystem);
                break;
        }

        logSystem.logExit(username);
    }
}

// 1. Authentication System
class AuthSystem {
    private final Map<String, String> users = new HashMap<>();
    private final Map<String, String> roles = new HashMap<>();

    public AuthSystem() {
        // Sample users
        users.put("admin", "admin123");
        roles.put("admin", "admin");

        users.put("faculty1", "fac123");
        roles.put("faculty1", "faculty");

        users.put("student1", "stu123");
        roles.put("student1", "student");
    }

    public String authenticate(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            return roles.get(username);
        }
        return null;
    }
}

// 2. Logging System
class LogSystem {
    private final Map<String, List<String>> parkingHistory = new HashMap<>();

    public void logEntry(String username) {
        String time = LocalDateTime.now().toString();
        parkingHistory.putIfAbsent(username, new ArrayList<>());
        parkingHistory.get(username).add("Entry: " + time);
        System.out.println("Entry time logged: " + time);
    }

    public void logExit(String username) {
        String time = LocalDateTime.now().toString();
        parkingHistory.get(username).add("Exit: " + time);
        System.out.println("Exit time logged: " + time);
    }

    public void showHistory(String username) {
        System.out.println("Parking History for " + username + ":");
        List<String> history = parkingHistory.get(username);
        if (history != null) {
            for (String record : history) {
                System.out.println(record);
            }
        } else {
            System.out.println("No history available.");
        }
    }

    public void generateReport() {
        System.out.println("\n--- All Users Parking Report ---");
        for (String user : parkingHistory.keySet()) {
            System.out.println("\nUser: " + user);
            showHistory(user);
        }
    }
}

// 3. Dashboards
class AdminDashboard {
    public AdminDashboard(String username, LogSystem logSystem) {
        System.out.println("\nWelcome to Admin Dashboard");
        System.out.println("1. View All Parking History");
        System.out.println("2. Generate Report");

        System.out.print("Enter option: ");
        int option = Main.sc.nextInt();
        Main.sc.nextLine(); // consume newline

        if (option == 1) {
            logSystem.generateReport();
        } else if (option == 2) {
            logSystem.generateReport(); // Same function as view all
        } else {
            System.out.println("Invalid option.");
        }
    }
}

class FacultyDashboard {
    public FacultyDashboard(String username, LogSystem logSystem) {
        System.out.println("\nWelcome to Faculty Dashboard");
        System.out.println("1. View My Parking History");

        System.out.print("Enter option: ");
        int option = Main.sc.nextInt();
        Main.sc.nextLine();

        if (option == 1) {
            logSystem.showHistory(username);
        } else {
            System.out.println("Invalid option.");
        }
    }
}

class StudentDashboard {
    public StudentDashboard(String username, LogSystem logSystem) {
        System.out.println("\nWelcome to Student Dashboard");
        System.out.println("1. View My Parking History");

        System.out.print("Enter option: ");
        int option = Main.sc.nextInt();
        Main.sc.nextLine();

        if (option == 1) {
            logSystem.showHistory(username);
        } else {
            System.out.println("Invalid option.");
        }
    }
}