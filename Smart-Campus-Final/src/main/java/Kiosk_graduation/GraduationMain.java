package Kiosk_graduation;

import java.awt.*;
import javax.swing.*;

public class GraduationMain extends JPanel {

    private JTabbedPane graduationTabs;
    private AssociateDegree pnlProfessionalBachelor; // 전문학사
    private College pnlCollege; // 학사
    private HoldOnGraduation pnlHoldOnGraduation; // 졸업보류

    public GraduationMain() {
        initGraduationTabs();
    }
    private void initGraduationTabs() {
        graduationTabs = new JTabbedPane(JTabbedPane.TOP);

        pnlProfessionalBachelor = new AssociateDegree();
        graduationTabs.addTab("전문학사", pnlProfessionalBachelor);

        pnlCollege = new College();
        graduationTabs.addTab("학사", pnlCollege);
        
        pnlHoldOnGraduation = new HoldOnGraduation();
        graduationTabs.addTab("졸업보류", pnlHoldOnGraduation);

        setLayout(new BorderLayout());
        add(graduationTabs, BorderLayout.CENTER);
    }
}
