package Kisok_Scholarship;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TuitionPayment extends JPanel {
    private JPanel staticPanel;

    public TuitionPayment() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("ㅡ 등록금납부 ㅡ ");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        initStaticPanel();
        JScrollPane mainScrollPane = new JScrollPane(staticPanel);
        add(mainScrollPane, BorderLayout.CENTER);
    }

    private void initStaticPanel() {
        staticPanel = new JPanel();
        staticPanel.setLayout(new BoxLayout(staticPanel, BoxLayout.Y_AXIS));
        // Remove or comment out the next line to make the background transparent
        // staticPanel.setBackground(Color.WHITE);
        staticPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // 시기 Section
        addPeriodSection();

        // 절차 Section
        addProcedureSection();

        // 등록금 고지서 발급 방법 Section
        addIssuanceSection();

        // 교육비납입증명서 발급 방법 Section
        addAdditionalInfoSection();

        // 유의사항 Section
        addNotesSection();
    }

    private void addPeriodSection() {
        JPanel periodPanel = new JPanel(new BorderLayout());
        periodPanel.setOpaque(false); // Make the panel transparent

        JLabel periodLabel = new JLabel("시기");
        periodLabel.setHorizontalAlignment(JLabel.LEFT); // Left alignment
        periodLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        // Adjust the bottom padding to reduce the gap between the label and the table
        periodLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10)); 
        periodLabel.setOpaque(false); // Make the label background transparent

        periodPanel.add(periodLabel, BorderLayout.WEST);
        staticPanel.add(periodPanel);

        JPanel semesterPanel = new JPanel(new GridLayout(1, 2));
        // Removing the title "1학기"
        addTableToPanel(semesterPanel, null, new String[]{"학기", "기간"}, new Object[][]{
            {"1학기", "2월 중순경 (학교에서 지정한 기간)"}
        }, 20);
        // Removing the title "2학기"
        addTableToPanel(semesterPanel, null, new String[]{"학기", "기간"}, new Object[][]{
            {"2학기", "8월 중순경 (학교에서 지정한 기간)"}
        }, 20);
        staticPanel.add(semesterPanel);
    }




    private void addProcedureSection() {
        addBarGraphSection("절차", new String[]{
            "등록금 고지서를 e-서비스에서 출력",
            "은행 납부"
        });
    }

    private void addIssuanceSection() {
        JPanel issuancePanel = new JPanel();
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

    private void addAdditionalInfoSection() {
        JPanel additionalInfoPanel = new JPanel();
        additionalInfoPanel.setLayout(new BorderLayout());
        additionalInfoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel additionalInfoTitle = new JLabel("교육비납입증명서 발급 방법");
        additionalInfoTitle.setHorizontalAlignment(JLabel.LEFT);
        additionalInfoTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        additionalInfoTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        additionalInfoPanel.add(additionalInfoTitle, BorderLayout.NORTH);

        JPanel additionalInfoContainer = createInfoBoxPanel(new String[]{
            "학교 홈페이지 e서비스에 로그인 하여 등록 → 교육비납입증명서 에서 출력함. (단, 외부기관에 제출할 경우 컬러프린터를 이용바람)",
            "교육비납입내역 중 장학금은 학교로부터 받은 모든 장학금이며, 환불금은 자퇴자 환불금임."
        });
        additionalInfoPanel.add(additionalInfoContainer, BorderLayout.CENTER);
        staticPanel.add(additionalInfoPanel);
    }

    private void addNotesSection() {
        JLabel notesTitle = new JLabel("유의사항");
        notesTitle.setHorizontalAlignment(JLabel.LEFT);
        notesTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        notesTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel notesTitlePanel = new JPanel(new BorderLayout());
        notesTitlePanel.add(notesTitle, BorderLayout.WEST);
        staticPanel.add(notesTitlePanel);

        // 신입생과 재학생을 같은 열에 정렬
        JPanel studentsPanel = new JPanel(new GridLayout(1, 2));
        
        JTextArea freshmanArea = createNoteArea("신입생", "입학이 허가된 자는 지정된 장소와 기일 내에 등록을 하여야 하며, 지정된 기일 내에 등록을 하지 않을 때는 입학이 자동 취소됨.");
        JTextArea currentStudentArea = createNoteArea("재학생", "지정한 등록기간 내에 등록을 하여야 하며, 정당한 이유 없이 기일 내에 등록을 하지 않을 때는 미등록 제적 처리됨.");
        
        studentsPanel.add(freshmanArea);
        studentsPanel.add(currentStudentArea);
        staticPanel.add(studentsPanel);

        // 복학생 부분
        JTextArea returningStudentArea = createNoteArea(
        	    "복학생", 
        	    "휴학 당시 해당 학기의 등록금을 납부하고 학기 개시일 기준 2주를 경과한 날부터 학기 종료 전에 휴학한 자 중 군입대 휴학을 제외한 휴학자는 복학시 다음 각 호와 같은 추가등록금을 납부해야 한다.\n\n" +
        	    "- 총수업일수 3/4 경과 전 휴학자: 휴학 당시 등록금을 해당학기의 총 수업 주수로 나눈 다음 휴학 당시의 휴학일이 속하는 경과 주수를 곱한 금액으로 하되 3주차부터 경과 주수를 적용함.\n\n" +
        	    "- 총수업일수 3/4 이상 경과 후 휴학자 : 복학시 해당학기 등록금 전액\n\n" +
        	    "- 각호의 기준일은 휴학원을 학교에 접수한 날짜를 기준으로 한다.\n\n" +
        	    "- 총수업일수의 3/4이 경과하기 전에 입영한 군입대 휴학자의 등록금은 복학 후 해당학기 등록금으로 대체한다.\n\n" +
        	    "단, 총수업일수의 3/4이상이 경과한 다음 입영한 군입대 휴학자는 취득학점에 관계없이 해당학기가 경과된 것으로 간주하여 등록금 대체를 인정하지 아니한다.\n\n" +
        	    "- 군입대 휴학 후 귀향조치 처분을 받은 자가 복학할 경우는 추가등록금을 납부하지 않는다."
        	);

        staticPanel.add(returningStudentArea);
    }


    private JTextArea createNoteArea(String title, String content) {
        JTextArea area = new JTextArea(title + ":\n" + content);
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
        JTable table = new JTable(data, columnNames);
        
        table.setShowGrid(false);  
        table.setRowHeight(rowHeight);
        table.setBackground(new Color(220, 220, 220)); 
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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setHorizontalAlignment(JLabel.LEFT);  // Change alignment to left
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel barContainer = new JPanel();
        barContainer.setLayout(new BoxLayout(barContainer, BoxLayout.X_AXIS));
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