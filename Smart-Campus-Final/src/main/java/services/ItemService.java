package services;

import java.awt.BorderLayout;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import models.Category;
import models.Item;

public class ItemService extends DatabaseService {
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ItemService::initAndShowGUI);
    }

    private static void initAndShowGUI() {
        // JFrame을 생성합니다.
        JFrame frame = new JFrame("Swing and JavaFX WebView Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 메인 JPanel을 생성하고 JFrame에 추가합니다.
        JPanel panel = new JPanel(new BorderLayout());
        frame.add(panel);

        // JFXPanel을 생성하고 JPanel에 추가합니다.
        final JFXPanel fxPanel = new JFXPanel();
        panel.add(fxPanel, BorderLayout.CENTER);

        // JavaFX Scene을 초기화하고 WebView를 로드합니다.
        Platform.runLater(() -> initFX(fxPanel));

        // 창을 보이게 설정합니다.
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private static void initFX(JFXPanel fxPanel) {
        // Scene 생성 및 JFXPanel에 설정
        Scene scene = createScene();
        fxPanel.setScene(scene);
    }

    private static Scene createScene() {
        // WebView 생성 및 웹 페이지 로드
        WebView webView = new WebView();
        webView.getEngine().load("http://127.0.0.1:5000");

        return new Scene(webView);
    }

  private final String queryItems = "SELECT * FROM items itm JOIN categories cat ON cat.category_id = itm.category_id";

  public ArrayList<Item> getAll() {
    ArrayList<Item> items = new ArrayList<>();

    this.connect();

    try {
      String sql = queryItems.concat(" ORDER BY itm.item_id");
      PreparedStatement stmt = this.conn.prepareStatement(sql);
      ResultSet result = stmt.executeQuery();
      while (result.next()) {
        items.add(mapResultOneItem(result));
      }
    } catch (SQLException e) {
      System.out.println(e);
    }

    this.disconnect();

    return items;
  }

  public ArrayList<Item> getAllByCategory(int id) {
    ArrayList<Item> items = new ArrayList<>();

    this.connect();

    try {
      String sql = queryItems.concat(" WHERE cat.category_id = ?");
      PreparedStatement stmt = this.conn.prepareStatement(sql);
      stmt.setInt(1, id);

      ResultSet result = stmt.executeQuery();
      while (result.next()) {
        items.add(mapResultOneItem(result));
      }
    } catch (SQLException e) {
      System.out.println(e);
    }

    this.disconnect();

    return items;
  }

  public Item getOneById(int id) {
    Item item = null;

    this.connect();

    try {
      String sql = queryItems.concat(" WHERE itm.item_id = ? LIMIT 1");
      PreparedStatement stmt = this.conn.prepareStatement(sql);
      stmt.setInt(1, id);

      ResultSet result = stmt.executeQuery();
      if (result.next()) {
        item = mapResultOneItem(result);
      }
    } catch (SQLException e) {
      System.out.println(e);
    }

    this.disconnect();

    return item;
  }

  public int createOne(Item item) {
    int rowCount = 0;

    this.connect();

    try {
      String sql = "INSERT INTO items (item_name, item_price, item_image, category_id) VALUES (?, ?, ?, ?)";
      PreparedStatement stmt = this.conn.prepareStatement(sql);
      stmt.setString(1, item.getName());
      stmt.setDouble(2, item.getPrice());
      stmt.setString(3, item.getImage());
      stmt.setInt(4, item.getCategory().getId());
      rowCount = stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e);
    }

    this.disconnect();

    return rowCount;
  }

  public int updateOne(Item item) {
    int rowCount = 0;

    this.connect();

    try {
      String sql = "UPDATE items SET item_name = ?, item_price = ?, item_image = ?, category_id = ? WHERE item_id = ?";
      PreparedStatement stmt = this.conn.prepareStatement(sql);
      stmt.setString(1, item.getName());
      stmt.setDouble(2, item.getPrice());
      stmt.setString(3, item.getImage());
      stmt.setInt(4, item.getCategory().getId());
      stmt.setInt(5, item.getId());
      rowCount = stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e);
    }

    this.disconnect();

    return rowCount;
  }

  public int deleteOne(int id) {
    int rowCount = 0;

    this.connect();

    try {
      String sql = "DELETE FROM items WHERE item_id = ?";
      PreparedStatement stmt = this.conn.prepareStatement(sql);
      stmt.setInt(1, id);
      rowCount = stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e);
    }

    this.disconnect();

    return rowCount;
  }

  private Item mapResultOneItem(ResultSet result) {
    Item item = null;

    try {
      Category category = new Category();
      category.setId(result.getInt("category_id"));
      category.setName(result.getString("category_name"));
      item = new Item();
      item.setId(result.getInt("item_id"));
      item.setName(result.getString("item_name"));
      item.setPrice(result.getDouble("item_price"));
      item.setImage(result.getString("item_image"));
      item.setCategory(category);
    } catch (SQLException e) {
      System.out.println(e);
    }

    return item;
  }

}
