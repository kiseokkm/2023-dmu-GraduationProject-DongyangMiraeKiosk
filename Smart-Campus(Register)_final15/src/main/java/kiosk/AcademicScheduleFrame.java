package kiosk;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.HashSet;

//학사일정


public class AcademicScheduleFrame extends JPanel {

    private int currentMonth;
    private int currentYear;
    private JLabel lblMonthYear;
    private JTable calendarTable;
    private JTable academicEventsTable; // 학사 일정을 나타낼 새로운 JTable
    private HashSet<String> eventDays = new HashSet<>();

    public AcademicScheduleFrame() {
        setLayout(new BorderLayout());
        currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        currentYear = 2023; // 설정된 연도

        // Create a JTable with date headers
        String[] columnNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames);
        calendarTable = new JTable(model);
        
        // 학사 일정을 나타낼 JTable 생성
        DefaultTableModel academicEventsModel = new DefaultTableModel(new Object[]{"Date", "Event"}, 0);
        academicEventsTable = new JTable(academicEventsModel);

        // Create a panel for the month/year and navigation buttons
        JPanel calendarPanel = new JPanel();
        JButton prevMonth = new JButton("<<");
        JButton nextMonth = new JButton(">>");
        lblMonthYear = new JLabel();

        // Add everything to the main panel
        add(calendarPanel, BorderLayout.NORTH);
        
        // 달력 테이블 크기 조절
        calendarTable.setPreferredScrollableViewportSize(new Dimension(500, 200)); 
        add(new JScrollPane(calendarTable), BorderLayout.CENTER);

        // 학사 일정 테이블 크기 및 열 너비 조절
        academicEventsTable.setPreferredScrollableViewportSize(new Dimension(500, 100));
        academicEventsTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Date 열 너비 조정
        academicEventsTable.getColumnModel().getColumn(1).setPreferredWidth(400); // Event 열 너비 조정
        add(new JScrollPane(academicEventsTable), BorderLayout.SOUTH);

        calendarPanel.add(prevMonth);
        calendarPanel.add(lblMonthYear);
        calendarPanel.add(nextMonth);

        prevMonth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentMonth--;
                if (currentMonth < 0) {
                    currentMonth = 11;
                    currentYear--;
                }
                updateCalendar();
            }
        });

        nextMonth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                currentMonth++;
                if (currentMonth > 11) {
                    currentMonth = 0;
                    currentYear++;
                }
                updateCalendar();
            }
        });

        updateCalendar();
    }


    private void updateCalendar() {
    	eventDays.clear();
        // Update the month label and clear the existing table
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        lblMonthYear.setText(currentYear + "." + (currentMonth + 1));
        
        // Clear existing table
        DefaultTableModel model = (DefaultTableModel) calendarTable.getModel();
        model.setRowCount(0);

        // Create a calendar object to find the first day of the month and the total number of days in the month
        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth, 1);
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 0 (Sunday) to 6 (Saturday)
        int numberOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Object[] week = new Object[7];
        int day = 1;

        // Fill the days into the JTable
        for (int i = 0; day <= numberOfDaysInMonth; i++) {
            for (int j = firstDayOfMonth; j < 7 && day <= numberOfDaysInMonth; j++) {
                week[j] = day++;
            }
            model.addRow(week);
            week = new Object[7]; // Reset the week array
            firstDayOfMonth = 0; // Reset the first day of the week
        }
        CustomTableCellRenderer renderer = new CustomTableCellRenderer(eventDays);
        for (int i = 0; i < calendarTable.getColumnCount(); i++) {
            calendarTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        
        // 학사 일정 테이블 초기화
        DefaultTableModel academicModel = (DefaultTableModel) academicEventsTable.getModel();
        academicModel.setRowCount(0);
        
        try {
            // 데이터베이스 연결
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");

            // 해당 월의 학사 일정 정보만 가져옵니다.
            String sql = "SELECT * FROM academic_schedule WHERE MONTH(date) = ? AND YEAR(date) = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, currentMonth + 1);  // DB는 1부터 시작
            preparedStatement.setInt(2, currentYear);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Date eventDate = resultSet.getDate("date");
                String event = resultSet.getString("event");

                eventDays.add(String.valueOf(eventDate.getDate()));  // 이벤트가 있는 날짜를 HashSet에 추가
                academicModel.addRow(new Object[]{eventDate, event}); // 학사 일정 테이블에 일정 추가
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "데이터베이스 연결 또는 쿼리 중 오류 발생: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
        
        
        
    }
    
}
