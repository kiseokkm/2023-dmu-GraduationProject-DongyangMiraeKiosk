package Scholarship;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;

public class ScholarshipMain extends JPanel {
    private TuitionPayment tuitionPayment; 
    private ScholarshipOnAndOffCampus scholarshipOnAndOffCampus;
    private StateScholarshipStudentLoans stateScholarshipStudentLoans;  
    private JTabbedPane tabbedPane;

    public ScholarshipMain() {
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        tuitionPayment = new TuitionPayment();
        scholarshipOnAndOffCampus = new ScholarshipOnAndOffCampus();
        StateScholarshipStudentLoans stateScholarshipStudentLoans = new StateScholarshipStudentLoans(); 

        tabbedPane.addTab("등록금납부", tuitionPayment);
        tabbedPane.addTab("교내외장학금", scholarshipOnAndOffCampus); 
        tabbedPane.addTab("국가장학금/학자금 대출", stateScholarshipStudentLoans); 

        add(tabbedPane, BorderLayout.CENTER);
    }
}
