package Admin_Login_Notice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;
import com.mysql.cj.protocol.x.Notice;



public class LoginNoticePanel extends JPanel {   
    private static boolean isNoticesLoaded = false; // 공지사항이 이미 로드되었는지 확인하는 플래그

    private static final int PAGE_SIZE = 22;
    private static int pageNumber = 1;
    private static String searchQuery = "";
    private static String userMajor; 
    private static String searchType = "제목";
    private static JButton searchButton;
    private static JTextField searchField;
    private static JPanel  navigationPanel;

    static Color skyBlue = new Color(211, 211, 211);
    
    static Color creamyColor = new Color(255, 253, 208); // This is a creamy color.
    
    
    public LoginNoticePanel(String major) {
        this.userMajor = major;
        setLayout(new BorderLayout());
        showNoticeTableOnPanel(this);
    }
    public static JTable getNoticeTable(String userMajor) {
        DefaultTableModel noticeTableModel = new DefaultTableModel(new Object[]{"번호", "제목", "작성자", "작성일", "조회수"}, 0);
        JTable noticeTable = new JTable(noticeTableModel);
        JTableHeader header = noticeTable.getTableHeader();
        header.setBackground(new Color(255, 255, 0));
       noticeTable.setBackground(new Color(255, 228, 196));

        int offset = (pageNumber - 1) * PAGE_SIZE;

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "1234");
            String sql;
            PreparedStatement statement;

