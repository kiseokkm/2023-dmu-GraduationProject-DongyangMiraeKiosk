package graduation;

import java.awt.*;
import javax.swing.*;

public class GraduationMain extends JPanel {

    private JTabbedPane graduationTabs;
    private AssociateDegree pnlProfessionalBachelor; // 전문학사

    public GraduationMain() {
        initGraduationTabs();
    }

    private void initGraduationTabs() {
        graduationTabs = new JTabbedPane(JTabbedPane.TOP); // Tabs at the top

        // 전문학사 tab
        pnlProfessionalBachelor = new AssociateDegree();
        graduationTabs.addTab("전문학사", pnlProfessionalBachelor);

        // Add more tabs as needed

        setLayout(new BorderLayout());
        add(graduationTabs, BorderLayout.CENTER);
    }
}
