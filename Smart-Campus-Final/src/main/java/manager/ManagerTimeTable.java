package manager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ManagerTimeTable extends JFrame {
    private JTextField searchField;
    private JButton searchButton;
    private JPanel searchPanel;
    private JPanel timeTablePanel;
    private JList<String> studentList;
    private JList<String> searchResultsList;
    private Connection connection;

    public ManagerTimeTable() {
        setTitle("시간표 관리");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        connectToDatabase();

        ArrayList<String> studentIds = fetchStudentIds();

        searchField = new JTextField(20);
        searchButton = new JButton("검색");
        searchButton.addActionListener(e -> searchStudents());

        studentList = new JList<>(studentIds.toArray(new String[0]));
        studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedStudentId = studentList.getSelectedValue();
                displayTimeTable(selectedStudentId);
            }
        });

        searchResultsList = new JList<>();
        searchResultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResultsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selectedStudentId = searchResultsList.getSelectedValue();
                    displayTimeTable(selectedStudentId);
                }
            }
        });

        JScrollPane listScrollPane = new JScrollPane(studentList);
        listScrollPane.setPreferredSize(new Dimension(150, 400));

        JScrollPane searchResultsScrollPane = new JScrollPane(searchResultsList);
        searchResultsScrollPane.setPreferredSize(new Dimension(250, 100));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(listScrollPane, BorderLayout.NORTH);

        searchPanel = new JPanel(new BorderLayout());
        JPanel topSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topSearchPanel.add(new JLabel("학생 ID 검색:"));
        topSearchPanel.add(searchField);
        topSearchPanel.add(searchButton);
        searchPanel.add(topSearchPanel, BorderLayout.NORTH);
        searchPanel.add(searchResultsScrollPane, BorderLayout.CENTER);

        timeTablePanel = new JPanel();

        add(searchPanel, BorderLayout.NORTH);
        add(timeTablePanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
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

    private ArrayList<String> fetchStudentIdsWithSearchTerm(String searchTerm) {
        ArrayList<String> studentIds = new ArrayList<>();
        String query = "SELECT DISTINCT studentId FROM timetable WHERE studentId LIKE ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + searchTerm + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                studentIds.add(rs.getString("studentId"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentIds;
    }

    private void searchStudents() {
        String searchTerm = searchField.getText().trim();
        ArrayList<String> searchResults = fetchStudentIdsWithSearchTerm(searchTerm);
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String studentId : searchResults) {
            model.addElement(studentId);
        }
        searchResultsList.setModel(model);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ManagerTimeTable().setVisible(true);
        });
    }
}