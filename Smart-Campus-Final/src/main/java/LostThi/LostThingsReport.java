package LostThi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class LostThingsReport extends JPanel {

    private JTextField titleField;
    private JTextArea contentArea;
    private JTextField authorField;
    private JComboBox<String> categoryBox;

    public LostThingsReport() {
        setLayout(new BorderLayout());

        JLabel headline = new JLabel("분실물 신고하기", SwingConstants.CENTER);
        headline.setFont(new java.awt.Font("Malgun Gothic", java.awt.Font.BOLD, 30));
        headline.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(headline, BorderLayout.NORTH);

        JPanel postPanel = new JPanel();
        postPanel.setLayout(new GridLayout(4, 2));  // Grid layout to 4 rows (added category row)

        categoryBox = new JComboBox<>(new String[] {"분실물 발견", "분실물 찾습니다"});
        postPanel.add(new JLabel("카테고리:"));
        postPanel.add(categoryBox);

        JLabel titleLabel = new JLabel("제목:");
        titleField = new JTextField();
        postPanel.add(titleLabel);
        postPanel.add(titleField);

        JLabel contentLabel = new JLabel("내용:");
        contentArea = new JTextArea();
        postPanel.add(contentLabel);
        postPanel.add(new JScrollPane(contentArea));

        JLabel authorLabel = new JLabel("작성자:");
        authorField = new JTextField();
        postPanel.add(authorLabel);
        postPanel.add(authorField);

        add(postPanel, BorderLayout.CENTER);

        JButton postButton = new JButton("글 등록");
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String content = contentArea.getText();
                String author = authorField.getText();
                String category = (String) categoryBox.getSelectedItem();

                saveToDatabase(title, content, author, category);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(postButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    public void saveToDatabase(String title, String content, String author, String category) {
    	String url = "jdbc:mysql://localhost:3306/self_order_kiosk?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String password = "dongyang";

        String tableName = category.equals("분실물 발견") ? "found_items" : "lost_items";
        String sql = "INSERT INTO " + tableName + " (title, content, author) VALUES (?, ?, ?)";


        try (Connection conn = DriverManager.getConnection(url, user, password);
        	     PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	    pstmt.setString(1, title);
        	    pstmt.setString(2, content);
        	    pstmt.setString(3, author);
        	    pstmt.executeUpdate();
        	    JOptionPane.showMessageDialog(null, "글이 성공적으로 등록되었습니다.");
        	} catch (Exception e) {
        	    e.printStackTrace();
        	    JOptionPane.showMessageDialog(null, "글 등록 중 오류가 발생했습니다.");
        	}
    }



    public void incrementViewCount(int postId, String category) {
        String url = "jdbc:mysql://localhost:3306/self_order_kiosk";  // Change database name to self_order_kiosk
        String user = "root";
        String password = "dongyang";

        String tableName = category.equals("분실물 발견") ? "found_items" : "lost_items";
        String sql = "UPDATE " + tableName + " SET views = views + 1 WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
               pstmt.setInt(1, postId);
               int updatedRows = pstmt.executeUpdate();
               
               if (updatedRows == 0) {
                   System.out.println("No rows updated. Check if the post with ID " + postId + " exists.");
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }


    
}
