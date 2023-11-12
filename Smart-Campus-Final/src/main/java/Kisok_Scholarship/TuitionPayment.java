package Kisok_Scholarship;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TuitionPayment extends JPanel {
    private JPanel staticPanel;
    // 납부시기
    private JLabel img_semester1;
    private JLabel img_semester2;
    // 등록금 납부 고지서 출력
    private JLabel img_procedure1;
    private JLabel img_procedure2;
    private JLabel img_procedure3;
    private JLabel img_procedure4;
    private JLabel img_procedure5;
    
    // 교육비납입증명서 이미지
    private JLabel img_additionalInfo1;
    
    // 유의사항 이미지
    private JLabel img_note1;
    private JLabel img_note2;
    private JLabel img_note3;

    public TuitionPayment() {
        setLayout(new BorderLayout());

//        JLabel titleLabel = new JLabel("등록금납부");
//        titleLabel.setHorizontalAlignment(JLabel.CENTER);
//        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
//        add(titleLabel, BorderLayout.NORTH);
//        titleLabel.setOpaque(true); // 배경색이 보이도록 설정
//        titleLabel.setBackground(Color.WHITE);

        initStaticPanel();
        JScrollPane mainScrollPane = new JScrollPane(staticPanel);
        mainScrollPane.getViewport().setBackground(Color.WHITE);
        add(mainScrollPane, BorderLayout.WEST);
    }

    private void initStaticPanel() {
        staticPanel = new JPanel();
        staticPanel.setLayout(new BoxLayout(staticPanel, BoxLayout.Y_AXIS));
        staticPanel.setBackground(Color.WHITE);  // 배경색을 흰색으로 설정

        // 시기 Section
        addPeriodSection();

        // 등록금 납부 절차 Section
        addProcedureSection();


        // 교육비납입증명서 발급 방법 Section
        addAdditionalInfoSection();

        // 유의사항 Section
        addNotesSection();
    }

    private void addPeriodSection() {
        JPanel periodPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        periodPanel.setOpaque(false); // Make the panel transparent
        
        JLabel periodLabel = new JLabel("등록금 납부시기");
        periodLabel.setHorizontalAlignment(JLabel.LEFT); // Left alignment
        periodLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        periodLabel.setForeground(new Color(70,70,70));
        // Adjust the bottom padding to reduce the gap between the label and the table
//        periodLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        periodLabel.setOpaque(false); // Make the label background transparent
        
        periodPanel.add(periodLabel, BorderLayout.CENTER);
        staticPanel.add(periodPanel);
        
        // 등록금 납부시기 Round Panel들을 담을 Panel
        JPanel semesterPanel = new JPanel();
        semesterPanel.setLayout(new FlowLayout(FlowLayout.CENTER,30,10));
        semesterPanel.setBackground(Color.white);
        
        // 이미지 width height 설정
	    int newWidth = 250; // 원하는 너비 설정
	    int newHeight = 140; // 원하는 높이 설정
        
        // 1학기 이미지
        img_semester1 = new JLabel();
        java.awt.Image semesterImage1 = new javax.swing.ImageIcon(getClass().getResource("/images/semester3.png")).getImage();
	    java.awt.Image semesterResized1 = semesterImage1.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
	    img_semester1.setIcon(new javax.swing.ImageIcon(semesterResized1));
	    semesterPanel.add(img_semester1);
        
        // 2학기 이미지
	    img_semester2 = new JLabel();
	    java.awt.Image semesterImage2 = new javax.swing.ImageIcon(getClass().getResource("/images/semester4.png")).getImage();
	    java.awt.Image semesterResized2 = semesterImage2.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
	    img_semester2.setIcon(new javax.swing.ImageIcon(semesterResized2));
	    semesterPanel.add(img_semester2);
	    
        // 이미지들 담기
	    
//	    semesterPanel.add(semesterPNG2);
        staticPanel.add(semesterPanel);
        
        
//        semesterPanel.setBorder(new LineBorder(Color.black, 2));
        
        // Removing the title "1학기"
//        addTableToPanel(semesterPanel, null, new String[]{"학기", "기간"}, new Object[][]{
//            {"1학기", "2월 중순경 (학교에서 지정한 기간)"}
//        }, 20);
//        // Removing the title "2학기"
//        addTableToPanel(semesterPanel, null, new String[]{"학기", "기간"}, new Object[][]{
//            {"2학기", "8월 중순경 (학교에서 지정한 기간)"}
//        }, 20);
        
    }
    




    private void addProcedureSection() {
//        addBarGraphSection("절차", new String[]{
//            "등록금 고지서를 e-서비스에서 출력",
//            "은행 납부"
//            
//        });
    	
    	// 타이틀 추가
    	JPanel procedureTitlePanel = new JPanel();
    	JLabel procedureTitle = new JLabel();
    	procedureTitle.setText("등록금 납부 및 고지서 출력안내");
    	procedureTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20));
    	procedureTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
    	procedureTitle.setForeground(new Color(70,70,70));
    	procedureTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    	procedureTitlePanel.add(procedureTitle, BorderLayout.WEST);
    	procedureTitlePanel.setBackground(Color.white);
    	staticPanel.add(procedureTitlePanel);
    	
    	// 등록금 납부 절차 이미지 추가
    	
    	
    	// 등록금 납부시기 Round Panel들을 담을 Panel
        JPanel procedurePanel = new JPanel();
        procedurePanel.setLayout(new FlowLayout(FlowLayout.CENTER,30,10));
        procedurePanel.setBackground(Color.white);
        
        // 이미지 width height 설정
	    int newWidth = 272; // 원하는 너비 설정
	    int newHeight = 120; // 원하는 높이 설정
        
        // 1학기 이미지
	    img_procedure1 = new JLabel();
        java.awt.Image procedureImage1 = new javax.swing.ImageIcon(getClass().getResource("/images/procedure1.png")).getImage();
	    java.awt.Image procedureResized1 = procedureImage1.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
	    img_procedure1.setIcon(new javax.swing.ImageIcon(procedureResized1));
	    procedurePanel.add(img_procedure1);
	    
	    img_procedure2 = new JLabel();
        java.awt.Image procedureImage2 = new javax.swing.ImageIcon(getClass().getResource("/images/procedure2.png")).getImage();
	    java.awt.Image procedureResized2 = procedureImage2.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
	    img_procedure2.setIcon(new javax.swing.ImageIcon(procedureResized2));
	    procedurePanel.add(img_procedure2);
	    
	    img_procedure3 = new JLabel();
        java.awt.Image procedureImage3 = new javax.swing.ImageIcon(getClass().getResource("/images/procedure3.png")).getImage();
	    java.awt.Image procedureResized3 = procedureImage3.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
	    img_procedure3.setIcon(new javax.swing.ImageIcon(procedureResized3));
	    procedurePanel.add(img_procedure3);
	    
	    img_procedure4 = new JLabel();
        java.awt.Image procedureImage4 = new javax.swing.ImageIcon(getClass().getResource("/images/procedure4.png")).getImage();
	    java.awt.Image procedureResized4 = procedureImage4.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
	    img_procedure4.setIcon(new javax.swing.ImageIcon(procedureResized4));
	    procedurePanel.add(img_procedure4);
	    
	    img_procedure5 = new JLabel();
        java.awt.Image procedureImage5 = new javax.swing.ImageIcon(getClass().getResource("/images/procedure5.png")).getImage();
	    java.awt.Image procedureResized5 = procedureImage5.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
	    img_procedure5.setIcon(new javax.swing.ImageIcon(procedureResized5));
	    procedurePanel.add(img_procedure5);
        
	    staticPanel.add(procedurePanel);
    }

    private void addIssuanceSection() {
        JPanel issuancePanel = new JPanel();
        issuancePanel.setBackground(Color.WHITE);
        issuancePanel.setLayout(new BorderLayout());
        issuancePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel issuanceTitle = new JLabel("등록금 고지서 발급 방법");
        issuanceTitle.setHorizontalAlignment(JLabel.LEFT);
        issuanceTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        issuanceTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        issuancePanel.add(issuanceTitle, BorderLayout.NORTH);

        JPanel issuanceContainer = createInfoBoxPanel(new String[]{
            "인터넷발급",
            "학교 홈페이지(e서비스)에 로그인하여 출력함."
        });
        issuancePanel.add(issuanceContainer, BorderLayout.CENTER);
        staticPanel.add(issuancePanel);
    }

    // 교육비납입증명서 구현
    private void addAdditionalInfoSection() {
    	// Title 추가
        JPanel additionalInfoPanel = new JPanel();
        additionalInfoPanel.setBackground(Color.WHITE);
        additionalInfoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        additionalInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel additionalInfoTitle = new JLabel("교육비납입증명서 발급 방법");
        additionalInfoTitle.setHorizontalAlignment(JLabel.LEFT);
        additionalInfoTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        additionalInfoTitle.setForeground(new Color(70,70,70));
        additionalInfoTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        additionalInfoPanel.add(additionalInfoTitle);
        
        staticPanel.add(additionalInfoPanel);
        
        // 이미지 추가
        // 이미지 추가 패널
        JPanel additionalInfoContainer = new JPanel();
        additionalInfoContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        additionalInfoContainer.setBackground(Color.white);
        
        // 교육비납입증명서 이미지추가
        int newWidth = 900;
        int newHeight = 261;
        img_additionalInfo1 = new JLabel();
        java.awt.Image additionalImage = new javax.swing.ImageIcon(getClass().getResource("/images/addtionalInfo.png")).getImage();
	    java.awt.Image additionalImageResized = additionalImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
	    img_additionalInfo1.setIcon(new javax.swing.ImageIcon(additionalImageResized));
	    additionalInfoContainer.add(img_additionalInfo1);
	    staticPanel.add(additionalInfoContainer);
        
        
    }

    private void addNotesSection() {
    	// 타이틀 추가
    	JPanel noteTitlePanel = new JPanel();
    	JLabel noteTitle = new JLabel();
    	noteTitle.setText("유의사항");
    	noteTitle.setFont(new Font("맑은 고딕", Font.BOLD, 20));
    	noteTitle.setForeground(new Color(70,70,70));
    	noteTitlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    	noteTitlePanel.add(noteTitle);
    	noteTitlePanel.setBackground(Color.white);
    	staticPanel.add(noteTitlePanel);
    	
    	// 등록금 납부 절차 이미지 추가
    	
    	
    	// 등록금 납부시기 Round Panel들을 담을 Panel
    	JPanel noteCenterContainer = new JPanel(); noteCenterContainer.setLayout(new FlowLayout(FlowLayout.CENTER)); noteCenterContainer.setBackground(Color.white);
        JPanel noteContentPanel = new JPanel();
        noteContentPanel.setLayout(new BorderLayout());
        noteContentPanel.setBackground(Color.white);
        
        // 이미지 width height 설정
	    int newWidth = 900; // 원하는 너비 설정
	    int newHeight = 135; // 원하는 높이 설정
        
        // 1학기 이미지
	    img_note1 = new JLabel();
        java.awt.Image noteImage1 = new javax.swing.ImageIcon(getClass().getResource("/images/note1.png")).getImage();
	    java.awt.Image noteResized1 = noteImage1.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
	    img_note1.setIcon(new javax.swing.ImageIcon(noteResized1));
	    noteContentPanel.add(img_note1,BorderLayout.NORTH);
	    
	    img_note2 = new JLabel();
        java.awt.Image noteImage2 = new javax.swing.ImageIcon(getClass().getResource("/images/note2.png")).getImage();
	    java.awt.Image noteResized2 = noteImage2.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
	    img_note2.setIcon(new javax.swing.ImageIcon(noteResized2));
	    noteContentPanel.add(img_note2,BorderLayout.CENTER);
	    
	    
	    newWidth = 900; // 원하는 너비 설정
	    newHeight = 540; // 원하는 높이 설정
	    img_note3 = new JLabel();
        java.awt.Image noteImage3 = new javax.swing.ImageIcon(getClass().getResource("/images/note3.png")).getImage();
	    java.awt.Image noteResized3 = noteImage3.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
	    img_note3.setIcon(new javax.swing.ImageIcon(noteResized3));
	    noteContentPanel.add(img_note3, BorderLayout.SOUTH);
        
	    noteCenterContainer.add(noteContentPanel);
	    staticPanel.add(noteCenterContainer);
    	
    }
    private JTextArea createNoteArea(String title, String content) {
        JTextArea area = new JTextArea(title + ":\n" + content);
        area.setBackground(Color.WHITE);
        area.setFont(new Font("SansSerif", Font.PLAIN, 16));
        area.setEditable(false);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setMargin(new Insets(10, 10, 10, 10));

        // 실선 테두리 추가
        area.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return area;
    }



    private void addTableSection(String title, String[] columnNames, Object[][] data, int rowHeight) {
        JPanel panel = createTablePanel(title, columnNames, data, rowHeight);
        staticPanel.add(panel);
    }
    
    private JPanel createTablePanel(String title, String[] columnNames, Object[][] data, int rowHeight) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JTable table = new JTable(data, columnNames);
        
        table.setShowGrid(false);  
        table.setRowHeight(rowHeight);
        table.setBackground(Color.WHITE);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        table.setFillsViewportHeight(true);
        table.getTableHeader().setBackground(Color.GRAY);  
        table.getTableHeader().setForeground(Color.WHITE);  
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBackground(new Color(220, 220, 220));  
        leftRenderer.setForeground(Color.BLACK);  
        table.setDefaultRenderer(Object.class, leftRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return panel;
    }
    
    private void addTableToPanel(JPanel container, String title, String[] columnNames, Object[][] data, int rowHeight) {
        JPanel panel = createTablePanel(title, columnNames, data, rowHeight);
        container.add(panel);
    }

    private void addBarGraphSection(String title, String[] items) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(JLabel.LEFT);  // Change alignment to left
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        titleLabel.setBackground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel barContainer = new JPanel();
        barContainer.setLayout(new BoxLayout(barContainer, BoxLayout.X_AXIS));
        barContainer.setBackground(Color.WHITE);
        Color barPanelColor = new Color(100, 149, 237);  // RoyalBlue color
        for (int i = 0; i < items.length; i++) {
            JPanel barPanel = new JPanel();
            barPanel.setLayout(new BorderLayout());
            barPanel.setBackground(barPanelColor);  // Set background to RoyalBlue
            barPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

            JLabel itemLabel = new JLabel(items[i], JLabel.CENTER);
            itemLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            itemLabel.setForeground(Color.black);  
            itemLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));  // Add padding for text

            barPanel.add(itemLabel, BorderLayout.CENTER);
            barContainer.add(barPanel);

            if (i < items.length - 1) {  // Add arrow between boxes, but not after the last one
                barContainer.add(Box.createRigidArea(new Dimension(5, 0)));  // Add spacing before arrow
                JLabel arrowLabel = new JLabel(">", JLabel.CENTER);
                arrowLabel.setFont(new Font("SansSerif", Font.BOLD, 24));  // Larger font for the arrow
                arrowLabel.setForeground(barPanelColor);  // Set color to RoyalBlue
                barContainer.add(arrowLabel);
                barContainer.add(Box.createRigidArea(new Dimension(5, 0)));  // Add spacing after arrow
            }
        }
        panel.add(barContainer, BorderLayout.CENTER);

        staticPanel.add(panel);
    }
    private JPanel createInfoBoxPanel(String[] texts) {
        JPanel container = new JPanel();
        container.setLayout(new GridLayout(1, 2, 10, 0));
        Color barPanelColor = new Color(200, 200, 200);
        container.setBackground(Color.WHITE);

        for (String text : texts) {
            JPanel boxPanel = new JPanel(new BorderLayout());
            boxPanel.setBackground(barPanelColor);
            JLabel boxLabel = new JLabel(text, JLabel.CENTER);
            boxLabel.setForeground(Color.black);
            boxLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            boxPanel.add(boxLabel, BorderLayout.CENTER);
            container.add(boxPanel);
        }

        return container;
    }
}