package Kiosk_Metro;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BusInfoApp {
    private static final String API_ENDPOINT_BUS_STATION = "http://ws.bus.go.kr/api/rest/stationinfo/getStationByName";
    private static final String SERVICE_KEY = "vAf4PQroMlMrEi8Qxuzbye09EV02MzY1Dqz8HyLGitB8P9Kx5Iu6tcN%2FJM9mkchLtyEV1yH9zjKmnYry%2BW9gyw%3D%3D";

    private JTextField tfSearch;
    private JButton btnSearch;
    private JButton btnNearbyStations;
    private JList<BusStation> listBusStations;
    private DefaultListModel<BusStation> busStationListModel;
    private JPanel mainPanel;

    public BusInfoApp() {
        mainPanel = new JPanel(new BorderLayout());
        initialize();
    }
    private void initialize() {
        tfSearch = new JTextField("버스 정류소 이름을 검색해주세요.", 20);
        tfSearch.setForeground(Color.GRAY);
        tfSearch.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (tfSearch.getText().equals("버스 정류소 이름을 검색해주세요.")) {
                    tfSearch.setText("");
                    tfSearch.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (tfSearch.getText().isEmpty()) {
                    tfSearch.setForeground(Color.GRAY);
                    tfSearch.setText("버스 정류소 이름을 검색해주세요.");
                }
            }
        });
        btnSearch = new JButton("Search");
        btnSearch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchBusStation();
            }
        });
        busStationListModel = new DefaultListModel<>();
        listBusStations = new JList<>(busStationListModel);
        listBusStations.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    BusStation selectedStation = listBusStations.getSelectedValue();
                    displayBusInfoForStation(selectedStation);
                }
            }
        });     
        btnNearbyStations = new JButton("근처 정류소");
        btnNearbyStations.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tfSearch.setText("동양미래대학");
                searchBusStation();
            }
        });
        listBusStations.setCellRenderer(new BusStationCellRenderer());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(btnNearbyStations);
        searchPanel.add(tfSearch);
        searchPanel.add(btnSearch);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JScrollPane scrollPane = new JScrollPane(listBusStations);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }
    private void searchBusStation() {
        String searchKeyword = tfSearch.getText();
        try {
            URL url = new URL(API_ENDPOINT_BUS_STATION + "?serviceKey=" + SERVICE_KEY + "&stSrch=" + URLEncoder.encode(searchKeyword, "UTF-8"));
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url.openStream());
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("itemList");
            busStationListModel.clear();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String arsId = getTagValue("arsId", element);
                    String stNm = getTagValue("stNm", element);
                    busStationListModel.addElement(new BusStation(arsId, stNm));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void displayBusInfoForStation(BusStation station) {
       System.out.println("Displaying info for station: " + station.getStId()); 
        try {
            String apiUrl = String.format("http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?serviceKey=%s&arsId=%s",
                                          SERVICE_KEY, station.getStId());
            URL url = new URL(apiUrl);
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(url.openStream());
            
            doc.getDocumentElement().normalize();
            
            NodeList busInfoList = doc.getElementsByTagName("itemList");
            
            DefaultListModel<String> busInfoJListModel = new DefaultListModel<>();
            
            Node node;
            Element element;
            String busRouteId;
            String rtNm;
            String arrmsg1;
            String arrmsg2;
            for (int i = 0; i < busInfoList.getLength(); i++) {
                node = busInfoList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    element = (Element) node;
                    
                    busRouteId = getTagValue("busRouteId", element);
                    rtNm = getTagValue("rtNm", element);
                    arrmsg1 = getTagValue("arrmsg1", element);
                    arrmsg2 = getTagValue("arrmsg2", element);
                    
                    String busInfo = "Bus Route ID: " + busRouteId +
                                     ", Route Name: " + rtNm +
                                     ", Arrival Message 1: " + arrmsg1 +
                                     ", Arrival Message 2: " + arrmsg2;
                    busInfoJListModel.addElement(busInfo);
                }
            }

            JList<String> busInfoJList = new JList<>(busInfoJListModel);
            
            JFrame infoFrame = new JFrame("Bus Information");
            infoFrame.setTitle("버스");
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            infoFrame.setLayout(new BorderLayout());
            infoFrame.add(new JScrollPane(busInfoJList), BorderLayout.CENTER);
            infoFrame.pack();
            infoFrame.setLocationRelativeTo(null);
            infoFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    class BusStationCellRenderer extends JPanel implements ListCellRenderer<BusStation> {
        private JLabel label;
        private ImageIcon busStopIcon;

        public BusStationCellRenderer() {
            setPreferredSize(new Dimension(200, 50)); 
            setLayout(new BorderLayout(5, 0)); 
            label = new JLabel();
            label.setOpaque(true);
            add(label, BorderLayout.CENTER);

            ImageIcon originalIcon = new ImageIcon(BusInfoApp.class.getResource("/icons/bus_StopIcon.png"));
            Image scaledImage = originalIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            busStopIcon = new ImageIcon(scaledImage);

            JLabel iconLabel = new JLabel(busStopIcon);
            add(iconLabel, BorderLayout.WEST);
        }

        @Override
        public Component getListCellRendererComponent(
                JList<? extends BusStation> list,
                BusStation value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            label.setText(value.getStNm());

            if (isSelected) {
                label.setBackground(list.getSelectionBackground());
                label.setForeground(list.getSelectionForeground());
            } else {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }

            return this;
        }
    }
}
class BusStation {
    private String arsId;
    private String stNm;

    public BusStation(String arsId, String stNm) {
        this.arsId = arsId;
        this.stNm = stNm;
    }

    public String getStId() {
        return arsId;
    }

    public String getStNm() {
        return stNm;
    }

    @Override
    public String toString() {
        return stNm;
    }
}

class BusStationCellRenderer extends JPanel implements ListCellRenderer<BusStation> {
    private JLabel label;
    private ImageIcon busStopIcon;

    public BusStationCellRenderer() {
        setPreferredSize(new Dimension(200, 50)); 
        setLayout(new BorderLayout(5, 0)); 
        label = new JLabel();
        label.setOpaque(true);
        add(label, BorderLayout.CENTER);

        ImageIcon originalIcon = new ImageIcon(getClass().getResource("bus_stop.png")); 
        Image scaledImage = originalIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
        busStopIcon = new ImageIcon(scaledImage);

        JLabel iconLabel = new JLabel(busStopIcon);
        add(iconLabel, BorderLayout.WEST);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends BusStation> list,
            BusStation value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        label.setText(value.getStNm());

        if (isSelected) {
            label.setBackground(list.getSelectionBackground());
            label.setForeground(list.getSelectionForeground());
        } else {
            label.setBackground(list.getBackground());
            label.setForeground(list.getForeground());
        }

        return this;
    }
}
