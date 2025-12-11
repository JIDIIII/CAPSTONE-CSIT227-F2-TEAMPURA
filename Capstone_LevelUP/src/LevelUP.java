import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.List;
import java.awt.Desktop;
import java.net.URI;

public class LevelUP extends JFrame {
    // === UI COMPONENTS ===
    private JPanel LevelUP, Start, CreateAcc, SignIn, ForgotPass;
    private JPanel HomePage, StreakPage, ChallPage, LogPage, AccPage;

    // Required bindings for GUI Designer
    private JPanel Challenge, Days, TimerJPanel;

    private JLabel ChallengeTitle;

    // Auth & Inputs
    private JButton SignInPageBT, CreateAccPageBT, CreateAccountBT, SignInBT, ChangePasswordButton, SignOutButton;
    private JTextField CreateUserTextField, CheckUsernameField, EnterUsernameTextField, CreatePassTextField, EnterNewPasswordTextField;
    private JPasswordField CheckPassField;
    private JLabel AppFBAccPage, SignInForgotPassword, CPassAccPage;

    // Navigation Labels
    private JLabel STKBTNHomePage, CHALBTNHomePage, LOGBTNHomePage, ACCBTNHomePage;
    private JLabel HMBTNStreakPage, CHALBTNStreakPage, LOGBTNStreakPage, ACCBTNStreakPage;
    private JLabel HMBTNChallPage, STKBTNChallPage, LOGBTNChallPage, ACCBTNChallPage;
    private JLabel HMBTNLogPage, STKBTNLogPage, CHALBTNLogPage, ACCBTNLogPage;
    private JLabel HMBTNAccPage, STKBTNAccPage, CHALBTNAccPage, LOGBTNAccPage;

    // Data Display
    private JLabel FIRESTREAK, USERNAME, ACCOUNTID;
    private JLabel HomePageDisplayName, HomePageDisplayDate;
    private JProgressBar HMPageProgressBar;
    private JLabel HMPageStreakCount, HMPageEXPCount, StreakCount;
    private JLabel HMPageLevelLabel, HMPageXPFractionLabel;

    // Calendar & Challenge
    private JLabel SDay, MDay, TDay, WDay, ThDay, FDay, StDay;
    private JProgressBar ChallPageProgressBar;
    private JButton CompleteChallengeButton;
    private JLabel RewardLabel, RewardCount;
    private JCheckBox Chall1, Chall2, Chall3, Chall4;
    private JLabel ChallCount1, ChallCount2, ChallCount3, ChallCount4;
    private JLabel ChallPageCaloriesLabel;

    // Log Page
    private JLabel CheckLogAccPage;
    private JButton LogWorkoutButton, LogMealButton;
    private JComboBox<String> ProteinComboBox, CarbohydratesComboBox, VegetablesComboBox;
    private JCheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8;
    private JCheckBox checkBox9, checkBox10, checkBox11, checkBox12, checkBox13, checkBox14, checkBox15, checkBox16;
    private JLabel GoBackCAcc, GoBackSignIn, GoBackForgPass;
    private JPanel PanelTopHMPage;
    private JPanel PanelBot1HMPage;
    private JPanel PanelBot2HMPage;
    private JPanel PanelStreakPage;
    private JPanel RewardPanel;
    private JPanel WorkoutPanel;
    private JPanel MealPanel;
    private JPanel CPassPanel;
    private JPanel StartPanel;
    private JPanel CreateAccPanel;
    private JPanel SignInPanel;
    private JPanel AccPanel;
    private JPanel AccTab;
    private JPanel HomeTab;
    private JPanel StreakTab;
    private JPanel ChallTab;
    private JPanel LogTab;

    // === BACKEND DATA ===
    private static User currentUser = null;
    private static Map<String, String> userCredentials = new HashMap<>();
    private List<JCheckBox> workoutCheckBoxes;
    private final Map<String, Integer> calorieMap = new HashMap<>();
    private LocalDate lastTrackedDate;

    // CHANGED: Target is now 350 Burned Calories
    private static final int DAILY_CALORIE_BURN_TARGET = 350;

    // === PLACEHOLDER TRACKING ===
    private final Map<JTextField, String> placeholderMap = new HashMap<>();

