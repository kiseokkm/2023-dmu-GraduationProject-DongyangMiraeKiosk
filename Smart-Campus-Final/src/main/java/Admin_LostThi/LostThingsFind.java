package Admin_LostThi;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.*;
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

public class LostThingsFind extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;
    private String currentCategory = "found_items"; 
    private JTextField searchField;
    private JButton seButton;
    public LostThingsFind() {
        
    // 하단 중앙 패널 생성
    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    // 드롭박스 생성 및 추가
    String[] categories = {"제목", "내용", "작성자"};
    JComboBox<String> categoryComboBox = new JComboBox<>(categories);
    bottomPanel.add(categoryComboBox);

    // 검색필드 생성 및 추가
    searchField = new JTextField(20);  // 인스턴스 변수로 변경
    bottomPanel.add(searchField);

    // 음성인식 버튼 생성 및 추가 (아이콘이나 텍스트로 설정 가능)
    JButton voiceButton = new JButton("음성인식");
    bottomPanel.add(voiceButton);

    // 검색 버튼 생성 및 추가
    seButton = new JButton("검색");
    bottomPanel.add(seButton);
    // 새로고침 버튼 생성 및 추가
    JButton refreshButton = new JButton("새로고침");
    bottomPanel.add(refreshButton);

    // 하단 중앙 패널을 메인 패널에 추가
    add(bottomPanel, BorderLayout.SOUTH);

        mainPanel = this;
        setLayout(new BorderLayout());

        JPanel categoryPanel = new JPanel();
        JButton foundButton = new JButton("분실물 발견");
        JButton shButton = new JButton("분실물 찾습니다");

        foundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataFromDatabase1("found_items");
            }
        });
        shButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataFromDatabase1("lost_items");
            }
        });
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        String[] searchOptions = {"제목", "내용", "작성자"};
        JComboBox<String> searchComboBox = new JComboBox<>(searchOptions);

        JTextField searchTextField = new JTextField(20);
        
        seButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) searchComboBox.getSelectedItem();
                String searchText = searchField.getText();  // searchTextField -> searchField
                
                String searchColumn = "";
                if ("제목".equals(selectedOption)) {
                    searchColumn = "title";
                } else if ("내용".equals(selectedOption)) {
                    searchColumn = "content";
                } else if ("작성자".equals(selectedOption)) {
                    searchColumn = "author";
                }

                loadDataFromDatabase(currentCategory, searchColumn, searchText);
            }
        });
        JButton voiceSearchButton = new JButton("음성");
        voiceSearchButton.addActionListener(e -> startSpeechRecognition());
        
        JButton rehButton = new JButton("새로고침");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadDataFromDatabase1(currentCategory);
            }
        });
        searchPanel.add(searchComboBox);
        searchPanel.add(searchField);
        searchPanel.add(voiceSearchButton);
        searchPanel.add(seButton);
        searchPanel.add(refreshButton);

        // 메인 패널에 검색 패널 추가
        add(searchPanel, BorderLayout.SOUTH);

        categoryPanel.add(foundButton);
        categoryPanel.add(shButton);
        add(categoryPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"번호", "제목", "내용", "작성자", "작성일", "조회수"}, 0);
        table = new JTable(tableModel);

        // 컬럼 너비 설정
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(2).setPreferredWidth(400);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(217); // 작성일 컬럼 너비 설정
        table.getColumnModel().getColumn(5).setPreferredWidth(50);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                int postId = (int) tableModel.getValueAt(rowIndex, 0);
                String category = currentCategory;
                incrementViewCount(postId, category);
                loadDataFromDatabase1(currentCategory);

                String title = (String) tableModel.getValueAt(rowIndex, 1);
                String content = (String) tableModel.getValueAt(rowIndex, 2);
                mainPanel.removeAll();
                showContentInPanel(content);
            }
        });
       add(new JScrollPane(table), BorderLayout.CENTER);
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

            // Timer를 사용하여 1~2초 후에 JOptionPane을 자동으로 닫습니다.
            new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            }) {{
                setRepeats(false); // Timer가 한 번만 실행되도록 설정
                start(); // Timer 시작
            }};

            dialog.setVisible(true); // 알림창 표시
            
            // TODO: 녹음 시간 설정 (여기서는 5초로 설정)
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
            // 검색 조건이 있는 경우 SQL 쿼리 수정
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
        String password = "1234";

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

            foundButton.addActionListener(e -> loadDataFromDatabase1("found_items"));
            searchButton.addActionListener(e -> loadDataFromDatabase1("lost_items"));

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
