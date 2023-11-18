package Admin_Manager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import services.DatabaseService;
public class ManagerDashboard extends JFrame {
    private JButton addNoticeButton;
    private JButton editNoticeButton;
    private JButton deleteNoticeButton;
    private JTable noticeTable; 
    private DefaultTableModel noticeTableModel;
    private JButton refreshButton;
    private DatabaseService dbService = new DatabaseService();
    private String[] types = {
        "전체","기계공학과", "기계설계공학과", "로봇공학과", "자동화공학과", "전기공학과", "정보전자공학과",
        "반도체전자공학과", "정보통신공학과", "소방안전관리과", "컴퓨터소프트웨어공학과", "컴퓨터정보공학과",
        "인공지능소프트웨어공학과", "생명화학공학과", "바이오융합공학과", "건축과", "실내건축디자인과",
        "시각디자인과", "경영학과", "세무회계학과", "유통마케팅학과", "호텔관광학과", "경영정보학과", "빅데이터경영과"
    };
    public ManagerDashboard() {
        setTitle("관리자 페이지");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }
    private void initComponents() {
        setLayout(new FlowLayout());
        addNoticeButton = new JButton("공지 추가");
        addNoticeButton.addActionListener(e -> {
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            panel.add(new JLabel("공지 제목을 입력하세요:"), gbc);
            JTextField titleField = new JTextField(20);
            panel.add(titleField, gbc);

            panel.add(new JLabel("공지 내용을 입력하세요:"), gbc);
            JTextArea contentArea = new JTextArea(15, 25); 
            contentArea.setLineWrap(true);
            contentArea.setWrapStyleWord(true);
            JScrollPane contentScrollPane = new JScrollPane(contentArea);
            panel.add(contentScrollPane, gbc);

            panel.add(new JLabel("작성자를 입력하세요:"), gbc);
            JTextField authorField = new JTextField(20);
            panel.add(authorField, gbc);

            panel.add(new JLabel("학과를 선택하세요:"), gbc);
            JComboBox<String> typeComboBox = new JComboBox<>(types);
            panel.add(typeComboBox, gbc);

            int result = JOptionPane.showConfirmDialog(null, panel, "Enter Notice Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String title = titleField.getText();
                String content = contentArea.getText();
                String author = authorField.getText();
                String selectedType = (String) typeComboBox.getSelectedItem();

                int rowIndex = noticeTable.getSelectedRow();
                boolean pinned = rowIndex != -1 && ((Boolean) noticeTableModel.getValueAt(rowIndex, 4)).booleanValue();
                
                if (!title.isEmpty() && !content.isEmpty() && !author.isEmpty() && selectedType != null) {
                    addNotice(title, content, author, selectedType, pinned);
                }
            }
        });
        editNoticeButton = new JButton("공지 수정");
        editNoticeButton.addActionListener(e -> {
            int rowIndex = noticeTable.getSelectedRow();
            if (rowIndex == -1) {
                JOptionPane.showMessageDialog(null, "수정할 공지를 선택하세요.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String oldTitle = (String) noticeTableModel.getValueAt(rowIndex, 1);
            String oldContent = fetchContentByTitle(oldTitle); 
            String newTitle = JOptionPane.showInputDialog("Enter new title:", oldTitle);
            String newContent = JOptionPane.showInputDialog("Enter new content:", oldContent); 
            JComboBox<String> typeComboBox = new JComboBox<>(types);
            JOptionPane.showMessageDialog(null, typeComboBox, "Select Type", JOptionPane.QUESTION_MESSAGE);
            String selectedType = (String) typeComboBox.getSelectedItem();

            if (newTitle != null && !newTitle.isEmpty() && selectedType != null) {
                if (newContent != null && !newContent.isEmpty()) {
                    editNotice(oldTitle, newTitle, newContent, selectedType);
                }
            }
        });
        deleteNoticeButton = new JButton("공지 삭제");
        deleteNoticeButton.addActionListener(e -> {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "선택한 글들을 삭제하시겠습니까?", "Delete Confirmation", dialogButton);
            if (dialogResult == JOptionPane.YES_OPTION) {
                for (int row = 0; row < noticeTableModel.getRowCount(); row++) {
                	boolean isSelected = (Boolean) noticeTableModel.getValueAt(row, 0);
                    if (isSelected) {
                        String title = (String) noticeTableModel.getValueAt(row, 1); 
                        deleteNotice(title);
                    }
                }
                loadNoticesIntoTable();
            }
        });    
        refreshButton = new JButton("새로고침");
        refreshButton.addActionListener(e -> {
            loadNoticesIntoTable();
        });
        add(addNoticeButton);
        add(editNoticeButton);
        add(deleteNoticeButton);
        add(refreshButton);
        String[] columnNames = {"Select", "Title", "Type", "Content", "Pinned"};
        noticeTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 4) return Boolean.class;  
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 3 || column == 4;  
            }
        };
        noticeTable = new JTable(noticeTableModel);
        noticeTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        noticeTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 4) {  
                int rowIndex = e.getFirstRow();
                String title = (String) noticeTableModel.getValueAt(rowIndex, 1); 
                boolean pinned = (boolean) noticeTableModel.getValueAt(rowIndex, 4); 
                updatePinStatus(title, pinned);  
            }
        });
        JScrollPane scrollPane = new JScrollPane(noticeTable);
        add(scrollPane);
        loadNoticesIntoTable();
    }
    private ArrayList<String> fetchAllNotices() {
        ArrayList<String> notices = new ArrayList<>();
        return notices;
    }
    private String fetchContentByTitle(String title) {
        String content = "";
        try {
            dbService.connect();
            String sql = "SELECT content FROM notices WHERE title = ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                content = rs.getString("content");
            }
            rs.close();
            statement.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return content;
    }
    private void addNotice(String title, String content, String author, String type, boolean pinned) {
        try {
            dbService.connect();
            String sql = "INSERT INTO notices (title, content, author, type, date, pinned) VALUES (?, ?, ?, ?, CURDATE(), ?)";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, content);
            statement.setString(3, author);
            statement.setString(4, type);
            statement.setBoolean(5, pinned);
            statement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
    private void editNotice(String oldTitle, String newTitle, String newContent, String type) {
        try {
            dbService.connect();
            String sql = "UPDATE notices SET title = ?, content = ?, type = ? WHERE title = ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setString(1, newTitle);
            statement.setString(2, newContent);
            statement.setString(3, type);
            statement.setString(4, oldTitle);
            statement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dbService.disconnect();
            loadNoticesIntoTable();
        }
    }
    private void deleteNotice(String title) {
        try {
            dbService.connect();
            String sql = "DELETE FROM notices WHERE title = ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setString(1, title);
            statement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
    private void loadNoticesIntoTable() {
        try {
            dbService.connect();
            String sql = "SELECT title, type, content, pinned FROM notices ORDER BY pinned DESC, date DESC";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            noticeTableModel.setRowCount(0);
            while (rs.next()) {
                String title = rs.getString("title");
                String type = rs.getString("type");
                String content = rs.getString("content");
                boolean pinned = rs.getBoolean("pinned");
                title = pinned ? "★ " + title : title;
                noticeTableModel.addRow(new Object[]{false, title, type, content, pinned});  
            }
            rs.close();
            statement.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
    private void updatePinStatus(String title, boolean pinned) {
        try {
            title = title.replace("★ ", "");
            dbService.connect();
            String sql = "UPDATE notices SET pinned = ? WHERE title = ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setBoolean(1, pinned);
            statement.setString(2, title);
            statement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dbService.disconnect();
            loadNoticesIntoTable();
        }
    }
    private void showNoticeDetails(String title) {
        try {
            dbService.connect();
            String sql = "SELECT content FROM notices WHERE title = ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String content = rs.getString("content");
                JTextArea textArea = new JTextArea(10, 40);
                textArea.setText(content);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setCaretPosition(0);
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Notice Details", JOptionPane.INFORMATION_MESSAGE);
            }
            rs.close();
            statement.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
}