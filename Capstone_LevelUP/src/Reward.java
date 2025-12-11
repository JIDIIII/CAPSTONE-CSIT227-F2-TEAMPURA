public class Reward {
    private String rewardName;
    private int levelRequirement;
    private String rewardType; // e.g. "Badge", "Title"
    private boolean isUnlocked;

    public Reward(String name, int levelRequirement, String type) {
        this.rewardName = name;
        this.levelRequirement = levelRequirement;
        this.rewardType = type;
        this.isUnlocked = false;
    }

    public boolean checkAndUnlock(User user) {
        if (!isUnlocked && user.getLevel() >= this.levelRequirement) {
            this.isUnlocked = true;
            return true;
        }
        return false;
    }

    public String getRewardName() { return rewardName; }
    public String getRewardType() { return rewardType; }
    public int getLevelRequirement() { return levelRequirement; }
    public boolean isUnlocked() { return isUnlocked; }

    @Override
    public String toString() {
        return rewardName + " (" + rewardType + ")";
    }
}