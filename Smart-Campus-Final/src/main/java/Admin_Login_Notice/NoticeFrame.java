package Admin_Login_Notice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

public class NoticeFrame {
    private static boolean isNoticesLoaded = false; // 공지사항이 이미 로드되었는지 확인하는 플래그
    private static final int PAGE_SIZE = 22;
    private static int pageNumber = 1;
    private static String searchQuery = "";
    private static String noticeType = "전체"; // 공지 유형을 '전체'로 기본 설정
    private static JButton searchButton;
    private static JTextField searchField;
    private static String searchType = "제목";
    private static JComboBox<String> searchComboBox;
 
    static Color skyBlue = new Color(211, 211, 211);
    
    public static void resetNoticeLoadedFlag() {
        isNoticesLoaded = false;
    }
    public static JTable getNoticeTable() {
        DefaultTableModel noticeTableModel = new DefaultTableModel(new Object[]{"번호", "제목", "작성자", "작성일", "조회수"}, 0);
        JTable noticeTable = new JTable(noticeTableModel);
        TableColumn c1 = noticeTable.getColumnModel().getColumn(0);
        c1.setPreferredWidth(20);
        TableColumn c2 = noticeTable.getColumnModel().getColumn(1);
        c2.setPreferredWidth(80);
        TableColumn c3 = noticeTable.getColumnModel().getColumn(2);
        c3.setPreferredWidth(40);
        TableColumn c4 = noticeTable.getColumnModel().getColumn(3);
        c4.setPreferredWidth(40);
        TableColumn c5 = noticeTable.getColumnModel().getColumn(4);
        c5.setPreferredWidth(20);
        
        System.out.println(c1.getPreferredWidth());
        System.out.println(noticeTable.getColumnModel().getColumn(0).getPreferredWidth());
        System.out.println(c2.getPreferredWidth());
        System.out.println(noticeTable.getColumnModel().getColumn(1).getPreferredWidth());
        System.out.println(noticeTable.getColumnModel().getColumn(1).getHeaderValue());
        
        
        JTableHeader header = noticeTable.getTableHeader();
        Dimension headerSize = header.getPreferredSize();
        headerSize.height = 30; // 원하는 높이로 설정
        
        header.setPreferredSize(headerSize);
        header.setBackground(new Color(96, 140, 255));
        header.setForeground(Color.white);
        header.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        
        noticeTable.setBackground(Color.white);
        noticeTable.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        int rowHeight = 30; // 원하는 행 높이를 지정
        noticeTable.setRowHeight(rowHeight);
        
        int offset = (pageNumber - 1) * PAGE_SIZE;
        
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql;
            PreparedStatement statement;

            if (searchQuery.isEmpty()) {
                sql = "SELECT * FROM notices WHERE type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, noticeType);
                statement.setInt(2, PAGE_SIZE);
                statement.setInt(3, offset);
            } else {
                // 검색 유형에 따라 다른 SQL 쿼리를 준비합니다.
                if (searchType.equals("제목")) {
                    sql = "SELECT * FROM notices WHERE title LIKE ? AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                } else if (searchType.equals("내용")) {
                    sql = "SELECT * FROM notices WHERE content LIKE ? AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                } else if (searchType.equals("작성자")) {
                    sql = "SELECT * FROM notices WHERE author LIKE ? AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                } else {
                    // 기본적으로 모든 유형을 포함하도록 설정합니다.
                    sql = "SELECT * FROM notices WHERE (title LIKE ? OR content LIKE ? OR author LIKE ?) AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                }
                statement = connection.prepareStatement(sql);
                // 검색 유형에 맞게 SQL 쿼리 파라미터 설정
                int paramIndex = 1;
                if (searchType.equals("제목")) {
                    statement.setString(paramIndex++, "%" + searchQuery + "%");
                }
                if (searchType.equals("내용")) {
                    statement.setString(paramIndex++, "%" + searchQuery + "%");
                }
                if (searchType.equals("작성자")) {
                    statement.setString(paramIndex++, "%" + searchQuery + "%");
                }
                statement.setString(paramIndex++, noticeType);
                statement.setInt(paramIndex++, PAGE_SIZE);
                statement.setInt(paramIndex, offset);
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
    // 열의 길이를 조정하는 함수
    private static void setColumnWidth(JTable table, int columnIndex, int width) {
       int tableWidth = table.getWidth();
       int columnWidth = (tableWidth * width) / 100;
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(width);
        column.setMinWidth(width);
        column.setMaxWidth(width);
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
            String sql = "SELECT COUNT(*) FROM notices WHERE type = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, noticeType);
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
    
    public static int getTotalNoticeCount() {
        return getNoticeCount();
    }

    public static void showNoticeTableOnPanel(JPanel panel) {
        if (isNoticesLoaded) return;
        
        // 총 공지사항의 개수를 가져옵니다.
        int totalNoticeCount = getTotalNoticeCount();
        JLabel lblTotalNoticeCount = new JLabel("총 " + totalNoticeCount + " 개의 게시물이 있습니다.");
        
        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) getNoticeCount() / PAGE_SIZE);

        // 페이지 정보를 표시하는 레이블 생성
        JLabel pageInfo = new JLabel(pageNumber + " / " + totalPages);
        
        JButton toMainButton = new JButton("첫 화면으로");
        toMainButton.addActionListener(e -> {
            searchQuery = ""; // 검색어 초기화
            pageNumber = 1;   // 페이지 번호 초기화
            isNoticesLoaded = false; // 공지사항을 다시 로드하기 위한 플래그 초기화
            showNoticeTableOnPanel(panel); // 원래의 공지 목록을 다시 표시
        });

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
       voiceButton .setBackground(new Color(96, 140, 255)); // 스카이블루 배경
       voiceButton .setForeground(Color.white); // 검은 글꼴
       voiceButton .setOpaque(true);
       voiceButton .setBorderPainted(false);
       voiceButton .addActionListener(e -> {
            searchQuery = searchField.getText().trim(); // 사용자가 입력한 검색어 가져오기
            searchType = (String) searchComboBox.getSelectedItem(); // 검색 유형 가져오기
            pageNumber = 1; // 검색 결과의 첫 페이지를 보여주기 위해 pageNumber를 1로 설정
            isNoticesLoaded = false; // 공지사항을 다시 로드하기 위한 플래그 초기화
            showNoticeTableOnPanel(panel); // 검색 결과를 반영하여 테이블을 다시 표시
        });
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
                long end = System.currentTimeMillis() + 5000;  
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
                RecognizeResponse response = speechClient.recognize(config, audio);
                for (SpeechRecognitionResult result : response.getResultsList()) {
                    SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                    searchField.setText(alternative.getTranscript());
                    System.out.println("음성 입력 결과: " + alternative.getTranscript());
                    searchButton.doClick();
                }
                speechClient.shutdown(); // 클라이언트를 닫고 모든 자원을 해제합니다.

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JComboBox<String> searchComboBox = new JComboBox<>(new String[]{"제목", "작성자", "내용"});
        searchComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchType = (String) searchComboBox.getSelectedItem();
            }
        });
        searchField = new JTextField(20);
        searchButton = new JButton("검색");
        searchButton.setBackground(new Color(96, 140, 255)); // 스카이블루 배경
        searchButton.setForeground(Color.white); // 검은 글꼴
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.addActionListener(e -> {
            searchQuery = searchField.getText().trim(); // 사용자가 입력한 검색어 가져오기
            searchType = (String) searchComboBox.getSelectedItem(); // 검색 유형 가져오기
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
        navigationPanel.add(searchComboBox); // 검색 유형 선택 박스를 네비게이션 패널에 추가
        navigationPanel.add(searchField); // 검색 필드 추가
        navigationPanel.add(voiceButton); // 음성 검색 버튼 추가
        navigationPanel.add(searchButton); //

        // 패널을 초기화하고 네비게이션 패널 추가
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        
        int noticeCount = getNoticeCount();
        JLabel lblNoticeCount = new JLabel("총 " + noticeCount + " 개의 게시물이 있습니다.");
        JTable noticeTable = getNoticeTable();
        
        JButton refreshButton = new JButton("새로고침");
        refreshButton.setBackground(new Color(96, 140, 255)); // 스카이블루 배경
        refreshButton.setForeground(Color.white); // 검은 글꼴
        refreshButton.setOpaque(true);
        refreshButton.setBorderPainted(false);
        refreshButton.addActionListener(e -> {
            pageNumber = 1; // 페이지 번호를 초기화합니다.
            isNoticesLoaded = false; // 공지사항을 다시 로드하기 위한 플래그 초기화
            showNoticeTableOnPanel(panel); // 패널을 새로 고침
        });

        // 테이블에 마우스 리스너를 추가합니다.
        noticeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = noticeTable.getSelectedRow();
                String title = (String) noticeTable.getModel().getValueAt(rowIndex, 1);
                incrementViewCount(title);
                JTextArea noticeContent = NoticeFrameDb.getNoticeDetails(title);
                noticeContent.setBackground(Color.white);
                noticeContent.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
                
                JScrollPane scrollPane = new JScrollPane(noticeContent);
                scrollPane.setBackground(Color.white);

                JPanel contentPanel = new JPanel(new BorderLayout());
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang")) {
                    String sql = "SELECT * FROM notices WHERE title = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, title);
                    ResultSet rs = statement.executeQuery();
                    if (rs.next()) {
                       JPanel titleContainer = new JPanel(); 
                       titleContainer.setLayout(new BorderLayout());
                       
                        String additionalInfo = String.format("번호 : %d   제목 : %s   작성자 : %s   작성일 : %s   조회수 : %d",
                              rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("date"), rs.getInt("viewCount"));
                        JLabel additionalLabel = new JLabel(additionalInfo);
                        additionalLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
                        additionalLabel.setForeground(new Color(60,60,60));
                        additionalLabel.setBackground(new Color(96,140,255));
                        
                        JPanel addtionalLabelContainer = new JPanel(new BorderLayout());
                        addtionalLabelContainer.add(additionalLabel, BorderLayout.EAST);
                        
                        contentPanel.add(addtionalLabelContainer, BorderLayout.NORTH);
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
        topPanel.add(lblTotalNoticeCount, BorderLayout.WEST);
        topPanel.add(toMainButton, BorderLayout.CENTER);
        topPanel.add(refreshButton, BorderLayout.EAST);

        // 패널에 컴포넌트들을 추가합니다.
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(noticeTable), BorderLayout.CENTER);
        panel.add(navigationPanel, BorderLayout.SOUTH);
        panel.revalidate();
        panel.repaint();
        isNoticesLoaded = true;
        
        
    }
}