package Kiosk_AcademicSchedule;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.HashSet;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    private HashSet<Integer> eventDays;
    private Color bodyColor = new Color(255, 239, 213);

    public CustomTableCellRenderer(HashSet<Integer> eventDays) {
        this.eventDays = eventDays != null ? eventDays : new HashSet<>();
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if (value != null) {
            Integer dayValue = null;
            if (value instanceof Integer) {
                dayValue = (Integer) value;
            } else if (value instanceof String) {
                try {
                    dayValue = Integer.parseInt((String) value);
                } catch (NumberFormatException e) {
                    dayValue = null;
                }
            }
            if (dayValue != null && eventDays.contains(dayValue)) {
                setBackground(Color.YELLOW);
            } else {
                setBackground(bodyColor); 
            }
        } else {
            setBackground(bodyColor); 
        }
        return this;
    }
    public static class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setOpaque(true);
            setBackground(new Color(255, 228, 196)); 
        }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if ("Sun".equals(value)) {
                setForeground(Color.RED);
            } else if ("Sat".equals(value)) {
                setForeground(Color.BLUE);
            } else {
                setForeground(Color.BLACK);
            }
            return this;
        }
    }
    public static class CustomTableCellRendererForEvents extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(new Color(255, 239, 213)); // 연한 살색
            return this;
        }
    }
    public static void setHeaderRenderer(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
    }
}
