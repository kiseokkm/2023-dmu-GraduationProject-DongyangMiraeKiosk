package Admin_Login_Notice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JTextArea;
import services.DatabaseService;

public class NoticeFrameDb {
	static DatabaseService dbService = new DatabaseService();

    public static JTextArea getNoticeDetails(String title) {
        JTextArea textArea = new JTextArea(10, 40);
        try {
            dbService.connect();
            String sql = "SELECT content FROM notices WHERE title = ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return textArea;
    }
    public static void incrementViewCount(String title) {
    	DatabaseService dbService = new DatabaseService();
        try {
            dbService.connect();
            String sql = "UPDATE notices SET viewCount = viewCount + 1 WHERE title = ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setString(1, title);
            int updatedRows = statement.executeUpdate();

            if (updatedRows == 0) {
                System.out.println("No rows updated for title: " + title);
            } else {
                System.out.println("Updated view count for title: " + title);
            }
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
    public static int getNoticeCount() {
        int count = 0;
        DatabaseService dbService = new DatabaseService();
        try {
            dbService.connect();
            String sql = "SELECT COUNT(*) FROM notices";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return count;
    }
    public static int getNoticeCount(String major) {
        int count = 0;
        DatabaseService dbService = new DatabaseService();
        try {
            dbService.connect();
            String sql = "SELECT COUNT(*) FROM notices WHERE type = ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setString(1, major);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return count;
    }
}
