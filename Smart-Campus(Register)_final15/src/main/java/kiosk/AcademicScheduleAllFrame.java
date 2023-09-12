package kiosk;

import javax.swing.*;
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
    private HashSet<String>[] eventDaysArray = new HashSet[12];

    public AcademicScheduleAllFrame() {
        setLayout(new GridLayout(6, 1));
        
        for (int i = 0; i < 12; i++) {
            eventDaysArray[i] = new HashSet<>();  // 여기서 각 원소를 초기화합니다.
        }

        for (int i = 0; i < 12; i += 2) {
            JPanel monthPairPanel = new JPanel(new GridLayout(1, 2));
            monthPairPanel.add(createMonthPanel(i));

            if (i + 1 < 12) { // 12월 이후에는 추가 달력이 없으므로 체크
                monthPairPanel.add(createMonthPanel(i + 1));
            } else {
                monthPairPanel.add(new JPanel());  // 12월만 있을 때 빈 패널 추가
            }

            add(monthPairPanel);
        }
    }

    private JPanel createMonthPanel(int month) {
        JPanel monthPanel = new JPanel(new BorderLayout());
        JLabel monthLabel = new JLabel(getMonthName(month) + " " + CURRENT_YEAR, SwingConstants.CENTER);

        String[] columnNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        calendarModels[month] = new DefaultTableModel(null, columnNames);
        calendarTables[month] = new JTable(calendarModels[month]);
        
        // 먼저 월의 데이터를 로드합니다.
        loadMonth(month);
        
        calendarTables[month].setDefaultRenderer(Object.class, new CustomTableCellRenderer(eventDaysArray[month]));
        calendarTables[month].setRowHeight(100);  // 행 높이 조정

        JPanel tablePanel = new JPanel(new GridLayout(1, 2));
        tablePanel.add(new JScrollPane(calendarTables[month]));
        tablePanel.add(new JScrollPane(eventTables[month]));

        monthPanel.add(monthLabel, BorderLayout.NORTH);
        monthPanel.add(tablePanel, BorderLayout.CENTER);

        return monthPanel;
    }



    private void loadMonth(int month) {
        // Clear existing table
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

        // Create a calendar object to find the first day of the month and the total number of days in the month
        Calendar calendar = Calendar.getInstance();
        calendar.set(CURRENT_YEAR, month, 1);
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 0 (Sunday) to 6 (Saturday)
        int numberOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Object[] week = new Object[7];
        int day = 1;

        // Fill the days into the JTable
        for (int i = 0; day <= numberOfDaysInMonth; i++) {
            for (int j = firstDayOfMonth; j < 7 && day <= numberOfDaysInMonth; j++) {
                week[j] = day++;
            }
            calendarModels[month].addRow(week);
            week = new Object[7]; // Reset the week array
            firstDayOfMonth = 0; // Reset the first day of the week
        }

        try {
            // 데이터베이스 연결
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");

            // 해당 월의 학사 일정 정보만 가져옵니다.
            String sql = "SELECT * FROM academic_schedule WHERE MONTH(date) = ? AND YEAR(date) = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, month + 1);  // DB는 1부터 시작
            preparedStatement.setInt(2, CURRENT_YEAR);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Date eventDate = resultSet.getDate("date");

                Calendar cal = Calendar.getInstance();
                cal.setTime(eventDate);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                eventDaysArray[month].add(String.valueOf(dayOfMonth));
                System.out.println("Added Event Day: " + dayOfMonth); // 이벤트가 있는 날짜를 출력

                String event = resultSet.getString("event");
                eventModels[month].addRow(new Object[]{eventDate, event});
            }




            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "데이터베이스 연결 또는 쿼리 중 오류 발생: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getMonthName(int month) {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        
        if (month >= 0 && month < 12) {
            return months[month];
        } else {
            return null; // 혹은 다른 에러 메시지를 반환할 수도 있습니다.
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
