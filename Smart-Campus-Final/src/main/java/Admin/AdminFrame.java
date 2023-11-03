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

public class AdminFrame extends javax.swing.JFrame {
    
    private JButton profileButton;
    private String currentLoggedInUsername; 
    private String currentLoggedInUserMajor = "전체"; // default value

    public AdminFrame(String loggedInUsername) {
        this.currentLoggedInUsername = loggedInUsername; 
        this.currentLoggedInUserMajor = getUserMajor(loggedInUsername); // 로그인한 사용자의 전공
        initComponents();
        app.Global.setAppIcon(this);
        initProfileButton();
    }

    private String getUserMajor(String username) {
        // 여기에서 DB에서 사용자의 전공을 가져옵니다.
        String major = "전체";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT major FROM user1 WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                major = rs.getString("major");
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return major;
    }

    private void initProfileButton() {
        profileButton = new JButton("@");
        profileButton.setPreferredSize(new Dimension(50, 25)); // 버튼의 크기 설정
        profileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProfileUpdateDelete.ShowProfile(currentLoggedInUsername); // 현재 로그인한 사용자의 username을 전달
            }
        });
        // JTabbedPane의 맨 오른쪽에 버튼을 추가
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(profileButton, BorderLayout.EAST);
        tabbedPane.addTab("", null); // 빈 탭 추가
        tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, buttonPanel); // 마지막 빈 탭에 버튼 추가
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        tabbedPane = new javax.swing.JTabbedPane();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Admin");
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(1080, 600));
        
        // 탭들을 추가
        tabbedPane.addTab("시간표", new TimetablePanel(currentLoggedInUsername));
        tabbedPane.addTab("학교에 바란다", new UnivHope());
        tabbedPane.addTab("학과 공지사항", new LoginNoticePanel(currentLoggedInUserMajor));
        tabbedPane.addTab("분실물찾기", new LostThings());
        tabbedPane.addTab("사용자 목록", new UsersPanel());

        getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);
        getAccessibleContext().setAccessibleName("");
        pack();
        setLocationRelativeTo(null);
        
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedComponent() instanceof LoginNoticePanel) {
                    LoginNoticePanel.resetNoticeLoadedFlag(); // 플래그 초기화
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
