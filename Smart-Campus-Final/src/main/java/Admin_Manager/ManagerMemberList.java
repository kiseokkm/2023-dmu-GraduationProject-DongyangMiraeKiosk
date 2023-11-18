package Admin_Manager;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManagerMemberList extends JFrame {
    private JTable memberTable;
    private DefaultTableModel memberTableModel;
    private JButton refreshButton;
    
    public ManagerMemberList() {
        setTitle("회원 목록");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        pack(); 
    }
    private void initComponents() {
    	String[] columnNames = {"Username", "Password", "Major", "Student ID", "Name", "Phone Number", "Most Precious Thing"};
        memberTableModel = new DefaultTableModel(columnNames, 0);
        memberTable = new JTable(memberTableModel);
        JScrollPane scrollPane = new JScrollPane(memberTable);

        memberTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = memberTable.getSelectedRow();
                    String username = (String) memberTableModel.getValueAt(selectedRow, 0);
                    String password = (String) memberTableModel.getValueAt(selectedRow, 1);
                    String major = (String) memberTableModel.getValueAt(selectedRow, 2);
                    String studentId = (String) memberTableModel.getValueAt(selectedRow, 3);
                    String name = (String) memberTableModel.getValueAt(selectedRow, 4);
                    String phoneNumber = (String) memberTableModel.getValueAt(selectedRow, 5);
                    String mostPreciousThing = (String) memberTableModel.getValueAt(selectedRow, 6);

                    MemberEditDialog dialog = new MemberEditDialog(ManagerMemberList.this, username,password, major, studentId, name, phoneNumber, mostPreciousThing);
                    dialog.setVisible(true);
                    loadMembersIntoTable();
                }
            }
        });
        refreshButton = new JButton("새로고침");
        refreshButton.addActionListener(e -> {
            loadMembersIntoTable();
        });
        JButton editButton = new JButton("수정하기");
        editButton.addActionListener(e -> {
            try {
                int selectedRow = memberTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(ManagerMemberList.this, "수정할 회원을 선택하세요.", "알림", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String username = (String) memberTableModel.getValueAt(selectedRow, 0);
                String password = (String) memberTableModel.getValueAt(selectedRow, 1);
                String major = (String) memberTableModel.getValueAt(selectedRow, 2);
                String studentId = (String) memberTableModel.getValueAt(selectedRow, 3);
                String name = (String) memberTableModel.getValueAt(selectedRow, 4);
                String phoneNumber = (String) memberTableModel.getValueAt(selectedRow, 5);
                String mostPreciousThing = (String) memberTableModel.getValueAt(selectedRow, 6);

                MemberEditDialog dialog = new MemberEditDialog(ManagerMemberList.this, username, password, major, studentId, name, phoneNumber, mostPreciousThing);
                dialog.setVisible(true);
                loadMembersIntoTable();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ManagerMemberList.this, "오류가 발생했습니다.", "에러", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton deleteButton = new JButton("탈퇴하기");
        deleteButton.addActionListener(e -> {
            int selectedRow = memberTable.getSelectedRow();
            if (selectedRow != -1) {
                String username = (String) memberTableModel.getValueAt(selectedRow, 0);
                int response = JOptionPane.showConfirmDialog(ManagerMemberList.this, "정말로 이 회원을 삭제하시겠습니까?", "회원 삭제", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    deleteMember(username);
                    loadMembersIntoTable();
                }
            }
        });
        JPanel northPanel = new JPanel();
        northPanel.add(refreshButton);
        northPanel.add(editButton);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadMembersIntoTable();
    }
    private void deleteMember(String username) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "DELETE FROM user1 WHERE username=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void loadMembersIntoTable() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT username, major, studentId, name, phoneNumber, mostPreciousThing, password FROM user1";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            memberTableModel.setRowCount(0); 
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String major = rs.getString("major");
                String studentId = rs.getString("studentId");
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phoneNumber");
                String mostPreciousThing = rs.getString("mostPreciousThing");
                memberTableModel.addRow(new Object[]{username, password, major, studentId, name, phoneNumber, mostPreciousThing});
                
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	public class MemberEditDialog extends JDialog {
	    private JComboBox<String> comboMajor;
	    private JTextField studentIdField, nameField, phoneNumberField, preciousThingField;
	    private JButton updateButton, deleteButton;
	    private String username;
	    private JTextField usernameField;  
	    private JTextField passwordField;

	    public MemberEditDialog(JFrame parent, String username, String password, String major, String studentId, String name, String phoneNumber, String mostPreciousThing) {
	        super(parent, "회원 정보 수정", true);
	        this.username = username;

	        comboMajor = new JComboBox<String>();
	        String[] majors = {
	            "기계공학과", "기계설계공학과", "로봇공학과", "자동화공학과", "전기공학과", 
	            "정보전자공학과", "반도체전자공학과", "정보통신공학과", "소방안전관리과",
	            "컴퓨터소프트웨어공학과", "컴퓨터정보공학과", "인공지능소프트웨어공학과", 
	            "생명화학공학과", "바이오융합공학과", "건축과", "실내건축디자인과", "시각디자인과", 
	            "경영학과", "세무회계학과", "유통마케팅학과", "호텔관광학과", "경영정보학과", "빅데이터경영과"
	        };
	        for (String m : majors) {
	            comboMajor.addItem(m);
	        }
	        comboMajor.setSelectedItem(major); 
	        
	        usernameField = new JTextField(username, 15);
	        passwordField = new JTextField(password, 15);
	        studentIdField = new JTextField(studentId, 15);
	        nameField = new JTextField(name, 15);
	        phoneNumberField = new JTextField(phoneNumber, 15);
	        preciousThingField = new JTextField(mostPreciousThing, 15);

	        updateButton = new JButton("수정하기");
	        updateButton.addActionListener(e -> updateMemberInfo());

	        deleteButton = new JButton("탈퇴하기");
	        deleteButton.addActionListener(e -> deleteMember());

	        JPanel formPanel = new JPanel();
	        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
	        formPanel.add(createEntryPanel("Username:", usernameField));  
	        formPanel.add(createEntryPanel("Password:", passwordField));
	        formPanel.add(createEntryPanel("Major:",comboMajor )); 
	        formPanel.add(createEntryPanel("Student ID:", studentIdField));
	        formPanel.add(createEntryPanel("Name:", nameField));
	        formPanel.add(createEntryPanel("Phone Number:", phoneNumberField));
	        formPanel.add(createEntryPanel("Most Precious Thing:", preciousThingField));

	        JPanel buttonPanel = new JPanel();
	        buttonPanel.add(updateButton);
	        buttonPanel.add(deleteButton);

	        setLayout(new BorderLayout());
	        add(formPanel, BorderLayout.CENTER);
	        add(buttonPanel, BorderLayout.SOUTH);

	        pack();
	        setLocationRelativeTo(parent);
	    }

	    private JPanel createEntryPanel(String labelText, JComponent component) {
	        JPanel entryPanel = new JPanel(new BorderLayout());
	        entryPanel.add(new JLabel(labelText), BorderLayout.WEST);
	        entryPanel.add(component, BorderLayout.CENTER); 
	        return entryPanel;
	    }

	    private void updateMemberInfo() {
	        try {
	            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
	            
	            String sql = "UPDATE user1 SET username=?, password=?, major=?, studentId=?, name=?, phoneNumber=?, mostPreciousThing=? WHERE username=?";
	            PreparedStatement statement = connection.prepareStatement(sql);
	            
	            statement.setString(1, usernameField.getText());
	            statement.setString(2, passwordField.getText());
	            statement.setString(3, comboMajor.getSelectedItem().toString());
	            statement.setString(4, studentIdField.getText());
	            statement.setString(5, nameField.getText());
	            statement.setString(6, phoneNumberField.getText());
	            statement.setString(7, preciousThingField.getText());
	            statement.setString(8, username);  // 원래의 username을 사용하여 매칭
	            
	            statement.executeUpdate();
	            statement.close();
	            connection.close();
	            dispose();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }
        private void deleteMember() {
            int response = JOptionPane.showConfirmDialog(this, "정말로 이 회원을 삭제하시겠습니까?", "회원 삭제", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                ManagerMemberList.this.deleteMember(username);  
                dispose();  
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerMemberList managerMemberList = new ManagerMemberList();
            managerMemberList.setVisible(true);
        });
    }
}