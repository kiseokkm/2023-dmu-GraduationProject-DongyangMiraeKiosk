
package kiosk;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

public class CampusMapFXFrame extends JFXPanel {

    public CampusMapFXFrame() {
        Platform.runLater(this::initFX);
    }

    private void initFX() {
        WebView webView = new WebView();
        
        webView.getEngine().load(getClass().getResource("/Smart Campus_Map.html").toExternalForm());

        setScene(new Scene(webView));
    }
}
