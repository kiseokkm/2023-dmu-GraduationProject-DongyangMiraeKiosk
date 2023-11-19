package kiosk;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import javax.swing.JPanel;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.concurrent.Worker.State;

public class CafeTeria {
	public static JPanel createWebPanel() {
	    JFXPanel jfxPanel = new JFXPanel();
	    String rootPath = System.getProperty("user.dir");
	    ProcessBuilder processBuilder = new ProcessBuilder("python", rootPath +"/app/app.py");
//	    processBuilder.directory(new File("/Smart-Campus(Register)_final16")); // Spring 프로젝트 루트 디렉토리로 설정	    
	    System.out.println(rootPath);
	    try {
	        processBuilder.start();
	    } catch (IOException e) {
	    	System.out.println(e);
	    }	 
	    Platform.runLater(() -> {
	        String cssPath = "file://" + rootPath + "/app/static/css/all.css";
	        String cssLink = "<link rel='stylesheet' type='text/css' href='" + cssPath + "'>";
	        System.out.println("CSS LINK : "+cssLink);
	    	
	        WebView webView = new WebView();
	        WebEngine WebEngine = webView.getEngine();
	        WebEngine.setJavaScriptEnabled(true);
	        WebEngine.getLoadWorker().stateProperty().addListener(
            (ov, oldState, newState) -> {
                if (newState == State.SUCCEEDED) {
                	String script = "";
                	script += "document.head.innerHTML += '" + cssLink + "';";
                	script = "var xhr = new XMLHttpRequest();";
                    script += "xhr.setRequestHeader('Access-Control-Allow-Origin', '*');"; // CORS 헤더 추가
                    script += "xhr.send();";
                    WebEngine.executeScript(script);
                }
            });
	        WebEngine.load("http://127.0.0.1:5000");	        
//	        webView.getEngine().load("http://127.0.0.1:5000");        
	        jfxPanel.setScene(new Scene(webView));
	    });
	    JPanel panel = new JPanel(new BorderLayout());
	    panel.add(jfxPanel, BorderLayout.CENTER);
	    return panel;
	} 
    public static void main(String[] args) {
        createWebPanel();
    }
}
