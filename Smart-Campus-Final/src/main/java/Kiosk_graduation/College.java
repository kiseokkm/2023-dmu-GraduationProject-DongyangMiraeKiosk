package Kiosk_graduation;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class College extends JPanel {
	
	private JScrollPane scrollPane;

    public College() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        // Title "전문학사"
        JLabel lblTitle = new JLabel("ㅡ 학사 ㅡ");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 30)); 
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER); 
        lblTitle.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        addTextPaneSection(mainPanel);
        JLabel lblMajorMinimumCredits = new JLabel(" 학위종별 ");
        lblMajorMinimumCredits.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblMajorMinimumCredits.setAlignmentX(LEFT_ALIGNMENT);
        mainPanel.add(lblMajorMinimumCredits);
        addDegreeSection(mainPanel);
        JLabel lblGraduationCredits = new JLabel(">> 졸업조건 / 졸업이수 학점");
        lblGraduationCredits.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblGraduationCredits.setAlignmentX(LEFT_ALIGNMENT);
        mainPanel.add(lblGraduationCredits);
        addGraduationCreditSection(mainPanel);   
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
        add(scrollPane, BorderLayout.CENTER);  
    }
    private void addTextPaneSection(JPanel container) {
        JTextPane textPane = new JTextPane();
        textPane.setText("1년제 학과는 2학기 이상, 2년제 학과는 4학기 이상 재학한 학생이 졸업요건을 충족하면 졸업할 수 있습니다.");
        
        textPane.setEditable(false);
        textPane.setBackground(new Color(100, 149, 237));  // Setting background color to RoyalBlue
        textPane.setForeground(Color.WHITE);  // Setting text color to white for better visibility on blue background
        textPane.setFont(new Font("SansSerif", Font.BOLD, 18));

        // Setting the text to be centered
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // Set the JTextPane's maximum size to its preferred size
        textPane.setMaximumSize(textPane.getPreferredSize());

        JPanel textPanelWrapper = new JPanel();
        textPanelWrapper.setLayout(new BoxLayout(textPanelWrapper, BoxLayout.X_AXIS));
        textPanelWrapper.add(Box.createHorizontalGlue());
        textPanelWrapper.add(textPane);
        textPanelWrapper.add(Box.createHorizontalGlue());
        
        container.add(textPanelWrapper);
    }

    private void addDegreeSection(JPanel container) {
        String[] columnNames = {"종별", "해당 학과"};
        Object[][] data = {
            {"공학사", "기계공학과, 기계설계공학과, 자동화공학과, 전기공학과, 정보전자공학과, 정보통신공학과, 컴퓨터소프트웨어공학과, 컴퓨터정보공학과,"
            		+ " 실내환경디자인학과, 시각정보디자인학과, 생명화학공학과, 식품공학과, 건축학과"},
            {"경영학사", "경영학과, 호텔관광학과"}
        };
        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(1600, 180));
        table.setRowHeight(60);
        table.setBackground(new Color(220, 220, 220));
        table.getTableHeader().setBackground(Color.GRAY);
        table.getTableHeader().setForeground(Color.WHITE);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100); 
        columnModel.getColumn(1).setPreferredWidth(1500);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBackground(new Color(220, 220, 220));
        leftRenderer.setForeground(Color.BLACK);
        table.setDefaultRenderer(Object.class, leftRenderer);
        // Custom cell renderer for wrapping text in cell
        class TextAreaRenderer extends JTextArea implements TableCellRenderer {
            public TextAreaRenderer() {
                setWrapStyleWord(true);
                setLineWrap(true);
            }
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                setText((String) value);
                setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
                if (table.getRowHeight(row) != getPreferredSize().height) {
                    table.setRowHeight(row, getPreferredSize().height);
                }
                return this;
            }
        }
        table.setDefaultRenderer(String.class, new TextAreaRenderer());  
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(1600, table.getPreferredSize().height + table.getTableHeader().getPreferredSize().height));
        container.add(scrollPane);
    }
    
    private void addGraduationCreditSection(JPanel container) {
    	String[] columnNames = {"구분", "졸업 학점"};
    	Object[][] data = {
    	    {"1년제", "20" },
    	    {"2년제", "55"}
    	};
        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(550, 70));
        table.setRowHeight(35);
        table.setBackground(new Color(220, 220, 220));
        table.getTableHeader().setBackground(Color.GRAY);
        table.getTableHeader().setForeground(Color.WHITE);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setBackground(new Color(220, 220, 220));
        centerRenderer.setForeground(Color.BLACK);
        table.setDefaultRenderer(Object.class, centerRenderer);      
        JScrollPane scrollPane = new JScrollPane(table);
        container.add(scrollPane);
    }
}