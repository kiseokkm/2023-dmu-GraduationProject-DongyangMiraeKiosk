package manager;

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
                // 여기서 회원 목록을 보여주는 새로운 창을 띄울 수 있습니다.
                MemberList memberList = new MemberList();
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

        setLayout(new FlowLayout());
        add(noteButton);
        add(showMembersButton);
        add(academicScheduleButton);
        add(lostThingsButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerHome managerHome = new ManagerHome();
            managerHome.setVisible(true);
        });
    }
}
