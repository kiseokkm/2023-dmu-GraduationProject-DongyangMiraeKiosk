package kiosk;

import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CafeTeria {

    public static JTable getMealInfoTable() {
        DefaultTableModel mealInfoTableModel = new DefaultTableModel(new Object[]{"메뉴", "가격", "칼로리"}, 0);
        JTable mealInfoTable = new JTable(mealInfoTableModel);

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            String sql = "SELECT * FROM meal_info ORDER BY id DESC";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getString("menu"),
                    rs.getInt("price"),
                    rs.getInt("calories")
                };
                mealInfoTableModel.addRow(row);
            }
            
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mealInfoTable;
    }

    public static JPanel getMealInfoPanel() {
        JPanel mealInfoPanel = new JPanel(new BorderLayout(10, 10));
        mealInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTable mealInfoTable = getMealInfoTable();
        mealInfoPanel.add(new JScrollPane(mealInfoTable), BorderLayout.CENTER);

        return mealInfoPanel;
    }
}
