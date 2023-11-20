package services;

import java.awt.BorderLayout;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import models.Item;

public class ItemService extends DatabaseService {
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ItemService::initAndShowGUI);
    }

    private static void initAndShowGUI() {
        JFrame frame = new JFrame("Swing and JavaFX WebView Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        frame.add(panel);

        final JFXPanel fxPanel = new JFXPanel();
        panel.add(fxPanel, BorderLayout.CENTER);

        Platform.runLater(() -> initFX(fxPanel));

        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private static void initFX(JFXPanel fxPanel) {
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

    private static Scene createScene() {
        WebView webView = new WebView();
        webView.getEngine().load("http://127.0.0.1:5000");

        return new Scene(webView);
    }
}
