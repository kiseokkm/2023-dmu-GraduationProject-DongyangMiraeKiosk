package Admin_UnivHope;

import javax.swing.*;
import services.DatabaseService;
import javax.swing.table.DefaultTableModel;

import java.sql.*;
import java.util.Vector;

public class UnivHopeDb {
	private static DatabaseService dbService = new DatabaseService();

    public static void searchInDatabase(DefaultTableModel tableModel, String fieldName, String searchQuery) {
        try {
            dbService.connect();
            PreparedStatement stmt = dbService.conn.prepareStatement("SELECT * FROM communication_board WHERE " + fieldName + " LIKE ?");
            stmt.setString(1, "%" + searchQuery + "%");
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("title"));
                row.add(rs.getString("admin_reply").isEmpty() ? "답변 대기" : "답변 완료");
                row.add(rs.getString("author"));
                row.add(rs.getTimestamp("post_date"));
                row.add(rs.getInt("views"));
                tableModel.addRow(row);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "검색 중 오류가 발생했습니다.");
        } finally {
            dbService.disconnect();
        }
    }
    public static void loadDataFromDatabase(DefaultTableModel tableModel) {
        try {
            dbService.connect();
            PreparedStatement stmt = dbService.conn.prepareStatement("SELECT * FROM communication_board");
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("title"));
                row.add(rs.getString("admin_reply").isEmpty() ? "답변 대기" : "답변 완료");
                row.add(rs.getString("author"));
                row.add(rs.getTimestamp("post_date"));
                row.add(rs.getInt("views"));
                tableModel.addRow(row);
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
    public static String getAdminReplyDate(String title) {
        String replyDate = "";
        try {
            dbService.connect();
            PreparedStatement statement = dbService.conn.prepareStatement("SELECT admin_reply_date FROM communication_board WHERE title = ?");
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                replyDate = rs.getString("admin_reply_date");
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return replyDate;
    }
    public static String getPostDate(String title) {
        String date = "";
        try {
            dbService.connect();
            PreparedStatement statement = dbService.conn.prepareStatement("SELECT post_date FROM communication_board WHERE title = ?");
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                date = rs.getString("post_date");
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return date;
    }
    public static String getPostAuthor(String title) {
        String author = "";
        try {
            dbService.connect();
            PreparedStatement statement = dbService.conn.prepareStatement("SELECT author FROM communication_board WHERE title = ?");
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                author = rs.getString("author");
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return author;
    }
    public static int getPostViews(String title) {
        int views = 0;
        try {
            dbService.connect();
            PreparedStatement statement = dbService.conn.prepareStatement("SELECT views FROM communication_board WHERE title = ?");
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                views = rs.getInt("views");
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return views;
    }
    public static void increasePostViews(String title) {
        try {
            dbService.connect();
            PreparedStatement statement = dbService.conn.prepareStatement("UPDATE communication_board SET views = views + 1 WHERE title = ?");
            statement.setString(1, title);
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
    public static void saveAdminReply(String title, String reply) {
        try {
            dbService.connect();
            PreparedStatement statement = dbService.conn.prepareStatement("UPDATE communication_board SET admin_reply = ?, admin_reply_date = NOW() WHERE title = ?");
            statement.setString(1, reply);
            statement.setString(2, title);
            statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
    public static String getAdminReply(String title) {
        String reply = "";
        try {
            dbService.connect();
            PreparedStatement statement = dbService.conn.prepareStatement("SELECT admin_reply FROM communication_board WHERE title = ?");
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                reply = rs.getString("admin_reply");
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return reply;
    }
    public static String getPostContent(String title) {
        String content = "";
        try {
            dbService.connect();
            PreparedStatement statement = dbService.conn.prepareStatement("SELECT content FROM communication_board WHERE title = ?");
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                content = rs.getString("content");
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return content;
    }
    public static int getPostId(String title) {
        int id = -1;
        try {
            dbService.connect();
            PreparedStatement statement = dbService.conn.prepareStatement("SELECT id FROM communication_board WHERE title = ?");
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return id;
    }
}