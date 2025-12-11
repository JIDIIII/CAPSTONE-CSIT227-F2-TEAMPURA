import java.util.ArrayList;
import java.util.List;

public class User {
    private final String userId;
    private String username;
    private String password;
    private int xp;
    private int level;

    // Relationships
    private final List<Workout> workoutLog;
    private final List<Meal> mealLog;
    private Streak currentStreak;

    // Rewards Management
    private final List<Reward> availableRewards;
    private final List<Reward> unlockedRewards;

    public User(String userId, String name, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.xp = 0;
        this.level = 1;
        this.workoutLog = new ArrayList<>();
        this.mealLog = new ArrayList<>();
        this.currentStreak = new Streak();
        this.unlockedRewards = new ArrayList<>();
        this.availableRewards = new ArrayList<>();

        initializeRewards();
    }

    private void initializeRewards() {
        availableRewards.add(new Reward("Iron Rookie", 2, "Title"));
        availableRewards.add(new Reward("Bronze Warrior", 5, "Badge"));
        availableRewards.add(new Reward("Silver Gladiator", 10, "Badge"));
        availableRewards.add(new Reward("Gold Legend", 20, "Title"));
    }

    public String getCurrentRank() {
        String currentRank = "Rookie";
        for (Reward r : availableRewards) {
            if (this.level >= r.getLevelRequirement() && r.getRewardType().equalsIgnoreCase("Title")) {
                currentRank = r.getRewardName();
            }
        }
        return currentRank;
    }

    public Reward getNextReward() {
        for (Reward r : availableRewards) {
            if (!r.isUnlocked()) return r;
        }
        return null;
    }

    public List<Reward> getAvailableRewards() { return availableRewards; }

    public String checkRewards() {
        StringBuilder newUnlocks = new StringBuilder();
        for (Reward r : availableRewards) {
            if (r.checkAndUnlock(this)) {
                unlockedRewards.add(r);
                newUnlocks.append("\nUNLOCKED: ").append(r.toString());
            }
        }
        return newUnlocks.toString();
    }

    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public int getXp() { return xp; }
    public int getLevel() { return level; }
    public Streak getCurrentStreak() { return currentStreak; }

    public void setXp(int xp) { this.xp = xp; }
    public void setLevel(int level) {
        this.level = level;
        for(Reward r : availableRewards) {
            if(r.checkAndUnlock(this)) unlockedRewards.add(r);
        }
    }

    public void logWorkout(Workout workout) { workoutLog.add(workout); }

    public int logMeal(Meal meal) {
        mealLog.add(meal);
        int gained = 0;
        if(meal.isHealthy()) gained = 30;
        return gained;
    }

    public String gainXP(int amount) {
        this.xp += amount;
        StringBuilder message = new StringBuilder();

        // SCALING: 500 * 1.5^(level-1)
        while (true) {
            int xpRequired = (int) (500 * Math.pow(1.5, this.level - 1));

            if (this.xp >= xpRequired) {
                this.xp -= xpRequired; // Deduct cost
                this.level++;
                message.append("\nLEVEL UP! You are now Level ").append(this.level);
                message.append("\nNew Rank: ").append(getCurrentRank());
            } else {
                break;
            }
        }

        String rewards = checkRewards();
        if (!rewards.isEmpty()) message.append(rewards);
        return message.toString();
    }
}