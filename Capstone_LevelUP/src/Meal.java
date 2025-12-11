public class Meal {
    private String mealId;
    private String mealName;
    private int calories;
    private String mealType; // "Breakfast", "Lunch", "Dinner", "Snack"
    private boolean isHealthy;

    public Meal(String name, int calories, String mealType) {
        this.mealName = name;
        this.calories = calories;
        this.mealType = mealType;
        classifyMeal(); // Auto-classify
    }

    public void calculateCalories() {
        // In a real app, this would sum ingredients.
        System.out.println("Calories calculated: " + this.calories);
    }

    public void classifyMeal() {
        // Updated Logic: Healthy is between 300 and 700 calories inclusive
        if (this.calories >= 300 && this.calories <= 700) {
            this.isHealthy = true;
        } else {
            this.isHealthy = false;
        }
    }

    public void updateMeal(int newCalories, String newType) {
        this.calories = newCalories;
        this.mealType = newType;
        classifyMeal();
    }

    // Getters
    public boolean isHealthy() { return isHealthy; }
    public String getMealName() { return mealName; }
    public int getCalories() { return calories; }
}