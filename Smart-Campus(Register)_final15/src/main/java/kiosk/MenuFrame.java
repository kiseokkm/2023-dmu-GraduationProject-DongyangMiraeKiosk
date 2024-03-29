package kiosk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListModel;

import LostThi.LostThings;

import java.awt.FlowLayout;

import kiosk.NoticeFrame;


public class MenuFrame extends javax.swing.JFrame implements StateObserver {

  services.ItemService itemService;
  OrderTable tbmOrder;
  java.util.ArrayList<models.Item> itemsNoticeFrame;
  java.util.ArrayList<models.Item> itemsClass;
  java.util.ArrayList<models.Item> itemsFood;
  java.util.ArrayList<models.Item> itemsMap;

  public MenuFrame() {
      try {
          app.Global.setAppIcon(this);
          initModels();
          initComponents();
          // 공지사항 초기 로드
          NoticeFrame.showNoticeTableOnPanel(pnlNotice);
          initState();
       } catch (Exception e) {
           e.printStackTrace(); // 오류 메세지 출력
           JOptionPane.showMessageDialog(this, "오류발생" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
   }
  @SuppressWarnings("unchecked")
  private javax.swing.JPanel pnlLostThings;
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    pnlContent = new javax.swing.JPanel();
    tabbedPane = new javax.swing.JTabbedPane();
    pnlNotice = new javax.swing.JPanel();
    pnlClass = new javax.swing.JPanel();
    pnlFood = new javax.swing.JPanel();
    pnlMap = new javax.swing.JPanel();
    pnlOrder = new javax.swing.JPanel();
    lblOrder = new javax.swing.JLabel();
    scpOrder = new javax.swing.JScrollPane();
    tblOrder = new javax.swing.JTable();
    pnlTotal = new javax.swing.JPanel();
    lblTotal = new javax.swing.JLabel();
    lblTotalValue = new javax.swing.JLabel();
    pnlFooter = new javax.swing.JPanel();
    btnBack = new javax.swing.JButton();
    btnContinue = new javax.swing.JButton();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Menu");
    setMinimumSize(new java.awt.Dimension(720, 860));
    setName(""); // NOI18N
    setPreferredSize(new java.awt.Dimension(720, 860));

    pnlContent.setLayout(new java.awt.GridBagLayout());

    tabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
    tabbedPane.setMinimumSize(new java.awt.Dimension(574, 400));
    tabbedPane.setPreferredSize(new java.awt.Dimension(700, 400));
    tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        tabbedPaneStateChanged(evt);
      }
    });

    pnlNotice.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlNotice.setMaximumSize(new java.awt.Dimension(0, 0));
    pnlNotice.setPreferredSize(new java.awt.Dimension(560, 500));
    pnlNotice.setLayout(new java.awt.GridLayout(2, 3, 20, 20));
    tabbedPane.addTab("공지사항", pnlNotice);

    pnlClass.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlClass.setLayout(new java.awt.GridLayout(2, 3, 20, 20));
    tabbedPane.addTab("강의실 안내", pnlClass);

    pnlFood.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlFood.setLayout(new java.awt.GridLayout(2, 3, 20, 20));
    tabbedPane.addTab("학식 정보", pnlFood);

    pnlMap.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlMap.setLayout(new java.awt.GridLayout(2, 3, 20, 20));
    tabbedPane.addTab("캠퍼스 지도", pnlMap);
    
    pnlAcademicSchedule = new javax.swing.JPanel();
    pnlAcademicSchedule.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlAcademicSchedule.setLayout(new java.awt.GridLayout(2, 3, 20, 20));
    tabbedPane.addTab("학사 일정", pnlAcademicSchedule);
    
    pnlLostThings = new javax.swing.JPanel();
    pnlLostThings.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlLostThings.setLayout(new java.awt.GridLayout(2, 3, 20, 20));
    tabbedPane.addTab("분실물찾기", pnlLostThings);


    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
    pnlContent.add(tabbedPane, gridBagConstraints);

    pnlOrder.setPreferredSize(new java.awt.Dimension(472, 600));
    java.awt.GridBagLayout jPanel1Layout = new java.awt.GridBagLayout();
    jPanel1Layout.columnWidths = new int[] {140, 0};
    pnlOrder.setLayout(jPanel1Layout);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
    gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
    pnlOrder.add(pnlTotal, gridBagConstraints);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    pnlContent.add(pnlOrder, gridBagConstraints);

    getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);

    pnlFooter.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
    pnlFooter.setLayout(new java.awt.GridBagLayout());

    btnBack.setText("Back");
    btnBack.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnBackActionPerformed(evt);
      }
    });
    pnlFooter.add(btnBack, new java.awt.GridBagConstraints());

    btnContinue.setText("Continue");
    btnContinue.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btnContinueActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.weightx = 1.0;
    pnlFooter.add(btnContinue, gridBagConstraints);

    getContentPane().add(pnlFooter, java.awt.BorderLayout.SOUTH);

    getAccessibleContext().setAccessibleName("");

    pack();
    setLocationRelativeTo(null);  
  }
  
  private javax.swing.JTable noticeTable;
  private javax.swing.table.DefaultTableModel noticeTableModel;
  
  private void initModels() {
    itemService = new services.ItemService();
    itemsNoticeFrame = itemService.getAllByCategory(1);

    tbmOrder = new OrderTable();
  }
  private void initState() {
    getAllOrderedItems();
  }
  private void initCustomComponents() {
	    itemsNoticeFrame.forEach((item) -> {
	      BtnItem btnItem = new BtnItem(item);
	      btnItem.addActionListener((java.awt.event.ActionEvent evt) -> itemActionPeformed(item));
	      pnlNotice.add(btnItem);
	    });
	}
  private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {
    new StartFrame().setVisible(true);
    dispose();
  }
  private void btnContinueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContinueActionPerformed
    java.util.ArrayList<models.OrderDetail> orderedItems = StateManager.getOrderedItems();
    if (orderedItems != null && orderedItems.size() > 0) {
      new OrderSummaryFrame().setVisible(true);
      dispose();
    } else {
      javax.swing.JOptionPane.showMessageDialog(null, "오류", "오류", javax.swing.JOptionPane.WARNING_MESSAGE);
    }
  } 
  private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {
	    int tabIndex = tabbedPane.getSelectedIndex();

	    if (tabIndex == 0) {
	        NoticeFrame.resetNoticeLoadedFlag();
	        NoticeFrame.showNoticeTableOnPanel(pnlNotice);
	    }

       if (tabIndex == 1 && itemsClass == null) {
         itemsClass = itemService.getAllByCategory(2);
         itemsClass.forEach((item) -> {
           BtnItem btnItem = new BtnItem(item);
           btnItem.addActionListener((java.awt.event.ActionEvent e) -> itemActionPeformed(item));
           pnlClass.add(btnItem);
         });
       }

       if (tabIndex == 2 && itemsFood == null) {
          JPanel mealInfoPanel = CafeTeria.getMealInfoPanel();
          
          pnlFood.removeAll();
          pnlFood.setLayout(new BorderLayout());
          pnlFood.add(mealInfoPanel, BorderLayout.CENTER);
          pnlFood.revalidate();
          pnlFood.repaint();
        }
       
       if (tabIndex == 3 && itemsMap == null) {
           java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
           try {
             java.net.URI uri = new java.net.URI("file:///C:/jolspring/Smart%20Campus_Map.html");
             desktop.browse(uri);
           } catch (java.net.URISyntaxException | java.io.IOException ex) {
             ex.printStackTrace();
           }
         }
       
       if (tabIndex == 4) {
           // "학사 일정" 탭이 선택되었을 때
           AcademicScheduleFrame academicScheduleFrame = new AcademicScheduleFrame();
           
           pnlAcademicSchedule.removeAll();
           pnlAcademicSchedule.setLayout(new BorderLayout());
           pnlAcademicSchedule.add(academicScheduleFrame, BorderLayout.CENTER);
           pnlAcademicSchedule.revalidate();
           pnlAcademicSchedule.repaint();
       }
       if (tabIndex == 5) { // "분실물찾기" 탭이 선택되었을 때
           LostThings lostThingsFrame = new LostThings();
           pnlLostThings.removeAll();
           pnlLostThings.setLayout(new BorderLayout());
           pnlLostThings.add(lostThingsFrame, BorderLayout.CENTER);
           pnlLostThings.revalidate();
           pnlLostThings.repaint();
       }

   }
  
  
  
  
 
      /* 
  private void loadNotices() {
       try {
           Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
           String sql = "SELECT * FROM notices ORDER BY id DESC";
           Statement statement = connection.createStatement();
           ResultSet resultSet = statement.executeQuery(sql);
           
           noticeTableModel = new javax.swing.table.DefaultTableModel(new Object[]{"번호", "제목", "작성자", "작성일", "조회수"}, 0);
           noticeTable = new javax.swing.JTable(noticeTableModel);

           int noticeCount = 0; // 게시글의 개수를 저장할 변수입니다.
           
           while (resultSet.next()) {
               Object[] row = {
                   resultSet.getInt("id"),
                   resultSet.getString("title"),
                   resultSet.getString("author"),
                   resultSet.getString("date"),
                   resultSet.getInt("viewCount")
               };
               noticeTableModel.addRow(row);
               noticeCount++; // 게시글의 개수를 증가시킵니다.
           }

           // 게시글의 총 개수를 레이블에 표시합니다.
           JLabel lblNoticeCount = new JLabel("총 " + noticeCount + " 개의 게시물이 있습니다.");

           pnlNotice.removeAll();
           pnlNotice.setLayout(new BorderLayout());
           pnlNotice.add(lblNoticeCount, BorderLayout.NORTH);
           pnlNotice.add(new JScrollPane(noticeTable), BorderLayout.CENTER);
           
           pnlNotice.revalidate();
           pnlNotice.repaint();
       } catch (SQLException e) {
           e.printStackTrace();
           JOptionPane.showMessageDialog(null, "데이터베이스 연결 또는 쿼리 중 오류 발생: " + e.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
       }
   }
*/





       
     /*
  private void showNoticeDetails(String title) {
     System.out.println("showNoticeDetails called with title: " + title);

       try {
           Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8", "root", "dongyang");
           String sql = "SELECT content FROM notices WHERE title = ?";
           PreparedStatement statement = connection.prepareStatement(sql);
           statement.setString(1, title);
           ResultSet rs = statement.executeQuery();
           if (rs.next()) {
               String content = rs.getString("content");
               JTextArea textArea = new JTextArea(10, 40);
               textArea.setText(content);
               textArea.setWrapStyleWord(true);
               textArea.setLineWrap(true);
               textArea.setCaretPosition(0);
               textArea.setEditable(false);

               JDialog dialog = new JDialog();
               dialog.setTitle("Notice Details");
               dialog.setSize(400, 300);
               dialog.add(new JScrollPane(textArea));
               dialog.setLocationRelativeTo(null);  // center on screen
               dialog.setVisible(true);

           }
           rs.close();
           statement.close();
           connection.close();
       } catch (Exception ex) {
           ex.printStackTrace();
       }
   }
   */
       
  private void itemActionPeformed(models.Item item) {
    CustomizeDialog customizeDialog = new CustomizeDialog(item);
    customizeDialog.addObserver(this);
    customizeDialog.setVisible(true);
  }

  private void getAllOrderedItems() {
    java.util.ArrayList<models.OrderDetail> orderedItems = StateManager.getOrderedItems();
    if (orderedItems != null) {
      tbmOrder.addRows(orderedItems);
      tbmOrder.resizeColumns(tblOrder.getColumnModel());
      app.Global.setTotalPrice(orderedItems, lblTotalValue);
    }
  }

  @Override
  public void onStateChange() {
    getAllOrderedItems();
  }
  public static void main(String args[]) {
    app.Global.setDefaultTheme();
    java.awt.EventQueue.invokeLater(() -> {
      new MenuFrame().setVisible(true);
    });
  }
  private javax.swing.JButton btnBack;
  private javax.swing.JButton btnContinue;
  private javax.swing.JLabel lblOrder;
  private javax.swing.JLabel lblTotal;
  private javax.swing.JLabel lblTotalValue;
  private javax.swing.JPanel pnlMap;
  private javax.swing.JPanel pnlContent;
  private javax.swing.JPanel pnlFood;
  private javax.swing.JPanel pnlFooter;
  private javax.swing.JPanel pnlNotice;
  private javax.swing.JPanel pnlOrder;
  private javax.swing.JPanel pnlClass;
  private javax.swing.JPanel pnlTotal;
  private javax.swing.JScrollPane scpOrder;
  private javax.swing.JTabbedPane tabbedPane;
  private javax.swing.JTable tblOrder;
  private javax.swing.JPanel pnlAcademicSchedule;
}

class BtnItem extends javax.swing.JButton {

  BtnItem(models.Item item) {
    setIcon(app.Global.getImagePreview(item.getImage(), 100, 100, this));
    setText("<html><center>" + item.getName() + "<p style=\"font-weight:bold;margin-top:10px\">" + app.Global.toCurrency(item.getPrice()) + "</p></center></html>");
    setAlignmentY(0.0F);
    setHideActionText(true);
    setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    setIconTextGap(20);
    setPreferredSize(new java.awt.Dimension(160, 160));
    setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
  }

}