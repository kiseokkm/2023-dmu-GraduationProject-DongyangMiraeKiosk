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

import services.DatabaseService;
import Admin_Login_Notice.NoticeFrameDb;

public class NoticeFrame {
    private static boolean isNoticesLoaded = false;
    private static final int PAGE_SIZE = 22;
    private static int pageNumber = 1;
    private static String searchQuery = "";
    private static String noticeType = "전체";
    private static JButton searchButton;
    private static JTextField searchField;
    private static String searchType = "제목";
    private static JComboBox<String> searchComboBox;
    static DatabaseService dbService = new DatabaseService();
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
        headerSize.height = 30; 
        
        header.setPreferredSize(headerSize);
        header.setBackground(new Color(96, 140, 255));
        header.setForeground(Color.white);
        header.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        
        noticeTable.setBackground(Color.white);
        noticeTable.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        int rowHeight = 30; 
        noticeTable.setRowHeight(rowHeight);
        
        int offset = (pageNumber - 1) * PAGE_SIZE;
        dbService.connect();
        try {
            String sql;
            PreparedStatement statement;

            if (searchQuery.isEmpty()) {
                sql = "SELECT * FROM notices WHERE type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                statement = dbService.conn.prepareStatement(sql);
                statement.setString(1, noticeType);
                statement.setInt(2, PAGE_SIZE);
                statement.setInt(3, offset);
            } else {
                if (searchType.equals("제목")) {
                    sql = "SELECT * FROM notices WHERE title LIKE ? AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                } else if (searchType.equals("내용")) {
                    sql = "SELECT * FROM notices WHERE content LIKE ? AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                } else if (searchType.equals("작성자")) {
                    sql = "SELECT * FROM notices WHERE author LIKE ? AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                } else {
                    sql = "SELECT * FROM notices WHERE (title LIKE ? OR content LIKE ? OR author LIKE ?) AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                }
                statement = dbService.conn.prepareStatement(sql);
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
                identifier = isPinned ? "★" : rs.getInt("id");
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return noticeTable;
    }
    private static void setColumnWidth(JTable table, int columnIndex, int width) {
       int tableWidth = table.getWidth();
       int columnWidth = (tableWidth * width) / 100;
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(width);
        column.setMinWidth(width);
        column.setMaxWidth(width);
    }
    public static int getTotalNoticeCount() {
        return NoticeFrameDb.getNoticeCount();
    }
    public static void showNoticeTableOnPanel(JPanel panel) {
        if (isNoticesLoaded) return;
        
        int totalNoticeCount = getTotalNoticeCount();
        JLabel lblTotalNoticeCount = new JLabel("총 " + totalNoticeCount + " 개의 게시물이 있습니다.");
        
        int totalPages = (int) Math.ceil((double) NoticeFrameDb.getNoticeCount() / PAGE_SIZE);

        JLabel pageInfo = new JLabel(pageNumber + " / " + totalPages);
        
        JButton toMainButton = new JButton("첫 화면으로");
        toMainButton.addActionListener(e -> {
            searchQuery = ""; 
            pageNumber = 1;   
            isNoticesLoaded = false;
            showNoticeTableOnPanel(panel);
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
        JButton firstPageButton = new JButton("<<");
        firstPageButton.addActionListener(e -> {
            if (pageNumber != 1) {
                pageNumber = 1;
                isNoticesLoaded = false;
                showNoticeTableOnPanel(panel);
                pageInfo.setText(pageNumber + " / " + totalPages);
            }
        });
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
       voiceButton .setBackground(new Color(96, 140, 255));
       voiceButton .setForeground(Color.white);
       voiceButton .setOpaque(true);
       voiceButton .setBorderPainted(false);
       voiceButton .addActionListener(e -> {
            searchQuery = searchField.getText().trim(); 
            searchType = (String) searchComboBox.getSelectedItem(); 
            pageNumber = 1; 
            isNoticesLoaded = false; 
            showNoticeTableOnPanel(panel); 
        });
        voiceButton.addActionListener(e -> {
            try {
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
                new Timer(1000, (actionEvent) -> dialog.dispose()).start();
                dialog.setVisible(true); 

                long end = System.currentTimeMillis() + 5000;  
                while (System.currentTimeMillis() < end) {
                    int numBytesRead = microphone.read(data, 0, data.length);
                    byteArrayOutputStream.write(data, 0, numBytesRead);
                }
                byte[] audioData = byteArrayOutputStream.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
                AudioInputStream ais = new AudioInputStream(bais, format, audioData.length / format.getFrameSize());

                File wavFile1 = new File("recoding/recording.wav");  
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);

                microphone.close();
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
                speechClient.shutdown(); 

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
        searchButton.setBackground(new Color(96, 140, 255));
        searchButton.setForeground(Color.white); 
        searchButton.setOpaque(true);
        searchButton.setBorderPainted(false);
        searchButton.addActionListener(e -> {
            searchQuery = searchField.getText().trim(); 
            searchType = (String) searchComboBox.getSelectedItem(); 
            pageNumber = 1; 
            isNoticesLoaded = false; 
            showNoticeTableOnPanel(panel); 
        });

        JPanel navigationPanel = new JPanel(new FlowLayout());
        navigationPanel.add(firstPageButton);  
        navigationPanel.add(prevButton);
        navigationPanel.add(pageInfo); 
        navigationPanel.add(nextButton);
        navigationPanel.add(lastPageButton);  
        navigationPanel.add(searchComboBox); 
        navigationPanel.add(searchField); 
        navigationPanel.add(voiceButton); 
        navigationPanel.add(searchButton);

        panel.removeAll();
        panel.setLayout(new BorderLayout());
        
        int noticeCount = NoticeFrameDb.getNoticeCount();
        JLabel lblNoticeCount = new JLabel("총 " + noticeCount + " 개의 게시물이 있습니다.");
        JTable noticeTable = getNoticeTable();
        
        JButton refreshButton = new JButton("새로고침");
        refreshButton.setBackground(new Color(96, 140, 255));
        refreshButton.setForeground(Color.white);
        refreshButton.setOpaque(true);
        refreshButton.setBorderPainted(false);
        refreshButton.addActionListener(e -> {
            pageNumber = 1; 
            isNoticesLoaded = false; 
            showNoticeTableOnPanel(panel);
        });
        noticeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = noticeTable.getSelectedRow();
                String title = (String) noticeTable.getModel().getValueAt(rowIndex, 1);
                NoticeFrameDb.incrementViewCount(title);
                JTextArea noticeContent = NoticeFrameDb.getNoticeDetails(title);
                noticeContent.setBackground(Color.white);
                noticeContent.setFont(new Font("맑은 고딕", Font.PLAIN, 16));

                JScrollPane scrollPane = new JScrollPane(noticeContent);
                scrollPane.setBackground(Color.white);

                JPanel contentPanel = new JPanel(new BorderLayout());
                DatabaseService dbService = new DatabaseService();

                try {
                    dbService.connect();
                    String sql = "SELECT * FROM notices WHERE title = ?";
                    PreparedStatement statement = dbService.conn.prepareStatement(sql);
                    statement.setString(1, title);
                    ResultSet rs = statement.executeQuery();
                    if (rs.next()) {
                        JPanel titleContainer = new JPanel(); 
                        titleContainer.setLayout(new BorderLayout());

                        String additionalInfo = String.format("번호 : %d   제목 : %s   작성자 : %s   작성일 : %s   조회수 : %d",
                            rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getString("date"), rs.getInt("viewCount"));
                        JLabel additionalLabel = new JLabel(additionalInfo);
                        additionalLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
                        additionalLabel.setForeground(new Color(60, 60, 60));
                        additionalLabel.setBackground(new Color(96, 140, 255));

                        JPanel additionalLabelContainer = new JPanel(new BorderLayout());
                        additionalLabelContainer.add(additionalLabel, BorderLayout.EAST);

                        contentPanel.add(additionalLabelContainer, BorderLayout.NORTH);
                        contentPanel.add(scrollPane, BorderLayout.CENTER);
                    }
                    rs.close();
                    statement.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    dbService.disconnect();
                }
                JButton backButton = new JButton("뒤로 가기");
                backButton.addActionListener(event -> {
                    panel.removeAll();
                    panel.setLayout(new BorderLayout());

                    JPanel topPanelBack = new JPanel(new BorderLayout());
                    topPanelBack.add(lblNoticeCount, BorderLayout.WEST);
                    topPanelBack.add(toMainButton, BorderLayout.CENTER);
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