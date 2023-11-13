package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {

  private final String url = "jdbc:mysql://localhost:3306/self_order_kiosk?serverTimezone=UTC&characterEncoding=utf-8";
  private final String username = "root";
  private final String password = "dongyang";
  public Connection conn;

  public DatabaseService() {
  }

  public void connect() {
    try {
      conn = DriverManager.getConnection(url, username, password);
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  public void disconnect() {
    try {
      conn.close();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

}
