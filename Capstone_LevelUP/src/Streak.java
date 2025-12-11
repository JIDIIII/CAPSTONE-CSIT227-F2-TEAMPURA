import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Streak {
    private int currentStreak;
    private LocalDate lastActiveDate;

    public Streak() {
        this.currentStreak = 0;
        this.lastActiveDate = null; // Null indicates no activity yet
    }

    public void updateStreak(User user) {
        LocalDate today = LocalDate.now();

        // If it's the first time ever
        if (lastActiveDate == null) {
            this.currentStreak = 1;
            this.lastActiveDate = today;
            return;
        }

        long daysBetween = ChronoUnit.DAYS.between(lastActiveDate, today);

        if (daysBetween == 0) {
            // Already active today, do nothing
            return;
        } else if (daysBetween == 1) {
            // Perfect streak (Yesterday -> Today)
            this.currentStreak++;
        } else {
            // Missed a day or more, reset to 1 (since we are active today)
            this.currentStreak = 1;
        }
        this.lastActiveDate = today;
    }

    public int getCurrentStreak() { return currentStreak; }

    // Setter for loading from file
    public void setStreak(int s) {
        this.currentStreak = s;
        // If loading a positive streak, assume last active was yesterday or today
        // (This logic is handled better in Test.java validation, this is just a setter)
        if (s > 0 && lastActiveDate == null) {
            lastActiveDate = LocalDate.now();
        }
    }
}