package admin;

import javax.swing.*;

import models.User1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterFrame extends JFrame {
    private JTextField txtUsername;
    private JButton btnCheckDuplicate;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtMajor;
    private JTextField txtStudentId;
    private JTextField txtEmail;
    private JTextField txtPhoneNumber;
    private JButton btnRegister;
    private JButton btnCancel;

    public RegisterFrame() {
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("회원가입");
        setSize(500, 500);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 3, 5, 5));

        JLabel lblUsername = new JLabel("아이디:");
        JLabel lblPassword = new JLabel("비밀번호:");
        JLabel lblConfirmPassword = new JLabel("비밀번호 확인:");
        JLabel lblMajor = new JLabel("학과:");
        JLabel lblStudentId = new JLabel("학번:");
        JLabel lblEmail = new JLabel("이메일:");
        JLabel lblPhoneNumber = new JLabel("전화번호:");

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtConfirmPassword = new JPasswordField(20);
        txtMajor = new JTextField(20);
        txtStudentId = new JTextField(20);
        txtEmail = new JTextField(20);
        txtPhoneNumber = new JTextField(20);

        btnCheckDuplicate = new JButton("중복확인");
        btnCheckDuplicate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                boolean isDuplicate = checkDuplicate(username);

                if (isDuplicate) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "중복된 아이디입니다.", "중복확인", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "사용 가능한 아이디입니다.", "중복확인", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        btnRegister = new JButton("확인");
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());
                String confirmPassword = new String(txtConfirmPassword.getPassword());
                String major = txtMajor.getText();
                String studentId = txtStudentId.getText();
                String email = txtEmail.getText();
                String phoneNumber = txtPhoneNumber.getText();

                // 입력값 유효성 검사
                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "비밀번호가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User1 user1 = new User1();
                user1.setUsername(username);
                user1.setPassword(password);
                user1.setMajor(major);
                user1.setStudentId(studentId);
                user1.setEmail(email);
                user1.setPhoneNumber(phoneNumber);

                boolean isSaved = saveUser1ToDatabase(user1);
                if (isSaved) {
                    dispose();
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(RegisterFrame.this, "회원가입에 실패하였습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }

            private boolean saveUser1ToDatabase(User1 user1) {
                try {
                    // 데이터베이스 연동 코드 작성
                    // 예시: JDBC를 사용한 데이터베이스 연동 코드
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
                    
                    // INSERT 쿼리 실행
                    String sql = "INSERT INTO user1 (username, password, major, studentId, email, phoneNumber) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, user1.getUsername());
                    statement.setString(2, user1.getPassword());
                    statement.setString(3, user1.getMajor());
                    statement.setString(4, user1.getStudentId());
                    statement.setString(5, user1.getEmail());
                    statement.setString(6, user1.getPhoneNumber());

                    int rowsAffected = statement.executeUpdate();

                    // 적절한 예외 처리 및 반환 값 결정
                    if (rowsAffected > 0) {
                        // 저장 성공
                        return true;
                    } else {
                        // 저장 실패
                        return false;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    // 예외 발생 시 저장 실패 처리
                    return false;
                }
            }
        });


        btnCancel = new JButton("취소");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(btnCheckDuplicate);
        panel.add(lblPassword);
        panel.add(txtPassword);
        panel.add(new JLabel()); // Empty Label for spacing
        panel.add(lblConfirmPassword);
        panel.add(txtConfirmPassword);
        panel.add(new JLabel()); // Empty Label for spacing
        panel.add(lblMajor);
        panel.add(txtMajor);
        panel.add(new JLabel()); // Empty Label for spacing
        panel.add(lblStudentId);
        panel.add(txtStudentId);
        panel.add(new JLabel()); // Empty Label for spacing
        panel.add(lblEmail);
        panel.add(txtEmail);
        panel.add(new JLabel()); // Empty Label for spacing
        panel.add(lblPhoneNumber);
        panel.add(txtPhoneNumber);
        panel.add(new JLabel()); // Empty Label for spacing
        panel.add(new JLabel()); // Empty Label for spacing
        panel.add(btnRegister);
        panel.add(btnCancel);

        add(panel);
    }

    private boolean checkDuplicate(String username) {
        // Check duplicate logic here
        // Placeholder logic
        return username.equals("admin");
    }

    private boolean saveUserToDatabase(User1 user1) {
        try {
            // 데이터베이스 연동 코드 작성
            // 예시: JDBC를 사용한 데이터베이스 연동 코드
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            Statement statement = connection.createStatement();

            // INSERT 쿼리 실행
            String sql = "INSERT INTO users (username, password, major, studentId, email, phoneNumber) VALUES ('"
                    + user1.getUsername() + "', '"
                    + user1.getPassword() + "', '"
                    + user1.getMajor() + "', '"
                    + user1.getStudentId() + "', '"
                    + user1.getEmail() + "', '"
                    + user1.getPhoneNumber() + "')";

            int rowsAffected = statement.executeUpdate(sql);

            // 적절한 예외 처리 및 반환 값 결정
            if (rowsAffected > 0) {
                // 저장 성공
                return true;
            } else {
                // 저장 실패
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // 예외 발생 시 저장 실패 처리
            return false;
        }
    }

}
