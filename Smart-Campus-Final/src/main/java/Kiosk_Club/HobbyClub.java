package Kiosk_Club;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.*;

public class HobbyClub extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> clubTypeComboBox;
    private CardLayout cardLayout;
    private JPanel cardsPanel;

    public HobbyClub() {
        setLayout(new BorderLayout());

        // 상단 패널 생성
        JPanel topPanel = new JPanel(new BorderLayout());

        // JComboBox와 그 옆의 라벨을 위한 패널
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // JComboBox 초기화
        clubTypeComboBox = new JComboBox<>(new String[]{"취미동아리", "전공동아리"});
        clubTypeComboBox.addActionListener(e -> switchPanel((String) clubTypeComboBox.getSelectedItem()));
        comboBoxPanel.add(new JLabel("동아리 선택:"));  // 라벨 추가
        comboBoxPanel.add(clubTypeComboBox);

        topPanel.add(comboBoxPanel, BorderLayout.NORTH);  // NORTH에 JComboBox 패널 추가

        // 제목 라벨 생성
        JLabel titleLabel = new JLabel("취미동아리/ C.Ⅰ. Lab , 전공동아리/(PDLab)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));  // 폰트 설정
        topPanel.add(titleLabel, BorderLayout.CENTER);  // CENTER에 제목 라벨 추가

        add(topPanel, BorderLayout.NORTH);

        // 카드 레이아웃을 사용하여 패널을 토글합니다.
        cardsPanel = new JPanel(cardLayout = new CardLayout());
        cardsPanel.add(createHobbyClubPanel(), "취미동아리");

        JPanel majorClubPanel = MajorClub.createMajorClubPanel();
        cardsPanel.add(majorClubPanel, "전공동아리");

        add(cardsPanel, BorderLayout.CENTER);
    }


    private JPanel createHobbyClubPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 설명 라벨 생성
        String description = "<html><div style='text-align: center; border: 2px solid black; padding: 10px;'>C.Ⅰ. Lab 이란<br>"
        	    + "우리대학의 핵심역량인 인성, 리더십, 공동체정신, 협업능력 등의 함양을 통해 미래 인재를 양성하기 위한 <u>핵심역량 향상 동아리"
        	    + "(이하 C.Ⅰ. Lab : Competency Improving Lab)</u>를 선발하여 지원하고 있음<br>"
        	    + "스터디, 취미, 문화, 봉사 등 다양한 활동을 목적으로 구성되어 있는 취미동아리의 신청을 받아 C.Ⅰ. Lab을 선정함</div></html>";
        JLabel descriptionLabel = new JLabel(description, SwingConstants.CENTER);
        panel.add(descriptionLabel, BorderLayout.NORTH);

        String[] columnNames = {"연번", "동아리명", "C.Ⅰ. Lab", "소개 및 활동 내용"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        // 데이터베이스 연결 및 데이터 추출
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM HobbyClubs");

            while (rs.next()) {
                int 연번 = rs.getInt("연번");
                String 동아리명 = rs.getString("동아리명");
                String CILab = rs.getString("CILab");
                String 소개및활동내용 = rs.getString("소개및활동내용");

                tableModel.addRow(new Object[]{연번, 동아리명, CILab, 소개및활동내용});
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(table);
        int preferredHeight = table.getRowHeight() * 5 + table.getTableHeader().getPreferredSize().height;
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, preferredHeight));

        // 칼럼 너비 조절
        TableColumn column = null;

        column = table.getColumnModel().getColumn(0);  // "연번" 칼럼
        column.setMinWidth(30);
        column.setMaxWidth(30);
        column.setPreferredWidth(30);

        column = table.getColumnModel().getColumn(1);  // "동아리명" 칼럼
        column.setMinWidth(100);  // 원하는 너비로 설정
        column.setMaxWidth(100);  // 원하는 최대 너비로 설정
        column.setPreferredWidth(90);  // 원하는 선호 너비로 설정

        column = table.getColumnModel().getColumn(2);  // "C.Ⅰ. Lab" 칼럼
        column.setMinWidth(60);
        column.setMaxWidth(60);
        column.setPreferredWidth(50);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }


    private void switchPanel(String selectedClub) {
        cardLayout.show(cardsPanel, selectedClub);
    }


    public void refreshData() {
        // 모든 행을 삭제하여 테이블을 초기화합니다.
        tableModel.setRowCount(0);

        // 데이터베이스 연결 및 데이터 추출
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM HobbyClubs");

            while (rs.next()) {
                int 연번 = rs.getInt("연번");
                String 동아리명 = rs.getString("동아리명");
                String CILab = rs.getString("CILab");
                String 소개및활동내용 = rs.getString("소개및활동내용");

                tableModel.addRow(new Object[]{연번, 동아리명, CILab, 소개및활동내용});
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public JTable getTable() {
        return table;
    }

}