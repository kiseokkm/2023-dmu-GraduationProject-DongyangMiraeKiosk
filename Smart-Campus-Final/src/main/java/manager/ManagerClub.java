package manager;

import kiosk.HobbyClub;
import kiosk.MajorClub;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ManagerClub extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel hobbyClubPanel;
    private JPanel majorClubPanel;

    private final String DB_URL = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "dongyang";

    public ManagerClub() {
        setTitle("Club Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        hobbyClubPanel = new HobbyClub();
        majorClubPanel = MajorClub.createMajorClubPanel();

        JPanel hobbyButtonPanel = new JPanel();
        JButton hobbyAddButton = new JButton("취미동아리 추가");
        JButton hobbyEditButton = new JButton("취미동아리 수정");
        JButton hobbyDeleteButton = new JButton("취미동아리 삭제");
        JButton hobbyRefreshButton = new JButton("새로고침");
        hobbyAddButton.addActionListener(e -> addHobbyData());
        hobbyEditButton.addActionListener(e -> editHobbyData());
        hobbyDeleteButton.addActionListener(e -> deleteHobbyData());
        hobbyRefreshButton.addActionListener(e -> ((HobbyClub) hobbyClubPanel).refreshData());
        hobbyButtonPanel.add(hobbyAddButton);
        hobbyButtonPanel.add(hobbyEditButton);
        hobbyButtonPanel.add(hobbyDeleteButton);
        hobbyButtonPanel.add(hobbyRefreshButton);
        hobbyClubPanel.add(hobbyButtonPanel, BorderLayout.SOUTH);

        JPanel majorButtonPanel = new JPanel();
        JButton majorAddButton = new JButton("전공동아리 추가");
        JButton majorEditButton = new JButton("전공동아리 수정");
        JButton majorDeleteButton = new JButton("전공동아리 삭제");
        JButton majorRefreshButton = new JButton("새로고침");
        majorAddButton.addActionListener(e -> addMajorData());
        majorEditButton.addActionListener(e -> editMajorData());
        majorDeleteButton.addActionListener(e -> deleteMajorData());
        majorRefreshButton.addActionListener(e -> MajorClub.refreshData(MajorClub.getTableFromPanel(majorClubPanel)));
        majorButtonPanel.add(majorAddButton);
        majorButtonPanel.add(majorEditButton);
        majorButtonPanel.add(majorDeleteButton);
        majorButtonPanel.add(majorRefreshButton);
        majorClubPanel.add(majorButtonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("취미동아리", hobbyClubPanel);
        tabbedPane.addTab("전공동아리", majorClubPanel);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void addHobbyData() {
        String clubName = JOptionPane.showInputDialog(this, "동아리명을 입력하세요");
        String ciLab = JOptionPane.showInputDialog(this, "C.Ⅰ. Lab을 입력하세요");
        String intro = JOptionPane.showInputDialog(this, "소개 및 활동 내용을 입력하세요");
        String query = "INSERT INTO hobbyclubs (동아리명, CILab, 소개및활동내용) VALUES (?, ?, ?)";
        executeUpdateQuery(query, clubName, ciLab, intro);
        ((HobbyClub) hobbyClubPanel).refreshData();
    }

    private void editHobbyData() {
        JTable currentTable = ((HobbyClub) hobbyClubPanel).getTable();
        int selectedRow = currentTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) currentTable.getValueAt(selectedRow, 0);
            String updatedClubName = JOptionPane.showInputDialog(this, "수정할 동아리명을 입력하세요");
            String updatedCILab = JOptionPane.showInputDialog(this, "수정할 C.Ⅰ. Lab을 입력하세요");
            String updatedIntro = JOptionPane.showInputDialog(this, "수정할 소개 및 활동 내용을 입력하세요");
            String query = "UPDATE HobbyClubs SET 동아리명=?, CILab=?, 소개및활동내용=? WHERE 연번=?";
            executeUpdateQuery(query, updatedClubName, updatedCILab, updatedIntro, String.valueOf(id));
            ((HobbyClub) hobbyClubPanel).refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
        }
    }

    private void deleteHobbyData() {
        JTable currentTable = ((HobbyClub) hobbyClubPanel).getTable();
        int selectedRow = currentTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) currentTable.getValueAt(selectedRow, 0);
            String query = "DELETE FROM HobbyClubs WHERE 연번=?";
            executeDeleteQuery(query, id);
            ((HobbyClub) hobbyClubPanel).refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

    private void addMajorData() {
        String faculty = JOptionPane.showInputDialog(this, "학부를 입력하세요");
        String pdLab = JOptionPane.showInputDialog(this, "PDLab을 입력하세요");
        String activity = JOptionPane.showInputDialog(this, "기본 활동을 입력하세요");
        String project = JOptionPane.showInputDialog(this, "개발 과제를 입력하세요");
        String professor = JOptionPane.showInputDialog(this, "지도교수를 입력하세요");
        String query = "INSERT INTO majorclubs (학부, PDLab, 기본활동, 개발과제, 지도교수) VALUES (?, ?, ?, ?, ?)";
        executeUpdateQuery(query, faculty, pdLab, activity, project, professor);
        MajorClub.refreshData(MajorClub.getTableFromPanel(majorClubPanel));
    }

    private void editMajorData() {
        JTable currentTable = MajorClub.getTableFromPanel(majorClubPanel);
        int selectedRow = currentTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) currentTable.getValueAt(selectedRow, 0);
            String updatedFaculty = JOptionPane.showInputDialog(this, "수정할 학부를 입력하세요");
            String updatedPDLab = JOptionPane.showInputDialog(this, "수정할 PDLab을 입력하세요");
            String updatedActivity = JOptionPane.showInputDialog(this, "수정할 기본 활동을 입력하세요");
            String updatedProject = JOptionPane.showInputDialog(this, "수정할 개발 과제(연구 및 관련 활동)를 입력하세요");
            String updatedProfessor = JOptionPane.showInputDialog(this, "수정할 지도교수를 입력하세요");
            String query = "UPDATE majorclubs SET 학부=?, PDLab=?, 기본활동=?, 개발과제=?, 지도교수=? WHERE id=?";
            executeUpdateQuery(query, updatedFaculty, updatedPDLab, updatedActivity, updatedProject, updatedProfessor, String.valueOf(id));
            MajorClub.refreshData(currentTable);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
        }
    }


    private void deleteMajorData() {
        JTable currentTable = MajorClub.getTableFromPanel(majorClubPanel);
        int selectedRow = currentTable.getSelectedRow();
        if (selectedRow != -1) {
            int id = (int) currentTable.getValueAt(selectedRow, 0);
            String query = "DELETE FROM majorclubs WHERE id=?";
            executeDeleteQuery(query, id);
            MajorClub.refreshData(currentTable);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void executeUpdateQuery(String query, String... params) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setString(i + 1, params[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void executeDeleteQuery(String query, int id) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerClub managerClub = new ManagerClub();
            managerClub.setVisible(true);
        });
    }
}
