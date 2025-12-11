import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DailyChallenge {

    // === 1. DYNAMIC SETS LOGIC ===
    // Levels 1-4: 3 Sets
    // Levels 5-9: 4 Sets
    // Level 10+:  5 Sets (Cap)
    public static int calculateSets(int userLevel) {
        if (userLevel < 5) return 3;
        if (userLevel < 10) return 4;
        return 5;
    }

    // === 2. GAMIFIED CALORIE VALUES ===
    // These values are inflated relative to real life to make the 350 target achievable
    // and rewarding for the player.
    public static int getBaseCaloriesPerSet(String exerciseName) {
        Map<String, Integer> burnMap = new HashMap<>();

        // Strength (Avg ~25-30 per set)
        burnMap.put("Push-Ups", 25);
        burnMap.put("Tricep Dips", 20);
        burnMap.put("Shoulder Taps", 20);
        burnMap.put("Plank", 25);
        burnMap.put("Squats", 30);
        burnMap.put("Lunges", 28);
        burnMap.put("Calf Raises", 20);
        burnMap.put("Wall Sit", 25);
        burnMap.put("Superman", 20);
        burnMap.put("Sit-Ups", 25);
        burnMap.put("Russian Twists", 22);
        burnMap.put("Leg Raises", 22);

        // Cardio / HIIT (Avg ~30-40 per set)
        burnMap.put("Jumping Jacks", 35);
        burnMap.put("High Knees", 30);
        burnMap.put("Mtn Climbers", 35);
        burnMap.put("Burpees", 40);

        return burnMap.getOrDefault(exerciseName, 20);
    }

    public static class ExerciseTask {
        String name;
        String displayString; // Formatted for UI: "3 | 12"

        public ExerciseTask(String name, String reps, int sets) {
            this.name = name;
            this.displayString = sets + " | " + reps;
        }
    }

    public static class ChallengeData {
        private String title;
        private ExerciseTask[] exercises;

        public ChallengeData(String title, ExerciseTask[] exercises) {
            this.title = title;
            this.exercises = exercises;
        }

        public String getTitle() { return title; }
        public ExerciseTask[] getExercises() { return exercises; }
    }

    public static ChallengeData getChallengeForToday(int userLevel) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        // Determine sets based on level
        int sets = calculateSets(userLevel);

        String title;
        ExerciseTask[] exercises = new ExerciseTask[4];

        switch (today) {
            case MONDAY:
                title = "Monday: Push & Power";
                exercises[0] = new ExerciseTask("Push-Ups", "12", sets);
                exercises[1] = new ExerciseTask("Tricep Dips", "10", sets);
                exercises[2] = new ExerciseTask("Shoulder Taps", "20", sets);
                exercises[3] = new ExerciseTask("Plank", "45s", sets);
                break;
            case TUESDAY:
                title = "Tuesday: Leg Day";
                exercises[0] = new ExerciseTask("Squats", "15", sets);
                exercises[1] = new ExerciseTask("Lunges", "12", sets);
                exercises[2] = new ExerciseTask("Calf Raises", "20", sets);
                exercises[3] = new ExerciseTask("Wall Sit", "45s", sets);
                break;
            case WEDNESDAY:
                title = "Wednesday: Upper & Back";
                exercises[0] = new ExerciseTask("Superman", "15", sets);
                exercises[1] = new ExerciseTask("Push-Ups", "15", sets);
                exercises[2] = new ExerciseTask("Shoulder Taps", "20", sets);
                exercises[3] = new ExerciseTask("Tricep Dips", "12", sets);
                break;
            case THURSDAY:
                title = "Thursday: Cardio Burn";
                exercises[0] = new ExerciseTask("Jumping Jacks", "50", sets);
                exercises[1] = new ExerciseTask("High Knees", "30s", sets);
                exercises[2] = new ExerciseTask("Mtn Climbers", "30s", sets);
                exercises[3] = new ExerciseTask("Burpees", "10", sets);
                break;
            case FRIDAY:
                title = "Friday: Core Crusher";
                exercises[0] = new ExerciseTask("Sit-Ups", "20", sets);
                exercises[1] = new ExerciseTask("Russian Twists", "20", sets);
                exercises[2] = new ExerciseTask("Leg Raises", "15", sets);
                exercises[3] = new ExerciseTask("Plank", "60s", sets);
                break;
            case SATURDAY:
                title = "Saturday: Full Body HIIT";
                exercises[0] = new ExerciseTask("Burpees", "12", sets);
                exercises[1] = new ExerciseTask("Squats", "20", sets);
                exercises[2] = new ExerciseTask("Push-Ups", "15", sets);
                exercises[3] = new ExerciseTask("Jumping Jacks", "60", sets);
                break;
            case SUNDAY:
                title = "Sunday: Active Recovery";
                exercises[0] = new ExerciseTask("Wall Sit", "60s", sets);
                exercises[1] = new ExerciseTask("Superman", "15", sets);
                exercises[2] = new ExerciseTask("Calf Raises", "20", sets);
                exercises[3] = new ExerciseTask("Plank", "90s", sets);
                break;
            default:
                title = "Rest Day";
                exercises[0] = new ExerciseTask("Rest", "-", 0);
                exercises[1] = new ExerciseTask("Rest", "-", 0);
                exercises[2] = new ExerciseTask("Rest", "-", 0);
                exercises[3] = new ExerciseTask("Rest", "-", 0);
                break;
        }
        return new ChallengeData(title, exercises);
    }
}