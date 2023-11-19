package Kiosk_Club;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.sql.*;
import services.DatabaseService;

public class HobbyClub extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> clubTypeComboBox;
    private CardLayout cardLayout;
    private JPanel cardsPanel;
    private static DatabaseService dbService = new DatabaseService();

    public HobbyClub() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        table = new JTable();
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.YELLOW); 
        tableHeader.setForeground(Color.BLACK); 
        table.setBackground(new Color(255, 228, 196));

        clubTypeComboBox = new JComboBox<>(new String[]{"취미동아리", "전공동아리"});
        clubTypeComboBox.addActionListener(e -> switchPanel((String) clubTypeComboBox.getSelectedItem()));
        comboBoxPanel.add(new JLabel("동아리 선택:")); 
        comboBoxPanel.add(clubTypeComboBox);

        topPanel.add(comboBoxPanel, BorderLayout.NORTH); 

        JLabel titleLabel = new JLabel("취미동아리/ C.Ⅰ. Lab , 전공동아리/(PDLab)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        cardsPanel = new JPanel(cardLayout = new CardLayout());
        cardsPanel.add(createHobbyClubPanel(), "취미동아리");

        JPanel majorClubPanel = MajorClub.createMajorClubPanel();
        cardsPanel.add(majorClubPanel, "전공동아리");

        add(cardsPanel, BorderLayout.CENTER);
    }
    private JPanel createHobbyClubPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        String description = "<html><div style='text-align: center; border: 2px solid black; padding: 10px;'>C.Ⅰ. Lab 이란<br>"
        	    + "우리대학의 핵심역량인 인성, 리더십, 공동체정신, 협업능력 등의 함양을 통해 미래 인재를 양성하기 위한 <u>핵심역량 향상 동아리"
        	    + "(이하 C.Ⅰ. Lab : Competency Improving Lab)</u>를 선발하여 지원하고 있음<br>"
        	    + "스터디, 취미, 문화, 봉사 등 다양한 활동을 목적으로 구성되어 있는 취미동아리의 신청을 받아 C.Ⅰ. Lab을 선정함</div></html>";
        JLabel descriptionLabel = new JLabel(description, SwingConstants.CENTER);
        panel.add(descriptionLabel, BorderLayout.NORTH);

        String[] columnNames = {"연번", "동아리명", "C.Ⅰ. Lab", "소개 및 활동 내용"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(new Color(255, 228, 196));
                }
                return c;
            }
        };
        try {
        	dbService.connect();
            Statement stmt = dbService.conn.createStatement();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbService.disconnect();
        }
        JScrollPane scrollPane = new JScrollPane(table);
        int preferredHeight = table.getRowHeight() * 5 + table.getTableHeader().getPreferredSize().height;
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, preferredHeight));

        TableColumn column = null;

        column = table.getColumnModel().getColumn(0); 
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setPreferredWidth(30);

        column = table.getColumnModel().getColumn(1);  
        column.setMinWidth(100);  
        column.setMaxWidth(100);  
        column.setPreferredWidth(90);  

        column = table.getColumnModel().getColumn(2);
        column.setMinWidth(60);
        column.setMaxWidth(60);
        column.setPreferredWidth(50);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }
    private void switchPanel(String selectedClub) {
        cardLayout.show(cardsPanel, selectedClub);
    }
    public void refreshData() {
        tableModel.setRowCount(0);
        try {
        	dbService.connect();
            Statement stmt = dbService.conn.createStatement();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            dbService.disconnect();
        }
    }
    public JTable getTable() {
        return table;
    }

}