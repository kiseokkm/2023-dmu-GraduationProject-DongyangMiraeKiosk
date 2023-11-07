package Admin_UnivHope;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;
import java.util.Vector;
import Admin_UnivHope.UnivHopeDb;

public class UnivHopeDb {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "dongyang";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void searchInDatabase(DefaultTableModel tableModel, String fieldName, String searchQuery) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM communication_board WHERE " + fieldName + " LIKE ?")) {
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
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "검색 중 오류가 발생했습니다.");
        }
    }
    public static void loadDataFromDatabase(DefaultTableModel tableModel) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM communication_board")) {
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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "데이터 로딩 중 오류가 발생했습니다.");
        }
    }
    public static String getAdminReplyDate(String title) {
        String replyDate = "";
        try (Connection connection = getConnection()) {
            String sql = "SELECT admin_reply_date FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                replyDate = rs.getString("admin_reply_date");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return replyDate;
    }

    public static String getPostDate(String title) {
        String date = "";
        try (Connection connection = getConnection()) {
            String sql = "SELECT post_date FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                date = rs.getString("post_date");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getPostAuthor(String title) {
        String author = "";
        try (Connection connection = getConnection()) {
            String sql = "SELECT author FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                author = rs.getString("author");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return author;
    }

    public static int getPostViews(String title) {
        int views = 0;
        try (Connection connection = getConnection()) {
            String sql = "SELECT views FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                views = rs.getInt("views");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return views;
    }

    public static void increasePostViews(String title) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE communication_board SET views = views + 1 WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAdminReply(String title, String reply) {
        try (Connection connection = getConnection()) {
            String sql = "UPDATE communication_board SET admin_reply = ?, admin_reply_date = NOW() WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reply);
            statement.setString(2, title);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getAdminReply(String title) {
        String reply = "";
        try (Connection connection = getConnection()) {
            String sql = "SELECT admin_reply FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                reply = rs.getString("admin_reply");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reply;
    }

    public static String getPostContent(String title) {
        String content = "";
        try (Connection connection = getConnection()) {
            String sql = "SELECT content FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                content = rs.getString("content");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }   
        return content;
    }
public static int getPostId(String title) {
    int id = -1; // 초기값을 -1로 설정하여, 게시물을 찾지 못했을 경우를 처리
    try (Connection connection = getConnection()) {
        String sql = "SELECT id FROM communication_board WHERE title = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, title);
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            id = rs.getInt("id");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return id;
}
}




