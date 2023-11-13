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
import java.util.logging.Level;
import java.util.logging.Logger;
import services.DatabaseService;

public class FindIdFrame extends JFrame {

    private JTextField txtStudentId;
    private JTextField txtName;
    private JTextField txtPhoneNumber;
    private JButton btnFind;
    private DatabaseService dbService;

    public FindIdFrame() {
    	dbService = new DatabaseService();
        initComponents();
        setTitle("아이디 찾기");
        setSize(400, 300);
        setLocationRelativeTo(null);
    }
    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 5, 5));
        panel.setBackground(new Color(180, 210, 255)); 

        JLabel lblStudentId = new JLabel("학번:");
        JLabel lblName = new JLabel("이름:");
        JLabel lblPhoneNumber = new JLabel("전화번호:");

        txtStudentId = new JTextField(20);
        txtName = new JTextField(20);
        txtPhoneNumber = new JTextField(20);

        btnFind = new JButton("아이디 찾기");
        btnFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findId();
            }
        });
        panel.add(lblStudentId);
        panel.add(txtStudentId);
        panel.add(lblName);
        panel.add(txtName);
        panel.add(lblPhoneNumber);
        panel.add(txtPhoneNumber);
        panel.add(new JLabel());
        panel.add(btnFind);
        add(panel);
    }
    private void findId() {
        try {
            executeFindId();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
    private void executeFindId() throws SQLException {
        dbService.connect();
        try (PreparedStatement statement = createPreparedStatement();
             ResultSet resultSet = statement.executeQuery()) {
            processResultSet(resultSet);
        } finally {
            dbService.disconnect();
        }
    }
    private PreparedStatement createPreparedStatement() throws SQLException {
        String sql = "SELECT username FROM user1 WHERE studentId = ? AND name = ? AND phoneNumber = ?";
        PreparedStatement statement = dbService.conn.prepareStatement(sql);
        statement.setString(1, txtStudentId.getText());
        statement.setString(2, txtName.getText());
        statement.setString(3, txtPhoneNumber.getText());
        return statement;
    }
    private void processResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            String foundId = resultSet.getString("username");
            JOptionPane.showMessageDialog(this, "당신의 아이디는: " + foundId + " 입니다.", "아이디 찾기", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "입력하신 정보와 일치하는 아이디를 찾을 수 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static final Logger LOGGER = Logger.getLogger(FindIdFrame.class.getName());

    private void handleSQLException(SQLException e) {
        LOGGER.log(Level.SEVERE, "데이터베이스 오류 발생", e);
        JOptionPane.showMessageDialog(this, "데이터베이스 오류 발생.", "오류", JOptionPane.ERROR_MESSAGE);
    }
    public static void main(String[] args) {
        FindIdFrame findIdFrame = new FindIdFrame();
        findIdFrame.setVisible(true);
    }
}