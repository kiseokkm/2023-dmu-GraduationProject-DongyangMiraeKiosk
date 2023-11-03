package Kiosk_graduation;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class HoldOnGraduation extends JPanel {
	
	private JScrollPane scrollPane;

    public HoldOnGraduation() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JLabel lblTitle = new JLabel("ㅡ 졸업보류  ㅡ");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 30)); 
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER); 
        lblTitle.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(lblTitle);
        addTextPaneSection(mainPanel);
        JLabel lblGraduationCredits = new JLabel("수강신청 학점에 따른 등록금 산정");
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
        textPane.setText("졸업보류자의 등록금은 수강신청 학점에 따라 아래와 같이 등록금이 산정됩니다.");
        
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



    private void addGraduationCreditSection(JPanel container) {
        String[] columnNames = {"1학점∼3학점", "4학점∼6학점", "7학점∼9학점", "10학점 이상"};
        Object[][] data = {
            {"해당학기 등록금의 1/6", "해당학기 등록금의 1/3", "해당학기 등록금의 1/2", "해당학기 등록금 전액"}
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