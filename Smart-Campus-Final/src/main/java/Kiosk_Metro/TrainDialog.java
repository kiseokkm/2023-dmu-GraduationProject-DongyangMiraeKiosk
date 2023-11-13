package Kiosk_Metro;
import javax.swing.*;
import java.awt.Font;

public class TrainDialog extends JDialog {
	
	JScrollPane jsc;
	JTextArea jta;
	
	String result = "";
	public TrainDialog(String str, String destination, String trainNo, String status, String station) {
		setSize(400, 300);
		setResizable(false);
		getContentPane().setLayout(null);
	
		result += "[열차번호] " + trainNo + "\n";
		result += "[종착역명] " + destination + "\n";
		result += "[열차위치] " + station + "역 " + status + "\n";
		result += "[역 별 도착시간]─────────────────────\n";
		result += str;
		result += "────────────────────────────\n";
		
		jsc = new JScrollPane();
		jta = new JTextArea(6, 30);
		jta.setText(result);
		jta.setEditable(false);
		jsc.setBounds(0, 0, 394, 271);
		jsc.setViewportView(jta);
		getContentPane().add(jsc);
		
	}
}
