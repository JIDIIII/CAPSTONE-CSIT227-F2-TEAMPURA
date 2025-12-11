import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataManager {
    private static final String ROOT_DIR = "Data/";
    private static final String USERS_DIR = "Data/Users/";

    public static void setupDirectories() {
        new File(ROOT_DIR).mkdirs();
        new File(USERS_DIR).mkdirs();
    }

    private static String getUserDir(String username) {
        String path = USERS_DIR + username + "/";
        new File(path).mkdirs();
        return path;
    }

    public static void saveCreationDate(String username) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getUserDir(username) + "created.txt"))) {
            bw.write(LocalDate.now().toString());
        } catch (IOException e) { e.printStackTrace(); }
    }

    // --- LOGGING ACTIVITY ---
    public static void logActivity(User user, String type, String logText) {
        String filename = getUserDir(user.getUsername()) + type.toLowerCase() + ".log";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(logText);
            writer.newLine();
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static String readLogFile(User user, String type) {
        String filename = getUserDir(user.getUsername()) + type.toLowerCase() + ".log";
        File f = new File(filename);
        if (!f.exists()) return "No records found.\n";
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) content.append(line).append("\n");
        } catch (IOException e) { return "Error reading logs.\n"; }
        return content.toString();
    }

    // --- CALCULATE CALORIES BURNED (FROM WORKOUTS) ---
    public static int getCaloriesBurnedToday(User user) {
        String filename = getUserDir(user.getUsername()) + "workouts.log";
        File f = new File(filename);
        if (!f.exists()) return 0;

        String todayTag = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int totalBurned = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if line is from today and contains "Burned:"
                if (line.contains(todayTag) && line.contains("Burned:")) {
                    String[] parts = line.split("\\|");
                    for (String part : parts) {
                        if (part.trim().startsWith("Burned:")) {
                            try {
                                totalBurned += Integer.parseInt(part.replace("Burned:", "").trim());
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return totalBurned;
    }

    // --- CALCULATE CALORIES CONSUMED (FROM MEALS) ---
    public static int getCaloriesConsumedToday(User user) {
        String filename = getUserDir(user.getUsername()) + "meals.log";
        File f = new File(filename);
        if (!f.exists()) return 0;

        String todayTag = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int totalCals = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(todayTag) && line.contains("Calories:")) {
                    String[] parts = line.split("\\|");
                    for (String part : parts) {
                        if (part.trim().startsWith("Calories:")) {
                            try { totalCals += Integer.parseInt(part.replace("Calories:", "").trim()); }
                            catch (NumberFormatException ignored) {}
                        }
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return totalCals;
    }

    public static boolean isWorkoutLoggedToday(User user, String activityName) {
        String filename = getUserDir(user.getUsername()) + "workouts.log";
        File f = new File(filename);
        if (!f.exists()) return false;
        String todayTag = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(todayTag) && line.toLowerCase().contains(activityName.toLowerCase())) {
                    return true;
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return false;
    }

    // --- PENALTY, CREDENTIALS, STREAK (Standard) ---
    public static String checkAndApplyDailyPenalty(User user) {
        String userPath = getUserDir(user.getUsername());
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        File creationFile = new File(userPath + "created.txt");
        if (creationFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(creationFile))) {
                if (today.toString().equals(br.readLine())) return null;
            } catch (IOException e) {}
        }

        File trackerFile = new File(userPath + "penalty_check.txt");
        if (trackerFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(trackerFile))) {
                if (today.toString().equals(br.readLine())) return null;
            } catch (IOException e) {}
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(trackerFile))) {
            bw.write(today.toString());
        } catch (IOException e) {}

        File f = new File(userPath + "challenge_" + yesterday + ".txt");
        boolean completedYesterday = false;
        if (f.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                if (br.readLine().contains("COMPLETED=true")) completedYesterday = true;
            } catch (IOException e) {}
        }

        if (!completedYesterday && user.getXp() > 0) {
            int deduction = (int) (user.getXp() * 0.20);
            Penalty penalty = new Penalty("Missed Daily Challenge", deduction);
            penalty.applyTo(user);
            saveUserCredentials(user);
            return penalty.getMessage();
        }
        return null;
    }

    public static Map<String, String> loadCredentials() {
        Map<String, String> credentials = new HashMap<>();
        File f = new File(ROOT_DIR + "users.txt");
        if (!f.exists()) return credentials;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length > 1) {
                    String val = p[1];
                    if (p.length > 2) val += "," + p[2];
                    if (p.length > 3) val += "," + p[3];
                    credentials.put(p[0], val);
                }
            }
        } catch (IOException e) {}
        return credentials;
    }

    public static void saveCredentials(Map<String, String> credentials) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ROOT_DIR + "users.txt"))) {
            for (Map.Entry<String, String> e : credentials.entrySet()) {
                bw.write(e.getKey() + "," + e.getValue());
                bw.newLine();
            }
        } catch (IOException e) {}
    }

    public static void saveUserCredentials(User user) {
        Map<String, String> creds = loadCredentials();
        String currentData = creds.getOrDefault(user.getUsername(), user.getPassword().hashCode() + ",0,1");
        String[] parts = currentData.split(",");
        String newData = parts[0] + "," + user.getXp() + "," + user.getLevel();
        creds.put(user.getUsername(), newData);
        saveCredentials(creds);
    }

    public static void saveStreakHistoryDate(User user, LocalDate date) {
        String filename = getUserDir(user.getUsername()) + "streak_history.txt";
        Set<String> dates = loadStreakHistoryDates(user);
        if (dates.contains(date.toString())) return;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(date.toString());
            writer.newLine();
        } catch (IOException e) {}
    }

    public static Set<String> loadStreakHistoryDates(User user) {
        Set<String> dates = new HashSet<>();
        File f = new File(getUserDir(user.getUsername()) + "streak_history.txt");
        if (!f.exists()) return dates;
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = reader.readLine()) != null) dates.add(line.trim());
        } catch (IOException e) {}
        return dates;
    }

    public static void saveUserStreak(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getUserDir(user.getUsername()) + "streak_count.txt"))) {
            writer.write(String.valueOf(user.getCurrentStreak().getCurrentStreak()));
        } catch (IOException e) {}
    }

    public static int loadUserStreak(User user) {
        File f = new File(getUserDir(user.getUsername()) + "streak_count.txt");
        if (f.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                return Integer.parseInt(reader.readLine());
            } catch (Exception e) {}
        }
        return 0;
    }

    public static void saveDailyChallengeState(User user, boolean completed) {
        try (FileWriter fw = new FileWriter(getUserDir(user.getUsername()) + "challenge_" + LocalDate.now() + ".txt")) {
            fw.write("COMPLETED=" + completed);
        } catch (IOException e) {}
    }

    public static boolean isChallengeClaimedToday(User user) {
        return new File(getUserDir(user.getUsername()) + "challenge_" + LocalDate.now() + ".txt").exists();
    }
}