package manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ManagerDashboard extends JFrame {
    private JButton addNoticeButton;
    private JButton editNoticeButton;
    private JButton deleteNoticeButton;
    private JTable noticeTable; 
    private DefaultTableModel noticeTableModel;

    public ManagerDashboard() {
        setTitle("Manager Dashboard");
        setSize(500, 400);
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
                String title = JOptionPane.showInputDialog("Enter notice title:");
                if (title != null && !title.isEmpty()) {
                    String content = JOptionPane.showInputDialog("Enter notice content:");
                    String author = JOptionPane.showInputDialog("Enter author:");
                    if (content != null && author != null && !content.isEmpty() && !author.isEmpty()) {
                        addNotice(title, content, author);
                    }
                }
            }
        });

        editNoticeButton = new JButton("Edit Notice");
        editNoticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowIndex = noticeTable.getSelectedRow();
                if (rowIndex == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a notice to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String oldTitle = (String) noticeTableModel.getValueAt(rowIndex, 0);
                String newTitle = JOptionPane.showInputDialog("Enter new title:", oldTitle);
                if (newTitle != null && !newTitle.isEmpty()) {
                    String newContent = JOptionPane.showInputDialog("Enter new content:");
                    if (newContent != null && !newContent.isEmpty()) {
                        editNotice(oldTitle, newTitle, newContent);
                    }
                }
            }
        });

        deleteNoticeButton = new JButton("Delete Notice");
        deleteNoticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowIndex = noticeTable.getSelectedRow();
                if (rowIndex == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a notice to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String title = (String) noticeTableModel.getValueAt(rowIndex, 0);
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to delete this notice?", "Delete Confirmation", dialogButton);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    deleteNotice(title);
                    loadNoticesIntoTable();
                }
            }
        });

        add(addNoticeButton);
        add(editNoticeButton);
        add(deleteNoticeButton);
    
    
    String[] columnNames = {"Title"};
    noticeTableModel = new DefaultTableModel(columnNames, 0);
    noticeTable = new JTable(noticeTableModel);
    noticeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    noticeTable.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int rowIndex = noticeTable.getSelectedRow();
                String title = (String) noticeTableModel.getValueAt(rowIndex, 0);
                showNoticeDetails(title);
            }
        }
    });
    JScrollPane scrollPane = new JScrollPane(noticeTable);
    add(scrollPane);

    // 공지사항 데이터 로드
    loadNoticesIntoTable();
}

    // 데이터베이스에서 모든 공지사항의 제목을 가져오는 함수
    private ArrayList<String> fetchAllNotices() {
        ArrayList<String> notices = new ArrayList<>();
        // 데이터베이스 연결 및 조회 로직 구현
        return notices;
    }

    // 데이터베이스에 새로운 공지사항을 추가하는 함수
    private void addNotice(String title, String content, String author) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "INSERT INTO notices (title, content, author, date) VALUES (?, ?, ?, CURDATE())";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, content);
            statement.setString(3, author);
            statement.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void editNotice(String oldTitle, String newTitle, String newContent) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "UPDATE notices SET title = ?, content = ? WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newTitle);
            statement.setString(2, newContent);
            statement.setString(3, oldTitle);
            statement.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        loadNoticesIntoTable();  // 데이터를 수정한 후 테이블을 다시 로드
    }

    private void deleteNotice(String title) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "DELETE FROM notices WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.executeUpdate();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        loadNoticesIntoTable();  // 데이터를 삭제한 후 테이블을 다시 로드
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerDashboard managerDashboard = new ManagerDashboard();
            managerDashboard.setVisible(true);
        });
    }
    
    private void loadNoticesIntoTable() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT title FROM notices";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            noticeTableModel.setRowCount(0);  // 테이블 초기화
            while (rs.next()) {
                String title = rs.getString("title");
                noticeTableModel.addRow(new Object[]{title});
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
 // 공지사항의 상세 내용을 팝업 창에서 보여주는 메서드
    private void showNoticeDetails(String title) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT content FROM notices WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String content = rs.getString("content");
                JTextArea textArea = new JTextArea(10, 40);
                textArea.setText(content);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setCaretPosition(0);
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Notice Details", JOptionPane.INFORMATION_MESSAGE);
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}