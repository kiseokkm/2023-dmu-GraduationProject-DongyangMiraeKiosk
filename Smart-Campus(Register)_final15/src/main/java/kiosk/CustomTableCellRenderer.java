package kiosk;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.HashSet;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    private HashSet<String> eventDays;

    // 생성자를 통해 이벤트가 있는 날짜의 HashSet을 받아옵니다.
    public CustomTableCellRenderer(HashSet<String> eventDays) {
        this.eventDays = eventDays != null ? eventDays : new HashSet<>(); // null 체크를 추가
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    	System.out.println("Rendering cell for day: " + value);
    	super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (value != null) {
            String dayValue = String.valueOf(value);
            if (eventDays.contains(dayValue)) {
                System.out.println("Coloring Day: " + dayValue);  // 노란색으로 색칠되는 날짜를 출력
                setBackground(Color.YELLOW);
            } else {
                setBackground(Color.WHITE);
            }
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
