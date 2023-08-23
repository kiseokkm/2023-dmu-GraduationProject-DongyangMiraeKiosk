package kiosk;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class NoticeFrame extends javax.swing.JFrame {

    private JTable noticeTable;
    private DefaultTableModel noticeTableModel;

    public NoticeFrame() {
        initComponents();
        loadNotices();
    }

    private void initComponents() {
        // UI 컴포넌트 초기화
        setTitle("공지사항");
        setSize(600, 400);
        setLocationRelativeTo(null);

        noticeTableModel = new DefaultTableModel(new Object[]{"번호", "제목", "작성자", "작성일", "조회수"}, 0);
        noticeTable = new JTable(noticeTableModel);

        noticeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = noticeTable.getSelectedRow();
                String title = (String) noticeTableModel.getValueAt(rowIndex, 1);
                showNoticeDetails(title);
            }
        });

        add(new JScrollPane(noticeTable), BorderLayout.CENTER);
    }

    private void loadNotices() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT * FROM notices ORDER BY id DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("date"),
                    rs.getInt("viewCount")
                };
                noticeTableModel.addRow(row);
            }
            
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading notices: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showNoticeDetails(String title) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");

            // 1. 해당 공지사항의 조회수를 1 증가시키는 SQL 업데이트문을 실행합니다.
            String updateSql = "UPDATE notices SET viewCount = viewCount + 1 WHERE title = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);
            updateStatement.setString(1, title);
            updateStatement.executeUpdate();
            updateStatement.close();

            // 2. 공지사항의 내용을 가져와서 보여줍니다.
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

                JDialog dialog = new JDialog();
                dialog.setTitle("내용");
                dialog.setSize(400, 300);
                dialog.add(new JScrollPane(textArea));
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error displaying notice details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String args[]) {
        // 공지사항 프레임 실행
        java.awt.EventQueue.invokeLater(() -> {
            new NoticeFrame().setVisible(true);
        });
    }
}
