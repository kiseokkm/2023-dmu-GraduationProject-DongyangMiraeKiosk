package Admin;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.protobuf.ByteString;

public class UnivHope extends JPanel {

    private JTable table;
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JPanel mainPanel;
    private JPanel buttonPanel;
    private JButton postButton;
    private JButton voiceButton;
    private JButton searchButton;
    private JComboBox<String> searchComboBox;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "dongyang";

    public UnivHope() {
        mainPanel = this;
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new Object[]{"번호", "제목", "답변", "작성자", "작성일", "조회수"}, 0);
        table = new JTable(tableModel);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowIndex = table.getSelectedRow();
                String title = (String) tableModel.getValueAt(rowIndex, 1);
                showPostDetails(title);
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);
        postButton = new JButton("새글");
        postButton.addActionListener(e -> openPostDialog());
        JButton refreshButton = new JButton("새로고침");
        refreshButton.addActionListener(e -> loadDataFromDatabase());
        voiceButton = new JButton("음성");
        voiceButton.addActionListener(e -> startSpeechRecognition());
        searchField = new JTextField(25);
        searchComboBox = new JComboBox<>(new String[]{"제목", "답변", "작성자"});   
        searchButton = new JButton("검색");
        searchButton.addActionListener(e -> {
            String searchQuery = searchField.getText().trim();
            String selectedField = (String) searchComboBox.getSelectedItem();
            if (!searchQuery.isEmpty() && selectedField != null) {
                String fieldName = "";
                switch (selectedField) {
                    case "제목":
                        fieldName = "title";
                        break;
                    case "답변":
                        fieldName = "status";
                        break;
                    case "작성자":
                        fieldName = "author";
                        break;
                }
                if (!fieldName.isEmpty()) {
                    searchInDatabase(fieldName, searchQuery);
                }
            } else {
                loadDataFromDatabase();
            }
        });       
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(searchComboBox);
        buttonPanel.add(searchField);
        buttonPanel.add(voiceButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(refreshButton); 
        buttonPanel.add(postButton);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        loadDataFromDatabase();
    }
    private void searchInDatabase(String fieldName, String searchQuery) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM communication_board WHERE " + fieldName + " LIKE ?")) {
            stmt.setString(1, "%" + searchQuery + "%");
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("status").isEmpty() ? "답변 대기" : "답변 완료",
                    rs.getString("author"),
                    rs.getTimestamp("post_date"),
                    rs.getInt("views")
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "검색 중 오류가 발생했습니다.");
        }
    }
    private void openPostDialog() {
        PostDialog postDialog = new PostDialog();
        postDialog.setVisible(true);
    }   
    private void setupSearchButton() {
        searchComboBox.addActionListener(e -> {
            String searchQuery = searchField.getText().trim();
            String selectedField = (String) searchComboBox.getSelectedItem();
            if (!searchQuery.isEmpty() && selectedField != null) {
                String fieldName = "";
                switch (selectedField) {
                    case "제목":
                        fieldName = "title";
                        break;
                    case "답변":
                        fieldName = "status";
                        break;
                    case "작성자":
                        fieldName = "author";
                        break;
                }
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM communication_board WHERE " + fieldName + " LIKE ?")) {
                    stmt.setString(1, "%" + searchQuery + "%");
                    ResultSet rs = stmt.executeQuery();
                    tableModel.setRowCount(0);
                    while (rs.next()) {
                        // ... 결과 처리 코드 ...
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "검색 중 오류가 발생했습니다.");
                }
            } else {
                loadDataFromDatabase();
            }
        });
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
                searchButton.doClick();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void loadDataFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM communication_board")) {
            ResultSet rs = stmt.executeQuery();
            tableModel.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("admin_reply").isEmpty() ? "답변 대기" : "답변 완료",
                    rs.getString("author"),
                    rs.getTimestamp("post_date"),
                    rs.getInt("views")
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "데이터 로딩 중 오류가 발생했습니다.");
        }
    }
    private void showPostDetails(String title) {
        increasePostViews(title);
        JTextArea postContent = getPostDetails(title);
        JTextArea adminReply = new JTextArea(getAdminReply(title));
        String replyText = getAdminReply(title);
        String replyDate = getAdminReplyDate(title);
        if (replyDate != null && !replyDate.isEmpty()) {
            replyText += "\n\n답변 작성일: " + replyDate;
        }
        adminReply.setText(replyText);
        adminReply.setEditable(false);
        adminReply.setBorder(BorderFactory.createTitledBorder("관리자 답변"));
        postContent.setBorder(BorderFactory.createTitledBorder("게시물 내용"));
        JButton backButton = new JButton("목록");
        JLabel titleLabel = new JLabel("제목: " + title);
        JLabel dateLabel = new JLabel("날짜: " + getPostDate(title));
        JLabel authorLabel = new JLabel("작성자: " + getPostAuthor(title));
        JLabel viewsLabel = new JLabel("조회수: " + getPostViews(title));
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(titleLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(authorLabel);
        infoPanel.add(viewsLabel);
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        contentPanel.add(new JScrollPane(postContent));
        contentPanel.add(new JScrollPane(adminReply));
        backButton.addActionListener(e -> {
            mainPanel.removeAll();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            JPanel buttonPanelBack = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton refreshButtonBack = new JButton("새로고침");
            refreshButtonBack.addActionListener(evt -> loadDataFromDatabase());
            buttonPanelBack.add(refreshButtonBack);  // 새로고침 버튼 다시 추가
            buttonPanelBack.add(postButton);
            mainPanel.add(buttonPanelBack, BorderLayout.SOUTH);
            mainPanel.revalidate();
            mainPanel.repaint();
        });
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(infoPanel, BorderLayout.NORTH);
        topPanel.add(contentPanel, BorderLayout.CENTER);
        topPanel.add(backButton, BorderLayout.SOUTH);
        mainPanel.removeAll();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }
    private String getAdminReplyDate(String title) {
        String replyDate = "";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT admin_reply_date FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                replyDate = rs.getString("admin_reply_date");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return replyDate;
    }  
    private String getPostDate(String title) {
        String date = "";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT post_date FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                date = rs.getString("post_date");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    private String getPostAuthor(String title) {
        String author = "";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT author FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                author = rs.getString("author");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return author;
    }
    private int getPostViews(String title) {
        int views = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT views FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                views = rs.getInt("views");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return views;
    }
    private void increasePostViews(String title) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE communication_board SET views = views + 1 WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void saveAdminReply(String title, String reply) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE communication_board SET admin_reply = ?, admin_reply_date = NOW() WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, reply);
            statement.setString(2, title);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String getAdminReply(String title) {
        String reply = "";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT admin_reply FROM communication_board WHERE title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                reply = rs.getString("admin_reply");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reply;
    }
    private JTextArea getPostDetails(String title) {
        JTextArea textArea = new JTextArea(10, 40);
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT content FROM communication_board WHERE title = ?";
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
        } catch (Exception e) {
            e.printStackTrace();
        }	
        return textArea;
    }
    class PostDialog extends JDialog {
        private JTextField titleField;
        private JTextField authorField;
        private JCheckBox anonymousCheckbox;
        private JTextArea contentArea;
        private JButton postButton;
        public PostDialog() {
            setTitle("게시물 작성");
            setSize(400, 400);
            setModal(true);
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            JPanel topPanel = new JPanel(new GridLayout(3, 2));
            titleField = new JTextField();
            authorField = new JTextField();
            anonymousCheckbox = new JCheckBox("익명으로 게시");
            topPanel.add(new JLabel("제목:"));
            topPanel.add(titleField);
            topPanel.add(new JLabel("작성자:"));
            topPanel.add(authorField);
            topPanel.add(anonymousCheckbox);
            contentArea = new JTextArea(10, 30);
            postButton = new JButton("게시");
            postButton.addActionListener(e -> {
                String author = anonymousCheckbox.isSelected() ? "익명" : authorField.getText();
                savePostToDatabase(titleField.getText(), author, contentArea.getText());
                loadDataFromDatabase();
                dispose();
            });
            panel.add(topPanel, BorderLayout.NORTH);
            panel.add(new JScrollPane(contentArea), BorderLayout.CENTER);
            panel.add(postButton, BorderLayout.SOUTH);
            add(panel);
        }
        private void savePostToDatabase(String title, String author, String content) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO communication_board (title, author, content, anonymous, admin_reply) VALUES (?, ?, ?, ?, ?)")) {
                stmt.setString(1, title);
                stmt.setString(2, author);
                stmt.setString(3, content);
                stmt.setBoolean(4, "익명".equals(author));
                stmt.setString(5, "");
                stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "데이터 저장 중 오류가 발생했습니다.");
            }
        }
    }
}