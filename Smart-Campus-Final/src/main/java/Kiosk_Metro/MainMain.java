package Kiosk_Metro;

import javax.swing.*;
import java.awt.*;

public class MainMain {
    private JTabbedPane tabbedPane; // 프레임 대신 탭 패널을 멤버 변수로 사용합니다.

    public MainMain() {
        initialize();
    }
    private void initialize() {
        tabbedPane = new JTabbedPane(); // 탭 패널 초기화

        // 첫 번째 탭
        Main mainPanel = new Main();
        tabbedPane.addTab("Main.java", null, mainPanel, "첫 번째 탭");

        // 두 번째 탭
        BusInfoApp busInfoAppPanel = new BusInfoApp();
        tabbedPane.addTab("BusInfoApp.java", null, busInfoAppPanel.getMainPanel(), "두 번째 탭");
    }

    // MainMain 객체의 메인 패널을 반환하는 메서드를 추가합니다.
    public JTabbedPane getMainPanel() {
        return tabbedPane;
    }
}