package kiosk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import services.DatabaseService;

public class EmergencyClassroom extends JPanel {    
    private DefaultTableModel staffTableModel;
    private JPanel staticPanel;
    private JScrollPane resultScrollPane;
    private static DatabaseService dbService = new DatabaseService();
   
    public EmergencyClassroom() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("교직원 검색");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        initSearchPanel(centerPanel);
        initStaticPanel(centerPanel);
        add(centerPanel, BorderLayout.CENTER);

        staffTableModel = new DefaultTableModel(new Object[]{"소속", "성명", "담당업무", "전화번호"}, 0);
        JTable staffTable = new JTable(staffTableModel);
        setUpTableAppearance(staffTable); 
        resultScrollPane = new JScrollPane(staffTable);
        add(resultScrollPane, BorderLayout.SOUTH);
        resultScrollPane.setVisible(false);
    }
    
    private void setUpTableAppearance(JTable table) {
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(255, 255, 224));
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer bodyRenderer = new DefaultTableCellRenderer();
        bodyRenderer.setBackground(new Color(255, 228, 196));
        bodyRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.setDefaultRenderer(Object.class, bodyRenderer);
    }

    private void initStaticPanel(JPanel centerPanel) {
        staticPanel = new JPanel();
        staticPanel.setLayout(new BoxLayout(staticPanel, BoxLayout.Y_AXIS));
        staticPanel.setBackground(new Color(255, 228, 196)); 

        addPanel("학생서비스센터", new String[]{"센터", "전화번호"}, new Object[][]{
            {"학적 (휴 ·복학, 제증명발급, 전과, 학점 · 학기포기)", "02) 2610-1707"},
            {"수업 (수강신청, 계절학기)", "02) 2610-1708"},
            {"취업 · 현장실습", "02) 2610-1720"},
            {"장학 (장학, 학자금대출)", "02) 2610-1715"},
            {"학생지원 (복지, 학생증발급, 상담, 시설물사용신청)", "02) 2610-1714"},
            {"병사(예비군)", "02) 2610-1749"},
            {"보건실", "02) 2610-1716"}
        });
        addPanel("사무처", new String[]{"센터", "전화번호"}, new Object[][]{
            {"관리영선팀(시설관리)", "02) 2610-1876"},
            {"경리팀(등록)", "02) 2610-1725"},
            {"총무팀", "02) 2610-1770"}
        });
        addPanel("종합정보지원실", new String[]{"조직명", "전화번호"}, new Object[][]{
            {"도서관", "02) 2610-1728"},
            {"전산실", "02) 2610-1737"}
        });
        addPanel("학부(과)", new String[]{"조직명", "전화번호"}, new Object[][]{
            {"기계공학부", "02) 2610-1751"},
            {"로봇자동화공학부", "02) 2610-1832"},
            {"전기전자통신공학부", "02) 2610-1931"},
            {"컴퓨터공학부", "02) 2610-1842"},
            {"생활환경공학부\n(건축과, 실내환경디자인과, 시각정보디자인과)", "02) 2610-1886"},
            {"생명화학공학과", "02) 2610-1820"},
            {"식품공학과", "02) 2610-1851"},
            {"경영학부", "02) 2610-5215"},
            {"교양과", "02) 2610-1872"}
        });
        addPanel("학생대표기구", new String[]{"조직명", "전화번호"}, new Object[][]{
            {"총학생실", "02) 2610-1891"},
            {"대의원실", "02) 2610-1892"},
            {"동양미디어", "02) 2610-1895 (방송국)"}
        });
        addPanel("기타", new String[]{"조직명", "전화번호"}, new Object[][]{
            {"구내서점", "02) 2610-1867"},
            {"구내식당", "02) 2610-1868"},
            {"수위실", "02) 2610-1861"}
        });

        JScrollPane mainScrollPane = new JScrollPane(staticPanel);
        centerPanel.add(mainScrollPane, BorderLayout.CENTER);
    }
    private void addPanel(String title, String[] columns, Object[][] data) {
        JPanel panel = createTablePanel(title, columns, data);
        staticPanel.add(panel);
    }
    private JPanel createTablePanel(String title, String[] columnNames, Object[][] data) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);

        table.setBackground(new Color(255, 228, 196));

        if (title.equals("사무처") || title.equals("종합정보지원실")) {
            table.setRowHeight(40);
        } else {
            table.setRowHeight(40);
        }
        table.getTableHeader().setBackground(Color.YELLOW);

        Dimension tableSize = table.getPreferredSize();
        tableSize.height = table.getRowHeight() * table.getRowCount();
        table.setPreferredScrollableViewportSize(tableSize);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setPreferredSize(new Dimension(tableSize.width, tableSize.height + 25));
        
       
        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    private void initSearchPanel(JPanel centerPanel) {
        JPanel searchPanel = new JPanel(new FlowLayout());
        JComboBox<String> searchCategoryComboBox = new JComboBox<>(new String[]{"소속", "성명", "담당업무", "전화번호"});
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("검색");

        searchPanel.add(searchCategoryComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        centerPanel.add(searchPanel, BorderLayout.NORTH);

        searchButton.addActionListener(e -> {
            String selectedCategory = (String) searchCategoryComboBox.getSelectedItem();
            String query = searchField.getText().trim();
            
            new Thread(() -> {
                searchForStaff(selectedCategory, query, staffTableModel);
                javax.swing.SwingUtilities.invokeLater(() -> {
                    centerPanel.removeAll();  
                    centerPanel.add(searchPanel, BorderLayout.NORTH); 
                    centerPanel.add(resultScrollPane, BorderLayout.CENTER); 
                    centerPanel.revalidate(); 
                    centerPanel.repaint();  
                    resultScrollPane.setVisible(true);
                });
            }).start(); 
        });
    }
    public void searchForStaff(String category, String query, DefaultTableModel model) {
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
        try {
            dbService.connect();
            String sql = "SELECT department, name, task, phone FROM staff WHERE " + searchColumn + " LIKE ?";
            PreparedStatement statement = dbService.conn.prepareStatement(sql);
            statement.setString(1, "%" + query + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getString("department"),
                    rs.getString("name"),
                    rs.getString("task"),
                    rs.getString("phone")
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbService.disconnect();
        }
    }
    public DefaultTableModel getTableModel() {
        return staffTableModel;
    }
}