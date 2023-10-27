package Scholarship;

import javax.swing.*;
import java.awt.*;

public class ScholarshipOnAndOffCampus extends JPanel {
    public ScholarshipOnAndOffCampus() {
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("교내외장학금");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        add(titleLabel, BorderLayout.NORTH);
        addApplicationProcedureSection();
        addScholarshipTableSection();
    }

    private void addApplicationProcedureSection() {
        JPanel procedurePanel = new JPanel();
        procedurePanel.setLayout(new BoxLayout(procedurePanel, BoxLayout.Y_AXIS));
        JLabel procedureTitle = new JLabel("신청절차");
        procedureTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        procedureTitle.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        procedurePanel.add(procedureTitle);
        JPanel stepsPanel = new JPanel();
        stepsPanel.setLayout(new BoxLayout(stepsPanel, BoxLayout.X_AXIS));
        String[] steps = {
            "01 학생서비스센터", "장학금 신청 안내\n(홈페이지 공지 및 친구톡 발송 등)",
            "02 본인", "홈페이지 e-서비스에서 신청(서류는 학부(과)에 제출)",
            "03 각 학부(과)", "장학금 관련 서류 수합하여 장학부서에 제출",
            "04 학생서비스센터", "장학부서 수합, 심사 및 지급"
        };
        for (int i = 0; i < steps.length; i += 2) {
            JPanel stepPanel = new JPanel();
            stepPanel.setLayout(new BoxLayout(stepPanel, BoxLayout.Y_AXIS));
            stepPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            stepPanel.setMaximumSize(new Dimension(280, 105));
            JPanel stepNumberPanel = new JPanel(new BorderLayout());
            stepNumberPanel.setBackground(Color.CYAN);
            stepNumberPanel.setMaximumSize(new Dimension(400, 50));
            JLabel stepNumber = new JLabel(steps[i], JLabel.CENTER);
            stepNumber.setFont(new Font("SansSerif", Font.BOLD, 20));
            stepNumber.setBorder(BorderFactory.createEmptyBorder(5, 10, 2, 10));
            stepNumberPanel.add(stepNumber, BorderLayout.CENTER);
            JPanel stepDescriptionPanel = new JPanel(new BorderLayout());
            JLabel stepDescription = new JLabel("<html>" + steps[i + 1].replace("\n", "<br>") + "</html>", JLabel.CENTER);
            stepDescription.setFont(new Font("SansSerif", Font.PLAIN, 16));
            stepDescription.setBorder(BorderFactory.createEmptyBorder(2, 10, 5, 10));
            stepDescriptionPanel.add(stepDescription, BorderLayout.CENTER);
            stepPanel.add(stepNumberPanel);
            stepPanel.add(stepDescriptionPanel);
            stepsPanel.add(stepPanel);
            if (i < steps.length - 2) {
                JButton arrowButton = new JButton(">");
                arrowButton.setFont(new Font("SansSerif", Font.BOLD, 24));
                arrowButton.setForeground(Color.CYAN);
                arrowButton.setBorderPainted(false);
                arrowButton.setContentAreaFilled(false);
                stepsPanel.add(arrowButton);
            }
        }
        procedurePanel.add(stepsPanel);
        add(procedurePanel, BorderLayout.NORTH);
    }

    private void addScholarshipTableSection() {
        JPanel scholarshipPanel = new JPanel(new BorderLayout());  // 새로운 패널 생성

        JLabel scholarshipTitle = new JLabel("교내 장학금");
        scholarshipTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        scholarshipTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        scholarshipTitle.setHorizontalAlignment(JLabel.CENTER);
        scholarshipPanel.add(scholarshipTitle, BorderLayout.NORTH);  // 여기서 NORTH 에 추가

        String[] columnNames = {"장학금 종류", "수혜자격", "장학금액 및 비율", "세부사항"};
        Object[][] data = {
            {"성적우수장학금", "학과별 재학생 중 직전학기 성적이 우수한 상위 10%에 해당하는 자", "가) 1등 : 등록금의 100%\n나) 2등 : 등록금의 80%\n다) 3등 : 등록금의 60%\n라) 4등 : 등록금의 50%\n마) 5등 이하 : 등록금의 40%", "- 1학년 2학기부터 등록금 감면 처리\n- 장학규칙 제18조(동점자 처리)에 따라 동점일 경우 해당 순위의 장학금을 합산하여 균등 배분할 수 있음\n- 교내장학금 운영 기준[내규] 제2조(운영기준)에 따라 재학생수가 10명 미만일 경우 '등수에 따른 지급비율x재학생수/20명' 과 '30%' 중 큰 값을 적용함"},
            // ... [다른 데이터 행들] ...
        };
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scholarshipPanel.add(scrollPane, BorderLayout.CENTER);  // 여기서 CENTER 에 추가

        add(scholarshipPanel, BorderLayout.CENTER);  // 전체 패널을 CENTER 에 추가
    }

}
