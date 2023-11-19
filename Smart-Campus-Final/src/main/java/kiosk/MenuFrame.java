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

import Admin_Login_Notice.NoticeFrame;
import Admin_LostThi.LostThings;
import Kiosk_AcademicSchedule.AcademicScheduleFrame;
import Kiosk_Club.HobbyClub;
import Kiosk_graduation.GraduationMain;
import Kisok_Scholarship.ScholarshipMain;
import Kiosk_Metro.*;
import java.awt.FlowLayout;

public class MenuFrame extends javax.swing.JFrame implements StateObserver {

  services.ItemService itemService;
  java.util.ArrayList<models.Item> itemsNoticeFrame;
  java.util.ArrayList<models.Item> itemsClass;
  java.util.ArrayList<models.Item> itemsFood;
  java.util.ArrayList<models.Item> itemsMap;
  private javax.swing.JPanel pnlMetro;
  
  public MenuFrame() {
       try {
           app.Global.setAppIcon(this);
           initModels();
           initComponents();
           initAllTabs();
           initState();
       } catch (Exception e) {
           e.printStackTrace(); 
           JOptionPane.showMessageDialog(this, "오류발생" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
       }
   }
  private void initAllTabs() {
       NoticeFrame.showNoticeTableOnPanel(pnlNotice);
       initHobbyClubPanel();
       initScholarshipPanel();
       initClassPanel();  
       initAcademicSchedulePanel();
       initGraduationPanel();
       initMetroPanel();
   }
  private void initHobbyClubPanel() {
       HobbyClub hobbyClubPanel = new HobbyClub();
       pnlHobbyClub.setLayout(new BorderLayout());
       pnlHobbyClub.add(hobbyClubPanel, BorderLayout.CENTER);
   }
  private void initScholarshipPanel() {
       ScholarshipMain scholarshipPanel = new ScholarshipMain();
       pnlScholarship.setLayout(new BorderLayout());
       pnlScholarship.add(scholarshipPanel, BorderLayout.CENTER);
   }
  private void initClassPanel() {
       EmergencyClassroom emergencyClassroom = new EmergencyClassroom();
       pnlClass.removeAll();
       pnlClass.setLayout(new BorderLayout());
       pnlClass.add(emergencyClassroom, BorderLayout.CENTER);
       pnlClass.revalidate();
       pnlClass.repaint();
   }
   private void initAcademicSchedulePanel() {
       AcademicScheduleFrame academicScheduleFrame = new AcademicScheduleFrame();         
       pnlAcademicSchedule.removeAll();
       pnlAcademicSchedule.setLayout(new BorderLayout());
       pnlAcademicSchedule.add(academicScheduleFrame, BorderLayout.CENTER);
       pnlAcademicSchedule.revalidate();
       pnlAcademicSchedule.repaint();
   }
   private void initGraduationPanel() {
       GraduationMain graduationMainPanel = new GraduationMain();
       pnlGraduation.removeAll();
       pnlGraduation.setLayout(new BorderLayout());
       pnlGraduation.add(graduationMainPanel, BorderLayout.CENTER);
       pnlGraduation.revalidate();
       pnlGraduation.repaint();
   }
   private void initMetroPanel() {
       Kiosk_Metro.MainMain metroMain = new Kiosk_Metro.MainMain(); 
       pnlMetro.setLayout(new BorderLayout()); 
       pnlMetro.add(metroMain.getMainPanel(), BorderLayout.CENTER); 
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
    pnlMetro = new javax.swing.JPanel();
    
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("Menu");
    setMinimumSize(new java.awt.Dimension(1800, 800));
    setName("");
    setPreferredSize(new java.awt.Dimension(1800, 800));

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
       
    pnlMetro = new javax.swing.JPanel();
    pnlMetro.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlMetro.setLayout(new java.awt.BorderLayout());

    Kiosk_Metro.MainMain metroMain = new Kiosk_Metro.MainMain();

    pnlMetro.add(metroMain.getMainPanel(), BorderLayout.CENTER);
    
    pnlAcademicSchedule = new javax.swing.JPanel();
    pnlAcademicSchedule.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlAcademicSchedule.setLayout(new java.awt.GridLayout(2, 3, 20, 20));
    tabbedPane.addTab("학사 일정", pnlAcademicSchedule);
    
    tabbedPane.addTab("대중교통", pnlMetro);
    
    CampusMapFXFrame campusMapFXFrame = new CampusMapFXFrame();
    tabbedPane.addTab("캠퍼스 지도", campusMapFXFrame);
    
    pnlFood.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlFood.setLayout(new java.awt.GridLayout(2, 3, 20, 20));
    tabbedPane.addTab("학식 정보", pnlFood);
 
    pnlScholarship = new javax.swing.JPanel();
    pnlScholarship.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlScholarship.setLayout(new java.awt.GridLayout(2, 3, 20, 20));
    tabbedPane.addTab("장학/등록", pnlScholarship);

    pnlHobbyClub = new javax.swing.JPanel();
    pnlHobbyClub.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlHobbyClub.setLayout(new java.awt.GridLayout(2, 3, 20, 20));
    tabbedPane.addTab("취미동아리/전공동아리", pnlHobbyClub);

    pnlClass.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlClass.setLayout(new java.awt.GridLayout(2, 3, 20, 20));
    tabbedPane.addTab("교직원 검색", pnlClass);

    pnlGraduation = new javax.swing.JPanel();
    pnlGraduation.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 20));
    pnlGraduation.setLayout(new java.awt.GridLayout(2, 3, 20, 20));
    tabbedPane.addTab("졸업정보", pnlGraduation);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
    pnlContent.add(tabbedPane, gridBagConstraints);