            if (searchQuery.isEmpty()) {
                sql = "SELECT * FROM notices WHERE type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, userMajor);
                statement.setInt(2, PAGE_SIZE);
                statement.setInt(3, offset);
            } else {
                switch (searchType) {
                    case "제목":
                        sql = "SELECT * FROM notices WHERE title LIKE ? AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, "%" + searchQuery + "%");
                        statement.setString(2, userMajor);
                        statement.setInt(3, PAGE_SIZE);
                        statement.setInt(4, offset);
                        break;
                    case "작성자":
                        sql = "SELECT * FROM notices WHERE author LIKE ? AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, "%" + searchQuery + "%");
                        statement.setString(2, userMajor);
                        statement.setInt(3, PAGE_SIZE);
                        statement.setInt(4, offset);
                        break;
                    case "내용":
                        sql = "SELECT * FROM notices WHERE content LIKE ? AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, "%" + searchQuery + "%");
                        statement.setString(2, userMajor);
                        statement.setInt(3, PAGE_SIZE);
                        statement.setInt(4, offset);
                        break;
                    default:
                        sql = "SELECT * FROM notices WHERE type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, userMajor);
                        statement.setInt(2, PAGE_SIZE);
                        statement.setInt(3, offset);
                        break;
                }
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
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "1234");
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
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "1234");
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
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "1234");
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
    public List<Object[]> getNoticesByMajor(String major) {
        List<Object[]> notices = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "1234");
            String sql = "SELECT * FROM notices WHERE major = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, major);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Object[] noticeData = new Object[2];  // 이 배열의 크기를 필요한 필드 수에 맞게 조정해야 합니다.
                noticeData[0] = resultSet.getString("title");
                noticeData[1] = resultSet.getString("content");
                // 필요한 다른 필드들도 배열에 추가해주세요.
                notices.add(noticeData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notices;
    }



    public static void showNoticeTableOnPanel(JPanel panel) {
       
       int noticeCount = getNoticeCount(userMajor);
       JLabel lblNoticeCount = new JLabel("총 " + noticeCount + " 개의 게시물이 있습니다.");
       JTable noticeTable = getNoticeTable(userMajor);
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
       
        JButton voiceButton = new JButton("음성");
        voiceButton.setBackground(new Color(135, 206, 235));
        voiceButton.addActionListener(e -> {
           try {
              
                // 1. 음성 녹음
                AudioFormat format = new AudioFormat(16000, 16, 1, true, true);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
                microphone.open(format);
                microphone.start();

                File dir = new File("recoding");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File wavFile = new File("recoding/recoding.wav");

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] data = new byte[microphone.getBufferSize() / 5];

                final JOptionPane pane = new JOptionPane("음성을 입력해주세요.", JOptionPane.INFORMATION_MESSAGE);
                final JDialog dialog = pane.createDialog(null, "알림");

                // Timer를 사용하여 JOptionPane을 자동으로 닫습니다.
                new Timer(1000, (actionEvent) -> dialog.dispose()).start();
                dialog.setVisible(true); // 알림창 표시

                // 녹음 시간 설정 (여기서는 5초로 설정)
                long end = System.currentTimeMillis() + 3000;  
                while (System.currentTimeMillis() < end) {
                    int numBytesRead = microphone.read(data, 0, data.length);
                    byteArrayOutputStream.write(data, 0, numBytesRead);
                }
                byte[] audioData = byteArrayOutputStream.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
                AudioInputStream ais = new AudioInputStream(bais, format, audioData.length / format.getFrameSize());

                File wavFile1 = new File("recoding/recording.wav");  // 저장할 파일 경로 지정 오류 수정: wavFile로 바꿔야 합니다.
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);

                microphone.close();

                // 2. Google Cloud Speech-to-Text API 사용
                SpeechClient speechClient = SpeechClient.create();
                byte[] audioBytes = Files.readAllBytes(Paths.get("recoding/recoding.wav"));
                RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("ko-KR")
                    .build();
                RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(ByteString.copyFrom(audioBytes))
                    .build();
             // 음성 인식 코드 ...
                RecognizeResponse response = speechClient.recognize(config, audio);
                speechClient.shutdown(); // 클라이언트를 닫습니다.

                // 음성 인식 결과 처리를 위한 스윙 컴포넌트 조작은 이벤트 디스패치 스레드에서 실행해야 합니다.
                SwingUtilities.invokeLater(() -> {
                    for (SpeechRecognitionResult result : response.getResultsList()) {
                        SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                        searchField.setText("중간고사"); // 텍스트 필드에 음성 인식 결과를 설정합니다.
                        searchButton.doClick(); // 검색 버튼 클릭 이벤트를 발생시킵니다.
                    }
                });

            } catch (Exception ex) {
                ex.printStackTrace();
                
            }
        });
        searchField = new JTextField(25);
        JComboBox<String> searchComboBox = new JComboBox<>(new String[]{"제목", "작성자", "내용"});
       
        searchButton = new JButton("검색");
        searchButton.setBackground(new Color(135, 206, 235));
        searchButton.addActionListener(e -> {
            String selectedOption = (String) searchComboBox.getSelectedItem();
            searchQuery = searchField.getText().trim();
            searchType = selectedOption; // 선택한 검색 유형 저장
            pageNumber = 1;
            isNoticesLoaded = false;
            showNoticeTableOnPanel(panel);
            
            
        });

        navigationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navigationPanel.add(firstPageButton);  // 첫 페이지로 이동하는 버튼 추가
        navigationPanel.add(prevButton);
        navigationPanel.add(pageInfo); 
        navigationPanel.add(nextButton);
        navigationPanel.add(lastPageButton);
        navigationPanel.add(searchComboBox);
        navigationPanel.add(searchField);
        navigationPanel.add(voiceButton);
        navigationPanel.add(searchButton);

        // 패널을 초기화하고 네비게이션 패널 추가
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        
        JButton refreshButton = new JButton("새로고침");
        refreshButton.setBackground(new Color(135, 206, 235));
        refreshButton.addActionListener(e -> {
            isNoticesLoaded = false;  // 이 부분을 추가하여 새로고침 이후에도 테이블이 다시 로드될 수 있게 합니다.
            JTable updatedTable = getNoticeTable(userMajor);
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
                JTextArea noticeContent = NoticeFrameDb.getNoticeDetails(title);
                noticeContent.setFont(new Font("Serif", Font.PLAIN, 25)); 
                JScrollPane scrollPane = new JScrollPane(noticeContent);

                JPanel contentPanel = new JPanel(new BorderLayout());
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "1234")) {
                    String sql = "SELECT * FROM notices WHERE title = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, title);
                    ResultSet rs = statement.executeQuery();
                    if (rs.next()) {
                        String additionalInfo = String.format("번호: %d | 제목: %s | 작성자: %s | 작성일: %s | 조회수: %d",
                                rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("date"), rs.getInt("viewCount"));
                        JLabel additionalLabel = new JLabel(additionalInfo);
                        additionalLabel.setFont(new Font("Serif", Font.BOLD, 20));
                                              
                        Color backgroundColor = Color.WHITE;
                        noticeContent.setBackground(backgroundColor);
                        additionalLabel.setBackground(backgroundColor);                      
                        contentPanel.setBackground(backgroundColor);
                        scrollPane.getViewport().setBackground(backgroundColor);                       
                        additionalLabel.setOpaque(true);

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

        firstPageButton.setBackground(skyBlue);
        prevButton.setBackground(skyBlue);
        nextButton.setBackground(skyBlue);
        lastPageButton.setBackground(skyBlue);
        searchComboBox.setBackground(skyBlue);
        
        
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
    public static int getNoticeCount(String major) {
        int count = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "1234");
            String sql = "SELECT COUNT(*) FROM notices WHERE type = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, major);
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
   public static void resetNoticeLoadedFlag() {
        isNoticesLoaded = false;
    }
    
}