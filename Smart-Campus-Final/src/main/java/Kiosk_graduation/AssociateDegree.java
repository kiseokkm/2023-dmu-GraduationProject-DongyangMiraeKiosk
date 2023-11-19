package Kiosk_graduation;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class AssociateDegree extends JPanel {
	private JScrollPane scrollPane;
    public AssociateDegree() {
        initUI();
    }
    private void initUI() {
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        addTextPaneSection(mainPanel);
        addDegreeSection(mainPanel);
        JLabel lblGraduationCredits = new JLabel(">> 졸업이수 학점");
        lblGraduationCredits.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblGraduationCredits.setAlignmentX(LEFT_ALIGNMENT);
        mainPanel.add(lblGraduationCredits);
        addGraduationCreditSection(mainPanel);   
        JLabel lblMajorMinimumCredits = new JLabel(">> 전공최저이수 학점(편입생의 전공최저이수학점은 [별표2] 참조)");
        lblMajorMinimumCredits.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblMajorMinimumCredits.setAlignmentX(LEFT_ALIGNMENT);
        mainPanel.add(lblMajorMinimumCredits);
        addGraduationCreditSection1(mainPanel);
        addMajorMinimumCreditRangeTable(mainPanel);
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); 
        add(scrollPane, BorderLayout.CENTER);     
        addTransferStudentCreditTable(mainPanel);
    }
    private void addTextPaneSection(JPanel container) {
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setMargin(new Insets(5, 0, 0, 0));
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        SimpleAttributeSet blueBackground = new SimpleAttributeSet();
        StyleConstants.setBackground(blueBackground, new Color(100, 149, 237));  
        try {
            doc.insertString(doc.getLength(), "2년제 학과는 4학기 이상, 3년제 학과는 6학기 이상 재학한 학생이 졸업요건을 충족하면 졸업할 수 있습니다.", blueBackground);
        } catch (Exception e) {
            e.printStackTrace();
        }
        textPane.setFont(new Font("SansSerif", Font.BOLD, 18));
        textPane.setOpaque(false);
        container.add(textPane);
    }
    private void addDegreeSection(JPanel container) {
        String[] columnNames = {"종별", "해당 학과"};
        Object[][] data = {
            {"공학 전문학사", "기계공학과, 기계설계공학과, 자동화공학과, 로봇공학과, 전기공학과, 정보전자공학과, 반도체전자공학과, 정보통신공학과, 컴퓨터정보공학과, 컴퓨터소프트웨어공학과, 인공지능소프트웨어학과, 생명화학공학과, 바이오융합공학과, 건축과, 실내건축디자인과, 시각디자인과, 식품공학과, 실내환경디자인과, 시각정보디자인과"},
            {"경영 전문학사", "경영학과, 세무회계학과, 유통마케팅학과, 경영정보학과, 빅데이터경영과"},
            {"관광 전문학사", "호텔관광학과"}
        };
        JTable table = new JTable(data, columnNames);
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
        container.add(scrollPane);
    }
    private void addGraduationCreditSection(JPanel container) {
        String[] columnNames = {"구분", "2022년 2월 및 8월 졸업대상자", "2023년 2월 이후 졸업대상자"};
        Object[][] data = {
            {"2년제", "75", "75"},
            {"3년제", "120", "110"}
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
    private void addGraduationCreditSection1(JPanel container) {
        String[] columnNames = {"구분", "2022년 2월 및 8월 졸업대상자", "2023년 2월 이후 졸업대상자"};
        Object[][] data = {
            {"2년제", "52", "52"},
            {"3년제", "[별표 1 ]", "78"}
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
    private void addMajorMinimumCreditRangeTable(JPanel container) {
        JLabel lblMajorCreditRange = new JLabel("[별표 1] 학과별 전공최저이수학점 범위 지정표");
        lblMajorCreditRange.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblMajorCreditRange.setHorizontalAlignment(SwingConstants.LEFT);
        container.add(lblMajorCreditRange);
        String[] columnNames = {"적용년도", "적용학점", "해당 학과명"};
        Object[][] data = {
            {"2006, 2007학년도 입학자", "54학점", "전기시스템과, 로봇제어과, 모바일인터넷과, 인터넷비즈니스과, 경영과, 유통마케팅과, 관광경영과"},
            {"", "60학점", "기계과, 기계설계과, 응용화학과, 정보전자과, 반도체전자과, 시각디자인과"},
            {"", "81학점", "무선정보통신과, 네트워크정보통신과"},
            {"", "90학점", "자동화시스템과, 건축과, 실내건축과, 소프트웨어정보과"},
            {"2008학년도 입학자", "54학점", "전기시스템과, 로봇제어과, 모바일인터넷과, 인터넷비즈니스과, 경영과, 유통마케팅과, 관광경영과"},
            {"", "60학점", "기계과, 기계설계과, 응용화학과, 정보전자과, 반도체전자과, 시각디자인과"},
            {"", "90학점", "자동화시스템과, 무선정보통신과, 네트워크정보통신과, 소프트웨어정보과, 건축과, 실내건축과"},
            {"2009학년도 입학자", "60학점", "기계과, 기계설계과, 전기시스템과, 생명화공과, 정보전자과, 반도체전자과, 인터넷정보과, 인터넷비즈니스과, 디지털경영과, 세무회계과, 유통마케팅과, 관광경영과, 시각디자인과"},
            {"", "90학점", "자동화시스템과, 로봇시스템과, 무선정보통신과, 네트워크정보통신과, 소프트웨어정보과, 건축과, 실내디자인과"},
            {"2010학년도 이후 입학자", "60학점", "2년제 학과"},
            {"", "90학점", "3년제 학과"}
        };          
        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(1600, 400));
        table.setRowHeight(40);
        table.setBackground(new Color(220, 220, 220));
        table.getTableHeader().setBackground(Color.GRAY);
        table.getTableHeader().setForeground(Color.WHITE);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200); 
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(1300);
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBackground(new Color(220, 220, 220));
        leftRenderer.setForeground(Color.BLACK);
        table.setDefaultRenderer(Object.class, leftRenderer);
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
        container.add(scrollPane);
    }
    private void addTransferStudentCreditTable(JPanel container) {
        JLabel lblTransferCreditRange = new JLabel("[별표 2] 편입학생의 전공최저이수학점 및 졸업학점 범위 지정표");
        lblTransferCreditRange.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTransferCreditRange.setHorizontalAlignment(SwingConstants.LEFT);
        container.add(lblTransferCreditRange);
        String[] columnNames = {
            "편입연도", "수업연한", "편입학 시기", "전공최저 이수학점", "인정학점", "졸업학점", "비고"
        };
        Object[][] data = {
            {"2020학년도", "2년", "2학년 1학기 편입학생", "30학점", "36~40학점", "80학점", "편입학생에게 적용되는 교육과정 중 편입학 학기부터 편성된 전공필수 교과목 포함"},
            {"", "3년", "2학년 1학기 편입학생", "60학점", "36~40학점", "120학점", ""},
            {"", "", "3학년 1학기 편입학생", "30학점", "72~80학점", "120학점", ""},
            {"2021학년도", "2년", "2학년 1학기 편입학생", "26학점", "36학점", "75학점", ""},
            {"", "3년", "2학년 1학기 편입학생", "52학점", "36학점", "110학점", ""},
            {"", "", "3학년 1학기 편입학생", "30학점", "72~80학점", "120학점", ""},
            {"2022학년도 이후", "2년", "2학년 1학기 편입학생", "26학점", "36학점", "75학점", ""},
            {"", "3년", "2학년 1학기 편입학생", "52학점", "36학점", "110학점", ""},
            {"", "", "3학년 1학기 편입학생", "26학점", "72학점", "110학점", ""}
        };
        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(1600, 400));
        table.setRowHeight(40);
        table.setBackground(new Color(220, 220, 220));
        table.getTableHeader().setBackground(Color.GRAY);
        table.getTableHeader().setForeground(Color.WHITE);   
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);  // 편입연도
        columnModel.getColumn(1).setPreferredWidth(100);  // 수업연한
        columnModel.getColumn(2).setPreferredWidth(250);  // 편입학 시기
        columnModel.getColumn(3).setPreferredWidth(150);  // 전공최저 이수학점
        columnModel.getColumn(4).setPreferredWidth(150);  // 인정학점
        columnModel.getColumn(5).setPreferredWidth(150);  // 졸업학점
        columnModel.getColumn(6).setPreferredWidth(600);  // 비고
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBackground(new Color(220, 220, 220));
        leftRenderer.setForeground(Color.BLACK);
        table.setDefaultRenderer(Object.class, leftRenderer);
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
        int totalRowHeight = 0;
        for (int row = 0; row < table.getRowCount(); row++) {
            totalRowHeight += table.getRowHeight(row);
        }
        table.setPreferredScrollableViewportSize(new Dimension(1500, totalRowHeight));
        table.setDefaultRenderer(String.class, new TextAreaRenderer());
        JScrollPane scrollPane = new JScrollPane(table);
        container.add(scrollPane);
    }
}