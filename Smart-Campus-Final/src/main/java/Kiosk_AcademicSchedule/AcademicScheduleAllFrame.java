package Kiosk_AcademicSchedule;

import javax.swing.*;
import services.DatabaseService;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashSet;

public class AcademicScheduleAllFrame extends JPanel {
    private final int CURRENT_YEAR = 2023;
    private JTable[] calendarTables = new JTable[12];
    private JTable[] eventTables = new JTable[12];
    private DefaultTableModel[] calendarModels = new DefaultTableModel[12];
    private DefaultTableModel[] eventModels = new DefaultTableModel[12];
    private HashSet<Integer>[] eventDaysArray = new HashSet[12];
    private static DatabaseService dbService = new DatabaseService();

    public AcademicScheduleAllFrame() {
        setLayout(new GridLayout(6, 2)); 

        for (int i = 0; i < 12; i++) {
            eventDaysArray[i] = new HashSet<>(); 
        }
        for (int i = 0; i < 12; i++) {
            add(createMonthPanel(i));
        }
    }
    private JPanel createMonthPanel(int month) {
        JPanel monthPanel = new JPanel(new BorderLayout());
        JLabel monthLabel = new JLabel(getMonthName(month) + " " + CURRENT_YEAR, SwingConstants.CENTER);

        String[] columnNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        calendarModels[month] = new DefaultTableModel(null, columnNames);
        calendarTables[month] = new JTable(calendarModels[month]);
        
        loadMonth(month);
        
        calendarTables[month].setDefaultRenderer(Object.class, new CustomTableCellRenderer(eventDaysArray[month]));
        calendarTables[month].setRowHeight(100); 

        calendarTables[month].setPreferredScrollableViewportSize(new Dimension(350, 200)); 
        eventTables[month].setPreferredScrollableViewportSize(new Dimension(350, 200));   

        JPanel tablePanel = new JPanel(new GridLayout(1, 2));
        tablePanel.add(new JScrollPane(calendarTables[month]));
        tablePanel.add(new JScrollPane(eventTables[month]));

        monthPanel.add(monthLabel, BorderLayout.NORTH);
        monthPanel.add(tablePanel, BorderLayout.CENTER);

        return monthPanel;
    }
    private void loadMonth(int month) {
        calendarModels[month].setRowCount(0);  
        if (eventModels[month] == null) {
            eventModels[month] = new DefaultTableModel();
            eventTables[month] = new JTable(eventModels[month]);
            eventModels[month].addColumn("Date");
            eventModels[month].addColumn("Event");
        } else {
            eventModels[month].setRowCount(0);
        }
        if(eventDaysArray[month] == null) {
            eventDaysArray[month] = new HashSet<>();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(CURRENT_YEAR, month, 1);
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int numberOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Object[] week = new Object[7];
        int day = 1;

        for (int i = 0; day <= numberOfDaysInMonth; i++) {
            for (int j = firstDayOfMonth; j < 7 && day <= numberOfDaysInMonth; j++) {
                week[j] = day++;
            }
            calendarModels[month].addRow(week);
            week = new Object[7]; 
            firstDayOfMonth = 0; 
        }
        try {
            dbService.connect();
            String sql = "SELECT * FROM academic_schedule WHERE MONTH(start_date) = ? AND YEAR(start_date) = ? ORDER BY start_date ASC";
            PreparedStatement preparedStatement = dbService.conn.prepareStatement(sql);
            preparedStatement.setInt(1, month + 1);  
            preparedStatement.setInt(2, CURRENT_YEAR);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Date startDate = resultSet.getDate("start_date");
                Date endDate = resultSet.getDate("end_date");  
                String event = resultSet.getString("event");

                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                int startDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                eventDaysArray[month].add(startDayOfMonth);
                System.out.println("Added Event Day: " + startDayOfMonth); 

                if (endDate != null) {
                    cal.setTime(endDate);
                    int endDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    eventModels[month].addRow(new Object[]{startDate + " ~ " + endDate, event}); 
                } else {
                    eventModels[month].addRow(new Object[]{startDate, event});
                }
            }
            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "데이터베이스 연결 또는 쿼리 중 오류 발생: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);       
    } finally {
        dbService.disconnect();
    }
}
    private String getMonthName(int month) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        
        if (month >= 0 && month < 12) {
            return months[month];
        } else {
            return null;
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Academic Schedule for " + 2023);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        JScrollPane scrollPane = new JScrollPane(new AcademicScheduleAllFrame());
        frame.add(scrollPane);
        frame.setVisible(true);
    }
}