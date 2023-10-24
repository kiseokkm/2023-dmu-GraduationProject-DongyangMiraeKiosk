package kiosk;

import java.awt.BorderLayout;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class HobbyClub extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public HobbyClub() {
        setLayout(new BorderLayout());
        
        String[] columnNames = {"연번", "동아리명", "C.Ⅰ. Lab", "소개 및 활동 내용"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM HobbyClubs");

            while (rs.next()) {
                int 연번 = rs.getInt("연번");
                String 동아리명 = rs.getString("동아리명");
                String CILab = rs.getString("CILab");
                String 소개및활동내용 = rs.getString("소개및활동내용");
                
                tableModel.addRow(new Object[]{연번, 동아리명, CILab, 소개및활동내용});
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 칼럼 너비 조절
        TableColumn column = null;
        
        column = table.getColumnModel().getColumn(0);  // "연번" 칼럼
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setPreferredWidth(30);

        column = table.getColumnModel().getColumn(1);  // "동아리명" 칼럼
        column.setMinWidth(100);  // 원하는 너비로 설정
        column.setMaxWidth(150);  // 원하는 최대 너비로 설정
        column.setPreferredWidth(125);  // 원하는 선호 너비로 설정

        column = table.getColumnModel().getColumn(2);  // "C.Ⅰ. Lab" 칼럼
        column.setMinWidth(60);
        column.setMaxWidth(40);
        column.setPreferredWidth(40);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // GUI 업데이트
        revalidate();
        repaint();
    }
}
