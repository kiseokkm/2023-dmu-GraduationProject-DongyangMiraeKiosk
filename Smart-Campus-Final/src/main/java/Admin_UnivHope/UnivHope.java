package Admin_UnivHope;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class UnivHope extends JPanel {
    private JTable table;
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JButton postButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton voiceButton;
    private JButton searchButton;
    private JComboBox<String> searchComboBox;
    private UnivHope univHopeInstance;
    private String loggedInUsername; 
    private static final String DB_URL = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "dongyang";
    public UnivHope(String loggedInName) {
    
        this.loggedInUsername = loggedInName; 
        mainPanel = this;
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"번호", "제목", "답변", "작성자", "작성일", "조회수"}, 0);
        table = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(new Color(255, 255, 224)); 
                }
                return c;
            }
        };
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(173, 216, 230));//테이블 헤더 백그라운드 색깔
        
        addTableMouseListener();
         
        add(new JScrollPane(table), BorderLayout.CENTER);
        postButton = new JButton("새글");
        postButton .setBackground(new Color(135, 206, 235)); 
        postButton.addActionListener(e -> openPostDialog());
        JButton refreshButton = new JButton("새로고침");
        refreshButton .setBackground(new Color(135, 206, 235)); 
        refreshButton.addActionListener(e -> loadDataFromDatabase());   
        voiceButton = new JButton("음성");
        voiceButton .setBackground(new Color(135, 206, 235)); 
        searchField = new JTextField(25);
        searchComboBox = new JComboBox<>(new String[]{"제목", "답변", "작성자"});    
        searchComboBox.setBackground(new Color(211, 211, 211)); 
        searchButton = new JButton("검색");
        searchButton .setBackground(new Color(135, 206, 235)); 
        searchButton.addActionListener(e -> {
            String searchQuery = searchField.getText().trim();
            String selectedField = (String) searchComboBox.getSelectedItem();
            if (!searchQuery.isEmpty() && selectedField != null) {
                String fieldName = "";
                switch (selectedField) {
                    case "제목":
                        fieldName = "title";
                        break;
                    case "답변":
                        fieldName = "status";
                        break;
                    case "작성자":
                        fieldName = "author";
                        break;
                }
                if (!fieldName.isEmpty()) {
                    searchInDatabase(fieldName, searchQuery);
                }
            } else {
                loadDataFromDatabase();
            }
        });   
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(searchComboBox);
        buttonPanel.add(searchField);
        buttonPanel.add(voiceButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(postButton);

        JScrollPane tableScrollPane = new JScrollPane(table);
        add(tableScrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadDataFromDatabase();
    }  
    private void addTableMouseListener() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 더블 클릭 이벤트 확인
                if (e.getClickCount() == 1) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String title = tableModel.getValueAt(row, 1).toString();
                        showPostDetails(title); 
                    }
                }
            }
        });
    }
    private void searchInDatabase(String fieldName, String searchQuery) {
        UnivHopeDb.searchInDatabase(tableModel, fieldName, searchQuery);
    }
    private void openPostDialog() {
        PostDialog postDialog = new PostDialog(loggedInUsername); 
        postDialog.setVisible(true);
    }
    private void setupSearchButton() {
        searchComboBox.addActionListener(e -> {
            String searchQuery = searchField.getText().trim();
            String selectedField = (String) searchComboBox.getSelectedItem();
            if (!searchQuery.isEmpty() && selectedField != null) {
                String fieldName = "";
                switch (selectedField) {
                    case "제목":
                        fieldName = "title";
                        break;
                    case "답변":
                        fieldName = "status";
                        break;
                    case "작성자":
                        fieldName = "author";
                        break;
                }
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM communication_board WHERE " + fieldName + " LIKE ?")) {
                    stmt.setString(1, "%" + searchQuery + "%");
                    ResultSet rs = stmt.executeQuery();
                    tableModel.setRowCount(0);
                    while (rs.next()) {
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "검색 중 오류가 발생했습니다.");
                }
            } else {
                loadDataFromDatabase();
            }
        });
    }
    private void loadDataFromDatabase() {
        UnivHopeDb.loadDataFromDatabase(tableModel);
    }
    private void showPostDetails(String title) {
        UnivHopeDb.increasePostViews(title);

        String postContentText = UnivHopeDb.getPostContent(title);
        String replyText = UnivHopeDb.getAdminReply(title);
        String replyDate = UnivHopeDb.getAdminReplyDate(title);
        if (replyDate != null && !replyDate.isEmpty()) {
            replyText += "\n\n답변 작성일: " + replyDate;
        }
 

        JTextArea postContent = new JTextArea();
        postContent.setText(postContentText);
        postContent.setWrapStyleWord(true);
        postContent.setLineWrap(true);
        postContent.setEditable(false);
        postContent.setCaretPosition(0);
        postContent.setBorder(BorderFactory.createTitledBorder("게시물 내용"));

        JTextArea adminReply = new JTextArea();
        adminReply.setText(replyText);
        adminReply.setWrapStyleWord(true);
        adminReply.setLineWrap(true);
        adminReply.setEditable(false);
        adminReply.setBorder(BorderFactory.createTitledBorder("관리자 답변"));

        // Set background color to white for the text areas
        postContent.setBackground(Color.WHITE);
        adminReply.setBackground(Color.WHITE);

        JButton backButton = new JButton("목록");
        JLabel titleLabel = new JLabel("제목: " + title);
        JLabel dateLabel = new JLabel("날짜: " + UnivHopeDb.getPostDate(title));
        JLabel authorLabel = new JLabel("작성자: " + UnivHopeDb.getPostAuthor(title));
        JLabel viewsLabel = new JLabel("조회수: " + UnivHopeDb.getPostViews(title));
        
        Font infoFont = new Font("SansSerif", Font.BOLD, 16);
        postContent.setFont(infoFont);
        adminReply.setFont(infoFont);
        titleLabel.setFont(infoFont);
        dateLabel.setFont(infoFont);
        authorLabel.setFont(infoFont);
        viewsLabel.setFont(infoFont);

        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(titleLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(viewsLabel);

        infoPanel.setBackground(Color.WHITE);
        
        JScrollPane postContentScrollPane = new JScrollPane(postContent);
        JScrollPane adminReplyScrollPane = new JScrollPane(adminReply);

        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        contentPanel.add(postContentScrollPane);
        contentPanel.add(adminReplyScrollPane);

        contentPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(infoPanel, BorderLayout.NORTH);
        topPanel.add(contentPanel, BorderLayout.CENTER);
        topPanel.add(backButton, BorderLayout.SOUTH);

        topPanel.setBackground(Color.WHITE);

        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();

        backButton.addActionListener(e -> {
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            JScrollPane tableScrollPane = new JScrollPane(table);
            mainPanel.add(tableScrollPane, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            loadDataFromDatabase(); // This should now call the static method from UnivHopeDb class
            mainPanel.revalidate();
            mainPanel.repaint();
        });
    }

    class PostDialog extends JDialog {
        private JTextField titleField;
        private JTextField authorField;
        private JTextArea contentArea;
        private JCheckBox anonymousCheckBox;
        private String displayAuthor; 

        public PostDialog(String loggedInname) {
            this.displayAuthor = loggedInname; 
            initializeDialog();
        }
        private void initializeDialog() {
            setTitle("게시물 작성");
            setSize(400, 400);
            setLayout(new BorderLayout());
            JPanel topPanel = new JPanel(new GridLayout(3, 2));
            titleField = new JTextField();
            authorField = new JTextField(displayAuthor);
            authorField.setEditable(false); 
            anonymousCheckBox = new JCheckBox("익명으로 게시");
            anonymousCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (anonymousCheckBox.isSelected()) {
                        authorField.setText("익명");
                        authorField.setEditable(false);
                    } else {
                        authorField.setText(displayAuthor);
                        authorField.setEditable(false); 
                    }
                }
            });
            topPanel.add(new JLabel("제목:"));
            topPanel.add(titleField);
            topPanel.add(new JLabel("작성자:"));
            topPanel.add(authorField);
            topPanel.add(anonymousCheckBox);
            contentArea = new JTextArea(10, 30);
            JScrollPane scrollPane = new JScrollPane(contentArea);
            postButton = new JButton("게시");
            postButton.addActionListener(e -> {
                boolean isAnonymous = anonymousCheckBox.isSelected();
                String author = isAnonymous ? "익명" : displayAuthor;
                savePostToDatabase(titleField.getText(), author, contentArea.getText(), isAnonymous);
                loadDataFromDatabase();
                dispose();
            });
            add(topPanel, BorderLayout.NORTH);
            add(scrollPane, BorderLayout.CENTER);
            add(postButton, BorderLayout.SOUTH);

            pack();
        }        
        private void prefillAuthor() {
            authorField.setText(displayAuthor); 
            authorField.setEditable(!"익명".equals(displayAuthor)); 
        }
        private void savePostToDatabase(String title, String author, String content, boolean isAnonymous) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO communication_board (title, author, content, anonymous, admin_reply) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setString(1, title);
                stmt.setString(2, author); 
                stmt.setString(3, content);
                stmt.setBoolean(4, isAnonymous); 
                stmt.setString(5, ""); 
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "데이터 저장 중 오류가 발생했습니다.");
            }
        }
        private String getDisplayName(int postId) {
            String displayName = "익명"; 
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("SELECT author, anonymous FROM communication_board WHERE id = ?")) {
                stmt.setInt(1, postId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    boolean isAnonymous = rs.getBoolean("anonymous");
                    if (!isAnonymous) {
                        displayName = rs.getString("author");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "데이터 저장 중 오류가 발생했습니다.");
            }
            return displayName; 
           }
        }
}