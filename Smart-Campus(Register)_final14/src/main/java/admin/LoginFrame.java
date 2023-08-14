package admin;

import javax.swing.*;

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

public class LoginFrame extends javax.swing.JFrame {

    private services.AuthService authService;

    /**
     * Creates new form LoginFrame
     */
    public LoginFrame() {
        initComponents();
        app.Global.setAppIcon(this);
        authService = new services.AuthService();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Login");
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(720, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        pnlContainer.setPreferredSize(new java.awt.Dimension(400, 200));
        java.awt.GridBagLayout pnlFormLayout = new java.awt.GridBagLayout();
        pnlFormLayout.columnWidths = new int[]{100, 250};
        pnlFormLayout.rowHeights = new int[]{60, 0, 60, 40};
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
        lblUsername.setText("Username");
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        pnlContainer.add(btnRegister, gridBagConstraints);

        getContentPane().add(pnlContainer, new java.awt.GridBagConstraints());

        getAccessibleContext().setAccessibleName("");

        pack();
        setLocationRelativeTo(null);
    }

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        if (getValidFields()) {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            // 사용자 인증 확인
            if (authenticateUser(username, password)) {
                new AdminFrame().setVisible(true);
                dispose();
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "로그인 실패!!", "Login alert", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean authenticateUser(String username, String password) {
        try {
            // 데이터베이스 연결
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");

            // 사용자 인증 쿼리 실행
            String sql = "SELECT * FROM user1 WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            // 적절한 예외 처리 및 반환 값 결정
            if (resultSet.next()) {
                // 일치하는 사용자가 존재함
                return true;
            } else {
                // 일치하는 사용자가 없음
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 예외 발생 시 인증 실패 처리
            return false;
        }
    }

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {
        RegisterFrame registerFrame = new RegisterFrame();
        registerFrame.setVisible(true);
        dispose();
    }

    private boolean getValidFields() {
        if (txtUsername.getText().isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Username is required", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (new String(txtPassword.getPassword()).isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(null, "Password is required", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set default theme */
        app.Global.setDefaultTheme();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel pnlContainer;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration
}
