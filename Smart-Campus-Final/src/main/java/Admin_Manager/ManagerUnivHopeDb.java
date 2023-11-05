package Admin_Manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;

public class ManagerUnivHopeDb {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=Asia/Seoul&characterEncoding=utf-8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "dongyang";

    public static void loadWaitingData(DefaultTableModel waitingTableModel) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM communication_board WHERE admin_reply IS NULL OR admin_reply = ''")) {
            ResultSet rs = stmt.executeQuery();
            waitingTableModel.setRowCount(0);  // 기존 데이터 제거
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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "데이터 로딩 중 오류가 발생했습니다.");
        }
    }

    public static void loadRepliedData(DefaultTableModel repliedTableModel) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM communication_board WHERE admin_reply IS NOT NULL AND admin_reply <> ''")) {
               ResultSet rs = stmt.executeQuery();
               repliedTableModel.setRowCount(0);  // 기존 데이터 제거
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
           } catch (Exception e) {
               e.printStackTrace();
               JOptionPane.showMessageDialog(null, "데이터 로딩 중 오류가 발생했습니다.");
           }
       }
    

    public static JTextArea getPostDetails(String title) {
        JTextArea textArea = new JTextArea(10, 40);
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT content, admin_reply FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textArea;
    }

    public static void deletePost(String title) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "게시글 삭제 중 오류가 발생했습니다.");
        }
    }
    public static void saveAdminReply(String title, String reply) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE communication_board SET admin_reply = ?, admin_reply_date = NOW(), status = '답변완료' WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reply);
            statement.setString(2, title);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void addPost(String title, String content) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO communication_board (title, content, admin_reply, author, post_date, views) VALUES (?, ?, ?, ?, NOW(), ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, content);
            statement.setString(3, "");  // admin_reply: 초기에는 빈 문자열
            statement.setString(4, "admin");  // author: 예시로 'admin'을 설정
            statement.setInt(5, 0);  // views: 초기 조회수는 0
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void updatePost(String originalTitle, String newTitle, String content) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE communication_board SET title = ?, content = ? WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newTitle);
            statement.setString(2, content);
            statement.setString(3, originalTitle);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }   
    }