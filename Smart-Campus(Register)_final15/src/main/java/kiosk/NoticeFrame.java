package kiosk;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class NoticeFrame {   
	private static boolean isNoticesLoaded = false; // 공지사항이 이미 로드되었는지 확인하는 플래그
	
    private static final int PAGE_SIZE = 20;
    private static int pageNumber = 1;
    private static String searchQuery = "";
    

    public static void resetNoticeLoadedFlag() { //플래그 초기화 시킴
        isNoticesLoaded = false;
    }

    public static JTable getNoticeTable() {
        DefaultTableModel noticeTableModel = new DefaultTableModel(new Object[]{"번호", "제목", "작성자", "작성일", "조회수"}, 0);
        JTable noticeTable = new JTable(noticeTableModel);
        int offset = (pageNumber - 1) * PAGE_SIZE;

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql;
            PreparedStatement statement;
            if (searchQuery.isEmpty()) {
                sql = "SELECT * FROM notices ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?"; 
                statement = connection.prepareStatement(sql);
                statement.setInt(1, PAGE_SIZE);
                statement.setInt(2, offset);
            } else {
                sql = "SELECT * FROM notices WHERE title LIKE ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + searchQuery + "%");
                statement.setInt(2, PAGE_SIZE);
                statement.setInt(3, offset);
            }
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Object identifier; 
                boolean isPinned = rs.getBoolean("pinned");
                if (isPinned) {
                    identifier = "★"; 
                } else {
                    identifier = rs.getInt("id");
                }

                Object[] row = {
                    identifier,
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("date"),
                    rs.getInt("viewCount")
                };
                noticeTableModel.addRow(row);
            }

            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return noticeTable;
    }

    
    public static JTextArea getNoticeDetails(String title) {
        JTextArea textArea = new JTextArea(10, 40);
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT content FROM notices WHERE title = ?";
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

            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textArea;
    }
    
    public static void incrementViewCount(String title) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "UPDATE notices SET viewCount = viewCount + 1 WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            int updatedRows = statement.executeUpdate();
            if (updatedRows == 0) {
                System.out.println("No rows updated for title: " + title);
            } else {
                System.out.println("Updated view count for title: " + title);
            }
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getNoticeCount() {
        int count = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT COUNT(*) FROM notices";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
            
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static void showNoticeTableOnPanel(JPanel panel) {
        if (isNoticesLoaded) return;

        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> {
            pageNumber++;
            isNoticesLoaded = false; // 테이블을 새로 로드하도록 플래그를 설정
            showNoticeTableOnPanel(panel); // 테이블 업데이트
        });

        JButton prevButton = new JButton("<");
        prevButton.addActionListener(e -> {
            if (pageNumber > 1) {
                pageNumber--;
                isNoticesLoaded = false; // 테이블을 새로 로드하도록 플래그를 설정
                showNoticeTableOnPanel(panel); // 테이블 업데이트
            }
        });

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("검색");
        searchButton.addActionListener(e -> {
            searchQuery = searchField.getText().trim();
            pageNumber = 1;
            showNoticeTableOnPanel(panel);
        });

        JPanel navigationPanel = new JPanel(new FlowLayout());
        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        navigationPanel.add(searchField);
        navigationPanel.add(searchButton);

        // 패널을 초기화하고 네비게이션 패널 추가
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        
        int noticeCount = getNoticeCount();
        JLabel lblNoticeCount = new JLabel("총 " + noticeCount + " 개의 게시물이 있습니다.");
        JTable noticeTable = getNoticeTable();

        JButton refreshButton = new JButton("새로고침");
        refreshButton.addActionListener(e -> {
            isNoticesLoaded = false;  // 이 부분을 추가하여 새로고침 이후에도 테이블이 다시 로드될 수 있게 합니다.
            JTable updatedTable = getNoticeTable();
            panel.removeAll();
            panel.setLayout(new BorderLayout());
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(lblNoticeCount, BorderLayout.WEST);
            topPanel.add(refreshButton, BorderLayout.EAST);
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(updatedTable), BorderLayout.CENTER);
            panel.add(navigationPanel, BorderLayout.SOUTH); 
            panel.revalidate();
            panel.repaint();
            showNoticeTableOnPanel(panel);  // 새로고침 이후에도 테이블을 다시 로드하게 합니다.
        });
        noticeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = noticeTable.getSelectedRow();
                String title = (String) noticeTable.getModel().getValueAt(rowIndex, 1);
                incrementViewCount(title);
                JTextArea noticeContent = getNoticeDetails(title);
                JScrollPane scrollPane = new JScrollPane(noticeContent);

                JPanel contentPanel = new JPanel(new BorderLayout());
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang")) {
                    String sql = "SELECT * FROM notices WHERE title = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, title);
                    ResultSet rs = statement.executeQuery();
                    if (rs.next()) {
                        String additionalInfo = String.format("번호: %d | 제목: %s | 작성자: %s | 작성일: %s | 조회수: %d",
                                rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("date"), rs.getInt("viewCount"));
                        JLabel additionalLabel = new JLabel(additionalInfo);
                        contentPanel.add(additionalLabel, BorderLayout.NORTH);
                        contentPanel.add(scrollPane, BorderLayout.CENTER);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                JButton backButton = new JButton("뒤로 가기");
                backButton.addActionListener(event -> {
                    int noticeCountInside = getNoticeCount();
                    JLabel lblNoticeCountInside = new JLabel("총 " + noticeCountInside + " 개의 게시물이 있습니다.");
                    panel.removeAll();
                    panel.setLayout(new BorderLayout());
                    JPanel topPanel = new JPanel(new BorderLayout());
                    topPanel.add(lblNoticeCount, BorderLayout.WEST);
                    topPanel.add(refreshButton, BorderLayout.EAST);
                    panel.add(topPanel, BorderLayout.NORTH);
                    panel.add(new JScrollPane(noticeTable), BorderLayout.CENTER);
                    panel.add(navigationPanel, BorderLayout.SOUTH);
                    panel.revalidate();
                    panel.repaint();
                    isNoticesLoaded = true;
                });

                contentPanel.add(backButton, BorderLayout.SOUTH);
                panel.removeAll();
                panel.setLayout(new BorderLayout());
                panel.add(contentPanel, BorderLayout.CENTER); 
                // 여기서는 panel.add(navigationPanel, BorderLayout.SOUTH);를 추가하지 않습니다.
                panel.revalidate();
                panel.repaint();
            }
        });


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lblNoticeCount, BorderLayout.WEST);
        topPanel.add(refreshButton, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(noticeTable), BorderLayout.CENTER);
        panel.add(navigationPanel, BorderLayout.SOUTH);
        panel.revalidate();
        panel.repaint();
        isNoticesLoaded = true;
    }
}