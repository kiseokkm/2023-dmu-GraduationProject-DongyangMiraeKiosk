package admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AdminFrame extends javax.swing.JFrame {
		
	 private JButton profileButton;
	 private String currentLoggedInUsername; // 현재 로그인한 사용자의 아이디를 저장할 필드 추가
	 
	    public AdminFrame(String loggedInUsername) {
	        this.currentLoggedInUsername = loggedInUsername; // 로그인한 사용자의 아이디 저장

	        initComponents();
	        app.Global.setAppIcon(this);
	        tabbedPane.addTab("수강중인 강좌", new OrdersPanel());
	        tabbedPane.addTab("시간표", new ItemsPanel());
	        tabbedPane.addTab("과제 및 수업 자료 알림", new CategoriesPanel());
	        tabbedPane.addTab("사용자 목록", new UsersPanel());
	        
	        initProfileButton();
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

	    // 버튼을 JTabbedPane의 우측 상단에 배치
	    JPanel rightUpperPanel = new JPanel(new BorderLayout());
	    rightUpperPanel.setOpaque(false);
	    rightUpperPanel.add(profileButton, BorderLayout.EAST);
	    tabbedPane.setTabComponentAt(tabbedPane.getTabCount() - 1, rightUpperPanel);
	}
  @SuppressWarnings("unchecked")
  private void initComponents() {
    tabbedPane = new javax.swing.JTabbedPane();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Admin");
    setName(""); // NOI18N
    setPreferredSize(new java.awt.Dimension(720, 600));
    getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);

    getAccessibleContext().setAccessibleName("");

    pack();
    setLocationRelativeTo(null);
  }

 
  
  
  
  
  
  
  
  public static void main(String args[]) {
	    app.Global.setDefaultTheme();

	    java.awt.EventQueue.invokeLater(() -> {
	      new AdminFrame("defaultUser").setVisible(true); 
	    });
	}
  private javax.swing.JTabbedPane tabbedPane;
}