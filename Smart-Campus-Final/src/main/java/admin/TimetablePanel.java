package admin;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TimetablePanel extends JPanel {
    private JPanel mainPanel, inputPanel;
    private JLabel[][] labels;
    private JComboBox<String> startTimeCombo, endTimeCombo, dayCombo;
    private JTextField subjectField, placeField;
    private JButton addButton;
    private Connection connection;
    private String studentId;

    public TimetablePanel(String studentId) {
        this.studentId = studentId;
        connectToDatabase();
        setLayout(new BorderLayout());
        mainPanel = new JPanel(new GridLayout(11, 6));
        inputPanel = new JPanel();
        labels = new JLabel[11][6];
        Font defaultFont = new Font("SansSerif", Font.PLAIN, 12);
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 6; j++) {
                labels[i][j] = new JLabel("", SwingConstants.CENTER);
                labels[i][j].setFont(defaultFont);
                labels[i][j].setOpaque(true);
                labels[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                if (i == 0) {
                    labels[i][j].setBackground(new Color(220, 220, 220));
                }
                mainPanel.add(labels[i][j]);
            }
        }
        labels[0][0].setText("시간 / 요일");
        labels[0][1].setText("월요일");
        labels[0][2].setText("화요일");
        labels[0][3].setText("수요일");
        labels[0][4].setText("목요일");
        labels[0][5].setText("금요일");
        for (int i = 1; i <= 10; i++) {
            if (i == 1) {
                labels[i][0].setText("9AM - 10AM");
            } else if (i == 2) {
                labels[i][0].setText("10AM - 11AM");
            } else if (i == 3) {
                labels[i][0].setText("11AM - 12PM");
            } else if (i == 4) {
                labels[i][0].setText("12PM - 1PM");
            } else {
                int startHour = (i + 8) % 12 == 0 ? 12 : (i + 8) % 12;
                int endHour = (i + 9) % 12 == 0 ? 12 : (i + 9) % 12;
                labels[i][0].setText(startHour + "PM - " + endHour + "PM");
            }
        }
        String[] times = {
            "9AM - 10AM", "10AM - 11AM", "11AM - 12PM", "12PM - 1PM",
            "1PM - 2PM", "2PM - 3PM", "3PM - 4PM", "4PM - 5PM", "5PM - 6PM", "6PM - 7PM"
        };
        String[] days = {"월요일", "화요일", "수요일", "목요일", "금요일"};
        startTimeCombo = new JComboBox<>(times);
        endTimeCombo = new JComboBox<>(times);
        dayCombo = new JComboBox<>(days);
        subjectField = new JTextField(10);
        placeField = new JTextField(10);
        addButton = new JButton("추가");
        addButton.setBackground(new Color(70, 130, 180));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> {
            if (checkIfTimeSlotExists()) {
                JOptionPane.showMessageDialog(null, "이미 있습니다");
            } else {
                int startIdx = startTimeCombo.getSelectedIndex() + 1;
                int endIdx = endTimeCombo.getSelectedIndex() + 1;
                int dayIdx = dayCombo.getSelectedIndex() + 1;
                String data = "<html>" + subjectField.getText() + "<br>" + placeField.getText() + "</html>";
                for (int i = startIdx; i <= endIdx; i++) {
                    labels[i][dayIdx].setText(data);
                }
                addToDatabase(dayCombo.getSelectedItem().toString(), startTimeCombo.getSelectedItem().toString(), endTimeCombo.getSelectedItem().toString(), subjectField.getText(), placeField.getText());
            }
        });
        inputPanel.add(new JLabel("시작 시간:"));
        inputPanel.add(startTimeCombo);
        inputPanel.add(new JLabel("끝 시간:"));
        inputPanel.add(endTimeCombo);
        inputPanel.add(new JLabel("요일:"));
        inputPanel.add(dayCombo);
        inputPanel.add(new JLabel("과목:"));
        inputPanel.add(subjectField);
        inputPanel.add(new JLabel("장소:"));
        inputPanel.add(placeField);
        inputPanel.add(addButton);
        add(mainPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        loadTimetableFromDatabase();
    }

    private boolean checkIfTimeSlotExists() {
        String query = "SELECT * FROM timetable WHERE studentId = ? AND day = ? AND startTime = ? AND endTime = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, dayCombo.getSelectedItem().toString());
            preparedStatement.setString(3, startTimeCombo.getSelectedItem().toString());
            preparedStatement.setString(4, endTimeCombo.getSelectedItem().toString());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8";
        String user = "root";
        String password = "dongyang";
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addToDatabase(String day, String startTime, String endTime, String subject, String place) {
        String query = "INSERT INTO timetable(day, startTime, endTime, subject, place, studentId) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, day);
            preparedStatement.setString(2, startTime);
            preparedStatement.setString(3, endTime);
            preparedStatement.setString(4, subject);
            preparedStatement.setString(5, place);
            preparedStatement.setString(6, studentId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTimetableFromDatabase() {
        String query = "SELECT day, startTime, endTime, subject, place FROM timetable WHERE studentId = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, studentId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String day = rs.getString("day");
                String startTime = rs.getString("startTime");
                String endTime = rs.getString("endTime");
                String subject = rs.getString("subject");
                String place = rs.getString("place");
                int dayIdx = getDayIndex(day);
                int startIdx = getTimeIndex(startTime);
                int endIdx = getTimeIndex(endTime);
                String data = "<html>" + subject + "<br>" + place + "</html>";
                for (int i = startIdx; i <= endIdx; i++) {
                    labels[i][dayIdx].setText(data);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getDayIndex(String day) {
        switch (day) {
            case "월요일": return 1;
            case "화요일": return 2;
            case "수요일": return 3;
            case "목요일": return 4;
            case "금요일": return 5;
            default: return -1;
        }
    }

    private int getTimeIndex(String time) {
        switch (time) {
            case "9AM - 10AM": return 1;
            case "10AM - 11AM": return 2;
            case "11AM - 12PM": return 3;
            case "12PM - 1PM": return 4;
            case "1PM - 2PM": return 5;
            case "2PM - 3PM": return 6;
            case "3PM - 4PM": return 7;
            case "4PM - 5PM": return 8;
            case "5PM - 6PM": return 9;
            case "6PM - 7PM": return 10;
            default: return -1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame();
            testFrame.add(new TimetablePanel("123456")); // 임시 학번
            testFrame.pack();
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setVisible(true);
        });
    }
}
