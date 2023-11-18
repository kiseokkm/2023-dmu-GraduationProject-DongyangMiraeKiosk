package Admin;

import javax.swing.*;

import services.DatabaseService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TimetablePanel extends JPanel {
    private JPanel mainPanel, inputPanel;
    private JLabel[][] labels;
    private JComboBox<String> startTimeCombo, endTimeCombo, dayCombo;
    private JTextField subjectField, placeField;
    private JButton addButton, editButton, deleteButton;
    private Connection connection;
    private String studentId;
    private DatabaseService dbService;
    private String[] times = {
        "9AM - 10AM", "10AM - 11AM", "11AM - 12PM", "12PM - 1PM",
        "1PM - 2PM", "2PM - 3PM", "3PM - 4PM", "4PM - 5PM", "5PM - 6PM", "6PM - 7PM"
    };
    public TimetablePanel(String studentId) {
        this.studentId = studentId;
        dbService = new DatabaseService();
        dbService.connect();
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
            labels[i][0].setText(times[i - 1]);
        }
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

        editButton = new JButton("수정");
        editButton.setBackground(new Color(46, 139, 87));
        editButton.setForeground(Color.WHITE);
        editButton.addActionListener(e -> editTimetableEntry());

        deleteButton = new JButton("삭제");
        deleteButton.setBackground(new Color(220, 20, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteTimetableEntry());

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
        inputPanel.add(editButton);
        inputPanel.add(deleteButton);

        add(mainPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 6; j++) {
                final int row = i;
                final int col = j;
                labels[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        fillInputFieldsFromLabel(row, col);
                    }
                });
            }
        }
        loadTimetableFromDatabase();
    }
    private void editTimetableEntry() {
        String oldDay = dayCombo.getSelectedItem().toString();
        String oldStartTime = startTimeCombo.getSelectedItem().toString();
        String oldEndTime = endTimeCombo.getSelectedItem().toString();

        String newSubject = subjectField.getText();
        String newPlace = placeField.getText();

        updateDatabase(oldDay, oldStartTime, oldEndTime, oldDay, oldStartTime, oldEndTime, newSubject, newPlace);
        
        int startIdx = startTimeCombo.getSelectedIndex() + 1;
        int endIdx = endTimeCombo.getSelectedIndex() + 1;
        int dayIdx = dayCombo.getSelectedIndex() + 1;
        String data = "<html>" + newSubject + "<br>" + newPlace + "</html>";
        for (int i = startIdx; i <= endIdx; i++) {
            labels[i][dayIdx].setText(data);
        }
        
        JOptionPane.showMessageDialog(null, "수정되었습니다.");
    }
    private void deleteTimetableEntry() {
        int startIdx = startTimeCombo.getSelectedIndex() + 1;
        int endIdx = endTimeCombo.getSelectedIndex() + 1;
        int dayIdx = dayCombo.getSelectedIndex() + 1;

        String selectedDay = dayCombo.getSelectedItem().toString();
        String selectedStartTime = startTimeCombo.getSelectedItem().toString();
        String selectedEndTime = endTimeCombo.getSelectedItem().toString();

        deleteFromDatabase(selectedDay, selectedStartTime, selectedEndTime);

        for (int i = startIdx; i <= endIdx; i++) {
            labels[i][dayIdx].setText("");
        }
    }
    private void updateDatabase(String oldDay, String oldStartTime, String oldEndTime, String newDay, String newStartTime, String newEndTime, String newSubject, String newPlace) {
        String query = "UPDATE timetable SET day = ?, startTime = ?, endTime = ?, subject = ?, place = ? WHERE studentId = ? AND day = ? AND startTime = ? AND endTime = ?";
        try {
            dbService.connect();
            PreparedStatement preparedStatement = dbService.conn.prepareStatement(query);
            preparedStatement.setString(1, newDay);
            preparedStatement.setString(2, newStartTime);
            preparedStatement.setString(3, newEndTime);
            preparedStatement.setString(4, newSubject);
            preparedStatement.setString(5, newPlace);
            preparedStatement.setString(6, studentId);
            preparedStatement.setString(7, oldDay);
            preparedStatement.setString(8, oldStartTime);
            preparedStatement.setString(9, oldEndTime);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                JOptionPane.showMessageDialog(null, "해당 항목을 찾을 수 없습니다.");
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "수정 중 오류가 발생했습니다: " + e.getMessage());
        } finally {
            dbService.disconnect();
        }
    }
    private void deleteFromDatabase(String day, String startTime, String endTime) {
        String query = "DELETE FROM timetable WHERE studentId = ? AND day = ? AND startTime = ? AND endTime = ?";
        try {
            dbService.connect();
            PreparedStatement preparedStatement = dbService.conn.prepareStatement(query);
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, day);
            preparedStatement.setString(3, startTime);
            preparedStatement.setString(4, endTime);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                JOptionPane.showMessageDialog(null, "해당 항목을 찾을 수 없습니다.");
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "삭제 중 오류가 발생했습니다: " + e.getMessage());
        } finally {
            dbService.disconnect();
        }
    }
    private boolean checkIfTimeSlotExists() {
        String query = "SELECT * FROM timetable WHERE studentId = ? AND day = ? AND startTime = ? AND endTime = ?";
        try {
            dbService.connect();
            PreparedStatement preparedStatement = dbService.conn.prepareStatement(query);
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, dayCombo.getSelectedItem().toString());
            preparedStatement.setString(3, startTimeCombo.getSelectedItem().toString());
            preparedStatement.setString(4, endTimeCombo.getSelectedItem().toString());
            ResultSet rs = preparedStatement.executeQuery();
            boolean exists = rs.next();
            rs.close();
            preparedStatement.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            dbService.disconnect();
        }
    }
    private void addToDatabase(String day, String startTime, String endTime, String subject, String place) {
        String query = "INSERT INTO timetable(day, startTime, endTime, subject, place, studentId) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            dbService.connect();
            PreparedStatement preparedStatement = dbService.conn.prepareStatement(query);
            preparedStatement.setString(1, day);
            preparedStatement.setString(2, startTime);
            preparedStatement.setString(3, endTime);
            preparedStatement.setString(4, subject);
            preparedStatement.setString(5, place);
            preparedStatement.setString(6, studentId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            JOptionPane.showMessageDialog(null, "추가되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
    private void loadTimetableFromDatabase() {
        String query = "SELECT day, startTime, endTime, subject, place FROM timetable WHERE studentId = ?";
        try {
            dbService.connect();
            PreparedStatement preparedStatement = dbService.conn.prepareStatement(query);
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
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
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
    private void fillInputFieldsFromLabel(int row, int col) {
        String text = labels[row][col].getText();
        if (text != null && !text.isEmpty()) {
            String[] split = text.split("<br>");
            if (split.length == 2) {
                String subject = split[0].replace("<html>", "").trim();
                String place = split[1].replace("</html>", "").trim();
                subjectField.setText(subject);
                placeField.setText(place);
                dayCombo.setSelectedIndex(col - 1);
                
                int endRow = row;
                while (endRow < 10 && labels[endRow + 1][col].getText().equals(text)) {
                    endRow++;
                }
                
                startTimeCombo.setSelectedIndex(row - 1);
                endTimeCombo.setSelectedIndex(endRow - 1);
            }
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame();
            testFrame.add(new TimetablePanel("123456"));
            testFrame.pack();
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setVisible(true);
        });
    }
}