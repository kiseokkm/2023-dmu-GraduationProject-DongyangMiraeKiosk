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
import services.DatabaseService;
import Admin_Login_Notice.NoticeFrameDb;

public class LoginNoticePanel extends JPanel {   
    private static boolean isNoticesLoaded = false; 
    private static final int PAGE_SIZE = 22;
    private static int pageNumber = 1;
    private static String searchQuery = "";
    private static String userMajor; 
    private static String searchType = "제목";
    private static JButton searchButton;
    private static JTextField searchField;
    private static JPanel  navigationPanel;
    static Color skyBlue = new Color(211, 211, 211);
    static Color creamyColor = new Color(255, 253, 208);
    static DatabaseService dbService = new DatabaseService();
    public LoginNoticePanel(String major) {
        this.userMajor = major;
        this.dbService = new DatabaseService();
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
        DatabaseService dbService = new DatabaseService();
        try {
            dbService.connect();
            String sql;
            PreparedStatement statement;
            if (searchQuery.isEmpty()) {
                sql = "SELECT * FROM notices WHERE type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                statement = dbService.conn.prepareStatement(sql);
                statement.setString(1, userMajor);
                statement.setInt(2, PAGE_SIZE);
                statement.setInt(3, offset);
            } else {
                switch (searchType) {
                    case "제목":
                        sql = "SELECT * FROM notices WHERE title LIKE ? AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                        break;
                    case "작성자":
                        sql = "SELECT * FROM notices WHERE author LIKE ? AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                        break;
                    case "내용":
                        sql = "SELECT * FROM notices WHERE content LIKE ? AND type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                        break;
                    default:
                        sql = "SELECT * FROM notices WHERE type = ? ORDER BY pinned DESC, id DESC LIMIT ? OFFSET ?";
                        break;
                }
                statement = dbService.conn.prepareStatement(sql);
                statement.setString(1, "%" + searchQuery + "%");
                statement.setString(2, userMajor);
                statement.setInt(3, PAGE_SIZE);
                statement.setInt(4, offset);
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
    public List<Object[]> getNoticesByMajor(String major) {
        List<Object[]> notices = new ArrayList<>();
        try {
            dbService.connect();
            String sql = "SELECT * FROM notices WHERE major = ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setString(1, major);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Object[] noticeData = new Object[2];
                noticeData[0] = resultSet.getString("title");
                noticeData[1] = resultSet.getString("content");
                notices.add(noticeData);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
        return notices;
    }
    public static void showNoticeTableOnPanel(JPanel panel) {    
       int noticeCount = NoticeFrameDb.getNoticeCount(userMajor);
       JLabel lblNoticeCount = new JLabel("총 " + noticeCount + " 개의 게시물이 있습니다.");
       JTable noticeTable = getNoticeTable(userMajor);
        if (isNoticesLoaded) return;
               
        int totalPages = (int) Math.ceil((double) NoticeFrameDb.getNoticeCount() / PAGE_SIZE);
        
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
        voiceButton.setBackground(new Color(135, 206, 235));
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
                long end = System.currentTimeMillis() + 3000;  
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
                speechClient.shutdown();
                SwingUtilities.invokeLater(() -> {
                    for (SpeechRecognitionResult result : response.getResultsList()) {
                        SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                        searchField.setText("중간고사"); 
                        searchButton.doClick();
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
            searchType = selectedOption;
            pageNumber = 1;
            isNoticesLoaded = false;
            showNoticeTableOnPanel(panel);           
        });
        navigationPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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
        
        JButton refreshButton = new JButton("새로고침");
        refreshButton.setBackground(new Color(135, 206, 235));
        refreshButton.addActionListener(e -> {
            isNoticesLoaded = false;  
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
            showNoticeTableOnPanel(panel); 
        });  
        JButton toMainButton = new JButton("첫 화면으로");
        toMainButton.setPreferredSize(refreshButton.getPreferredSize()); 
        toMainButton.addActionListener(e -> {
            searchQuery = ""; 
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
                noticeContent.setFont(new Font("Serif", Font.PLAIN, 25)); 
                JScrollPane scrollPane = new JScrollPane(noticeContent);
                JPanel contentPanel = new JPanel(new BorderLayout());
                try {
                    dbService.connect();
                    String sql = "SELECT * FROM notices WHERE title = ?";
                    PreparedStatement statement = dbService.conn.prepareStatement(sql);
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
                }finally {
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

   public static void resetNoticeLoadedFlag() {
        isNoticesLoaded = false;
    }  
}