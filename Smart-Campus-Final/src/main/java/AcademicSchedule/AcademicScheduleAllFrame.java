package AcademicSchedule;

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
    private HashSet<Integer>[] eventDaysArray = new HashSet[12];


    public AcademicScheduleAllFrame() {
        setLayout(new GridLayout(6, 2)); // 6행 2열의 그리드 레이아웃

        for (int i = 0; i < 12; i++) {
            eventDaysArray[i] = new HashSet<>();  // 각 원소 초기화
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
        
        // 먼저 월의 데이터를 로드합니다.
        loadMonth(month);
        
        calendarTables[month].setDefaultRenderer(Object.class, new CustomTableCellRenderer(eventDaysArray[month]));
        calendarTables[month].setRowHeight(100);  // 행 높이 조정

        calendarTables[month].setPreferredScrollableViewportSize(new Dimension(350, 200)); // 가로 크기 조절
        eventTables[month].setPreferredScrollableViewportSize(new Dimension(350, 200));    // 가로 크기 조절

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

            // 변경된 테이블 구조에 맞게 SQL 쿼리 수정
            String sql = "SELECT * FROM academic_schedule WHERE MONTH(start_date) = ? AND YEAR(start_date) = ? ORDER BY start_date ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, month + 1);  // DB는 1부터 시작
            preparedStatement.setInt(2, CURRENT_YEAR);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Date startDate = resultSet.getDate("start_date");
                Date endDate = resultSet.getDate("end_date");  // end_date도 가져옵니다.
                String event = resultSet.getString("event");

                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                int startDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                eventDaysArray[month].add(startDayOfMonth);
                System.out.println("Added Event Day: " + startDayOfMonth); // 이벤트 시작 날짜를 출력

                if (endDate != null) {
                    cal.setTime(endDate);
                    int endDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    eventModels[month].addRow(new Object[]{startDate + " ~ " + endDate, event});  // 시작 및 종료 날짜를 모두 보여줍니다.
                } else {
                    eventModels[month].addRow(new Object[]{startDate, event});
                }
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
