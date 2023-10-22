package admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class ManagerDashboard extends JFrame {
    private JButton addNoticeButton;
    private JButton editNoticeButton;
    private JButton deleteNoticeButton;

    public ManagerDashboard() {
        setTitle("Manager Dashboard");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        setLayout(new FlowLayout());

        addNoticeButton = new JButton("Add Notice");
        addNoticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newNotice = JOptionPane.showInputDialog("Enter new notice:");
                if (newNotice != null && !newNotice.isEmpty()) {
                    addNotice(newNotice);
                }
            }
        });

        editNoticeButton = new JButton("Edit Notice");
        editNoticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notices = fetchAllNotices();
                String selectedNotice = (String) JOptionPane.showInputDialog(
                        null,
                        "Select notice to edit:",
                        "Edit Notice",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        notices.toArray(),
                        notices.get(0)
                );

                if (selectedNotice != null) {
                    String updatedNotice = JOptionPane.showInputDialog("Update notice:", selectedNotice);
                    if (updatedNotice != null && !updatedNotice.isEmpty()) {
                        updateNotice(selectedNotice, updatedNotice);
                    }
                }
            }
        });

        deleteNoticeButton = new JButton("Delete Notice");
        deleteNoticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<String> notices = fetchAllNotices();
                String selectedNotice = (String) JOptionPane.showInputDialog(
                        null,
                        "Select notice to delete:",
                        "Delete Notice",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        notices.toArray(),
                        notices.get(0)
                );

                if (selectedNotice != null) {
                    deleteNotice(selectedNotice);
                }
            }
        });

        add(addNoticeButton);
        add(editNoticeButton);
        add(deleteNoticeButton);
    }

    // 데이터베이스에서 모든 공지사항을 가져오는 함수
    private ArrayList<String> fetchAllNotices() {
        ArrayList<String> notices = new ArrayList<>();
        
        // 데이터베이스 연결 및 조회 로직 구현
        
        return notices;
    }

    // 데이터베이스에 새로운 공지사항을 추가하는 함수
    private void addNotice(String notice) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "INSERT INTO notices (content) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, notice);
            statement.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 데이터베이스의 기존 공지사항을 수정하는 함수
    private void updateNotice(String oldNotice, String newNotice) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "UPDATE notices SET content = ? WHERE content = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newNotice);
            statement.setString(2, oldNotice);
            statement.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 데이터베이스의 공지사항을 삭제하는 함수
    private void deleteNotice(String notice) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "DELETE FROM notices WHERE content = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, notice);
            statement.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerDashboard managerDashboard = new ManagerDashboard();
            managerDashboard.setVisible(true);
        });
    }
}
