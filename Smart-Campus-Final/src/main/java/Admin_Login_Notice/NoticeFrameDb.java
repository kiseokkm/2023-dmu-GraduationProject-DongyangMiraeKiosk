package Admin_Login_Notice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JTextArea;

public class NoticeFrameDb {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8";
    private static final String USER = "root";
    private static final String PASSWORD = "dongyang";

    public static JTextArea getNoticeDetails(String title) {
        JTextArea textArea = new JTextArea(10, 40);
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT content FROM notices WHERE title = ?")) {
            
            statement.setString(1, title);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String content = rs.getString("content");
                    textArea.setText(content);
                    textArea.setWrapStyleWord(true);
                    textArea.setLineWrap(true);
                    textArea.setCaretPosition(0);
                    textArea.setEditable(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textArea;
    }
}
