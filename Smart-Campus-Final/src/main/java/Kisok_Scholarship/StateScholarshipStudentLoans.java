package Kisok_Scholarship;

import javax.swing.*;
import java.awt.*;

public class StateScholarshipStudentLoans extends JPanel {
    public StateScholarshipStudentLoans() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("ㅡ 국가장학금/학자금 대출 ㅡ ");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        contentPanel.add(createLeftSection());
        contentPanel.add(createRightSection());

        add(contentPanel, BorderLayout.CENTER);
    }
    private JPanel createLeftSection() {
        JPanel panel = createItemWithStyledContent("신청기간", "<html>" +
                "<div style='font-size: 16px; margin-bottom: 15px;'>>> 1학기 12월 ~ 3월 / 2학기 6(7)월 ~ 9월</div>" +
                "<div style='font-size: 16px; margin-bottom: 15px;'>※ 시행기관 국가장학사업 시행계획에 따라 변동될 수 있음</div>" +
                "<div style='font-size: 16px; margin-bottom: 15px;'>※ 학자금 대출 시행계획에 따라 변동될 수 있음</div>" +
                "</html>");
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)); 
        return panel;
    }
    private JPanel createRightSection() {
        return createItemWithStyledContent("신청방법", "<html>" +
                "<div style='font-size: 16px; margin-bottom: 15px;'>>> 한국장학재단 : 한국장학재단 홈페이지 (http://www.kosaf.go.kr) 온라인 신청</div>" +
                "<div style='font-size: 16px; margin-bottom: 15px;'>※ 대출일정 및 대출절차 등 자세한 내용은 한국장학재단 홈페이지 참조</div>" +
                "<div style='font-size: 16px; margin-bottom: 15px;'>※ 한국장학재단 콜센터 : ☎ 1599-2000</div>" +
                "</html>");
    }
    private JPanel createItemWithStyledContent(String title, String styledContent) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 5, 10, 5));
        itemPanel.add(titleLabel, BorderLayout.NORTH);

        JTextPane contentPane = new JTextPane();
        contentPane.setContentType("text/html");
        contentPane.setText(styledContent);
        contentPane.setEditable(false);
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        itemPanel.add(contentPane, BorderLayout.CENTER);

        return itemPanel;
    }
    private JPanel createItem(String title, String content) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BorderLayout());
        itemPanel.setBackground(Color.WHITE);

        if (title.isEmpty()) {
            JTextPane contentPane = new JTextPane();
            contentPane.setContentType("text/html");
            String styledContent = "<html>" +
                    "<div style='font-size: 16px; font-weight: bold; margin-bottom: 15px;'>신청기간</div>" +
                    "<div style='font-size: 16px; margin-bottom: 15px;'>>> 1학기 12월 ~ 3월 / 2학기 6(7)월 ~ 9월</div>" +
                    "<div style='font-size: 16px; margin-bottom: 15px;'>※ 시행기관 국가장학사업 시행계획에 따라 변동될 수 있음</div>" +
                    "<div style='font-size: 16px; margin-top: 15px;'>※ 학자금 대출 시행계획에 따라 변동될 수 있음</div>" +
                    "</html>";
            contentPane.setText(styledContent);
            contentPane.setEditable(false);
            contentPane.setBackground(Color.WHITE);
            itemPanel.add(contentPane, BorderLayout.CENTER);
        } else {
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            itemPanel.add(titleLabel, BorderLayout.NORTH);

            JTextArea contentArea = new JTextArea(content);
            contentArea.setFont(new Font("SansSerif", Font.PLAIN, 16));
            contentArea.setWrapStyleWord(true);
            contentArea.setLineWrap(true);
            contentArea.setOpaque(false);
            contentArea.setEditable(false);
            contentArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            itemPanel.add(contentArea, BorderLayout.CENTER);
        }

        return itemPanel;
    }
}
