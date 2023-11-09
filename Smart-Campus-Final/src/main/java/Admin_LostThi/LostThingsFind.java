package Admin_LostThi;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;

import java.awt.*;
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
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class LostThingsFind extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;
    private String currentCategory = "found_items";
    private JTextField searchField;
    private JButton seButton;
    private JPanel categoryPanel;
    private JPanel searchPanel;
    
    public LostThingsFind() {
        mainPanel = this;
        setLayout(new BorderLayout());
        categoryPanel = new JPanel();
        JButton foundButton = new JButton("분실물 발견");
        JButton shButton = new JButton("분실물 찾습니다");
        foundButton.addActionListener(e -> loadDataFromDatabase1("found_items"));
        shButton.addActionListener(e -> loadDataFromDatabase1("lost_items"));
        categoryPanel.add(foundButton);
        categoryPanel.add(shButton);
        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        String[] searchOptions = {"제목", "내용", "작성자"};
        JComboBox<String> searchComboBox = new JComboBox<>(searchOptions);
        searchField = new JTextField(20);
        
        JButton voiceSearchButton = new JButton("음성 인식");
        voiceSearchButton.addActionListener(e -> startSpeechRecognition());
        searchPanel.add(voiceSearchButton, 0);
        
        seButton = new JButton("검색");
        seButton.addActionListener(e -> {
            String selectedOption = (String) searchComboBox.getSelectedItem();
            String searchText = searchField.getText();
            String searchColumn = "";
            if ("제목".equals(selectedOption)) {
                searchColumn = "title";
            } else if ("내용".equals(selectedOption)) {
                searchColumn = "content";
            } else if ("작성자".equals(selectedOption)) {
                searchColumn = "author";
            }
            loadDataFromDatabase(currentCategory, searchColumn, searchText);
        });
        JButton refreshButton = new JButton("새로고침");
        refreshButton.addActionListener(e -> loadDataFromDatabase1(currentCategory));
        
        voiceSearchButton.setBackground(new Color(180, 210, 255));
        voiceSearchButton.setOpaque(true);
        voiceSearchButton.setBorderPainted(false); 

        seButton.setBackground(new Color(180, 210, 255));
        seButton.setOpaque(true);
        seButton.setBorderPainted(false); 

        refreshButton.setBackground(new Color(180, 210, 255));
        refreshButton.setOpaque(true);
        refreshButton.setBorderPainted(false);
        
        searchPanel.add(searchComboBox);
        searchPanel.add(searchField);
        searchPanel.add(voiceSearchButton);
        searchPanel.add(seButton);
        searchPanel.add(refreshButton);

        add(categoryPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new Object[]{"번호", "제목", "내용", "작성자", "작성일", "조회수"}, 0);
        table = new JTable(tableModel);
              
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus,int row, int column)
            {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);
                label.setBackground(new Color(180, 210, 255));
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
        });
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(255, 240, 245) : Color.WHITE); 
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
                return c;
            }
        });
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) { 
                    int rowIndex = table.getSelectedRow();
                    int postId = (int) tableModel.getValueAt(rowIndex, 0);
                    incrementViewCount(postId, currentCategory); 
                    showContentInPanel(postId);
                }
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);
        loadDataFromDatabase1("found_items");
    }
    private void startSpeechRecognition() {
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
            int numBytesRead;
            byte[] data = new byte[microphone.getBufferSize() / 5];

            final JOptionPane pane = new JOptionPane("음성을 입력해주세요.", JOptionPane.INFORMATION_MESSAGE);
            final JDialog dialog = pane.createDialog(null, "알림");

            new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
              }) 
            {{
                setRepeats(false); // Timer가 한 번만 실행되도록 설정
                start(); // Timer 시작
            }};
            dialog.setVisible(true); // 알림창 표시          
            long end = System.currentTimeMillis() + 5000;  
            while (System.currentTimeMillis() < end) {
                numBytesRead = microphone.read(data, 0, data.length);
                byteArrayOutputStream.write(data, 0, numBytesRead);
            }
            byte[] audioData = byteArrayOutputStream.toByteArray();

            ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
            AudioInputStream ais = new AudioInputStream(bais, format, audioData.length / format.getFrameSize());

            File wavFile1 = new File("recoding/recording.wav");  // 저장할 파일 경로 지정
            AudioSystem.write(ais, javax.sound.sampled.AudioFileFormat.Type.WAVE, wavFile);
            microphone.close();
            SpeechClient speech = SpeechClient.create();
            byte[] audioBytes = Files.readAllBytes(Paths.get("recoding/recoding.wav"));
            RecognitionConfig config = RecognitionConfig.newBuilder()
                .setEncoding(AudioEncoding.LINEAR16)
                .setSampleRateHertz(16000)
                .setLanguageCode("ko-KR")
                .build();
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                .setContent(ByteString.copyFrom(audioBytes))
                .build();
            RecognizeResponse response = speech.recognize(config, audio);
            for (SpeechRecognitionResult result : response.getResultsList()) {
                // Just use the first alternative here.
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                searchField.setText(alternative.getTranscript());
                System.out.println("음성 입력 결과: " + alternative.getTranscript());
             seButton.doClick();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }  
    private void loadDataFromDatabase1(String tableName) {
        loadDataFromDatabase(tableName, null, null);
    }
    private void loadDataFromDatabase(String tableName, String searchColumn, String searchText) {
        String url = "jdbc:mysql://localhost:3306/self_order_kiosk?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String password = "dongyang";

        currentCategory = tableName;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            String query = "SELECT * FROM " + tableName;
            if (searchColumn != null && !searchColumn.isEmpty() && searchText != null && !searchText.isEmpty()) {
                query += " WHERE " + searchColumn + " LIKE '%" + searchText + "%'";
            }

            ResultSet rs = stmt.executeQuery(query);

            tableModel.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String author = rs.getString("author");
                boolean isAnonymous = rs.getBoolean("is_anonymous");
                
                if (isAnonymous) {
                    author = "익명";
                }
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
    private void showContentInPanel(int postId) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang")) {
                    String sql = "SELECT * FROM " + currentCategory + " WHERE id = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setInt(1, postId);
                    ResultSet rs = statement.executeQuery();

                    if (rs.next()) {
                        String content = rs.getString("content");
                        String title = rs.getString("title");
                        String author = rs.getString("author");
                        Timestamp postDate = rs.getTimestamp("post_date");
                        int views = rs.getInt("views");

                        JTextArea textArea = new JTextArea(10, 40);
                        textArea.setText(content);
                        textArea.setWrapStyleWord(true);
                        textArea.setLineWrap(true);
                        textArea.setCaretPosition(0);
                        textArea.setEditable(false);
                        textArea.setBackground(Color.WHITE);
                        textArea.setFont(new Font("Serif", Font.PLAIN, 20));

                        JScrollPane scrollPane = new JScrollPane(textArea);
                        scrollPane.getViewport().setBackground(Color.WHITE);

                        String additionalInfo = String.format("번호: %d | 제목: %s | 작성자: %s | 작성일: %s | 조회수: %d",
                                postId, title, author, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(postDate), views);
                        JLabel additionalLabel = new JLabel(additionalInfo);
                        additionalLabel.setFont(new Font("Serif", Font.BOLD, 20));
                        additionalLabel.setOpaque(true); 
                        additionalLabel.setBackground(new Color(180, 210, 255));
                        additionalLabel.setForeground(Color.black); 

                        JPanel contentPanel = new JPanel(new BorderLayout());
                        contentPanel.setBackground(Color.WHITE);
                        contentPanel.add(additionalLabel, BorderLayout.NORTH);
                        contentPanel.add(scrollPane, BorderLayout.CENTER);

                        JButton backButton = new JButton("뒤로 가기");
                        backButton.addActionListener(event -> {
                            mainPanel.removeAll();
                            mainPanel.setLayout(new BorderLayout());
                            mainPanel.add(categoryPanel, BorderLayout.NORTH);
                            mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
                            mainPanel.add(searchPanel, BorderLayout.SOUTH);
                            mainPanel.revalidate();
                            mainPanel.repaint();
                        });

                        contentPanel.add(backButton, BorderLayout.SOUTH);
                        mainPanel.removeAll();
                        mainPanel.add(contentPanel, BorderLayout.CENTER);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "상세 정보를 가져오는 중 오류가 발생했습니다.");
                }
            }
        });
    }
}