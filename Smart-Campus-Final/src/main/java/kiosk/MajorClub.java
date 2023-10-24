package kiosk;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.*;

public class MajorClub {

    public static JPanel createMajorClubPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnNames = {"학부", "PD Lab", "기본활동", "개발 과제 (연구 및 관심분야)", "지도교수"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        // 데이터베이스 연결 및 데이터 추출
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM MajorClubs");

            while (rs.next()) {
                String 학부 = rs.getString("학부");
                String PDLab = rs.getString("PDLab");
                String 기본활동 = rs.getString("기본활동");
                String 개발과제 = rs.getString("개발과제");
                String 지도교수 = rs.getString("지도교수");

                tableModel.addRow(new Object[]{학부, PDLab, 기본활동, 개발과제, 지도교수});
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(table);
        int preferredHeight = table.getRowHeight() * 5 + table.getTableHeader().getPreferredSize().height;
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, preferredHeight));

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
}
