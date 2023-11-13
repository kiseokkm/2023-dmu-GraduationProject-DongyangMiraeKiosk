package Kiosk_Metro;

import javax.swing.*;
import java.awt.*;

public class MainMain {
    private JTabbedPane tabbedPane; 

    public MainMain() {
        initialize();
    }
    private void initialize() {
        tabbedPane = new JTabbedPane(); 

        Main mainPanel = new Main();
        tabbedPane.addTab("Main.java", null, mainPanel, "첫 번째 탭");

        BusInfoApp busInfoAppPanel = new BusInfoApp();
        tabbedPane.addTab("BusInfoApp.java", null, busInfoAppPanel.getMainPanel(), "두 번째 탭");
    }
    public JTabbedPane getMainPanel() {
        return tabbedPane;
    }
}