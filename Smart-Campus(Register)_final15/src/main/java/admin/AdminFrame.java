/*
 * Copyright (c) 2020 Self-Order Kiosk
 */
package admin;

public class AdminFrame extends javax.swing.JFrame {

  /**
   * Creates new form AdminFrame
   */
  public AdminFrame() {
    initComponents();
    app.Global.setAppIcon(this);
    tabbedPane.addTab("수강중인 강좌", new OrdersPanel());
    tabbedPane.addTab("시간표", new ItemsPanel());
    tabbedPane.addTab("과제 및 수업 자료 알림", new CategoriesPanel());
    tabbedPane.addTab("사용자 목록", new UsersPanel());
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    tabbedPane = new javax.swing.JTabbedPane();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Admin");
    setName(""); // NOI18N
    setPreferredSize(new java.awt.Dimension(720, 600));
    getContentPane().add(tabbedPane, java.awt.BorderLayout.CENTER);

    getAccessibleContext().setAccessibleName("");

    pack();
    setLocationRelativeTo(null);
  }// </editor-fold>//GEN-END:initComponents

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set default theme */
    app.Global.setDefaultTheme();

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(() -> {
      new AdminFrame().setVisible(true);
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JTabbedPane tabbedPane;
  // End of variables declaration//GEN-END:variables
}