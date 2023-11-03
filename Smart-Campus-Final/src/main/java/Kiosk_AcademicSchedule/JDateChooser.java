package Kiosk_AcademicSchedule;

import javax.swing.*;
import java.util.Date;
import java.util.Calendar;

public class JDateChooser extends JPanel {
    private Date selectedDate;
    private JSpinner dateSpinner;

    public JDateChooser() {
        SpinnerDateModel model = new SpinnerDateModel();
        dateSpinner = new JSpinner(model);
        
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);

        add(dateSpinner);

        dateSpinner.addChangeListener(e -> selectedDate = (Date) dateSpinner.getValue());
    }

    public Date getDate() {
        return selectedDate;
    }

    public void setDate(Date date) {
        this.selectedDate = date;
        dateSpinner.setValue(date);
    }
}