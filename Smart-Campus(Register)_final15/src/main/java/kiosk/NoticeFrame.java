package kiosk;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;

public class NoticeFrame {	

	public static JTable getNoticeTable() {
	    DefaultTableModel noticeTableModel = new DefaultTableModel(new Object[]{"번호", "제목", "작성자", "작성일", "조회수"}, 0);
	    JTable noticeTable = new JTable(noticeTableModel);

	    try {
	        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
	        String sql = "SELECT * FROM notices ORDER BY pinned DESC, id DESC"; 
	        PreparedStatement statement = connection.prepareStatement(sql);
	        ResultSet rs = statement.executeQuery();
	        
	        while (rs.next()) {
	            Object identifier; 
	            boolean isPinned = rs.getBoolean("pinned");
	            if (isPinned) {
	                identifier = "★"; 
	            } else {
	                identifier = rs.getInt("id");
	            }
	            
	            Object[] row = {
	                identifier,
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
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return noticeTable;
	}

    
    public static JTextArea getNoticeDetails(String title) {
        JTextArea textArea = new JTextArea(10, 40);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT content FROM notices WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String content = rs.getString("content");
                textArea.setText(content);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setCaretPosition(0);
                textArea.setEditable(false);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textArea;
    }
    
    public static void incrementViewCount(String title) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "UPDATE notices SET viewCount = viewCount + 1 WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                System.out.println("No rows updated for title: " + title);
            } else {
                System.out.println("Updated view count for title: " + title);
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getNoticeCount() {
        int count = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT COUNT(*) FROM notices";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static JPanel getNoticesPanel() {
        JPanel noticePanel = new JPanel(new BorderLayout(10, 10));
        noticePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        int noticeCount = getNoticeCount();
        JLabel lblNoticeCount = new JLabel("총 " + noticeCount + " 개의 게시물이 있습니다.");

        // 새로고침 버튼 생성
        JButton refreshButton = new JButton("새로고침");
        JTable noticeTable = getNoticeTable();
        JScrollPane scrollPane = new JScrollPane(noticeTable);
        noticePanel.add(scrollPane, BorderLayout.CENTER);

        refreshButton.addActionListener(e -> {
            JTable updatedTable = getNoticeTable();
            scrollPane.setViewportView(updatedTable);
            noticePanel.revalidate();
            noticePanel.repaint();
        });

        // 우측 상단에 버튼 배치
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lblNoticeCount, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);
        noticePanel.add(topPanel, BorderLayout.NORTH);

        return noticePanel;
    }


}
