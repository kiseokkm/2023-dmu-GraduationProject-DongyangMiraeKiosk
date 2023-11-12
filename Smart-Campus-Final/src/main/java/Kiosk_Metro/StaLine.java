package Kiosk_Metro;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class StaLine extends JPanel {
	
	JButton btn1, btn2;
	JLabel lbl;
	Color color;
	String str_line = "1001";
	
	private void setLine() {
		
		switch(str_line) {
		case "1001":
			color = new Color(34, 62, 146);
			break;
			
		case "1002":
			color = new Color(55, 193, 1);
			break;
			
		case "1003":
			color = new Color(255, 150, 0);
			break;
			
		case "1004":
			color = new Color(0, 186, 255);
			break;
			
		case "1005":
			color = new Color(125, 0, 205);
			break;
			
		case "1006":
			color = new Color(185, 83, 0);
			break;
			
		case "1007":
			color = new Color(95, 134, 0);
			break;
			
		case "1008":
			color = new Color(255, 0, 180);
			break;
			
		case "1009":
			color = new Color(224, 174, 0);
			break;
			
		case "1063":
			color = new Color(0, 205, 188);
			break;
			
		case "1065":
			color = new Color(0, 192, 233);
			break;
			
		case "1067":
			color = new Color(0, 192, 145);
			break;
			
		case "1071":
			color = new Color(255, 228, 0);
			break;
			
		case "1075":
			color = new Color(255, 216, 0);
			break;
			
		case "1077":
			color = new Color(208, 0, 0);
			break;
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.setColor(new Color(255, 255, 255));
	    g.fillOval(0, 0, 33, 33);
	    Graphics2D g2 = (Graphics2D) g;
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setStroke(new BasicStroke(3));
	    g2.setColor(color);
	    g2.drawOval(1, 1, 31, 31);
	}
	
	StaLine(String str, int x, int y, int size, String line) {
		setLayout(null);
		setBackground(Color.WHITE);
		setOpaque(false);
		
		str_line = line;
		setLine();
		
		int num = str.length();
		String result = "<html><center>" + str + "</center></html>";
				
		btn1 = new JButton();
		//btn1.setIcon(new ImageIcon("images/img_bg_sta.png"));
		btn1.setBorderPainted(false);
		btn1.setFocusPainted(false);
		btn1.setContentAreaFilled(false);
		btn1.setBounds(0, 0, 34, 34);
		btn1.addMouseListener(new MouseAdapter()
        {
            public void mouseEntered(MouseEvent evt)
            {
            	lbl.setToolTipText(result.replace("<br>", ""));
            }
            public void mouseExited(MouseEvent evt)
            {
            
            }
            public void mousePressed(MouseEvent evt)
            {
                
            }
            public void mouseReleased(MouseEvent evt)
            {
                
            }
        });
		
		lbl = new JLabel(result, SwingConstants.CENTER);
		Font f = new Font("맑은 고딕", Font.BOLD, size);
		lbl.setFont(f);
		lbl.setBounds(0, 0, 34, 34);

		add(lbl);
		add(btn1);
		setBounds(x, y, 34, 34);
	}

}
