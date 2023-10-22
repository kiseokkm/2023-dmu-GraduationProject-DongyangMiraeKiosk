package manager;

import java.awt.BorderLayout;
import java.awt.Component;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import AcademicSchedule.JDateChooser;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



public class AcademicScheduleManager extends JFrame {
    private JTable scheduleTable;
    private DefaultTableModel tableModel;
    private JTextField eventField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JDateChooser dateChooser;  // 날짜 선택기

    public AcademicScheduleManager() {
        setTitle("학사일정 관리");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Date", "Event"}, 0);
        scheduleTable = new JTable(tableModel);

        JPanel inputPanel = new JPanel();
        dateChooser = new JDateChooser();
        eventField = new JTextField(20);
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        	deleteButton = new JButton("Delete");

        addButton.addActionListener(e -> addSchedule());
        updateButton.addActionListener(e -> updateSchedule());
        deleteButton.addActionListener(e -> deleteSchedule());
        
        

        inputPanel.add(dateChooser);
        inputPanel.add(eventField);
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);

        add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        
        scheduleTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && scheduleTable.getSelectedRow() != -1) {
                    Date selectedDate = (Date) tableModel.getValueAt(scheduleTable.getSelectedRow(), 0);
                    String selectedEvent = (String) tableModel.getValueAt(scheduleTable.getSelectedRow(), 1);
                    
                    dateChooser.setDate(selectedDate);
                    eventField.setText(selectedEvent);
                }
            }
        });

        loadSchedules();
    }

    private void loadSchedules() {
        tableModel.setRowCount(0); // Clear existing rows

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT * FROM academic_schedule ORDER BY date ASC";  // ORDER BY clause added here
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                Date eventDate = resultSet.getDate("date");
                String event = resultSet.getString("event");
                tableModel.addRow(new Object[]{eventDate, event});
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void addSchedule() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            
            // 날짜와 이벤트 중복 확인
            String checkSql = "SELECT * FROM academic_schedule WHERE date = ? AND event = ?";
            preparedStatement = connection.prepareStatement(checkSql);
            preparedStatement.setDate(1, new java.sql.Date(dateChooser.getDate().getTime()));
            preparedStatement.setString(2, eventField.getText());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                JOptionPane.showMessageDialog(this, "이미 같은 날짜와 이벤트가 존재합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String sql = "INSERT INTO academic_schedule (date, event) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setDate(1, new java.sql.Date(dateChooser.getDate().getTime()));
            preparedStatement.setString(2, eventField.getText());

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                loadSchedules();
                eventField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "데이터베이스에 항목을 추가하는 데 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "오류 발생: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void updateSchedule() {
        // Selected row from the table
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
                String sql = "UPDATE academic_schedule SET event = ? WHERE date = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, eventField.getText());
                preparedStatement.setDate(2, new java.sql.Date(dateChooser.getDate().getTime()));

                int result = preparedStatement.executeUpdate();
                if (result > 0) {
                    loadSchedules(); // Refresh the table
                }

                preparedStatement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteSchedule() {
        int selectedRow = scheduleTable.getSelectedRow();
        if (selectedRow != -1) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
                String sql = "DELETE FROM academic_schedule WHERE date = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setDate(1, new java.sql.Date(dateChooser.getDate().getTime()));

                int result = preparedStatement.executeUpdate();
                if (result > 0) {
                    loadSchedules(); // Refresh the table
                }

                preparedStatement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}