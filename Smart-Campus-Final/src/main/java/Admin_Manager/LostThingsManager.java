package Admin_Manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

public class LostThingsManager extends JFrame {
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JTable lostThingsTable;
    private DefaultTableModel lostThingsTableModel;
    private JButton refreshButton;
    private String currentCategory = "found_items";  // 기본값 설정

    public LostThingsManager() {
        setTitle("분실물 관리");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel categoryPanel = new JPanel();
        JButton foundButton = new JButton("분실물 발견");
        JButton searchButton = new JButton("분실물 찾습니다");
        foundButton.addActionListener(e -> loadDataFromDatabase("found_items"));
        searchButton.addActionListener(e -> loadDataFromDatabase("lost_items"));

        categoryPanel.add(foundButton);
        categoryPanel.add(searchButton);
        add(categoryPanel, BorderLayout.NORTH);
        
        String[] columnNames = {"ID", "제목", "내용", "작성자", "작성일", "조회수"};
        lostThingsTableModel = new DefaultTableModel(columnNames, 0);
        lostThingsTable = new JTable(lostThingsTableModel);
        
        lostThingsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int rowIndex = lostThingsTable.getSelectedRow();
                    if (rowIndex != -1) {
                        String content = (String) lostThingsTableModel.getValueAt(rowIndex, 2);
                        JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea(content, 5, 30)), "상세 내용", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        addButton = new JButton("분실물 추가");
        addButton.addActionListener(e -> {
            String title = JOptionPane.showInputDialog("분실물 제목을 입력하세요:");
            if (title != null && !title.isEmpty()) {
                String content = JOptionPane.showInputDialog("분실물 내용을 입력하세요:");
                String author = JOptionPane.showInputDialog("작성자 이름을 입력하세요:");
                if (content != null && author != null && !content.isEmpty() && !author.isEmpty()) {
                    addLostThing(title, content, author);
                }
            }
        });

        editButton = new JButton("분실물 수정");
        editButton.addActionListener(e -> {
            int rowIndex = lostThingsTable.getSelectedRow();
            if (rowIndex == -1) {
                JOptionPane.showMessageDialog(null, "수정할 분실물을 선택하세요.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String oldTitle = (String) lostThingsTableModel.getValueAt(rowIndex, 1);
            String newTitle = JOptionPane.showInputDialog("새 제목을 입력하세요:", oldTitle);
            if (newTitle != null && !newTitle.isEmpty()) {
                String newContent = JOptionPane.showInputDialog("새 내용을 입력하세요:");
                if (newContent != null && !newContent.isEmpty()) {
                    editLostThing(oldTitle, newTitle, newContent);
                }
            }
        });

        deleteButton = new JButton("분실물 삭제");
        deleteButton.addActionListener(e -> {
            int rowIndex = lostThingsTable.getSelectedRow();
            if (rowIndex == -1) {
                JOptionPane.showMessageDialog(null, "삭제할 분실물을 선택하세요.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String title = (String) lostThingsTableModel.getValueAt(rowIndex, 1);
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(null, "분실물을 삭제하시겠습니까?", "Delete Confirmation", dialogButton);
            if (dialogResult == JOptionPane.YES_OPTION) {
                deleteLostThing(title);
                loadDataFromDatabase(currentCategory);
            }
        });

        refreshButton = new JButton("새로고침");
        refreshButton.addActionListener(e -> {
            loadDataFromDatabase(currentCategory);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);


        add(new JScrollPane(lostThingsTable), BorderLayout.CENTER);

        loadDataFromDatabase(currentCategory);
    }

    private void addLostThing(String title, String content, String author) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "INSERT INTO " + currentCategory + " (title, content, author, post_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.setString(2, content);
            statement.setString(3, author);
            statement.executeUpdate();
            connection.close();
            loadDataFromDatabase(currentCategory);  // 데이터 추가 후 데이터 갱신
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void editLostThing(String oldTitle, String newTitle, String newContent) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "UPDATE " + currentCategory + " SET title = ?, content = ? WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, newTitle);
            statement.setString(2, newContent);
            statement.setString(3, oldTitle);
            statement.executeUpdate();
            connection.close();
            loadDataFromDatabase(currentCategory);  // 데이터 수정 후 데이터 갱신
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteLostThing(String title) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "DELETE FROM " + currentCategory + " WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.executeUpdate();
            connection.close();
            loadDataFromDatabase(currentCategory);  // 데이터 삭제 후 데이터 갱신
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadDataFromDatabase(String tableName) {
        currentCategory = tableName;
        String url = "jdbc:mysql://localhost:3306/self_order_kiosk?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String password = "dongyang";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT * FROM " + tableName;
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            lostThingsTableModel.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String author = rs.getString("author");
                String postDate = rs.getString("post_date");
                int views = rs.getInt("views");
                lostThingsTableModel.addRow(new Object[]{id, title, content, author, postDate, views});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "데이터 로딩 중 오류가 발생했습니다.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LostThingsManager lostThingsManager = new LostThingsManager();
            lostThingsManager.setVisible(true);
        });
    }
}
