package manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

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
        addNoticeButton.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("Enter notice title:");
            if (title != null && !title.isEmpty()) {
                String content = JOptionPane.showInputDialog("Enter notice content:");
                String author = JOptionPane.showInputDialog("Enter author:");
                int rowIndex = noticeTable.getSelectedRow();
                boolean pinned = rowIndex != -1 && (boolean) noticeTableModel.getValueAt(rowIndex, 1);
                if (content != null && author != null && !content.isEmpty() && !author.isEmpty()) {
                    addNotice(title, content, author, pinned);
                }
            }
        });

        editNoticeButton = new JButton("Edit Notice");
        editNoticeButton.addActionListener(e -> {
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
        });

        deleteNoticeButton = new JButton("Delete Notice");
        deleteNoticeButton.addActionListener(e -> {
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
        });

        add(addNoticeButton);
        add(editNoticeButton);
        add(deleteNoticeButton);

        String[] columnNames = {"Title", "Pinned"};
        noticeTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 1 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;  // 체크박스만 수정 가능
            }
        };

        noticeTable = new JTable(noticeTableModel);
        noticeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        noticeTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 1) {  // 체크박스의 상태가 변경되면
                int rowIndex = e.getFirstRow();
                String title = (String) noticeTableModel.getValueAt(rowIndex, 0);
                boolean pinned = (boolean) noticeTableModel.getValueAt(rowIndex, 1);
                updatePinStatus(title, pinned);  // 핀 상태 업데이트
                loadNoticesIntoTable();  // 테이블 다시 로드
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

    private void addNotice(String title, String content, String author, boolean pinned) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "INSERT INTO notices (title, content, author, date, pinned) VALUES (?, ?, ?, CURDATE(), ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, content);
            statement.setString(3, author);
            statement.setInt(4, pinned ? 1 : 0);
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
        loadNoticesIntoTable();
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
        loadNoticesIntoTable();
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
            String sql = "SELECT title, pinned FROM notices ORDER BY pinned DESC, date DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            noticeTableModel.setRowCount(0);
            while (rs.next()) {
                String title = rs.getString("title");
                boolean pinned = rs.getBoolean("pinned");
                if (pinned) {
                    title = "★ " + title;
                }
                noticeTableModel.addRow(new Object[]{title, pinned});
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updatePinStatus(String title, boolean pinned) {
        try {
            // 원래 제목에서 ★ 제거
            title = title.replace("★ ", "");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "UPDATE notices SET pinned = ? WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, pinned ? 1 : 0);
            statement.setString(2, title);
            statement.executeUpdate();
            connection.close();
            loadNoticesIntoTable();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


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
