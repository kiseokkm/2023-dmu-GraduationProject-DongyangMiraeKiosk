package Admin_Manager;
import services.DatabaseService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

public class ManagerUnivHopeDb {
	private static DatabaseService dbService = new DatabaseService();

	public static void loadWaitingData(DefaultTableModel waitingTableModel) {
	    try {
	        dbService.connect();
	        PreparedStatement stmt = dbService.conn.prepareStatement("SELECT * FROM communication_board WHERE admin_reply IS NULL OR admin_reply = ''");
	        ResultSet rs = stmt.executeQuery();
	        waitingTableModel.setRowCount(0);
	        while (rs.next()) {
	            Object[] row = {
	                rs.getInt("id"),
	                rs.getString("title"),
	                rs.getString("author"),
	                rs.getTimestamp("post_date"),
	                rs.getInt("views")
	            };
	            waitingTableModel.addRow(row);
	        }
	        rs.close();
	        stmt.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "데이터 로딩 중 오류가 발생했습니다.");
	    } finally {
	        dbService.disconnect();
	    }
	}
	public static void loadRepliedData(DefaultTableModel repliedTableModel) {
	    try {
	        dbService.connect();
	        PreparedStatement stmt = dbService.conn.prepareStatement("SELECT * FROM communication_board WHERE admin_reply IS NOT NULL AND admin_reply <> ''");
	        ResultSet rs = stmt.executeQuery();
	        repliedTableModel.setRowCount(0);
	        while (rs.next()) {
	            Object[] row = {
	                rs.getInt("id"),
	                rs.getString("title"),
	                rs.getString("author"),
	                rs.getTimestamp("post_date"),
	                rs.getInt("views")
	            };
	            repliedTableModel.addRow(row);
	        }
	        rs.close();
	        stmt.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "데이터 로딩 중 오류가 발생했습니다.");
	    } finally {
	        dbService.disconnect();
	    }
	}
	public static JTextArea getPostDetails(String title) {
	    JTextArea textArea = new JTextArea(10, 40);
	    try {
	        dbService.connect();
	        PreparedStatement statement = dbService.conn.prepareStatement("SELECT content, admin_reply FROM communication_board WHERE title = ?");
	        statement.setString(1, title);
	        ResultSet rs = statement.executeQuery();
	        if (rs.next()) {
	            String content = rs.getString("content");
	            String adminReply = rs.getString("admin_reply");
	            textArea.setText("게시글 내용:\n" + content + "\n\n관리자 답변:\n" + adminReply);
	            textArea.setWrapStyleWord(true);
	            textArea.setLineWrap(true);
	            textArea.setCaretPosition(0);
	            textArea.setEditable(false);
	        }
	        rs.close();
	        statement.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        dbService.disconnect();
	    }
	    return textArea;
	}
	public static void deletePost(String title) {
	    try {
	        dbService.connect();
	        PreparedStatement statement = dbService.conn.prepareStatement("DELETE FROM communication_board WHERE title = ?");
	        statement.setString(1, title);
	        statement.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "게시글 삭제 중 오류가 발생했습니다.");
	    } finally {
	        dbService.disconnect();
	    }
	}

	public static void saveAdminReply(String title, String reply) {
	    try {
	        dbService.connect();
	        PreparedStatement statement = dbService.conn.prepareStatement("UPDATE communication_board SET admin_reply = ?, admin_reply_date = NOW(), status = '답변완료' WHERE title = ?");
	        statement.setString(1, reply);
	        statement.setString(2, title);
	        statement.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        dbService.disconnect();
	    }
	}
	public static void addPost(String title, String content) {
	    try {
	        dbService.connect();
	        PreparedStatement statement = dbService.conn.prepareStatement("INSERT INTO communication_board (title, content, admin_reply, author, post_date, views) VALUES (?, ?, ?, ?, NOW(), ?)");
	        statement.setString(1, title);
	        statement.setString(2, content);
	        statement.setString(3, "");
	        statement.setString(4, "admin");
	        statement.setInt(5, 0);
	        statement.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        dbService.disconnect();
	    }
	}
	public static void updatePost(String originalTitle, String newTitle, String content) {
	    try {
	        dbService.connect();
	        PreparedStatement statement = dbService.conn.prepareStatement("UPDATE communication_board SET title = ?, content = ? WHERE title = ?");
	        statement.setString(1, newTitle);
	        statement.setString(2, content);
	        statement.setString(3, originalTitle);
	        statement.executeUpdate();
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        dbService.disconnect();
	    }
	}  
    }