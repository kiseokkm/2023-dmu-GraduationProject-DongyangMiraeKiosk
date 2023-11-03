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

import java.awt.FlowLayout;

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

	        // 모든 탭들을 초기화
	        initAllTabs();
	        initState();
	    } catch (Exception e) {
	        e.printStackTrace(); // 오류 메세지 출력
	        JOptionPane.showMessageDialog(this, "오류발생" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
  private void initAllTabs() {
	    // 공지사항 패널 초기화
	    NoticeFrame.showNoticeTableOnPanel(pnlNotice);

	    // 취미동아리 패널 초기화
	    initHobbyClubPanel();

	    // 장학/등록 패널 초기화
	    initScholarshipPanel();

	    // 교직원 검색 패널 초기화
	    initClassPanel();

	    // 학사 일정 패널 초기화
	    initAcademicSchedulePanel();

	    // 졸업정보 패널 초기화
	    initGraduationPanel();
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

	private void initLostThingsPanel() {
	    LostThings lostThingsFrame = new LostThings();
	    pnlLostThings.removeAll();
	    pnlLostThings.setLayout(new BorderLayout());
	    pnlLostThings.add(lostThingsFrame, BorderLayout.CENTER);
	    pnlLostThings.revalidate();
	    pnlLostThings.repaint();
	}

	private void initGraduationPanel() {
	    GraduationMain graduationMainPanel = new GraduationMain();
	    pnlGraduation.removeAll();
	    pnlGraduation.setLayout(new BorderLayout());
	    pnlGraduation.add(graduationMainPanel, BorderLayout.CENTER);
	    pnlGraduation.revalidate();
	    pnlGraduation.repaint();
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
  private javax.swing.JPanel pnlScholarship;

  
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
  private void btnContinueActionPerformed(java.awt.event.ActionEvent evt) {
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
      
      if (tabIndex == 1) {
    	    ScholarshipMain scholarshipPanel = new ScholarshipMain();
    	    pnlScholarship.removeAll();
    	    pnlScholarship.setLayout(new BorderLayout());
    	    pnlScholarship.add(scholarshipPanel, BorderLayout.CENTER);
    	    pnlScholarship.revalidate();
    	    pnlScholarship.repaint();
    	}


      if (tabIndex == 2) { // "교직원 검색" 탭이 선택되었을 때
    	    if (pnlClass.getComponentCount() == 0) { // 패널에 아무 컴포넌트도 없는 경우만 추가
    	        EmergencyClassroom emergencyClassroom = new EmergencyClassroom();
    	        pnlClass.setLayout(new BorderLayout());
    	        pnlClass.add(emergencyClassroom, BorderLayout.CENTER);
    	        pnlClass.revalidate();
    	        pnlClass.repaint();
    	    }
    	}

	       if (tabIndex == 3) { // "취미동아리 / C.Ⅰ. Lab" 탭이 선택되었을 때
	    	    HobbyClub hobbyClubPanel = new HobbyClub();
	    	    pnlHobbyClub.removeAll();
	    	    pnlHobbyClub.setLayout(new BorderLayout());
	    	    pnlHobbyClub.add(hobbyClubPanel, BorderLayout.CENTER);
	    	    pnlHobbyClub.revalidate();
	    	    pnlHobbyClub.repaint();
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

	    	if (tabIndex == 5 && itemsMap == null) {
	    	    java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
	    	    try {
	    	        java.net.URI uri = new java.net.URI("file:///C:/jolspring/Smart%20Campus_Map.html");
	    	        desktop.browse(uri);
	    	    } catch (java.net.URISyntaxException | java.io.IOException ex) {
	    	        ex.printStackTrace();
	    	    }
	    	}       
       if (tabIndex == 6) { 
           GraduationMain graduationMainPanel = new GraduationMain();
           pnlGraduation.removeAll();
           pnlGraduation.setLayout(new BorderLayout());
           pnlGraduation.add(graduationMainPanel, BorderLayout.CENTER);
           pnlGraduation.revalidate();
           pnlGraduation.repaint();
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
  private javax.swing.JPanel pnlHobbyClub;
  private javax.swing.JPanel pnlGraduation;

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