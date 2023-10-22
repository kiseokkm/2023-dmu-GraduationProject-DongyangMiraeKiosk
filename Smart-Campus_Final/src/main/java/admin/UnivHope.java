package admin;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UnivHope extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;
    private JButton postButton;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=Asia/Seoul&characterEncoding=utf-8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "dongyang";

    public UnivHope() {
        mainPanel = this;
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"번호", "제목", "답변", "작성자", "작성일", "조회수"}, 0);
        table = new JTable(tableModel);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                String title = (String) tableModel.getValueAt(rowIndex, 1);
                showPostDetails(title);
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);

        postButton = new JButton("새글");
        postButton.addActionListener(e -> openPostDialog());
        JButton refreshButton = new JButton("새로고침");
        refreshButton.addActionListener(e -> loadDataFromDatabase());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(refreshButton);  // 새로고침 버튼 패널에 추가
        buttonPanel.add(postButton);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        loadDataFromDatabase();
    }

    private void openPostDialog() {
        PostDialog postDialog = new PostDialog();
        postDialog.setVisible(true);
    }

    private void loadDataFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM communication_board")) {
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("admin_reply").isEmpty() ? "답변 대기" : "답변 완료",
                    rs.getString("author"),
                    rs.getTimestamp("post_date"),
                    rs.getInt("views")
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터 로딩 중 오류가 발생했습니다.");
        }
    }

    private void showPostDetails(String title) {
        increasePostViews(title);
        JTextArea postContent = getPostDetails(title);
        JTextArea adminReply = new JTextArea(getAdminReply(title));
        String replyText = getAdminReply(title);
        String replyDate = getAdminReplyDate(title);
        if (replyDate != null && !replyDate.isEmpty()) {
            replyText += "\n\n답변 작성일: " + replyDate;
        }
        adminReply.setText(replyText);
        adminReply.setEditable(false);
        adminReply.setBorder(BorderFactory.createTitledBorder("관리자 답변"));
        postContent.setBorder(BorderFactory.createTitledBorder("게시물 내용"));
        JButton backButton = new JButton("목록");
        JLabel titleLabel = new JLabel("제목: " + title);
        JLabel dateLabel = new JLabel("날짜: " + getPostDate(title));
        JLabel authorLabel = new JLabel("작성자: " + getPostAuthor(title));
        JLabel viewsLabel = new JLabel("조회수: " + getPostViews(title));
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(titleLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(viewsLabel);
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        contentPanel.add(new JScrollPane(postContent));
        contentPanel.add(new JScrollPane(adminReply));
        backButton.addActionListener(e -> {
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            JPanel buttonPanelBack = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton refreshButtonBack = new JButton("새로고침");
            refreshButtonBack.addActionListener(evt -> loadDataFromDatabase());
            buttonPanelBack.add(refreshButtonBack);  // 새로고침 버튼 다시 추가
            buttonPanelBack.add(postButton);
            mainPanel.add(buttonPanelBack, BorderLayout.SOUTH);
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(infoPanel, BorderLayout.NORTH);
        topPanel.add(contentPanel, BorderLayout.CENTER);
        topPanel.add(backButton, BorderLayout.SOUTH);
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }


    private String getAdminReplyDate(String title) {
        String replyDate = "";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
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
    
    private String getPostDate(String title) {
        String date = "";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
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

    private String getPostAuthor(String title) {
        String author = "";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
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

    private int getPostViews(String title) {
        int views = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
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
    private void increasePostViews(String title) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE communication_board SET views = views + 1 WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveAdminReply(String title, String reply) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE communication_board SET admin_reply = ?, admin_reply_date = NOW() WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reply);
            statement.setString(2, title);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getAdminReply(String title) {
        String reply = "";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
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

    private JTextArea getPostDetails(String title) {
        JTextArea textArea = new JTextArea(10, 40);
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT content FROM communication_board WHERE title = ?";
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textArea;
    }

    class PostDialog extends JDialog {
        private JTextField titleField;
        private JTextField authorField;
        private JCheckBox anonymousCheckbox;
        private JTextArea contentArea;
        private JButton postButton;

        public PostDialog() {
            setTitle("게시물 작성");
            setSize(400, 400);
            setModal(true);
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            JPanel topPanel = new JPanel(new GridLayout(3, 2));
            titleField = new JTextField();
            authorField = new JTextField();
            anonymousCheckbox = new JCheckBox("익명으로 게시");
            topPanel.add(new JLabel("제목:"));
            topPanel.add(titleField);
            topPanel.add(new JLabel("작성자:"));
            topPanel.add(authorField);
            topPanel.add(anonymousCheckbox);
            contentArea = new JTextArea(10, 30);
            postButton = new JButton("게시");
            postButton.addActionListener(e -> {
                String author = anonymousCheckbox.isSelected() ? "익명" : authorField.getText();
                savePostToDatabase(titleField.getText(), author, contentArea.getText());
                loadDataFromDatabase();
                dispose();
            });
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(contentArea), BorderLayout.CENTER);
            panel.add(postButton, BorderLayout.SOUTH);
            add(panel);
        }

        private void savePostToDatabase(String title, String author, String content) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO communication_board (title, author, content, anonymous, admin_reply) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setString(1, title);
                stmt.setString(2, author);
                stmt.setString(3, content);
                stmt.setBoolean(4, "익명".equals(author));
                stmt.setString(5, "");
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "데이터 저장 중 오류가 발생했습니다.");
            }
        }
    }
}