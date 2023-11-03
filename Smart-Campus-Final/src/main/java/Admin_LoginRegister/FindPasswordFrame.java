package Admin_LoginRegister;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FindPasswordFrame extends JFrame {

    private JTextField txtStudentId;
    private JTextField txtName;
    private JTextField txtPhoneNumber;
    private JButton btnFindPassword;
    private JTextField txtMostPreciousThing;

    public FindPasswordFrame() {
        initComponents();
        setTitle("비밀번호 찾기");
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 5, 5));

        JLabel lblStudentId = new JLabel("학번:");
        JLabel lblName = new JLabel("이름:");
        JLabel lblPhoneNumber = new JLabel("전화번호:");
        JLabel lblMostPreciousThing = new JLabel("당신의 가장 소중한것은:");

        txtStudentId = new JTextField(20);
        txtName = new JTextField(20);
        txtPhoneNumber = new JTextField(20);
        txtMostPreciousThing = new JTextField(20);

        btnFindPassword = new JButton("비밀번호 찾기");
        btnFindPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findPassword();
            }
        });

        panel.add(lblStudentId);
        panel.add(txtStudentId);
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblPhoneNumber);
        panel.add(txtPhoneNumber);
        panel.add(lblMostPreciousThing);
        panel.add(txtMostPreciousThing);
        panel.add(new JLabel());
        panel.add(btnFindPassword);

        add(panel);
    }

    private void findPassword() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT password FROM user1 WHERE studentId = ? AND name = ? AND phoneNumber = ? AND mostPreciousThing = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, txtStudentId.getText());
            statement.setString(2, txtName.getText());
            statement.setString(3, txtPhoneNumber.getText());
            statement.setString(4, txtMostPreciousThing.getText());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String foundPassword = resultSet.getString("password");
                JOptionPane.showMessageDialog(this, "당신의 비밀번호는: " + foundPassword + " 입니다.", "비밀번호 찾기", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "입력하신 정보와 일치하는 비밀번호를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터베이스 오류 발생.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        FindPasswordFrame findPasswordFrame = new FindPasswordFrame();
        findPasswordFrame.setVisible(true);
    }
}
