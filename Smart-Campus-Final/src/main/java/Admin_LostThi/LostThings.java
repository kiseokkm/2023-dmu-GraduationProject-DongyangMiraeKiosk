package Admin_LostThi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LostThings extends JPanel {

    private String loggedInUsername; // 로그인한 사용자의 이름을 저장하는 변수

    public LostThings(String loggedInUsername) {
        this.loggedInUsername = loggedInUsername;
        setLayout(new BorderLayout());
        initializeComponents();
    }

    private void initializeComponents() {
        JLabel headline = new JLabel("분실물찾기", SwingConstants.CENTER);
        headline.setFont(new Font("Malgun Gothic", Font.BOLD, 40));
        headline.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(headline, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 10, 10));

        JButton btnReport = createButtonWithImage("등록", "/LostThings/FindSearch1.jpeg");
        btnReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame reportFrame = new JFrame();
                reportFrame.setTitle("분실물 신고");
                reportFrame.setSize(600, 400);
                reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                reportFrame.setLocationRelativeTo(null);
                reportFrame.add(new LostThingsReport(loggedInUsername)); // 로그인한 사용자 이름을 전달
                reportFrame.setVisible(true);
            }
        });
        buttonsPanel.add(btnReport);

        JButton btnFind = createButtonWithImage("찾기", "/LostThings/Report.jpg");
        btnFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAll();
                add(new LostThingsFind());
                revalidate();
                repaint();
            }
        });
        buttonsPanel.add(btnFind);

        add(buttonsPanel, BorderLayout.CENTER);
    }

    private JButton createButtonWithImage(String text, String imagePath) {
        JButton button = new JButton(text);
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        Image img = icon.getImage().getScaledInstance(100, 40, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(img));
        button.setFont(new Font("Malgun Gothic", Font.BOLD, 18));
        button.setBackground(Color.LIGHT_GRAY);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 50));
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        return button;
    }
}