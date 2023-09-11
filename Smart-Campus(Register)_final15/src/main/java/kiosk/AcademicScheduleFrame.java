package kiosk;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

public class AcademicScheduleFrame extends JPanel {

    private int currentMonth;
    private int currentYear;
    private JLabel lblMonthYear;
    private JTable calendarTable;

    public AcademicScheduleFrame() {
        setLayout(new BorderLayout());
        currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        currentYear = 2023; // 설정된 연도

        // Create a JTable with date headers
        String[] columnNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames);
        calendarTable = new JTable(model);

        // Create a panel for the month/year and navigation buttons
        JPanel calendarPanel = new JPanel();
        JButton prevMonth = new JButton("<<");
        JButton nextMonth = new JButton(">>");
        lblMonthYear = new JLabel();

        // Add everything to the main panel
        add(calendarPanel, BorderLayout.NORTH);
        add(new JScrollPane(calendarTable), BorderLayout.CENTER);

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
        // Update the month label and clear the existing table
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        lblMonthYear.setText(months[currentMonth] + " " + currentYear);
        
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
        CustomTableCellRenderer renderer = new CustomTableCellRenderer();
        for (int i = 0; i < calendarTable.getColumnCount(); i++) {
            calendarTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // TODO: Fetch academic events from the database and update the JTable
        // For example, if there is an event on the 5th day of the month, update model's cell at (row, 5) to include the event
    }
    
}
