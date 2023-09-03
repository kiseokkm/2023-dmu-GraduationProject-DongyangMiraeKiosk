package manager;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ManagerHome extends JFrame {
    private JButton noteButton;

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

        setLayout(new FlowLayout());
        add(noteButton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ManagerHome managerHome = new ManagerHome();
            managerHome.setVisible(true);
        });
    }
}
