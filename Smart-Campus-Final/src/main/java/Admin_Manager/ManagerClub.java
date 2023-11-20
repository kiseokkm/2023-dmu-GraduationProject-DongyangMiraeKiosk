package Admin_Manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Kiosk_Club.HobbyClub;
import Kiosk_Club.MajorClub;

import java.awt.*;
import java.sql.*;
import services.DatabaseService;

public class ManagerClub extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel hobbyClubPanel;
    private JPanel majorClubPanel;
    private DatabaseService dbService = new DatabaseService();

    public ManagerClub() {
        setTitle("Club Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        hobbyClubPanel = new HobbyClub();
        majorClubPanel = MajorClub.createMajorClubPanel();

        JTable majorClubTable = MajorClub.getTableFromPanel(majorClubPanel);
        DefaultTableModel majorClubModel = (DefaultTableModel) majorClubTable.getModel();
        majorClubModel.addColumn("ID", new Object[]{});  
        refreshDataForAdmin(majorClubTable); 

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
        majorRefreshButton.addActionListener(e -> refreshDataForAdmin(majorClubTable));
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
    private void refreshDataForAdmin(JTable table) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);
        try {
            dbService.connect();
            Statement stmt = dbService.conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT id, 학부, PDLab, 기본활동, 개발과제, 지도교수 FROM MajorClubs");
            while (rs.next()) {
                String 학부 = rs.getString("학부");
                String PDLab = rs.getString("PDLab");
                String 기본활동 = rs.getString("기본활동");
                String 개발과제 = rs.getString("개발과제");
                String 지도교수 = rs.getString("지도교수");
                int id = rs.getInt("id");
                tableModel.addRow(new Object[]{ 학부, PDLab, 기본활동, 개발과제, 지도교수, id});
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
    private void executeUpdateQuery(String query, String... params) {
        try {
            dbService.connect();
            PreparedStatement pstmt = dbService.conn.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                pstmt.setString(i + 1, params[i]);
            }
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
    private void executeDeleteQuery(String query, int id) {
        try {
            dbService.connect();
            PreparedStatement pstmt = dbService.conn.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerClub managerClub = new ManagerClub();
            managerClub.setVisible(true);
        });
    }
}
