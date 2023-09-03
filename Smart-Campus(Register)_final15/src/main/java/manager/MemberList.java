package manager;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MemberList extends JFrame {
    private JTable memberTable;
    private DefaultTableModel memberTableModel;

    public MemberList() {
        setTitle("회원 목록");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Username", "Major", "Student ID", "Name", "Phone Number", "Most Precious Thing"};
        memberTableModel = new DefaultTableModel(columnNames, 0);
        memberTable = new JTable(memberTableModel);
        JScrollPane scrollPane = new JScrollPane(memberTable);

        // Double-click to edit a member
        memberTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int selectedRow = memberTable.getSelectedRow();
                    String username = (String) memberTableModel.getValueAt(selectedRow, 0);
                    // 여기서 다이얼로그 또는 다른 프레임을 열어 회원 정보를 수정할 수 있습니다.
                    // 수정 후 데이터베이스를 업데이트합니다.
                }
            }
        });

        // 데이터베이스에서 회원 정보를 가져와 테이블에 추가합니다.
        loadMembersIntoTable();

        setLayout(new FlowLayout());
        add(scrollPane);
    }

    private void loadMembersIntoTable() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT username, major, studentId, name, phoneNumber, mostPreciousThing FROM user1";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            memberTableModel.setRowCount(0);  // 테이블 초기화
            while (rs.next()) {
                String username = rs.getString("username");
                String major = rs.getString("major");
                String studentId = rs.getString("studentId");
                String name = rs.getString("name");
                String phoneNumber = rs.getString("phoneNumber");
                String mostPreciousThing = rs.getString("mostPreciousThing");
                memberTableModel.addRow(new Object[]{username, major, studentId, name, phoneNumber, mostPreciousThing});
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}