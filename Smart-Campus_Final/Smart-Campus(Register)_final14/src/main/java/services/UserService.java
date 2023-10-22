// UserService.java
package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import models.User1;

public class UserService extends DatabaseService {

  public ArrayList<User1> getAll() {
    ArrayList<User1> users = new ArrayList<>();

    this.connect();

    try {
      String sql = "SELECT * FROM User1";
      PreparedStatement stmt = this.conn.prepareStatement(sql);
      ResultSet result = stmt.executeQuery();
      while (result.next()) {
        users.add(mapResultOneUser(result));
      }
    } catch (SQLException e) {
      System.out.println(e);
    }

    this.disconnect();

    return users;
  }

  public User1 getOne(int id) {
    User1 user = null;

    this.connect();

    try {
      String sql = "SELECT * FROM User1 WHERE user_id = ? LIMIT 1";
      PreparedStatement stmt = this.conn.prepareStatement(sql);
      stmt.setInt(1, id);
      ResultSet result = stmt.executeQuery();
      if (result.next()) {
        user = mapResultOneUser(result);
      }
    } catch (SQLException e) {
      System.out.println(e);
    }

    this.disconnect();

    return user;
  }

  public int createOne(User1 user) {
    int rowCount = 0;

    this.connect();

    try {
      String sql = "INSERT INTO User1 (Username, password) VALUES (?, ?)";
      PreparedStatement stmt = this.conn.prepareStatement(sql);
      stmt.setString(1, user.getUsername());
      stmt.setString(2, user.getPassword());
      rowCount = stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e);
    }

    this.disconnect();

    return rowCount;
  }

  public int updateOne(User1 user) {
    int rowCount = 0;

    this.connect();

    try {
      String sql = "UPDATE User1 SET Username = ?, password = ? WHERE user_id = ?";
      PreparedStatement stmt = this.conn.prepareStatement(sql);
      stmt.setString(1, user.getUsername());
      stmt.setString(2, user.getPassword());
      stmt.setInt(3, user.getUser_id());
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
      String sql = "DELETE FROM User1 WHERE user_id = ?";
      PreparedStatement stmt = this.conn.prepareStatement(sql);
      stmt.setInt(1, id);
      rowCount = stmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e);
    }

    this.disconnect();

    return rowCount;
  }

  private User1 mapResultOneUser(ResultSet result) {
    User1 user = null;

    try {
      user = new User1();
      user.setUser_id(result.getInt("user_id"));
      user.setUsername(result.getString("Username"));
      user.setPassword(result.getString("password"));
    } catch (SQLException e) {
      System.out.println(e);
    }

    return user;
  }
}
