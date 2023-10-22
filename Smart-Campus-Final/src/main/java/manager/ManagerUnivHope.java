package manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ManagerUnivHope extends JFrame {
    private JTable waitingTable;
    private DefaultTableModel waitingTableModel;
    private JTable repliedTable;
    private DefaultTableModel repliedTableModel;

    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JButton replyButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=Asia/Seoul&characterEncoding=utf-8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "dongyang";

    public ManagerUnivHope() {
        setTitle("학교에 바란다 - 관리자 페이지");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        waitingTableModel = new DefaultTableModel(new Object[]{"번호", "제목", "작성자", "작성일", "조회수"}, 0);
        waitingTable = new JTable(waitingTableModel);
        tabbedPane.addTab("답변 대기", new JScrollPane(waitingTable));

        repliedTableModel = new DefaultTableModel(new Object[]{"번호", "제목", "작성자", "작성일", "조회수"}, 0);
        repliedTable = new JTable(repliedTableModel);
        tabbedPane.addTab("답변 완료", new JScrollPane(repliedTable));

        add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        addButton = new JButton("추가");
        addButton.addActionListener(e -> {
            PostDialog postDialog = new PostDialog(null);
            postDialog.setVisible(true);
            loadDataFromDatabase();
        });
        buttonPanel.add(addButton);

        editButton = new JButton("수정");
        buttonPanel.add(editButton);

        deleteButton = new JButton("삭제");
        buttonPanel.add(deleteButton);

        replyButton = new JButton("답변달기");
        buttonPanel.add(replyButton);

        refreshButton = new JButton("새로고침");
        refreshButton.addActionListener(e -> loadDataFromDatabase());
        buttonPanel.add(refreshButton);

        add(buttonPanel, BorderLayout.SOUTH);
        
        loadDataFromDatabase();
        
        editButton.addActionListener(e -> {
            JTable currentTable = tabbedPane.getSelectedIndex() == 0 ? waitingTable : repliedTable;
            int selectedRow = currentTable.getSelectedRow();
            if (selectedRow != -1) {
                String title = (String) currentTable.getValueAt(selectedRow, 1);
                PostDialog postDialog = new PostDialog(title);
                postDialog.setVisible(true);
                loadDataFromDatabase();
            }
        });

        deleteButton.addActionListener(e -> {
            JTable currentTable = tabbedPane.getSelectedIndex() == 0 ? waitingTable : repliedTable;
            int selectedRow = currentTable.getSelectedRow();
            if (selectedRow != -1) {
                String title = (String) currentTable.getValueAt(selectedRow, 1);
                int confirm = JOptionPane.showConfirmDialog(null, "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    deletePost(title);
                    loadDataFromDatabase();
                }
            }
        });

        replyButton.addActionListener(e -> {
            int selectedRow = waitingTable.getSelectedRow();
            if (selectedRow != -1) {
                String title = (String) waitingTable.getValueAt(selectedRow, 1);
                JTextArea replyArea = new JTextArea();
                replyArea.setBorder(BorderFactory.createTitledBorder("관리자 답변"));
                int result = JOptionPane.showConfirmDialog(null, new JScrollPane(replyArea), "답변 입력", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    saveAdminReply(title, replyArea.getText());
                    loadDataFromDatabase();
                }
            } else {
                JOptionPane.showMessageDialog(null, "답변 대기 목록에서 게시글을 선택해주세요.");
            }
        });
        
        waitingTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {  // 더블 클릭을 감지
                    int row = waitingTable.getSelectedRow();
                    String title = (String) waitingTable.getValueAt(row, 1);
                    JTextArea textArea = getPostDetails(title);
                    JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "게시글 내용", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        repliedTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {  // 더블 클릭을 감지
                    int row = repliedTable.getSelectedRow();
                    String title = (String) repliedTable.getValueAt(row, 1);
                    JTextArea textArea = getPostDetails(title);
                    JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "게시글 내용", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });


        editButton.addActionListener(e -> {
            JTable currentTable = tabbedPane.getSelectedIndex() == 0 ? waitingTable : repliedTable;
            int selectedRow = currentTable.getSelectedRow();
            if (selectedRow != -1) {
                String title = (String) currentTable.getValueAt(selectedRow, 1);
                if (currentTable == repliedTable) {
                    JTextArea replyArea = new JTextArea();
                    replyArea.setBorder(BorderFactory.createTitledBorder("관리자 답변 수정"));
                    int result = JOptionPane.showConfirmDialog(null, new JScrollPane(replyArea), "답변 수정", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        saveAdminReply(title, replyArea.getText());
                        loadDataFromDatabase();
                    }
                } else {
                    PostDialog postDialog = new PostDialog(title);
                    postDialog.setVisible(true);
                    loadDataFromDatabase();
                }
            }
        });
    }

    private void loadDataFromDatabase() {
        loadWaitingData();
        loadRepliedData();
    }

    private void loadWaitingData() {
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
            JOptionPane.showMessageDialog(this, "데이터 로딩 중 오류가 발생했습니다.");
        }
    }

    private void loadRepliedData() {
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
            JOptionPane.showMessageDialog(this, "데이터 로딩 중 오류가 발생했습니다.");
        }
    }



    private JTextArea getPostDetails(String title) {
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


    private void deletePost(String title) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "게시글 삭제 중 오류가 발생했습니다.");
        }
    }

    private void saveAdminReply(String title, String reply) {
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
    


    class PostDialog extends JDialog {
        private JTextField titleField;
        private JTextArea contentArea;
        private JButton saveButton;
        private String originalTitle;

        public PostDialog(String title) {
            this.originalTitle = title;

            setTitle(title == null ? "게시글 추가" : "게시글 수정");
            setSize(400, 300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            titleField = new JTextField();
            contentArea = new JTextArea();

            if (title != null) {
                loadPostDetails(title);
            }

            saveButton = new JButton(title == null ? "저장" : "수정하기");
            saveButton.addActionListener(e -> {
                if (originalTitle == null) {
                    addPost(titleField.getText(), contentArea.getText());
                } else {
                    updatePost(originalTitle, titleField.getText(), contentArea.getText());
                }
                dispose();
            });

            add(titleField, BorderLayout.NORTH);
            add(new JScrollPane(contentArea), BorderLayout.CENTER);
            add(saveButton, BorderLayout.SOUTH);
        }

        private void loadPostDetails(String title) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT title, content FROM communication_board WHERE title = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, title);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    titleField.setText(rs.getString("title"));
                    contentArea.setText(rs.getString("content"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void addPost(String title, String content) {
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


        private void updatePost(String originalTitle, String newTitle, String content) {
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ManagerUnivHope().setVisible(true);
        });
    }
}