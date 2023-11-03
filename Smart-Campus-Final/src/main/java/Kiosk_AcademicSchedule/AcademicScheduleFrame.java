package Kiosk_AcademicSchedule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Kiosk_AcademicSchedule.CustomTableCellRenderer.CustomTableCellRendererForEvents;

import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashSet;

public class AcademicScheduleFrame extends JPanel {

    private int currentMonth;
    private int currentYear;
    private JLabel lblMonthYear;
    private JTable calendarTable;
    private JTable academicEventsTable;
    private HashSet<Integer> eventDays = new HashSet<>();
    
    public AcademicScheduleFrame() {
        setLayout(new BorderLayout());
        currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        currentYear = 2023;

        String[] columnNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames);
        calendarTable = new JTable(model);
        calendarTable.setRowHeight(47); 

        DefaultTableModel academicEventsModel = new DefaultTableModel(new Object[]{"날짜", "일정"}, 0);
        academicEventsTable = new JTable(academicEventsModel);
        academicEventsTable.setRowHeight(38);

        JButton moveToAllFrame = new JButton("학사일정 전체보기");
        moveToAllFrame.addActionListener(e -> {
            JFrame frame = new JFrame("Academic Schedule for All Months");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setSize(screenSize.width, screenSize.height - 100);
            JScrollPane scrollPane = new JScrollPane(new AcademicScheduleAllFrame());
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            frame.add(scrollPane);
            frame.setVisible(true);
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(moveToAllFrame, BorderLayout.WEST);

        JPanel calendarPanel = new JPanel();
        JButton prevMonth = new JButton("<<");
        JButton nextMonth = new JButton(">>");
        lblMonthYear = new JLabel();

        add(calendarPanel, BorderLayout.NORTH);
        calendarTable.setPreferredScrollableViewportSize(new Dimension(500, 200)); 
        add(new JScrollPane(calendarTable), BorderLayout.CENTER);
        academicEventsTable.setPreferredScrollableViewportSize(new Dimension(500, 230));
        academicEventsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        academicEventsTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        add(new JScrollPane(academicEventsTable), BorderLayout.SOUTH);

        calendarPanel.add(prevMonth);
        calendarPanel.add(lblMonthYear);
        calendarPanel.add(nextMonth);
        topPanel.add(calendarPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH); 

        prevMonth.addActionListener(e -> {
            currentMonth--;
            if (currentMonth < 0) {
                currentMonth = 11;
                currentYear--;
            }
            updateCalendar();
        });

        nextMonth.addActionListener(e -> {
            currentMonth++;
            if (currentMonth > 11) {
                currentMonth = 0;
                currentYear++;
            }
            updateCalendar();
        });

        CustomTableCellRenderer.setHeaderRenderer(calendarTable);
        CustomTableCellRenderer.setHeaderRenderer(academicEventsTable);

        updateCalendar();
    }

    private void updateCalendar() {
        eventDays.clear();
        lblMonthYear.setText(currentYear + "." + (currentMonth + 1));
        DefaultTableModel model = (DefaultTableModel) calendarTable.getModel();
        model.setRowCount(0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(currentYear, currentMonth, 1);
        int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int numberOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        Object[] week = new Object[7];
        int day = 1;

        for (int i = 0; day <= numberOfDaysInMonth; i++) {
            for (int j = firstDayOfMonth; j < 7 && day <= numberOfDaysInMonth; j++) {
                week[j] = day++;
            }
            model.addRow(week);
            week = new Object[7];
            firstDayOfMonth = 0;
        }

        CustomTableCellRenderer renderer = new CustomTableCellRenderer(eventDays);
        for (int i = 0; i < calendarTable.getColumnCount(); i++) {
            calendarTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        DefaultTableModel academicModel = (DefaultTableModel) academicEventsTable.getModel();
        academicModel.setRowCount(0);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT * FROM academic_schedule WHERE (MONTH(start_date) = ? OR MONTH(end_date) = ?) AND YEAR(start_date) = ? ORDER BY start_date ASC";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, currentMonth + 1);
            preparedStatement.setInt(2, currentMonth + 1);
            preparedStatement.setInt(3, currentYear);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Date startDate = resultSet.getDate("start_date");
                Date endDate = resultSet.getDate("end_date");
                String event = resultSet.getString("event");

                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                int startDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                eventDays.add(startDayOfMonth);

                if (endDate != null) {
                    cal.setTime(endDate);
                    int endDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    eventDays.add(endDayOfMonth);
                    academicModel.addRow(new Object[]{startDate + " ~ " + endDate, event});
                } else {
                    academicModel.addRow(new Object[]{startDate, event});
                }
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

            // 학사 이벤트 테이블의 셀 렌더러 설정
            CustomTableCellRendererForEvents eventsRenderer = new CustomTableCellRendererForEvents();
            for (int i = 0; i < academicEventsTable.getColumnCount(); i++) {
                academicEventsTable.getColumnModel().getColumn(i).setCellRenderer(eventsRenderer);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "데이터베이스 연결 또는 쿼리 중 오류 발생: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}