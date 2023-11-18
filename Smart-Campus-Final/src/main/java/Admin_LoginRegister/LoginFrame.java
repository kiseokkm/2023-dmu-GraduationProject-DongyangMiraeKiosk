package Admin_LoginRegister;

import javax.swing.*;

import Admin.AdminFrame;
import Admin_Manager.ManagerDashboard;
import Admin_Manager.ManagerHome;
import models.User1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import services.DatabaseService;

public class LoginFrame extends javax.swing.JFrame {
	
    private static final String MANAGER_USERNAME = "manager";
    private static final String MANAGER_PASSWORD = "password123";
    private DatabaseService dbService;

    public LoginFrame() {
        dbService = new DatabaseService(); 
        initComponents();
        app.Global.setAppIcon(this);
    }
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        
        pnlContainer = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();
        btnFindId = new javax.swing.JButton();
        btnFindPassword = new javax.swing.JButton();
        
        Color buttonColor = new Color(255,255,255);

        btnLogin.setBackground(buttonColor);
        btnRegister.setBackground(buttonColor);
        btnFindId.setBackground(buttonColor);
        btnFindPassword.setBackground(buttonColor);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login");
        setName("");
        setPreferredSize(new java.awt.Dimension(720, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());
        
        this.getContentPane().setBackground(new Color(96, 140, 255));
        pnlContainer.setPreferredSize(new java.awt.Dimension(400, 200));
        pnlContainer.setBackground(new Color(96, 140, 255));
        java.awt.GridBagLayout pnlFormLayout = new java.awt.GridBagLayout();
        pnlFormLayout.columnWidths = new int[]{100, 150};
        pnlFormLayout.rowHeights = new int[]{50, 0, 60, 40};
        pnlContainer.setLayout(pnlFormLayout);

        lblTitle.setFont(lblTitle.getFont().deriveFont(lblTitle.getFont().getSize() + 10f));
        lblTitle.setText("Welcome Back");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        pnlContainer.add(lblTitle, gridBagConstraints);

        lblUsername.setLabelFor(txtUsername);
        lblUsername.setText("ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        pnlContainer.add(lblUsername, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlContainer.add(txtUsername, gridBagConstraints);

        lblPassword.setLabelFor(txtPassword);
        lblPassword.setText("Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        pnlContainer.add(lblPassword, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        pnlContainer.add(txtPassword, gridBagConstraints);

        btnLogin.setText("Log in");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0; 
        gridBagConstraints.gridy = 3; 
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; 
        gridBagConstraints.insets = new Insets(10, 0, 10, 5); 
        gridBagConstraints.weightx = 0.5; 
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END; 
        pnlContainer.add(btnLogin, gridBagConstraints);

        btnRegister.setText("Register");
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1; 
        gridBagConstraints.gridy = 3; 
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(10, 0, 10, 5); 
        gridBagConstraints.weightx = 0.5; 
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START; 
        pnlContainer.add(btnRegister, gridBagConstraints);

        getContentPane().add(pnlContainer, new java.awt.GridBagConstraints());

        getAccessibleContext().setAccessibleName("");

        pack();
        setLocationRelativeTo(null);
        
        btnFindId.setText("아이디 찾기");
        btnFindId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FindIdFrame findIdFrame = new FindIdFrame();
                findIdFrame.setVisible(true);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0; // 첫 번째 열
        gridBagConstraints.gridy = 4; // 다섯 번째 행
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; // 가로로 채우기
        gridBagConstraints.insets = new Insets(10, 0, 10, 5); // 상하 좌우 여백 (위, 왼쪽, 아래, 오른쪽)
        gridBagConstraints.weightx = 1.0; // 추가 공간 분배 비율
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START; // 왼쪽 정렬
        pnlContainer.add(btnFindId, gridBagConstraints);
        btnFindPassword.setText("비밀번호 찾기");
        btnFindPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FindPasswordFrame findPasswordFrame = new FindPasswordFrame();
                findPasswordFrame.setVisible(true);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1; 
        gridBagConstraints.gridy = 4; 
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL; 
        gridBagConstraints.insets = new Insets(10, 0, 10, 5); 
        gridBagConstraints.weightx = 1.0; 
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START; 
        pnlContainer.add(btnFindPassword, gridBagConstraints);
        
        btnLogin.setForeground(Color.BLACK);
        btnRegister.setForeground(Color.BLACK);
        btnFindId.setForeground(Color.BLACK);
        btnFindPassword.setForeground(Color.BLACK);
    }
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        if (getValidFields()) {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            if (MANAGER_USERNAME.equals(username) && MANAGER_PASSWORD.equals(password)) {
                new ManagerHome().setVisible(true); 
                dispose();
                return;
            }
            if (authenticateUser(username, password)) {
                new AdminFrame(username).setVisible(true);
                dispose();
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "로그인 실패", "Login alert", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private boolean authenticateUser(String username, String password) {
        try {
            dbService.connect();
            String sql = "SELECT * FROM user1 WHERE username = ? AND password = ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            boolean isAuthenticated = resultSet.next();
            resultSet.close();
            statement.close();
            return isAuthenticated;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbService.disconnect(); // 연결 해제
        }
    }
    
    private Properties loadDatabaseProperties() {
        Properties properties = new Properties();
        return properties;
    }
    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
        dispose();
    }
    private boolean getValidFields() {
        if (txtUsername.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "ID를 입력하세요", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (new String(txtPassword.getPassword()).isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "비밀번호를 입력하세요", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            return true;
        }
    }
    private void btnFindIdActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "아이디 찾기 기능 구현!");
    }
    public String getLoggedInUserMajor(String username) {
        String major = "";
        try {
            dbService.connect();
            String sql = "SELECT major FROM user1 WHERE username = ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                major = resultSet.getString("major");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return major;
    }
    public static void main(String args[]) {
        app.Global.setDefaultTheme();
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel pnlContainer;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JButton btnFindId;
    private javax.swing.JButton btnFindPassword;
}