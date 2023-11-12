
package kiosk;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;

public class CampusMapFXFrame extends JFXPanel {

    public CampusMapFXFrame() {
        // Initialize JavaFX platform
        Platform.runLater(this::initFX);
    }

    private void initFX() {
        WebView webView = new WebView();
        
        // Load the Smart Campus_Map.html file
        webView.getEngine().load(getClass().getResource("/Smart Campus_Map.html").toExternalForm());

        // Set the WebView as the scene of this JFXPanel
        setScene(new Scene(webView));
    }
}
