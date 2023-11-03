package Kisok_Scholarship;

import javax.swing.*;
import javax.swing.table.JTableHeader;

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
        	    {"성적우수장학금", "학과별 재학생 중 직전학기 성적이 우수한 상위 10%에 해당하는 자", "가) 1등 : 등록금의 100%\n나) 2등 : 등록금의 80%\n다) 3등 : 등록금의 60%\n라) 4등 : 등록금의 50%\n마) 5등 이하 : 등록금의 40%",
        	        "- 1학년 2학기부터 등록금 감면 처리\n- 장학규칙 제18조(동점자 처리)에 따라 동점일 경우 해당 순위의 장학금을 합산하여 균등 배분할 수 있음\n- 교내장학금 운영 기준[내규] 제2조(운영기준)에 따라 재학생수가 10명 미만일 경우 '등수에 따른 지급비율x재학생수/20명' 과 '30%' 중 큰 값을 적용함"},
        	    {"봉사장학금", "학생 자치기구 임원 등","-", "재학 중 직전학기 성적이 2.5 이상인 경우 지급"},
        	    {"보훈장학금", "국가유공자 및 직계자녀인 자", "등록금의 100%", "자녀 : 직전학기 성적 70점 이상인 자\n본인 : 성적 무관 지급"},
        	    {"동직자장학금", "본 대학에 재직하는 교직원 본인 또는 배우자 및 직계자녀인 자", "등록금의 100%", "재학 중 직전학기 성적이 2.0 이상인 경우 지급"},
        	    {"근로장학금", "각 학부(과) 및 행정부서에서 근로를 하는 자","-", "재학 중 직전학기 성적이 2.0 이상인 경우 지급",},
        	    {"협약장학금", "고교와 대학, 산업체와 대학간 양자 협약 및 3자 협약 체결된 기관의 학생인 자", "등록금의 30%", "재학 중 직전학기 성적이 2.0 이상인 경우 지급"},
        	    {"미래장학금", "가정형편이 어려운 학생 중 학자금지원구간(0구간~9구간) 기준에 맞는 자", "학자금지원구간 중 0구간 ~ 9구간 별 차등지급", "재학 중 직전학기 성적이 2.0 이상인 경우 지급"},
        	    {"한가족장학금", "부모, 형제자매 중 2명 이상이 동시에 우리 대학에 재학 중인 자", "등록금의 30%", "재학 중 직전학기 성적이 2.0 이상인 경우 지급"},
        	    {"북한이탈주민장학금", "북한이탈주민인 자", "등록금의 100%", "재학 중 직전학기 성적이 70점 이상인 자"},
        	    {"기관재직자 장학금", "군인, 공무원 및 사립학교 교직원인 학생인 자", "등록금의 30%", "재학 중 직전학기 성적이 2.0 이상인 경우 지급"},
        	    {"디딤돌장학금", "장애정도가 중증인 학생인 자", "학기당 150만원", "재학 중 직전학기 성적이 2.0 이상인 경우 지급"},
        	    {"홍보도우미 장학금", "학교 대내외 행사에 안내와 홍보를 하는 자", "-", "재학 중 직전학기 성적이 2.0 이상인 경우 지급"},
        	    {"전공심화과정장학금", "전공심화과정 학생인 자", "등록금의 30%", "신입생 : 전문학사 성적 평균 2.5 이상시 지급\n재학생 : 직전학기 성적 2.5 이상시 지급"},
        	    {"다문화가족 장학금", "부모 중 1명 이상이 외국 국적이거나 귀화한 가정의 자녀", "학기당 150만원", "재학 중 직전학기 성적이 2.0 이상인 경우 지급"},
        	    {"산업체위탁생장학금", "산업체위탁과정 신입생인 자", "등록금의 20%", "입학학기에 한함"},
        	    {"튜터링장학금", "학습지원 프로그램의 튜터에게 지급", "-", "30시간 이상 3인이상의 튜티 지도"},
        	    {"자비유학생 장학금", "자비 유학생인 자", "-", "재학 중 직전학기 성적이 1.5 이상"},
        	    {"마일리지 장학금", "학생활동을 마일리지 포인트로 적립하여 학생에게 지급", "-", "대학홈페이지에 공지 후 진행"},
        	    {"월드스타 장학금", "국어, 수학, 영어, 탐구영역중 2개가 1등급인 신입생에게 지급", "전학년 등록금 전액", "재학 중 직전학기 성적이 3.5 이상인 경우 지급"},
        	    {"전체수석 장학금", "일반(고) 전형 및 특성화고 전형 신입생 중 전형별 최고 성적인 신입생에게 지급", "전학년 등록금 전액", "재학 중 직전학기 성적이 30% 이내인 경우 지급"},
        	    {"학과수석 장학금", "정원내 일반(고)전형 및 특성화고 전형 신입생 중 모집단위별 최고성적인 신입생에게 지급", "입학학기 등록금 전액", "-"},
        	    {"아카데미스타장학금", "학과별 지원인원이 가장 많은 고교에서 최고 성적인 신입생에게 지급", "입학학기 등록금 전액", "-"},
        	    {"터닝스타 장학금", "정원외 전문대학 이상 학력자 전형에 입학성적이 상위 30% 이내인 신입생에게 지급", "입학학기 등록금 50%", "전문대학 졸업자 50%, 4년제대학 졸업자 50%로 지급하되 입학인원에 따라 변경될 수 있음"},
        	    {"성적향상 장학금", "직전학기 대비 성적율 향상시킨 자", "한학기 100만원", "직전학기 2.0 이하인 학생이 해당학기 3.5 이상의 성적 취득 시 지급"}
        	};
        JTable table = new JTable(data, columnNames);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(255, 228, 196)); // 살색 배경
        header.setFont(new Font("SansSerif", Font.BOLD, 12)); // 글씨체를 두껍게

        JScrollPane scrollPane = new JScrollPane(table);
        scholarshipPanel.add(scrollPane, BorderLayout.CENTER);  // 여기서 CENTER 에 추가
        addFooterInfoSection(scholarshipPanel); 

        add(scholarshipPanel, BorderLayout.CENTER);   // 전체 패널을 CENTER 에 추가
    }
    private void addFooterInfoSection(JPanel scholarshipPanel) {
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.Y_AXIS));

        String[] infoTexts = {
            ">> 장학금 이중수혜는 가능하나, 등록금 범위 내에서 수혜받을 수 있습니다.",
            "      (단, 근로, 튜터링, 봉사, 홍보도우미, 마일리지장학금은 등록금 범위를 초과하여 지급이 가능함)",
            ">> 학자금 대출이 있는 경우 장학금은 학자금 대출 상환처리로 지급합니다.",
            ">> 기타 자세한 사항은 '장학 규칙' 및 '교내장학금 운영 기준[내규]'에 의함"
        };

        for (String text : infoTexts) {
            JLabel infoLabel = new JLabel(text);
            infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
            footerPanel.add(infoLabel);
        }
        JLabel externalScholarshipTitle = new JLabel("교외 장학금");
        externalScholarshipTitle.setFont(new Font("SansSerif", Font.BOLD, 25));
        externalScholarshipTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 5, 0));  // 상단 여백을 추가하여 아래로 내림
        footerPanel.add(externalScholarshipTitle);

        JLabel externalScholarshipInfo = new JLabel(">> 교외장학금 신청 및 안내는 해당 건 발생시 홈페이지 공지사항에 수시로 게시함");
        externalScholarshipInfo.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footerPanel.add(externalScholarshipInfo);

        scholarshipPanel.add(footerPanel, BorderLayout.SOUTH); 

        scholarshipPanel.add(footerPanel, BorderLayout.SOUTH);  // 여기서 SOUTH 에 추가
    }

}