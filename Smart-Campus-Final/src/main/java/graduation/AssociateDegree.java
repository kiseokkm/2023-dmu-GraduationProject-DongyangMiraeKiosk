package graduation;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class AssociateDegree extends JPanel {

    private JTextPane textPane;

    public AssociateDegree() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Title "전문학사"
        JLabel lblTitle = new JLabel("ㅡ 전문학사 ㅡ");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 30)); // Set font size
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER); // Center the label
        add(lblTitle, BorderLayout.NORTH);

        // Create a panel to hold the textPane and the table
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setMargin(new Insets(40, 0, 40, 0));

        // Center-align the text
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        // Highlight the specific text with blue background
        SimpleAttributeSet blueBackground = new SimpleAttributeSet();
        StyleConstants.setBackground(blueBackground, new Color(100, 149, 237));  // Set blue background
        try {
            doc.insertString(doc.getLength(), "2년제 학과는 4학기 이상, 3년제 학과는 6학기 이상 재학한 학생이 졸업요건을 충족하면 졸업할 수 있습니다.", blueBackground);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set the font size and style for textPane
        textPane.setFont(new Font("SansSerif", Font.BOLD, 18));
        textPane.setOpaque(false);

        JScrollPane textScrollPane = new JScrollPane(textPane);
        textScrollPane.setOpaque(false);
        textScrollPane.getViewport().setOpaque(false);
        textScrollPane.setBorder(null);
        textScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));  // Hide vertical scroll bar

        centerPanel.add(textScrollPane);

        // Create the table for 학위종별
        String[][] data = {
            {"공학 전문학사", "기계공학과, 기계설계공학과, 자동화공학과, 로봇공학과, 전기공학과, 정보전자공학과, 반도체전자공학과, 정보통신공학과, 컴퓨터정보공학과, 컴퓨터소프트웨어공학과, 인공지능소프트웨어학과, 생명화학공학과, 바이오융합공학과, 건축과, 실내건축디자인과, 시각디자인과, 식품공학과, 실내환경디자인과, 시각정보디자인과"},
            {"경영 전문학사", "경영학과, 세무회계학과, 유통마케팅학과, 경영정보학과, 빅데이터경영과"},
            {"관광 전문학사", "호텔관광학과"}
        };
        String[] columnNames = {"종별", "해당 학과"};

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);

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

        table.setDefaultRenderer(String.class, new TextAreaRenderer()); // Use custom renderer for String data

        // Adjust the table height to fit the contents
        table.setFillsViewportHeight(true);

        JScrollPane tableScrollPane = new JScrollPane(table);
        centerPanel.add(tableScrollPane);

        add(centerPanel, BorderLayout.CENTER);
    }
}

