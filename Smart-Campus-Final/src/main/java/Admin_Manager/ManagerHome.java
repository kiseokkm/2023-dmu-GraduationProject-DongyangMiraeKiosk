package Admin_Manager;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ManagerHome extends JFrame {
    private JButton noteButton;
    private JButton showMembersButton;
    private JButton academicScheduleButton;
    private JButton lostThingsButton;
    private JButton staffSearchButton;
    private JButton timeTableButton;
    private JButton univHopeButton; 
    private JButton clubManagementButton; 

    public ManagerHome() {
        setTitle("Manager Home");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        noteButton = new JButton("공지사항 탭");
        noteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerDashboard managerDashboard = new ManagerDashboard();
                managerDashboard.setVisible(true);
            }
        });

        showMembersButton = new JButton("회원 목록 보기");
        showMembersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerMemberList memberList = new ManagerMemberList();
                memberList.setVisible(true);
            }
        });

        academicScheduleButton = new JButton("학사일정 관리");
        academicScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AcademicScheduleManager scheduleManager = new AcademicScheduleManager();
                scheduleManager.setVisible(true);
            }
        });

        lostThingsButton = new JButton("분실물찾기");
        lostThingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LostThingsManager lostThingsManager = new LostThingsManager();
                lostThingsManager.setVisible(true);
            }
        });

        staffSearchButton = new JButton("교직원검색");
        staffSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmergencyClassroomManager manager = new EmergencyClassroomManager();
                manager.setVisible(true);
            }
        });

        timeTableButton = new JButton("시간표 관리");
        timeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerTimeTable managerTimeTable = new ManagerTimeTable();
                managerTimeTable.setVisible(true);
            }
        });

        // 추가된 버튼
        univHopeButton = new JButton("학교에 바란다 탭");
        univHopeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerUnivHope managerUnivHope = new ManagerUnivHope();
                managerUnivHope.setVisible(true);
            }
        });
        
        clubManagementButton = new JButton("동아리 관리");
        clubManagementButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagerClub managerClub = new ManagerClub();
                managerClub.setVisible(true);
            }
        });

        setLayout(new FlowLayout());
        add(noteButton);
        add(showMembersButton);
        add(academicScheduleButton);
        add(lostThingsButton);
        add(staffSearchButton);
        add(timeTableButton);
        add(univHopeButton);
        add(clubManagementButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerHome managerHome = new ManagerHome();
            managerHome.setVisible(true);
        });
    }
}