    LevelUP() {
        setContentPane(LevelUP);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setTitle("LevelUP");

        DataManager.setupDirectories();
        userCredentials = DataManager.loadCredentials();
        initializeLists();
        setupLogPageCheckboxes();
        setupLogPageComboBoxes();
        setupNavigation();
        setupChallengeLogic();
        startDayChangeChecker();

        setupPlaceholders();

        setupCustomFonts();

        setupPlaceholders();

        if (CreateUserTextField != null) CreateUserTextField.setHorizontalAlignment(JTextField.CENTER);
        if (CreatePassTextField != null) CreatePassTextField.setHorizontalAlignment(JTextField.CENTER);

        switchCard("Start");

        // --- BUTTON ACTIONS ---
        SignInPageBT.addActionListener(e -> switchCard("SignIn"));
        CreateAccPageBT.addActionListener(e -> switchCard("CreateAcc"));
        SignInBT.addActionListener(e -> handleLogin());
        CreateAccountBT.addActionListener(e -> handleRegistration());
        SignOutButton.addActionListener(e -> handleLogout());
        ChangePasswordButton.addActionListener(e -> handleChangePassword());

        setupLabelButton(SignInForgotPassword, "ForgotPass");
        setupLabelButton(CPassAccPage, "ForgotPass");
        setupLabelButton(GoBackCAcc, "Start");
        setupLabelButton(GoBackSignIn, "CreateAcc");
        setupLabelButton(GoBackForgPass, "SignIn");

        if (AppFBAccPage != null) AppFBAccPage.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        CompleteChallengeButton.addActionListener(e -> handleCompleteChallenge());
        LogWorkoutButton.addActionListener(e -> handleLogWorkout());
        LogMealButton.addActionListener(e -> handleLogMeal());

        if (CheckLogAccPage != null) {
            CheckLogAccPage.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            CheckLogAccPage.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) { showLogsPopup(); }
            });
        }

        AppFBAccPage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    String url = "https://docs.google.com/forms/d/e/1FAIpQLSeigar3SOI548cgvPsri4xITkpio2DDnPjtcA815HOgQtWJrQ/viewform?fbclid=IwY2xjawOn00xleHRuA2FlbQIxMABicmlkETFIRjdBWThQT1Q1Y09uRVBXc3J0YwZhcHBfaWQQMjIyMDM5MTc4ODIwMDg5MgABHrzf_Zp9UhBiUBthntt0C4F_xN6-Qg43dXMfjyEE8gBhY27hzqCLyJI9d1Tz_aem_x4DB7SaabjdhXNaB7JZ_pA";

                    // Check if Desktop is supported (Windows, Mac, Linux usually support it)
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new java.net.URI(url));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace(); // Prints error if link fails to open
                    JOptionPane.showMessageDialog(null, "Could not open link.");
                }
            }
        });
    }

    // =========================================================
    // === PLACEHOLDER & FIELDS ===
    // =========================================================

    private void setupPlaceholders() {
        setupPlaceholder(CheckUsernameField, "Username");
        setupPlaceholder(CheckPassField, "Password");
        setupPlaceholder(CreateUserTextField, "Create Username");
        setupPlaceholder(CreatePassTextField, "Create Password");
        setupPlaceholder(EnterUsernameTextField, "Enter Username");
        setupPlaceholder(EnterNewPasswordTextField, "Enter New Password");
    }

    private void setupPlaceholder(JTextField field, String placeholder) {
        if (field == null) return;
        placeholderMap.put(field, placeholder);
        final char defaultEchoChar = (field instanceof JPasswordField) ? ((JPasswordField) field).getEchoChar() : (char) 0;

        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        if (field instanceof JPasswordField) ((JPasswordField) field).setEchoChar((char) 0);

        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    if (field instanceof JPasswordField) ((JPasswordField) field).setEchoChar(defaultEchoChar);
                }
            }
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    if (field instanceof JPasswordField) ((JPasswordField) field).setEchoChar((char) 0);
                }
            }
        });
    }

    private String getFieldText(JTextField field) {
        if (field == null) return "";
        String text = field.getText().trim();
        String placeholder = placeholderMap.get(field);
        return (placeholder != null && text.equals(placeholder)) ? "" : text;
    }

    private void resetField(JTextField field) {
        if (field != null) {
            String placeholder = placeholderMap.get(field);
            if (placeholder != null) {
                field.setText(placeholder);
                field.setForeground(Color.GRAY);
                if (field instanceof JPasswordField) ((JPasswordField) field).setEchoChar((char) 0);
            } else {
                field.setText("");
            }
        }
    }

    private void clearAllAuthFields() {
        resetField(CheckUsernameField);
        resetField(CheckPassField);
        resetField(CreateUserTextField);
        resetField(CreatePassTextField);
        resetField(EnterUsernameTextField);
        resetField(EnterNewPasswordTextField);
    }

    // =========================================================
    // === AUTHENTICATION ===
    // =========================================================

    private void handleLogin() {
        String username = getFieldText(CheckUsernameField);
        String password = getFieldText(CheckPassField);

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and password.");
            return;
        }

        if (userCredentials.containsKey(username)) {
            String[] parts = userCredentials.get(username).split(",");
            if (parts[0].equals(String.valueOf(password.hashCode()))) {
                int xp = (parts.length > 1) ? Integer.parseInt(parts[1]) : 0;
                int level = (parts.length > 2) ? Integer.parseInt(parts[2]) : 1;

                currentUser = new User("ID: " + Math.abs(username.hashCode()), username, username, password);
                currentUser.setXp(xp);
                currentUser.setLevel(level);

                validateStreakOnLogin();
                JOptionPane.showMessageDialog(this, "Welcome, " + username + "!");

                String penaltyMsg = DataManager.checkAndApplyDailyPenalty(currentUser);
                if (penaltyMsg != null) JOptionPane.showMessageDialog(this, penaltyMsg, "Penalty Applied", JOptionPane.WARNING_MESSAGE);

                setupChallengeLogic();
                updateChallengeState();
                refreshUI();
                switchCard("HomePage");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void handleRegistration() {
        String username = getFieldText(CreateUserTextField);
        String password = getFieldText(CreatePassTextField);

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
        if (userCredentials.containsKey(username)) {
            JOptionPane.showMessageDialog(this, "Username taken.");
            return;
        }

        userCredentials.put(username, password.hashCode() + ",0,1");
        DataManager.saveCredentials(userCredentials);
        DataManager.saveCreationDate(username);

        JOptionPane.showMessageDialog(this, "Account Created!");
        switchCard("SignIn");
    }

    private void handleLogout() {
        if (currentUser != null) {
            DataManager.saveUserStreak(currentUser);
            syncUserData();
        }
        currentUser = null;
        switchCard("Start");
    }

    private void handleChangePassword() {
        String username = getFieldText(EnterUsernameTextField);
        String newPass = getFieldText(EnterNewPasswordTextField);

        if (username.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter username and new password.");
            return;
        }

        if (userCredentials.containsKey(username)) {
            String[] data = userCredentials.get(username).split(",");
            String currentXp = (data.length > 1) ? data[1] : "0";
            String currentLvl = (data.length > 2) ? data[2] : "1";

            if (currentUser != null && currentUser.getUsername().equals(username)) {
                currentXp = String.valueOf(currentUser.getXp());
                currentLvl = String.valueOf(currentUser.getLevel());
            }

            String newData = newPass.hashCode() + "," + currentXp + "," + currentLvl;
            userCredentials.put(username, newData);
            DataManager.saveCredentials(userCredentials);
            JOptionPane.showMessageDialog(this, "Password Updated! Progress Saved.");
            switchCard("SignIn");
        } else {
            JOptionPane.showMessageDialog(this, "User not found.");
        }
    }

    private void validateStreakOnLogin() {
        int savedStreak = DataManager.loadUserStreak(currentUser);
        Set<String> dates = DataManager.loadStreakHistoryDates(currentUser);
        LocalDate lastDate = null;

        for (String d : dates) {
            LocalDate pd = LocalDate.parse(d);
            if (lastDate == null || pd.isAfter(lastDate)) lastDate = pd;
        }

        if (lastDate != null) {
            long daysBetween = ChronoUnit.DAYS.between(lastDate, LocalDate.now());
            if (daysBetween > 1) savedStreak = 0;
        } else {
            savedStreak = 0;
        }

        currentUser.getCurrentStreak().setStreak(savedStreak);
        DataManager.saveUserStreak(currentUser);
    }

    // =========================================================
    // === WORKOUT LOGGING (Dynamic Sets & Calories) ===
    // =========================================================
    private void handleLogWorkout() {
        if (currentUser == null) return;
        StringBuilder activity = new StringBuilder();
        int count = 0;
        int totalCaloriesBurned = 0;

        // Calculate current sets based on level for accurate calorie count
        int currentSets = DailyChallenge.calculateSets(currentUser.getLevel());

        for (JCheckBox cb : workoutCheckBoxes) {
            if (cb != null && cb.isSelected()) {
                if (count > 0) activity.append(", ");
                String exerciseName = cb.getText();
                activity.append(exerciseName);

                // Calculate: Base Cal per Set * Number of Sets
                int basePerSet = DailyChallenge.getBaseCaloriesPerSet(exerciseName);
                totalCaloriesBurned += (basePerSet * currentSets);

                cb.setSelected(false);
                count++;
            }
        }
        if (count == 0) {
            JOptionPane.showMessageDialog(this, "Select an exercise!");
            return;
        }

        int xp = count * 10;
        Workout w = new Workout("Session", 0, "Low");
        currentUser.logWorkout(w);

        String rewardMsg = currentUser.gainXP(xp);

        // Log includes the Burned amount so DataManager can parse it later
        String log = String.format("%s | Workout: %s | Sets: %d | XP: %d | Burned: %d",
                now(), activity.toString(), currentSets, xp, totalCaloriesBurned);
        DataManager.logActivity(currentUser, "Workouts", log);

        syncUserData();

        JOptionPane.showMessageDialog(this,
                "Logged! \nSets Performed: " + currentSets +
                        "\nXP Gained: " + xp +
                        "\nCalories Burned: " + totalCaloriesBurned +
                        rewardMsg);

        updateChallengeState();
        refreshUI();
    }

    // =========================================================
    // === MEAL LOGGING ===
    // =========================================================
    private void handleLogMeal() {
        if (currentUser == null) return;
        String p = (String) ProteinComboBox.getSelectedItem();
        String c = (String) CarbohydratesComboBox.getSelectedItem();
        String v = (String) VegetablesComboBox.getSelectedItem();

        List<String> selectedItems = new ArrayList<>();
        int cals = 0;

        if (p != null && !p.startsWith("Choose")) { selectedItems.add(p); cals += calorieMap.getOrDefault(p, 0); }
        if (c != null && !c.startsWith("Choose")) { selectedItems.add(c); cals += calorieMap.getOrDefault(c, 0); }
        if (v != null && !v.startsWith("Choose")) { selectedItems.add(v); cals += calorieMap.getOrDefault(v, 0); }

        if (selectedItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one valid meal item.");
            return;
        }

        String mealName = String.join(" + ", selectedItems);
        Meal m = new Meal(mealName, cals, "Lunch");
        int xpGained = currentUser.logMeal(m);
        String rewardMsg = currentUser.gainXP(xpGained);

        String log = String.format("%s | Combo: %s | Calories: %d | Healthy: %s",
                now(), mealName, cals, m.isHealthy());
        DataManager.logActivity(currentUser, "Meals", log);

        syncUserData();

        String feedback = "Meal Logged!\nItems: " + mealName + "\nCalories: " + cals;
        feedback += m.isHealthy() ? "\nHealthy Meal Bonus: +30 XP" : "\nStatus: Unhealthy (No XP)";
        if (!rewardMsg.isEmpty()) feedback += rewardMsg;

        JOptionPane.showMessageDialog(this, feedback);
        ProteinComboBox.setSelectedIndex(0);
        CarbohydratesComboBox.setSelectedIndex(0);
        VegetablesComboBox.setSelectedIndex(0);

        updateChallengeState();
        refreshUI();
    }

    private void handleCompleteChallenge() {
        if (currentUser == null) return;
        String rewardMsg = currentUser.gainXP(500);
        DataManager.saveStreakHistoryDate(currentUser, LocalDate.now());
        currentUser.getCurrentStreak().updateStreak(currentUser);
        DataManager.saveUserStreak(currentUser);
        DataManager.saveDailyChallengeState(currentUser, true);
        syncUserData();

        JOptionPane.showMessageDialog(this, "Challenge Completed! +500 XP" + rewardMsg);
        updateChallengeState();
        refreshUI();
    }

    // =========================================================
    // === REFRESH UI ===
    // =========================================================
    private void refreshUI() {
        if (currentUser == null) return;
        HomePageDisplayName.setText("Hi, " + currentUser.getUsername() + "!");
        HomePageDisplayDate.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));

        int sVal = currentUser.getCurrentStreak().getCurrentStreak();
        HMPageStreakCount.setText(String.valueOf(sVal));
        StreakCount.setText(String.valueOf(sVal));

        if (sVal > 0) {
            HMPageStreakCount.setForeground(Color.BLACK);
            StreakCount.setForeground(new Color(255, 204, 0));
            try { if (FIRESTREAK != null) FIRESTREAK.setIcon(new ImageIcon(getClass().getResource("Resources/ACT Streak.png"))); } catch (Exception e) {}
        } else {
            HMPageStreakCount.setForeground(Color.BLACK);
            StreakCount.setForeground(Color.BLACK);
            try { if (FIRESTREAK != null) FIRESTREAK.setIcon(new ImageIcon(getClass().getResource("Resources/INC Streak.png"))); } catch (Exception e) {}
        }

        int level = currentUser.getLevel();
        int currentXP = currentUser.getXp();
        int xpRequiredForNextLevel = (int) (500 * Math.pow(1.5, level - 1));
        long lifetimeXP = 0;
        for (int i = 1; i < level; i++) lifetimeXP += (int) (500 * Math.pow(1.5, i - 1));
        lifetimeXP += currentXP;

        HMPageEXPCount.setText(String.valueOf(lifetimeXP));
        if (HMPageLevelLabel != null) HMPageLevelLabel.setText("Level " + level + " - " + currentUser.getCurrentRank());
        if (HMPageXPFractionLabel != null) HMPageXPFractionLabel.setText(currentXP + " / " + xpRequiredForNextLevel);

        if (xpRequiredForNextLevel > 0) {
            int progress = (int) (((double) currentXP / xpRequiredForNextLevel) * 100);
            HMPageProgressBar.setValue(Math.max(0, Math.min(100, progress)));
        } else {
            HMPageProgressBar.setValue(0);
        }

        updateCalendarColors();
        USERNAME.setText(currentUser.getUsername());
        ACCOUNTID.setText(currentUser.getUserId());
        LevelUP.revalidate();
        LevelUP.repaint();
    }

    private void updateChallengeState() {
        if (currentUser == null) return;

        Chall1.setSelected(DataManager.isWorkoutLoggedToday(currentUser, Chall1.getText()));
        Chall2.setSelected(DataManager.isWorkoutLoggedToday(currentUser, Chall2.getText()));
        Chall3.setSelected(DataManager.isWorkoutLoggedToday(currentUser, Chall3.getText()));
        Chall4.setSelected(DataManager.isWorkoutLoggedToday(currentUser, Chall4.getText()));

        int exercisesCompleted = 0;
        if(Chall1.isSelected()) exercisesCompleted++;
        if(Chall2.isSelected()) exercisesCompleted++;
        if(Chall3.isSelected()) exercisesCompleted++;
        if(Chall4.isSelected()) exercisesCompleted++;

        int burnedCals = DataManager.getCaloriesBurnedToday(currentUser);
        if (ChallPageCaloriesLabel != null) {
            ChallPageCaloriesLabel.setText("Burned: " + burnedCals + " / " + DAILY_CALORIE_BURN_TARGET);
            ChallPageCaloriesLabel.setForeground(burnedCals >= DAILY_CALORIE_BURN_TARGET ? new Color(0, 153, 76) : Color.BLACK);
        }

        double exercisePercent = exercisesCompleted * 20.0;
        double caloriePercent = (Math.min(burnedCals, DAILY_CALORIE_BURN_TARGET) / (double) DAILY_CALORIE_BURN_TARGET) * 20.0;

        int totalProgress = (int) (exercisePercent + caloriePercent);
        if (ChallPageProgressBar != null) ChallPageProgressBar.setValue(totalProgress);

        boolean calGoalMet = burnedCals >= DAILY_CALORIE_BURN_TARGET;

        if (RewardLabel != null && RewardCount != null) {
            RewardLabel.setText("Daily Reward:");
            RewardCount.setText("500 XP");
        }

        boolean claimed = DataManager.isChallengeClaimedToday(currentUser);
        if (CompleteChallengeButton != null) {
            if (claimed) {
                CompleteChallengeButton.setText("Completed Today");
                CompleteChallengeButton.setEnabled(false);
            } else if (exercisesCompleted == 4 && calGoalMet) {
                CompleteChallengeButton.setText("Complete Challenge");
                CompleteChallengeButton.setEnabled(true);
            } else {
                CompleteChallengeButton.setText("In Progress " + totalProgress + "%");
                CompleteChallengeButton.setEnabled(false);
            }
        }
    }

    private void updateCalendarColors() {
        Set<String> dates = DataManager.loadStreakHistoryDates(currentUser);
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        JLabel[] days = {SDay, MDay, TDay, WDay, ThDay, FDay, StDay};

        for (int i = 0; i < 7; i++) {
            if (dates.contains(startOfWeek.plusDays(i).toString())) {
                days[i].setForeground(new Color(255, 204, 0));
            } else {
                days[i].setForeground(Color.BLACK);
            }
        }
    }

    private void showLogsPopup() {
        if (currentUser == null) return;
        JTextArea ta = new JTextArea(20, 50);
        ta.setText("=== WORKOUTS ===\n" + DataManager.readLogFile(currentUser, "Workouts") +
                "\n=== MEALS ===\n" + DataManager.readLogFile(currentUser, "Meals"));
        ta.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(ta), "Logs", JOptionPane.INFORMATION_MESSAGE);
    }

    private void syncUserData() {
        if (currentUser == null) return;
        DataManager.saveUserCredentials(currentUser);
    }

    // PASS USER LEVEL HERE
    private void setupChallengeLogic() {
        int lvl = (currentUser != null) ? currentUser.getLevel() : 1;
        DailyChallenge.ChallengeData todayData = DailyChallenge.getChallengeForToday(lvl);
        DailyChallenge.ExerciseTask[] exercises = todayData.getExercises();

        if (ChallengeTitle != null) ChallengeTitle.setText(todayData.getTitle());

        if (Chall1 != null) {
            Chall1.setText(exercises[0].name); ChallCount1.setText(exercises[0].displayString);
            Chall2.setText(exercises[1].name); ChallCount2.setText(exercises[1].displayString);
            Chall3.setText(exercises[2].name); ChallCount3.setText(exercises[2].displayString);
            Chall4.setText(exercises[3].name); ChallCount4.setText(exercises[3].displayString);

            Chall1.setEnabled(false); Chall2.setEnabled(false); Chall3.setEnabled(false); Chall4.setEnabled(false);
        }
    }

    private void startDayChangeChecker() {
        lastTrackedDate = LocalDate.now();
        new javax.swing.Timer(1000, e -> {
            if (!LocalDate.now().equals(lastTrackedDate)) {
                lastTrackedDate = LocalDate.now();
                setupChallengeLogic();
                updateChallengeState();
                refreshUI();
            }
        }).start();
    }

    private String now() { return java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); }

    private void switchCard(String name) {
        clearAllAuthFields();
        ((CardLayout)LevelUP.getLayout()).show(LevelUP, name);
    }

    private void setupLabelButton(JLabel lbl, String card) {
        if(lbl == null) return;
        lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lbl.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { switchCard(card); refreshUI(); }});
    }

    private void initializeLists() {
        workoutCheckBoxes = Arrays.asList(checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7, checkBox8, checkBox9, checkBox10, checkBox11, checkBox12, checkBox13, checkBox14, checkBox15, checkBox16);
    }

    private void setupLogPageCheckboxes() {
        if (checkBox1 == null) return;
        checkBox1.setText("Push-Ups"); checkBox2.setText("Tricep Dips"); checkBox3.setText("Shoulder Taps"); checkBox4.setText("Superman");
        checkBox5.setText("Sit-Ups"); checkBox6.setText("Russian Twists"); checkBox7.setText("Leg Raises"); checkBox8.setText("Plank");
        checkBox9.setText("Squats"); checkBox10.setText("Lunges"); checkBox11.setText("Calf Raises"); checkBox12.setText("Wall Sit");
        checkBox13.setText("Jumping Jacks"); checkBox14.setText("High Knees"); checkBox15.setText("Burpees"); checkBox16.setText("Mtn Climbers");
    }

    private void setupLogPageComboBoxes() {
        if (ProteinComboBox == null) return;
        ProteinComboBox.removeAllItems(); CarbohydratesComboBox.removeAllItems(); VegetablesComboBox.removeAllItems();
        addItem(ProteinComboBox, "Choose Protein", 0);
        addItem(ProteinComboBox, "Chicken Breast (Grilled)", 165); addItem(ProteinComboBox, "Beef Steak (Lean)", 250);
        addItem(ProteinComboBox, "Grilled Salmon", 200); addItem(ProteinComboBox, "Tofu (Steamed)", 100);
        addItem(ProteinComboBox, "Boiled Eggs (2)", 155); addItem(ProteinComboBox, "Canned Tuna", 130);
        addItem(ProteinComboBox, "Pork Chop", 200); addItem(ProteinComboBox, "Lentils (1 Cup)", 230);
        addItem(CarbohydratesComboBox, "Choose Carbohydrates", 0);
        addItem(CarbohydratesComboBox, "White Rice (1 Cup)", 200); addItem(CarbohydratesComboBox, "Brown Rice (1 Cup)", 215);
        addItem(CarbohydratesComboBox, "Quinoa", 220); addItem(CarbohydratesComboBox, "Pasta", 200);
        addItem(CarbohydratesComboBox, "Sweet Potato", 100); addItem(CarbohydratesComboBox, "Oatmeal", 150);
        addItem(CarbohydratesComboBox, "Whole Wheat Bread", 160); addItem(CarbohydratesComboBox, "Mashed Potatoes", 200);
        addItem(VegetablesComboBox, "Choose Vegetable", 0);
        addItem(VegetablesComboBox, "Steamed Broccoli", 55); addItem(VegetablesComboBox, "Spinach", 20);
        addItem(VegetablesComboBox, "Carrots", 40); addItem(VegetablesComboBox, "Green Beans", 30);
        addItem(VegetablesComboBox, "Asparagus", 20); addItem(VegetablesComboBox, "Bell Peppers", 30);
        addItem(VegetablesComboBox, "Kale Salad", 35); addItem(VegetablesComboBox, "Mixed Green Salad", 25);
    }

    private void addItem(JComboBox<String> box, String n, int c) {
        box.addItem(n);
        calorieMap.put(n, c);
    }

    private void setupNavigation() {
        bindNav(STKBTNHomePage, "StreakPage"); bindNav(CHALBTNHomePage, "ChallPage");
        bindNav(LOGBTNHomePage, "LogPage"); bindNav(ACCBTNHomePage, "AccPage");
        bindNav(HMBTNStreakPage, "HomePage"); bindNav(CHALBTNStreakPage, "ChallPage");
        bindNav(LOGBTNStreakPage, "LogPage"); bindNav(ACCBTNStreakPage, "AccPage");
        bindNav(HMBTNChallPage, "HomePage"); bindNav(STKBTNChallPage, "StreakPage");
        bindNav(LOGBTNChallPage, "LogPage"); bindNav(ACCBTNChallPage, "AccPage");
        bindNav(HMBTNLogPage, "HomePage"); bindNav(STKBTNLogPage, "StreakPage");
        bindNav(CHALBTNLogPage, "ChallPage"); bindNav(ACCBTNLogPage, "AccPage");
        bindNav(HMBTNAccPage, "HomePage"); bindNav(STKBTNAccPage, "StreakPage");
        bindNav(CHALBTNAccPage, "ChallPage"); bindNav(LOGBTNAccPage, "LogPage");
    }

    private void bindNav(JLabel btn, String card) {
        if(btn != null) {
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.addMouseListener(new MouseAdapter(){
                public void mouseClicked(MouseEvent e){ switchCard(card); refreshUI(); }
            });
        }
    }

    private void setupCustomFonts() {
        // Define the Fonts
        Font bold18 = new Font("SansSerif", Font.BOLD, 18);
        Font bold14 = new Font("SansSerif", Font.BOLD, 14);
        Font plain18 = new Font("SansSerif", Font.PLAIN, 18);
        Font bold26 = new Font("SansSerif", Font.BOLD, 26);
        Font bold32 = new Font("SansSerif", Font.BOLD, 32);
        Font bold42 = new Font("SansSerif", Font.BOLD, 42);
        Font bold62 = new Font("SansSerif", Font.BOLD, 100);

        // --- Bold, Size: 18 (Buttons) ---
        if (SignInPageBT != null) SignInPageBT.setFont(bold18);
        if (CreateAccPageBT != null) CreateAccPageBT.setFont(bold18);
        if (CreateAccountBT != null) CreateAccountBT.setFont(bold18);
        if (SignInBT != null) SignInBT.setFont(bold18);
        if (ChangePasswordButton != null) ChangePasswordButton.setFont(bold18);
        if (CompleteChallengeButton != null) CompleteChallengeButton.setFont(bold18);
        if (SignOutButton != null) SignOutButton.setFont(bold18);

        // --- Regular, Size: 18 (TextFields & Placeholders) ---
        if (CreateUserTextField != null) CreateUserTextField.setFont(plain18);
        if (CreatePassTextField != null) CreatePassTextField.setFont(plain18);
        if (CheckUsernameField != null) CheckUsernameField.setFont(plain18);
        if (CheckPassField != null) CheckPassField.setFont(plain18);
        if (EnterUsernameTextField != null) EnterUsernameTextField.setFont(plain18);
        if (EnterNewPasswordTextField != null) EnterNewPasswordTextField.setFont(plain18);

        // --- Bold, Size: 18 (Labels) ---
        if (ACCOUNTID != null) ACCOUNTID.setFont(bold18);
        if (HomePageDisplayDate != null) HomePageDisplayDate.setFont(bold14);

        // --- Bold, Size: 32 ---
        if (HomePageDisplayName != null) HomePageDisplayName.setFont(bold32);
        if (USERNAME != null) USERNAME.setFont(bold32);

        // --- Bold, Size: 26 ---
        if (HMPageLevelLabel != null) HMPageLevelLabel.setFont(bold26);
        if (ChallengeTitle != null) ChallengeTitle.setFont(bold26);

        // --- Bold, Size: 42 ---
        if (HMPageStreakCount != null) HMPageStreakCount.setFont(bold42);
        if (HMPageEXPCount != null) HMPageEXPCount.setFont(bold42);

        // --- Bold, Size: 62 ---
        if (StreakCount != null) StreakCount.setFont(bold62);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); } catch (Exception e) {}
            new LevelUP();
        });
    }

    private void createUIComponents() {
        // Define your desired styles here
        int radius = 30;
        int btnRadius = 25;
        int sheetRadius = 35;

        // === 1. TOP ROUNDED PANELS (New) ===
        // These will be round on top, square on bottom
        CPassPanel = new TopRoundedPanel(sheetRadius);
        StartPanel = new TopRoundedPanel(sheetRadius);
        CreateAccPanel = new TopRoundedPanel(sheetRadius);
        SignInPanel = new TopRoundedPanel(sheetRadius);

        // === 2. BOTTOM ROUNDED PANELS (New) ===
        AccPanel = new BottomRoundedPanel(sheetRadius);

        // --- HOME PAGE PANELS ---
        PanelTopHMPage = new RoundedPanel(radius);
        PanelBot1HMPage = new RoundedPanel(radius);
        PanelBot2HMPage = new RoundedPanel(radius);

        // --- STREAK & CHALLENGE PAGE PANELS ---
        PanelStreakPage = new RoundedPanel(radius);
        RewardPanel = new RoundedPanel(radius);
        Challenge = new RoundedPanel(radius);

        // --- LOG PAGE PANELS ---
        WorkoutPanel = new RoundedPanel(radius);
        MealPanel = new RoundedPanel(radius);

        // --- Tab PANELS ---
        HomeTab = new RoundedPanel(radius);
        StreakTab = new RoundedPanel(radius);
        ChallTab = new RoundedPanel(radius);
        LogTab = new RoundedPanel(radius);
        AccTab = new RoundedPanel(radius);

        // --- BUTTONS ---
        SignInPageBT = new RoundedButton(btnRadius);
        CreateAccPageBT = new RoundedButton(btnRadius);
        SignInBT = new RoundedButton(btnRadius);
        CreateAccountBT = new RoundedButton(btnRadius);
        ChangePasswordButton = new RoundedButton(btnRadius);
        SignOutButton = new RoundedButton(btnRadius);
        CompleteChallengeButton = new RoundedButton(btnRadius);

        // --- REGULAR TEXT FIELDS (Usernames) ---
        CreateUserTextField = new RoundedTextField(btnRadius);
        CheckUsernameField = new RoundedTextField(btnRadius);
        EnterUsernameTextField = new RoundedTextField(btnRadius);
        CreatePassTextField = new RoundedTextField(btnRadius);
        EnterNewPasswordTextField = new RoundedTextField(btnRadius);

        // --- PASSWORD FIELDS (MUST BE RoundedPasswordField) ---
        CheckPassField = new RoundedPasswordField(btnRadius);
    }
}