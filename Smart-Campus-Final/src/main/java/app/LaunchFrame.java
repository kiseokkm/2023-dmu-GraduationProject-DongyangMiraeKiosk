package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import java.awt.Toolkit;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.Button;
import java.awt.Label;
import java.awt.Panel;
import java.awt.GridBagLayout;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class LaunchFrame extends JFrame {

     private String appName;

     public LaunchFrame() {
        getContentPane().setBackground(new Color(96, 140, 255));
       initComponents();
       app.Global.setAppIcon(this);
       getAppName();
       lblAppName.setText("SMART CAMPUS");
       setSystemTray();
     }


     @SuppressWarnings("unchecked")
     private void initComponents() {
       java.awt.GridBagConstraints gridBagConstraints;

       btgTheme = new javax.swing.ButtonGroup();
       pnlContainer = new javax.swing.JPanel();
       pnlContainer.setBackground(new Color(96, 140, 255));
       lblAppLogo = new javax.swing.JLabel();
       lblAppName = new javax.swing.JLabel();
       lblAppName.setHorizontalAlignment(SwingConstants.CENTER);
       lblAppName.setForeground(new Color(255, 255, 255));
       lblAppText = new javax.swing.JLabel();
       lblAppText.setHorizontalAlignment(SwingConstants.LEFT);
       lblAppText.setForeground(new Color(255, 255, 255));
       pnlActions = new javax.swing.JPanel();
       pnlActions.setBackground(new Color(96, 140, 255));
       btnLaunchKiosk = new javax.swing.JButton();
       btnLaunchAdmin = new javax.swing.JButton();
       menuBar = new javax.swing.JMenuBar();
       menuBar.setBorderPainted(false);
       mnuFile = new javax.swing.JMenu();
       mniExit = new javax.swing.JMenuItem();
       mnuTheme = new javax.swing.JMenu();
       cmuLight = new javax.swing.JCheckBoxMenuItem();
       cmuDark = new javax.swing.JCheckBoxMenuItem();
       mnuHelp = new javax.swing.JMenu();
       mniAbout = new javax.swing.JMenuItem();

       setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
       setTitle("Launcher");
       setName("");
       setPreferredSize(new java.awt.Dimension(720, 600));
       getContentPane().setLayout(new java.awt.GridBagLayout());

       pnlContainer.setMaximumSize(new java.awt.Dimension(640, 480));
       pnlContainer.setMinimumSize(new java.awt.Dimension(640, 480));
       pnlContainer.setPreferredSize(new java.awt.Dimension(640, 480));
       pnlContainer.setLayout(new java.awt.GridBagLayout());

       
       java.awt.Image originalImage = new javax.swing.ImageIcon(getClass().getResource("/app/customer-logo-2.png")).getImage();
       int newWidth = 70; // 원하는 너비 설정
       int newHeight = 70; // 원하는 높이 설정
       java.awt.Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, java.awt.Image.SCALE_SMOOTH);
       lblAppLogo.setIcon(new javax.swing.ImageIcon(resizedImage));// NOI18N;
       gridBagConstraints = new java.awt.GridBagConstraints();
       gridBagConstraints.insets = new Insets(0, 0, 10, 0);
       gridBagConstraints.gridx = 0;
       gridBagConstraints.gridy = 0;
       pnlContainer.add(lblAppLogo, gridBagConstraints);

       lblAppName.setFont(new Font("Arial", lblAppName.getFont().getStyle() | Font.BOLD, lblAppName.getFont().getSize() + 20));
       gridBagConstraints = new java.awt.GridBagConstraints();
       gridBagConstraints.gridx = 0;
       gridBagConstraints.gridy = 1;
       gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
       gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
       pnlContainer.add(lblAppName, gridBagConstraints);
       
       String lbltext = "동양미래대학교 안내 도우미";
       lblAppText.setFont(new Font("\uB9D1\uC740 \uACE0\uB515", lblAppText.getFont().getStyle() | Font.BOLD, lblAppText.getFont().getSize() + 1));
       lblAppText.setText(lbltext);
       gridBagConstraints = new java.awt.GridBagConstraints();
       gridBagConstraints.gridx = 0;
       gridBagConstraints.gridy = 2;
       gridBagConstraints.insets = new java.awt.Insets(0, 0, 50, 0);
       pnlContainer.add(lblAppText, gridBagConstraints);

       pnlActions.setMaximumSize(null);
       pnlActions.setPreferredSize(new java.awt.Dimension(360, 160));
       pnlActions.setLayout(new java.awt.GridLayout(1, 0, 30, 0));

       btnLaunchKiosk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/water-heater.png"))); // NOI18N
       btnLaunchKiosk.setText("Launch Kiosk");
       btnLaunchKiosk.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
       btnLaunchKiosk.setIconTextGap(20);
       btnLaunchKiosk.setPreferredSize(new java.awt.Dimension(100, 200));
       btnLaunchKiosk.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
       btnLaunchKiosk.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
           btnLaunchKioskActionPerformed(evt);
         }
       });
       pnlActions.add(btnLaunchKiosk);

       btnLaunchAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/online-test.png"))); // NOI18N
       btnLaunchAdmin.setText("Launch Admin");
       btnLaunchAdmin.setToolTipText("");
       btnLaunchAdmin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
       btnLaunchAdmin.setIconTextGap(20);
       btnLaunchAdmin.setPreferredSize(new java.awt.Dimension(100, 200));
       btnLaunchAdmin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
       btnLaunchAdmin.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
           btnLaunchAdminActionPerformed(evt);
         }
       });
       pnlActions.add(btnLaunchAdmin);

       gridBagConstraints_1 = new java.awt.GridBagConstraints();
       gridBagConstraints_1.insets = new Insets(0, 0, 5, 0);
       gridBagConstraints_1.gridx = 0;
       gridBagConstraints_1.gridy = 3;
       gridBagConstraints_1.anchor = java.awt.GridBagConstraints.SOUTH;
       pnlContainer.add(pnlActions, gridBagConstraints_1);

       getContentPane().add(pnlContainer, new java.awt.GridBagConstraints());

       mnuFile.setText("File");

       mniExit.setText("Exit");
       mniExit.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
           mniExitActionPerformed(evt);
         }
       });
       mnuFile.add(mniExit);

       menuBar.add(mnuFile);

       mnuTheme.setText("Theme");

       btgTheme.add(cmuLight);
       cmuLight.setSelected(true);
       cmuLight.setText("Light");
       cmuLight.addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
           cmuLightItemStateChanged(evt);
         }
       });
       mnuTheme.add(cmuLight);

       btgTheme.add(cmuDark);
       cmuDark.setText("Dark");
       cmuDark.addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
           cmuDarkItemStateChanged(evt);
         }
       });
       mnuTheme.add(cmuDark);

       menuBar.add(mnuTheme);

       mnuHelp.setText("Help");

       mniAbout.setText("About");
       mniAbout.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
           mniAboutActionPerformed(evt);
         }
       });
       mnuHelp.add(mniAbout);

       menuBar.add(mnuHelp);

       setJMenuBar(menuBar);

       getAccessibleContext().setAccessibleName("");

       pack();
       setLocationRelativeTo(null);
     }

     private void btnLaunchAdminActionPerformed(java.awt.event.ActionEvent evt) {
       new Admin_LoginRegister.LoginFrame().setVisible(true);
     
     }

     private void btnLaunchKioskActionPerformed(java.awt.event.ActionEvent evt) {
       new kiosk.StartFrame().setVisible(true);
     }

     private void mniExitActionPerformed(java.awt.event.ActionEvent evt) {
       System.exit(0);
     }

     private void mniAboutActionPerformed(java.awt.event.ActionEvent evt) {
       new AboutDialog().setVisible(true);
     }

     private void cmuLightItemStateChanged(java.awt.event.ItemEvent evt) {
       setTheme(evt, "light");
     }

     private void cmuDarkItemStateChanged(java.awt.event.ItemEvent evt) {
       setTheme(evt, "dark");
     }

     private void getAppName() {
       try {
         app.PropertiesReader reader = new app.PropertiesReader("properties-from-pom.properties");
         String name = reader.getProperty("name");
         appName = name;
       } catch (java.io.IOException ex) {
         appName = "";
         System.out.println(ex);
       }
     }

     private void setTheme(java.awt.event.ItemEvent evt, String name) {
       if (evt.getStateChange() == 1) {
         if ("light".equals(name)) {
           app.Global.setDefaultTheme();
         } else {
           com.formdev.flatlaf.FlatDarculaLaf.install();
         }
         for (java.awt.Window window : javax.swing.JFrame.getWindows()) {
           javax.swing.SwingUtilities.updateComponentTreeUI(window);
         }
       }
     }

     private void setSystemTray() {
       java.awt.PopupMenu popupMenu = new java.awt.PopupMenu();
       java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(new javax.swing.ImageIcon(getClass().getResource("/app/app-logo-1x.jpeg")).getImage());
       java.awt.SystemTray systemTray = java.awt.SystemTray.getSystemTray();

       java.awt.MenuItem menuItemAbout = new java.awt.MenuItem("About");
       java.awt.MenuItem menuItemExit = new java.awt.MenuItem("Exit");
       popupMenu.add(menuItemAbout);
       popupMenu.add(menuItemExit);

       trayIcon.setImageAutoSize(true);
       trayIcon.setToolTip(appName);
       trayIcon.setPopupMenu(popupMenu);

       try {
         systemTray.add(trayIcon);
       } catch (java.awt.AWTException ex) {
         System.out.println(ex);
         return;
       }

       trayIcon.addActionListener((java.awt.event.ActionEvent evt) -> {
         setState(javax.swing.JFrame.NORMAL);
       });

       menuItemAbout.addActionListener((java.awt.event.ActionEvent evt) -> {
         new AboutDialog().setVisible(true);
       });

       menuItemExit.addActionListener((java.awt.event.ActionEvent evt) -> {
         systemTray.remove(trayIcon);
         System.exit(0);
       });
     }

     public static void main(String args[]) {
       app.Global.setDefaultTheme();
       java.awt.EventQueue.invokeLater(() -> {
         new LaunchFrame().setVisible(true);
       });
     }
     private javax.swing.ButtonGroup btgTheme;
     private javax.swing.JButton btnLaunchAdmin;
     private javax.swing.JButton btnLaunchKiosk;
     private javax.swing.JCheckBoxMenuItem cmuDark;
     private javax.swing.JCheckBoxMenuItem cmuLight;
     private javax.swing.JLabel lblAppLogo;
     private javax.swing.JLabel lblAppName;
     private javax.swing.JLabel lblAppText;
     private javax.swing.JMenuBar menuBar;
     private javax.swing.JMenuItem mniAbout;
     private javax.swing.JMenuItem mniExit;
     private javax.swing.JMenu mnuFile;
     private javax.swing.JMenu mnuHelp;
     private javax.swing.JMenu mnuTheme;
     private javax.swing.JPanel pnlActions;
     private javax.swing.JPanel pnlContainer;
     private GridBagConstraints gridBagConstraints_1;
}