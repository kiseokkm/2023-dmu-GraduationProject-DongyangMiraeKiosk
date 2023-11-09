package Admin_LostThi;

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
    private JCheckBox anonymousCheckBox;
    private String loggedInUsername; // 로그인한 사용자의 이름을 저장하는 변수
    private String displayAuthor; // 화면에 표시될 작성자 이름 (익명 가능)

    public LostThingsReport(String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;
        this.displayAuthor = loggedInUsername; // 기본적으로 화면에 표시될 작성자는 로그인한 사용자의 이름
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
        postPanel.setLayout(new GridLayout(5, 2)); // 5 rows to accommodate the new anonymous checkbox

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
        postPanel.add(new JLabel()); // Empty label for alignment
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

                // 데이터베이스에는 실제 사용자 이름을 저장하고, 익명 여부를 함께 전달합니다.
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
        authorField.setText(displayAuthor); // 사용자 이름으로 작성자 필드를 미리 채움
    }
    private void saveToDatabase(String title, String content, String author, String category, boolean isAnonymous) {
        String url = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8";
        String user = "root";
        String password = "dongyang";

        // The table name is determined by the category
        String tableName = category.equals("분실물 발견") ? "found_items" : "lost_items";
        String sql = "INSERT INTO " + tableName + " (title, content, author, is_anonymous) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
               pstmt.setString(1, title);
               pstmt.setString(2, content);
               pstmt.setString(3, author);
               pstmt.setBoolean(4, isAnonymous);
               pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "글이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "글 등록 중 오류가 발생했습니다.");
        }
    }
}