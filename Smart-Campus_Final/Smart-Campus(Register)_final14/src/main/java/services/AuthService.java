package services;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService extends DatabaseService {

    public boolean getAuth(models.User1 user) {
        boolean isAuth = false;

        this.connect();

        try {
            String sql = "SELECT * FROM user1 WHERE username = ? AND password = ? LIMIT 1";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isAuth = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.disconnect(); // 연결 종료
        }

        return isAuth;
    }
}
