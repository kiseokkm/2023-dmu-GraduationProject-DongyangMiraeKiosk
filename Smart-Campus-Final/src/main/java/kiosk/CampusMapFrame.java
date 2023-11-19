
package kiosk;

import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import java.net.URL;

public class CampusMapFrame extends JPanel {

    private JEditorPane mapPane;

    public CampusMapFrame() {
        try {
            mapPane = new JEditorPane();
            mapPane.setEditable(false);
            
            URL mapURL = getClass().getResource("/Smart Campus_Map.html");
            if (mapURL != null) {
                mapPane.setPage(mapURL);
            } else {
                throw new RuntimeException("Cannot find Smart Campus_Map.html file");
            }
            JScrollPane scrollPane = new JScrollPane(mapPane);
            add(scrollPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
