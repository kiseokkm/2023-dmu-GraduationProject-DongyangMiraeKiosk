package kiosk;

import javax.swing.*;
import java.awt.*;

public class NoticeBoard extends JPanel {
    private JList<String> noticeList;
    private DefaultListModel<String> listModel;

    public NoticeBoard() {
        setLayout(new BorderLayout(10, 10));
        initComponents();
    }

    private void initComponents() {
        JLabel lblTitle = new JLabel("공지사항", SwingConstants.CENTER);
        lblTitle.setFont(new Font("돋움", Font.BOLD, 24));
        
        listModel = new DefaultListModel<>();
        noticeList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(noticeList);

        // Sample notices for demonstration
        listModel.addElement("공지 1 - 2023.08.15");
        listModel.addElement("공지 2 - 2023.08.14");
        listModel.addElement("공지 3 - 2023.08.13");

        add(lblTitle, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("공지사항");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 600);
            frame.setLocationRelativeTo(null);
            
            NoticeBoard noticeBoard = new NoticeBoard();
            frame.add(noticeBoard);
            frame.setVisible(true);
        });
    }
}
