package kiosk;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class NoticeFrame {   

   public static JTable getNoticeTable() {
       DefaultTableModel noticeTableModel = new DefaultTableModel(new Object[]{"번호", "제목", "작성자", "작성일", "조회수"}, 0);
       JTable noticeTable = new JTable(noticeTableModel);

       try {
           Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
           String sql = "SELECT * FROM notices ORDER BY pinned DESC, id DESC"; 
           PreparedStatement statement = connection.prepareStatement(sql);
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
        int tabIndex = 0;  // 공지사항 탭의 인덱스를 0으로 가정합니다.

        if (tabIndex == 0) {
            int noticeCount = getNoticeCount();
            JLabel lblNoticeCount = new JLabel("총 " + noticeCount + " 개의 게시물이 있습니다.");
            JTable noticeTable = getNoticeTable();

            // 새로고침 버튼 생성 및 액션 추가
            JButton refreshButton = new JButton("새로고침");
            refreshButton.addActionListener(e -> {
                JTable updatedTable = getNoticeTable();
                panel.removeAll();
                panel.setLayout(new BorderLayout());
                panel.add(lblNoticeCount, BorderLayout.NORTH);
                panel.add(new JScrollPane(updatedTable), BorderLayout.CENTER);
                panel.revalidate();
                panel.repaint();
                showNoticeTableOnPanel(panel);  // 테이블 갱신 후 다시 이벤트 리스너 추가
            });

            JPanel contentPanel = new JPanel(new BorderLayout());  // 이 부분을 외부로 이동

            noticeTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int rowIndex = noticeTable.getSelectedRow();
                    String title = (String) noticeTable.getModel().getValueAt(rowIndex, 1);
                    incrementViewCount(title);

                    JTextArea noticeContent = getNoticeDetails(title);
                    JScrollPane scrollPane = new JScrollPane(noticeContent);

                    JPanel contentPanel = new JPanel(new BorderLayout()); // 이 부분을 여기로 이동

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
                        
                        // 새로고침 버튼도 다시 추가합니다.
                        JPanel topPanel = new JPanel(new BorderLayout());
                        topPanel.add(lblNoticeCountInside, BorderLayout.WEST);
                        topPanel.add(refreshButton, BorderLayout.EAST);
                        panel.add(topPanel, BorderLayout.NORTH);
                        
                        panel.add(new JScrollPane(noticeTable), BorderLayout.CENTER);
                        panel.revalidate();
                        panel.repaint();
                    });

                    contentPanel.add(backButton, BorderLayout.SOUTH);
                    panel.removeAll();
                    panel.setLayout(new BorderLayout());
                    panel.add(contentPanel, BorderLayout.CENTER); // 이 부분만 panel에 추가합니다.
                    panel.revalidate();
                    panel.repaint();
                }
            });


            panel.removeAll();
            panel.setLayout(new BorderLayout());
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(lblNoticeCount, BorderLayout.WEST);
            topPanel.add(refreshButton, BorderLayout.EAST);
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(noticeTable), BorderLayout.CENTER);
            panel.revalidate();
            panel.repaint();
        }
    }
}
