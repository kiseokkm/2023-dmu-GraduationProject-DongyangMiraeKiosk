package AcademicSchedule;

import javax.swing.*;
import java.util.Date;
import java.util.Calendar;

public class JDateChooser extends JPanel {
    private Date selectedDate;
    private JSpinner dateSpinner;

    public JDateChooser() {
        // Set the spinner with a date model
        SpinnerDateModel model = new SpinnerDateModel();
        dateSpinner = new JSpinner(model);
        
        // Set the editor format
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);

        // Add the spinner to the panel
        add(dateSpinner);

        // Listener to update the selectedDate when changed
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
