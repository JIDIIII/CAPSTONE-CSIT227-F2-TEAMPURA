public class Penalty {
    private String reason;
    private int xpDeduction;

    public Penalty(String reason, int xpDeduction) {
        this.reason = reason;
        this.xpDeduction = xpDeduction;
    }

    public void applyTo(User user) {
        int currentXp = user.getXp();
        // Ensure XP doesn't go below 0
        int finalXp = Math.max(0, currentXp - xpDeduction);
        user.setXp(finalXp);
    }

    public String getMessage() {
        return "PENALTY: " + reason + "\nLost " + xpDeduction + " XP (20%)";
    }
}