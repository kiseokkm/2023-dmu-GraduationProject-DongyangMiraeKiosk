package manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ManagerTimeTable extends JFrame {
    private JComboBox<String> studentIdCombo;
    private JPanel timeTablePanel;
    private Connection connection;

    public ManagerTimeTable() {
        setTitle("시간표 관리");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        connectToDatabase();
        
        ArrayList<String> studentIds = fetchStudentIds();
        studentIdCombo = new JComboBox<>(studentIds.toArray(new String[0]));
        studentIdCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTimeTable((String) studentIdCombo.getSelectedItem());
            }
        });

        timeTablePanel = new JPanel();

        add(studentIdCombo, BorderLayout.NORTH);
        add(timeTablePanel, BorderLayout.CENTER);
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

    private ArrayList<String> fetchStudentIds() {
        ArrayList<String> studentIds = new ArrayList<>();
        String query = "SELECT DISTINCT studentId FROM timetable";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                studentIds.add(rs.getString("studentId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentIds;
    }

    private void displayTimeTable(String studentId) {
        timeTablePanel.removeAll();
        admin.TimetablePanel timetablePanel = new admin.TimetablePanel(studentId);
        timeTablePanel.add(timetablePanel);
        timeTablePanel.revalidate();
        timeTablePanel.repaint();
    }
}
