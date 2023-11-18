package Admin_Manager;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Kiosk_AcademicSchedule.JDateChooser;
import services.DatabaseService;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AcademicScheduleManager extends JFrame {
    private JTable scheduleTable;
    private DefaultTableModel tableModel;
    private JTextField eventField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private DatabaseService dbService = new DatabaseService();

    public AcademicScheduleManager() {
        setTitle("학사일정 관리");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Start Date", "End Date", "Event"}, 0);
        scheduleTable = new JTable(tableModel);

        JPanel inputPanel = new JPanel();
        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();
        eventField = new JTextField(20);
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("새로고침");

        addButton.addActionListener(e -> addSchedule());
        updateButton.addActionListener(e -> updateSchedule());
        deleteButton.addActionListener(e -> deleteSchedule());
        refreshButton.addActionListener(e -> loadSchedules());

        inputPanel.add(startDateChooser);
        inputPanel.add(endDateChooser);
        inputPanel.add(eventField);
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);
        inputPanel.add(refreshButton);

        add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        scheduleTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && scheduleTable.getSelectedRow() != -1) {
                    Date selectedStartDate = (Date) tableModel.getValueAt(scheduleTable.getSelectedRow(), 1);
                    Date selectedEndDate = (Date) tableModel.getValueAt(scheduleTable.getSelectedRow(), 2);
                    String selectedEvent = (String) tableModel.getValueAt(scheduleTable.getSelectedRow(), 3);

                    startDateChooser.setDate(selectedStartDate);
                    endDateChooser.setDate(selectedEndDate);
                    eventField.setText(selectedEvent);
                }
            }
        });

        loadSchedules();
    }
    private void loadSchedules() {
        tableModel.setRowCount(0);
        try {
            dbService.connect();
            PreparedStatement statement = dbService.conn.prepareStatement("SELECT * FROM academic_schedule ORDER BY start_date ASC");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date startDate = resultSet.getDate("start_date");
                Date endDate = resultSet.getDate("end_date");
                String event = resultSet.getString("event");
                tableModel.addRow(new Object[]{id, startDate, endDate, event});
            }
            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
    private void addSchedule() {
        try {
            dbService.connect();
            PreparedStatement insertStatement = dbService.conn.prepareStatement("INSERT INTO academic_schedule (start_date, end_date, event) VALUES (?, ?, ?)");
            insertStatement.setDate(1, new java.sql.Date(startDateChooser.getDate().getTime()));
            insertStatement.setDate(2, endDateChooser.getDate() != null ? new java.sql.Date(endDateChooser.getDate().getTime()) : null);
            insertStatement.setString(3, eventField.getText());
            int result = insertStatement.executeUpdate();
            if (result > 0) {
                loadSchedules();
                eventField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "데이터베이스에 항목을 추가하는 데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
            insertStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "오류 발생: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        } finally {
            dbService.disconnect();
        }
    }
    private void updateSchedule() {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow != -1) {
            int selectedId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                dbService.connect();
                PreparedStatement preparedStatement = dbService.conn.prepareStatement("UPDATE academic_schedule SET start_date = ?, end_date = ?, event = ? WHERE id = ?");
                preparedStatement.setDate(1, new java.sql.Date(startDateChooser.getDate().getTime()));
                preparedStatement.setDate(2, endDateChooser.getDate() != null ? new java.sql.Date(endDateChooser.getDate().getTime()) : null);
                preparedStatement.setString(3, eventField.getText());
                preparedStatement.setInt(4, selectedId);

                int result = preparedStatement.executeUpdate();
                if (result > 0) {
                    loadSchedules();
                }
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dbService.disconnect();
            }
        }
    }
    private void deleteSchedule() {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow != -1) {
            int selectedId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                dbService.connect();
                PreparedStatement preparedStatement = dbService.conn.prepareStatement("DELETE FROM academic_schedule WHERE id = ?");
                preparedStatement.setInt(1, selectedId);

                int result = preparedStatement.executeUpdate();
                if (result > 0) {
                    loadSchedules();
                }
                preparedStatement.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dbService.disconnect();
            }
        }
    }
}