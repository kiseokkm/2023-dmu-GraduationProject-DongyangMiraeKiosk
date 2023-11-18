package Admin_LostThi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import services.DatabaseService;

public class LostThingsReport extends JPanel {

    private JTextField titleField;
    private JTextArea contentArea;
    private JTextField authorField;
    private JComboBox<String> categoryBox;
    private JCheckBox anonymousCheckBox;
    private String loggedInUsername; 
    private String displayAuthor; 
    private DatabaseService dbService = new DatabaseService();

    public LostThingsReport(String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;
        this.displayAuthor = loggedInUsername; 
        setLayout(new BorderLayout());
        initializeComponents();
        prefillAuthor();
        this.setBackground(Color.WHITE);
    }
    private void initializeComponents() {
        JLabel headline = new JLabel("분실물 신고하기", SwingConstants.CENTER);
        headline.setFont(new Font("Malgun Gothic", Font.BOLD, 30));
        headline.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        headline.setOpaque(true);
        headline.setBackground(new Color(180, 210, 255));
        add(headline, BorderLayout.NORTH);

        JPanel postPanel = new JPanel();
        postPanel.setLayout(new GridLayout(5, 2)); 

        categoryBox = new JComboBox<>(new String[]{"분실물 발견", "분실물 찾습니다"});
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

        anonymousCheckBox = new JCheckBox("익명으로 등록");
        postPanel.add(new JLabel()); 
        postPanel.add(anonymousCheckBox);

        add(postPanel, BorderLayout.CENTER);

        anonymousCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (anonymousCheckBox.isSelected()) {
                    displayAuthor = "익명";
                    authorField.setText(displayAuthor);
                    authorField.setEditable(false);
                } else {
                    displayAuthor = loggedInUsername;
                    authorField.setText(displayAuthor);
                    authorField.setEditable(true);
                }
            }
        });
        JButton postButton = new JButton("글 등록");
        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String content = contentArea.getText();
                String category = (String) categoryBox.getSelectedItem();
                boolean isAnonymous = anonymousCheckBox.isSelected();

                saveToDatabase(title, content, loggedInUsername, category, isAnonymous);
            }
        });
        postPanel.setBackground(new Color(180, 210, 255));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(180, 210, 255));
        buttonPanel.add(postButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    private void prefillAuthor() {
        authorField.setText(displayAuthor); 
    }
    private void saveToDatabase(String title, String content, String author, String category, boolean isAnonymous) {
        String tableName = category.equals("분실물 발견") ? "found_items" : "lost_items";
        String sql = "INSERT INTO " + tableName + " (title, content, author, is_anonymous) VALUES (?, ?, ?, ?)";
        DatabaseService dbService = new DatabaseService();

        try {
            dbService.connect();
            PreparedStatement pstmt = dbService.conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, author);
            pstmt.setBoolean(4, isAnonymous);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "글이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "글 등록 중 오류가 발생했습니다.");
        } finally {
            dbService.disconnect();
        }
    }

}