    pnlOrder.setPreferredSize(new java.awt.Dimension(472, 400));
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
    gridBagConstraints.weighty = 0.5;
    pnlContent.add(pnlOrder, gridBagConstraints);

    getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);

    pnlFooter.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
    pnlFooter.setLayout(new java.awt.GridBagLayout());

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
    gridBagConstraints.weightx = 1.0;

    getContentPane().add(pnlFooter, java.awt.BorderLayout.SOUTH);

    getAccessibleContext().setAccessibleName("");
    
    pack();
    setLocationRelativeTo(null);  
  }
  
  private javax.swing.JTable noticeTable;
  private javax.swing.table.DefaultTableModel noticeTableModel;
  private javax.swing.JPanel pnlScholarship;

  private void initModels() {
    itemService = new services.ItemService();
    itemsNoticeFrame = itemService.getAllByCategory(1);

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
  private void tabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {
       int tabIndex = tabbedPane.getSelectedIndex();

       if (tabIndex == 0) {
           NoticeFrame.resetNoticeLoadedFlag();
           NoticeFrame.showNoticeTableOnPanel(pnlNotice);
       }
      if (tabIndex == 1) {
           ScholarshipMain scholarshipPanel = new ScholarshipMain();
           pnlScholarship.removeAll();
           pnlScholarship.setLayout(new BorderLayout());
           pnlScholarship.add(scholarshipPanel, BorderLayout.CENTER);
           pnlScholarship.revalidate();
           pnlScholarship.repaint();
       }
      if (tabIndex == 2) {
           if (pnlClass.getComponentCount() == 0) {
               EmergencyClassroom emergencyClassroom = new EmergencyClassroom();
               pnlClass.setLayout(new BorderLayout());
               pnlClass.add(emergencyClassroom, BorderLayout.CENTER);
               pnlClass.revalidate();
               pnlClass.repaint();
           }
       }
          if (tabIndex == 4) { 
              pnlFood.removeAll(); 
              JPanel webPanel = CafeTeria.createWebPanel(); 
              pnlFood.setLayout(new BorderLayout()); 
              pnlFood.add(webPanel, BorderLayout.CENTER);
              pnlFood.revalidate();
              pnlFood.repaint();
          }
  }
  private void itemActionPeformed(models.Item item) {
    CustomizeDialog customizeDialog = new CustomizeDialog(item);
    customizeDialog.addObserver(this);
    customizeDialog.setVisible(true);
  }
  private void getAllOrderedItems() {
    java.util.ArrayList<models.OrderDetail> orderedItems = StateManager.getOrderedItems();
    if (orderedItems != null) {
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
  private javax.swing.JPanel pnlHobbyClub;
  private javax.swing.JPanel pnlGraduation;
  private javax.swing.JPanel pnlmetro;
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