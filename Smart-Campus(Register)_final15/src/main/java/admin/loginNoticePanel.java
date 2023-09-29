package admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

public class loginNoticePanel extends JPanel {   
	private static boolean isNoticesLoaded = false; // 공지사항이 이미 로드되었는지 확인하는 플래그
	
    private static final int PAGE_SIZE = 22;
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
                // 제목과 내용에서 검색어 찾기
                sql = "SELECT * FROM notices WHERE title LIKE ? OR content LIKE ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, "%" + searchQuery + "%");
                statement.setString(2, "%" + searchQuery + "%");  // 내용에서도 검색
                statement.setInt(3, PAGE_SIZE);
                statement.setInt(4, offset);
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
        
        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) getNoticeCount() / PAGE_SIZE);

        // 페이지 정보를 표시하는 레이블 생성
        JLabel pageInfo = new JLabel(pageNumber + " / " + totalPages);

        JButton nextButton = new JButton(">");
        nextButton.addActionListener(e -> {
            if (pageNumber < totalPages) { 
                pageNumber++;
                isNoticesLoaded = false;
                showNoticeTableOnPanel(panel);
                pageInfo.setText(pageNumber + " / " + totalPages); 
            }
        });

        JButton prevButton = new JButton("<");
        prevButton.addActionListener(e -> {
            if (pageNumber > 1) {
                pageNumber--;
                isNoticesLoaded = false;
                showNoticeTableOnPanel(panel);
                pageInfo.setText(pageNumber + " / " + totalPages); 
            }
        });

        // 첫 페이지로 이동하는 버튼
        JButton firstPageButton = new JButton("<<");
        firstPageButton.addActionListener(e -> {
            if (pageNumber != 1) {
                pageNumber = 1;
                isNoticesLoaded = false;
                showNoticeTableOnPanel(panel);
                pageInfo.setText(pageNumber + " / " + totalPages);
            }
        });

        // 마지막 페이지로 이동하는 버튼
        JButton lastPageButton = new JButton(">>");
        lastPageButton.addActionListener(e -> {
            if (pageNumber != totalPages) {
                pageNumber = totalPages;
                isNoticesLoaded = false;
                showNoticeTableOnPanel(panel);
                pageInfo.setText(pageNumber + " / " + totalPages);
            }
        });

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("검색");
        searchButton.addActionListener(e -> {
            searchQuery = searchField.getText().trim(); // 사용자가 입력한 검색어 가져오기
            pageNumber = 1; // 검색 결과의 첫 페이지를 보여주기 위해 pageNumber를 1로 설정
            isNoticesLoaded = false; // 공지사항을 다시 로드하기 위한 플래그 초기화
            showNoticeTableOnPanel(panel); // 검색 결과를 반영하여 테이블을 다시 표시
        });

        JPanel navigationPanel = new JPanel(new FlowLayout());
        navigationPanel.add(firstPageButton);  // 첫 페이지로 이동하는 버튼 추가
        navigationPanel.add(prevButton);
        navigationPanel.add(pageInfo); 
        navigationPanel.add(nextButton);
        navigationPanel.add(lastPageButton);  // 마지막 페이지로 이동하는 버튼 추가
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
        
        JButton toMainButton = new JButton("첫 화면으로");
        toMainButton.setPreferredSize(refreshButton.getPreferredSize()); // "첫 화면으로" 버튼의 크기를 "새로고침" 버튼과 동일하게 설정
        toMainButton.addActionListener(e -> {
            searchQuery = ""; // 검색어 초기화
            pageNumber = 1;   // 페이지 번호 초기화
            isNoticesLoaded = false; // 공지사항을 다시 로드하기 위한 플래그 초기화
            showNoticeTableOnPanel(panel); // 원래의 공지 목록을 다시 표시
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
                    panel.removeAll();
                    panel.setLayout(new BorderLayout());

                    JPanel topPanelBack = new JPanel(new BorderLayout());
                    topPanelBack.add(lblNoticeCount, BorderLayout.WEST);
                    topPanelBack.add(toMainButton, BorderLayout.CENTER); // "첫 화면으로" 버튼 다시 추가
                    topPanelBack.add(refreshButton, BorderLayout.EAST);

                    panel.add(topPanelBack, BorderLayout.NORTH);
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
                panel.revalidate();
                panel.repaint();
            }
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(lblNoticeCount, BorderLayout.WEST);
        topPanel.add(toMainButton, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(noticeTable), BorderLayout.CENTER);
        panel.add(navigationPanel, BorderLayout.SOUTH);
        panel.revalidate();
        panel.repaint();
        isNoticesLoaded = true;
    }
}