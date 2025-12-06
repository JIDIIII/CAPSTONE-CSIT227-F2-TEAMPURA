import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Test extends JFrame{
    private JPanel LevelUP;
    private JPanel Start;
    private JPanel CreateAcc;
    private JPanel SignIn;
    private JPanel ForgotPass;
    private JPanel HomePage;
    private JPanel StreakPage;
    private JPanel ChallPage;
    private JPanel LogPage;
    private JPanel AccPage;
    private JButton SignInPageBT;
    private JButton CreateAccPageBT;
    private JTextField CreateUserTextField;
    private JTextField CreatePassTextField;
    private JButton CreateAccountBT;
    private JTextField CheckUsernameField;
    private JButton SignInBT;
    private JTextField CheckPassField;
    private JTextField EnterUsernameTextField;
    private JTextField EnterNewPasswordTextField;
    private JButton ChangePasswordButton;
    private JLabel HomePageDisplayName;
    private JLabel HomePageDisplayDate;
    private JLabel STKBTNHomePage;
    private JLabel CHALBTNHomePage;
    private JLabel LOGBTNHomePage;
    private JLabel ACCBTNHomePage;
    private JLabel HMBTNStreakPage;
    private JLabel CHALBTNStreakPage;
    private JLabel LOGBTNStreakPage;
    private JLabel ACCBTNStreakPage;
    private JLabel HMBTNChallPage;
    private JLabel STKBTNChallPage;
    private JLabel LOGBTNChallPage;
    private JLabel ACCBTNChallPage;
    private JLabel HMBTNLogPage;
    private JLabel STKBTNLogPage;
    private JLabel CHALBTNLogPage;
    private JLabel ACCBTNLogPage;
    private JLabel HMBTNAccPage;
    private JLabel STKBTNAccPage;
    private JLabel CHALBTNAccPage;
    private JLabel LOGBTNAccPage;
    private JLabel SignInForgotPassword;
    private JLabel FIRESTREAK;
    private JLabel USERNAME;
    private JLabel ACCOUNTID;


    Test(){
        setContentPane(LevelUP);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        List<JLabel> STKBTNs = Arrays.asList(
                STKBTNHomePage, STKBTNChallPage, STKBTNLogPage, STKBTNAccPage
        );

        List<JLabel> CHALBTNs = Arrays.asList(
                CHALBTNHomePage, CHALBTNStreakPage, CHALBTNLogPage, CHALBTNAccPage
        );

        List<JLabel> LOGBTNs = Arrays.asList(
                LOGBTNHomePage, LOGBTNStreakPage, LOGBTNChallPage, LOGBTNAccPage
        );

        List<JLabel> HMBTNs = Arrays.asList(
                HMBTNStreakPage, HMBTNChallPage, HMBTNLogPage, HMBTNAccPage
        );

        List<JLabel> ACCBTNs = Arrays.asList(
                ACCBTNHomePage, ACCBTNStreakPage, ACCBTNChallPage, ACCBTNLogPage
        );

        List<JLabel> allButtons = new ArrayList<>();
        allButtons.addAll(STKBTNs);
        allButtons.addAll(CHALBTNs);
        allButtons.addAll(LOGBTNs);
        allButtons.addAll(HMBTNs);
        allButtons.addAll(ACCBTNs);

        MouseAdapter goToHomePage = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) LevelUP.getLayout();
                cl.show(LevelUP, "HomePage");
            }
        };

        MouseAdapter goToStreakPage = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) LevelUP.getLayout();
                cl.show(LevelUP, "StreakPage");
            }
        };

        MouseAdapter goToChallengePage = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) LevelUP.getLayout();
                cl.show(LevelUP, "ChallPage");
            }
        };

        MouseAdapter goToLogPage = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) LevelUP.getLayout();
                cl.show(LevelUP, "LogPage");
            }
        };

        MouseAdapter goToAccountPage = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) LevelUP.getLayout();
                cl.show(LevelUP, "AccPage");
            }
        };

        for (JLabel lbl : HMBTNs) {
            lbl.addMouseListener(goToHomePage);
        }

        for (JLabel lbl : STKBTNs) {
            lbl.addMouseListener(goToStreakPage);
        }

        for (JLabel lbl : CHALBTNs) {
            lbl.addMouseListener(goToChallengePage);
        }

        for (JLabel lbl : LOGBTNs) {
            lbl.addMouseListener(goToLogPage);
        }

        for (JLabel lbl : ACCBTNs) {
            lbl.addMouseListener(goToAccountPage);
        }

        for (JLabel lbl : allButtons) {
            lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        SignInPageBT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) LevelUP.getLayout();
                cl.show(LevelUP, "SignIn");
            }
        });

        CreateAccPageBT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) LevelUP.getLayout();
                cl.show(LevelUP, "CreateAcc");
            }
        });

        SignInForgotPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                CardLayout cl = (CardLayout) LevelUP.getLayout();
                cl.show(LevelUP, "ForgotPass");
                SignInForgotPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        });
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            try {
                // Option A: Nimbus (modern cross-platform)
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
                // Option B: Force Metal
                // UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

                // Option C: System L&F
                // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Decorate frames with the chosen L&F (call before creating frames)
                JFrame.setDefaultLookAndFeelDecorated(true);

            } catch (Exception e) {
                // If setting L&F fails, continue with default
                System.err.println("Failed to set Look and Feel: " + e.getMessage());
            }

            // Create and show UI on EDT
            Test frame = new Test();
            frame.setVisible(true);
        });
    }
}
