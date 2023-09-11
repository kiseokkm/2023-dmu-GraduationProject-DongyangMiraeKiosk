package kiosk;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashSet;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    private HashSet<String> eventDays;

    // 생성자를 통해 이벤트가 있는 날짜의 HashSet을 받아옵니다.
    public CustomTableCellRenderer(HashSet<String> eventDays) {
        this.eventDays = eventDays;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // value가 eventDays HashSet에 포함되어 있으면 해당 셀의 배경색을 노란색으로 변경합니다.
        String dayValue = String.valueOf(value);
        if (eventDays.contains(dayValue)) {
            setBackground(Color.YELLOW);  // 이벤트가 있는 날짜는 배경색을 노란색으로 설정
        } else {
            setBackground(Color.WHITE);
        }
        
        // 주말 색상 설정
        if (column == 6) {
            setForeground(Color.BLUE);
        } else if (column == 0) {
            setForeground(Color.RED);
        } else {
            setForeground(Color.BLACK);
        }

        return this;
    }
}
