package app;

public class SplashFrame extends javax.swing.JFrame {

  public SplashFrame() {
    initComponents();
    app.Global.setAppIcon(this);
    add(new SplashGradientPanel(), java.awt.BorderLayout.CENTER);
  }
  @SuppressWarnings("unchecked")
  private void initComponents() {
    progressBar = new javax.swing.JProgressBar();
    
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("Splash");
    setBackground(java.awt.Color.decode(java.util.ResourceBundle.getBundle("app/global").getString("colorPrimary")));
    setMinimumSize(new java.awt.Dimension(480, 280));
    setName(""); 
    setUndecorated(true);
    setResizable(false);

    progressBar.setBackground(getBackground());
    progressBar.setForeground(java.awt.Color.decode(java.util.ResourceBundle.getBundle("app/global").getString("colorPrimaryAlt")));
    progressBar.setToolTipText("");
    progressBar.setBorder(null);
    progressBar.setBorderPainted(false);
    progressBar.setMinimumSize(null);
    progressBar.setPreferredSize(new java.awt.Dimension(480, 4));
    progressBar.setString("");
    getContentPane().add(progressBar, java.awt.BorderLayout.SOUTH);
    pack();
    setLocationRelativeTo(null);
  }
  public static void main(String args[]) {
    int delay = 2000;
    SplashFrame splashFrame = new SplashFrame();
    splashFrame.setVisible(true);
    splashFrame.startProgress(delay);
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
    }
    app.Global.setDefaultTheme();
    java.awt.EventQueue.invokeLater(() -> {
      new LaunchFrame().setVisible(true);
    });
    splashFrame.dispose();
  }
  void startProgress(int duration) {
    int i = 0;
    try {
      while (i <= 100) {
        progressBar.setValue(i);
        Thread.sleep(duration / 10);
        i += 10;
      }
    } catch (InterruptedException e) {
    }
  }
  private javax.swing.JProgressBar progressBar;
}
