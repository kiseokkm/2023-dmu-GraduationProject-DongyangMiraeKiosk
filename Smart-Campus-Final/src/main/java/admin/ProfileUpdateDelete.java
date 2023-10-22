package admin;
//@버튼 
import javax.swing.*;

import models.User1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfileUpdateDelete {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "dongyang";

    public static void ShowProfile(String username)  {
        JFrame profileFrame = new JFrame("Profile");
        profileFrame.setSize(300, 400);
        profileFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        profileFrame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2, 5, 5));

        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField();
        txtUsername.setEditable(false);

        JLabel lblMajor = new JLabel("Major:");
        JComboBox<String> comboMajor = new JComboBox<String>();
        comboMajor.addItem("기계공학과");
        comboMajor.addItem("기계설계공학과");
        comboMajor.addItem("로봇공학과");
        comboMajor.addItem("자동화공학과");
        comboMajor.addItem("전기공학과");
        comboMajor.addItem("정보전자공학과");
        comboMajor.addItem("반도체전자공학과");
        comboMajor.addItem("정보통신공학과");
        comboMajor.addItem("소방안전관리과");
        comboMajor.addItem("컴퓨터소프트웨어공학과");
        comboMajor.addItem("컴퓨터정보공학과");
        comboMajor.addItem("인공지능소프트웨어공학과");
        comboMajor.addItem("생명화학공학과");
        comboMajor.addItem("바이오융합공학과");
        comboMajor.addItem("건축과");
        comboMajor.addItem("실내건축디자인과");
        comboMajor.addItem("시각디자인과");
        comboMajor.addItem("경영학과");
        comboMajor.addItem("세무회계학과");
        comboMajor.addItem("유통마케팅학과");
        comboMajor.addItem("호텔관광학과");
        comboMajor.addItem("경영정보학과");
        comboMajor.addItem("빅데이터경영과");

        JLabel lblStudentId = new JLabel("Student ID:");
        JTextField txtStudentId = new JTextField();

        JLabel lblName = new JLabel("Name:");
        JTextField txtName = new JTextField();

        JLabel lblPhoneNumber = new JLabel("Phone Number:");
        JTextField txtPhoneNumber = new JTextField();

        //  사용자의 정보를 DB에서 가져와서 화면 컴포넌트에 설정합니다.
        getUserInfo(username, txtUsername, comboMajor, txtStudentId, txtName, txtPhoneNumber);

        
        

        JButton btnUpdate = new JButton("수정");
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = txtUsername.getText();
                    String major = (String) comboMajor.getSelectedItem();
                    String studentId = txtStudentId.getText();
                    String name = txtName.getText();
                    String phoneNumber = txtPhoneNumber.getText();
                    
                    updateUserInDatabase(username, major, studentId, name, phoneNumber);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "업데이트 중 오류 발생: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

     // Delete (탈퇴) 버튼
        JButton btnDelete = new JButton("탈퇴");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String username = txtUsername.getText();
                    deleteUserFromDatabase(username);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "삭제 중 오류 발생: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
     // Confirm (확인) 버튼
        JButton btnConfirm = new JButton("확인");
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                profileFrame.dispose(); // 현재 창을 닫습니다.
            }
        });

        panel.add(lblUsername);
        panel.add(txtUsername);
        panel.add(lblMajor);
        panel.add(comboMajor);
        panel.add(lblStudentId);
        panel.add(txtStudentId);
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblPhoneNumber);
        panel.add(txtPhoneNumber);
        panel.add(btnUpdate);
        panel.add(btnConfirm);
        panel.add(btnDelete);

        profileFrame.add(panel);
        profileFrame.setVisible(true);
    }
    private static void getUserInfo(String username, JTextField txtUsername, JComboBox<String> comboMajor, JTextField txtStudentId, JTextField txtName, JTextField txtPhoneNumber) {
        // DB에서 username에 해당하는 사용자 정보를 가져와서 화면에 표시하는 로직 추가
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT * FROM user1 WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                txtUsername.setText(result.getString("username"));
                comboMajor.setSelectedItem(result.getString("major")); // 학과 정보를 콤보박스에서 선택되게 함
                txtStudentId.setText(result.getString("studentId"));
                txtName.setText(result.getString("name"));
                txtPhoneNumber.setText(result.getString("phoneNumber"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateUserInDatabase(String username, String major, String studentId, String name, String phoneNumber) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "UPDATE user1 SET major=?, studentId=?, name=?, phoneNumber=? WHERE username=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, major);
            statement.setString(2, studentId);
            statement.setString(3, name);
            statement.setString(4, phoneNumber);
            statement.setString(5, username);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "사용자 정보가 업데이트되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "사용자 정보 업데이트에 실패하였습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteUserFromDatabase(String username) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            String sql = "DELETE FROM user1 WHERE username=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "사용자 정보가 삭제되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "사용자 정보 삭제에 실패하였습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}