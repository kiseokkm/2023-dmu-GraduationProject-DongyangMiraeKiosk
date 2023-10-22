package manager;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import kiosk.EmergencyClassroom;

public class EmergencyClassroomManager extends JFrame {
    private EmergencyClassroom emergencyClassroomPanel;
    private JTable staffTable;
    
    // DB 연결 정보
    private static final String DB_URL = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "dongyang";

    public EmergencyClassroomManager() {
        setTitle("교직원 관리");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        emergencyClassroomPanel = new EmergencyClassroom();

        // 테이블 모델 구성 변경
        DefaultTableModel staffTableModel = new DefaultTableModel(new Object[]{"번호", "소속", "성명", "담당업무", "전화번호"}, 0);
        staffTable = new JTable(staffTableModel);
        JScrollPane scrollPane = new JScrollPane(staffTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel searchPanel = new JPanel(new FlowLayout());
        JComboBox<String> searchCategoryComboBox = new JComboBox<>(new String[]{"소속", "성명", "담당업무", "전화번호"});
        searchPanel.add(searchCategoryComboBox);
        searchPanel.add(new JLabel("검색: "));
        JTextField searchField = new JTextField(20);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("검색");
        searchPanel.add(searchButton);

        searchButton.addActionListener(e -> {
            String selectedCategory = (String) searchCategoryComboBox.getSelectedItem();
            String query = searchField.getText().trim();
            refreshTableData(selectedCategory, query);
        });

        add(searchPanel, BorderLayout.NORTH);

        JButton addButton = new JButton("추가");
        JButton modifyButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");

        addButton.addActionListener(e -> {
            String department = JOptionPane.showInputDialog(this, "소속을 입력하세요:");
            String name = JOptionPane.showInputDialog(this, "이름을 입력하세요:");
            String task = JOptionPane.showInputDialog(this, "담당업무를 입력하세요:");
            String phone = JOptionPane.showInputDialog(this, "전화번호를 입력하세요:");
            
            if (department != null && name != null && task != null && phone != null) {
                addStaff(department, name, task, phone);
                refreshTableData("", "");
            }
        });

        modifyButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "수정할 교직원의 ID를 입력하세요:");
            String department = JOptionPane.showInputDialog(this, "새로운 소속을 입력하세요:");
            String name = JOptionPane.showInputDialog(this, "새로운 이름을 입력하세요:");
            String task = JOptionPane.showInputDialog(this, "새로운 담당업무를 입력하세요:");
            String phone = JOptionPane.showInputDialog(this, "새로운 전화번호를 입력하세요:");
            
            if (id != null && department != null && name != null && task != null && phone != null) {
                modifyStaff(id, department, name, task, phone);
            }
        });

        deleteButton.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "삭제할 교직원의 ID를 입력하세요:");
            if (id != null) {
                deleteStaff(id);
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(addButton);
        controlPanel.add(modifyButton);
        controlPanel.add(deleteButton);

        add(controlPanel, BorderLayout.SOUTH);
    }

    private void refreshTableData(String category, String query) {
        DefaultTableModel model = (DefaultTableModel) staffTable.getModel();
        model.setRowCount(0);
        
        String searchColumn = "";
        switch (category) {
            case "소속":
                searchColumn = "department";
                break;
            case "성명":
                searchColumn = "name";
                break;
            case "담당업무":
                searchColumn = "task";
                break;
            case "전화번호":
                searchColumn = "phone";
                break;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT id, department, name, task, phone FROM staff WHERE " + searchColumn + " LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + query + "%");

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("department"),
                    rs.getString("name"),
                    rs.getString("task"),
                    rs.getString("phone")
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addStaff(String department, String name, String task, String phone) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "INSERT INTO staff (department, name, task, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, department);
            statement.setString(2, name);
            statement.setString(3, task);
            statement.setString(4, phone);
            
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "교직원 정보가 추가되었습니다.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding staff: " + ex.getMessage());
        }
    }

    private void modifyStaff(String id, String department, String name, String task, String phone) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "UPDATE staff SET department=?, name=?, task=?, phone=? WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, department);
            statement.setString(2, name);
            statement.setString(3, task);
            statement.setString(4, phone);
            statement.setInt(5, Integer.parseInt(id));
            
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "교직원 정보가 수정되었습니다.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error modifying staff: " + ex.getMessage());
        }
    }

    private void deleteStaff(String id) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "DELETE FROM staff WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(id));
            
            statement.executeUpdate();
            JOptionPane.showMessageDialog(this, "교직원 정보가 삭제되었습니다.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting staff: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmergencyClassroomManager manager = new EmergencyClassroomManager();
            manager.setVisible(true);
        });
    }
}
