package Admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Admin_Login_Notice.LoginNoticePanel;
import Admin_LostThi.LostThings;
import Admin_UnivHope.UnivHope;
import services.DatabaseService;

public class AdminFrame extends javax.swing.JFrame {
    
    private JButton profileButton;
    private String currentLoggedInUsername; 
    private String currentLoggedInUserMajor = "전체"; 
    private String currentLoggedInName;
    private DatabaseService dbService;
 
    public AdminFrame(String loggedInUsername) {
        String[] userInfo = getUserInfo(loggedInUsername);
        this.currentLoggedInUsername = loggedInUsername; 
        this.currentLoggedInUserMajor = userInfo[0]; 
        this.currentLoggedInName = userInfo[1];
        initComponents();
        app.Global.setAppIcon(this);
        initProfileButton();
    }
    private String[] getUserInfo(String username) {
        String major = "전체";
        String name = "";
        try {
            dbService.connect();
            String sql = "SELECT major, name FROM user1 WHERE Username = ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                major = rs.getString("major");
                name = rs.getString("name");
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return new String[] { major, name };
    }
    private void initProfileButton() {
        profileButton = new JButton("@");
        profileButton.setPreferredSize(new Dimension(50, 25));
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfileUpdateDelete.ShowProfile(currentLoggedInUsername); 
            }
        });
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(profileButton, BorderLayout.EAST);
        tabbedPane.addTab("", null);
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, buttonPanel); 
    }
    @SuppressWarnings("unchecked")
    private void initComponents() {
        tabbedPane = new javax.swing.JTabbedPane();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Admin");
        setName("");
        setPreferredSize(new java.awt.Dimension(1080, 600));
        
        tabbedPane.addTab("시간표", new TimetablePanel(currentLoggedInUsername));
        tabbedPane.addTab("학교에 바란다", new UnivHope(currentLoggedInName));
        tabbedPane.addTab("학과 공지사항", new LoginNoticePanel(currentLoggedInUserMajor));
        tabbedPane.addTab("분실물찾기", new LostThings(currentLoggedInName));

        getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);
        getAccessibleContext().setAccessibleName("");
        pack();
        setLocationRelativeTo(null);
        
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedComponent() instanceof LoginNoticePanel) {
                    LoginNoticePanel.resetNoticeLoadedFlag();
                    LoginNoticePanel.showNoticeTableOnPanel((LoginNoticePanel) tabbedPane.getSelectedComponent());
                }
            }
        });
    }
    public static void main(String args[]) {
        app.Global.setDefaultTheme();
        java.awt.EventQueue.invokeLater(() -> {
            new AdminFrame("defaultUser").setVisible(true); 
        });
    }
    private javax.swing.JTabbedPane tabbedPane;
}