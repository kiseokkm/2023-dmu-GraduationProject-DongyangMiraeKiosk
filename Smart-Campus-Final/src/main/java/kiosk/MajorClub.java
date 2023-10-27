package kiosk;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.sql.*;

public class MajorClub {
	
	private static JTable table;

	public static JPanel createMajorClubPanel() {
	    JPanel panel = new JPanel(new BorderLayout());
	    JPanel imagePanel = new JPanel();
	    imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
	    table = new JTable();
	    
	    JTableHeader header = table.getTableHeader();
	    DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
	    headerRenderer.setBackground(new Color(255, 228, 196));
	    headerRenderer.setHorizontalAlignment(JLabel.CENTER);
	    header.setDefaultRenderer(headerRenderer);

	    try {
	        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
	        Statement stmt = connection.createStatement();

	        // 이미지 경로 가져오기
	        ResultSet rsImage = stmt.executeQuery("SELECT imagePath FROM MajorClubs WHERE imagePath IS NOT NULL");
	        while (rsImage.next()) {
	            String imagePath = rsImage.getString("imagePath");
	            if (imagePath != null && !imagePath.isEmpty()) {
	                ImageIcon imageIcon = new ImageIcon(MajorClub.class.getClassLoader().getResource(imagePath));
	                JLabel imageLabel = new JLabel(imageIcon);
	                imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
	                imagePanel.add(imageLabel);
	            }
	        }
	        rsImage.close();
	        panel.add(imagePanel, BorderLayout.NORTH);

	        String[] columnNames = {"학부", "PD Lab", "기본활동", "개발 과제 (연구 및 관심분야)", "지도교수"};
	        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
	        table.setModel(tableModel);

	        ResultSet rs = stmt.executeQuery("SELECT 학부, PDLab, 기본활동, 개발과제, 지도교수 FROM MajorClubs WHERE 학부 IS NOT NULL AND PDLab IS NOT NULL AND 기본활동 IS NOT NULL AND 개발과제 IS NOT NULL AND 지도교수 IS NOT NULL");
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


    public static void main(String[] args) {
        JFrame frame = new JFrame("Major Club");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(createMajorClubPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public static JTable getTableFromPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) comp;
                if (scrollPane.getViewport().getView() instanceof JTable) {
                    return (JTable) scrollPane.getViewport().getView();
                }
            }
        }
        return null;
    }

    public static void refreshData(JTable table) {
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.setRowCount(0);  // Clear existing data

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM MajorClubs");
            while (rs.next()) {
                int id = rs.getInt("id");
                String 학부 = rs.getString("학부");
                String PDLab = rs.getString("PDLab");
                String 기본활동 = rs.getString("기본활동");
                String 개발과제 = rs.getString("개발과제");
                String 지도교수 = rs.getString("지도교수");
                tableModel.addRow(new Object[]{id, 학부, PDLab, 기본활동, 개발과제, 지도교수});
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}