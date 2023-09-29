package LostThi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LostThingsFind extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;
    private String currentCategory = "found_items";  // 기본값 설정

    public LostThingsFind() {
        mainPanel = this;
        setLayout(new BorderLayout());

        JPanel categoryPanel = new JPanel();
        JButton foundButton = new JButton("분실물 발견");
        JButton searchButton = new JButton("분실물 찾습니다");

        foundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataFromDatabase("found_items");
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataFromDatabase("lost_items");
            }
        });

        categoryPanel.add(foundButton);
        categoryPanel.add(searchButton);
        add(categoryPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"번호", "제목", "내용", "작성자", "작성일", "조회수"}, 0);
        table = new JTable(tableModel);

        // 컬럼 너비 설정
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(4).setPreferredWidth(217); // 작성일 컬럼 너비 설정
        table.getColumnModel().getColumn(5).setPreferredWidth(50);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                int postId = (int) tableModel.getValueAt(rowIndex, 0);
                String category = currentCategory;
                incrementViewCount(postId, category);
                loadDataFromDatabase(currentCategory);

                String title = (String) tableModel.getValueAt(rowIndex, 1);
                String content = (String) tableModel.getValueAt(rowIndex, 2);
                mainPanel.removeAll();
                showContentInPanel(content);
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadDataFromDatabase(String tableName) {
        String url = "jdbc:mysql://localhost:3306/self_order_kiosk?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String password = "dongyang";

        currentCategory = tableName;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM " + tableName;
            ResultSet rs = stmt.executeQuery(query);

            tableModel.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String author = rs.getString("author");

                // 작성일 포맷 변경
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String postDate = sdf.format(rs.getTimestamp("post_date"));

                int views = rs.getInt("views");

                tableModel.addRow(new Object[]{id, title, content, author, postDate, views});
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "데이터 로딩 중 오류가 발생했습니다.");
        }
    }
    
    public void incrementViewCount(int postId, String category) {
        String url = "jdbc:mysql://localhost:3306/self_order_kiosk?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String password = "dongyang";

        String sql = "UPDATE " + category + " SET views = views + 1 WHERE id = ?";

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
    

    private void showContentInPanel(String content) {
        JTextArea textArea = new JTextArea(10, 40);
        textArea.setText(content);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setCaretPosition(0);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JButton backButton = new JButton("뒤로 가기");
        backButton.addActionListener(event -> {
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            
            JPanel categoryPanel = new JPanel();
            JButton foundButton = new JButton("분실물 발견");
            JButton searchButton = new JButton("분실물 찾습니다");

            foundButton.addActionListener(e -> loadDataFromDatabase("found_items"));
            searchButton.addActionListener(e -> loadDataFromDatabase("lost_items"));

            categoryPanel.add(foundButton);
            categoryPanel.add(searchButton);
            
            mainPanel.add(categoryPanel, BorderLayout.NORTH);
            mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }
}
