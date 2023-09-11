package kiosk;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // 토요일 (column == 6)은 파란색으로, 일요일 (column == 0)은 빨간색으로 설정
        if (column == 6) {
            setForeground(Color.BLUE);
        } else if (column == 0) {
            setForeground(Color.RED);
        } else {
            setForeground(Color.BLACK); // 다른 요일은 검은색으로
        }

        return this;
    }
}
