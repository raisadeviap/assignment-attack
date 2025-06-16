import java.sql.*;
import java.util.*;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:sqlite:scores.db";

    public DatabaseHandler() {
        createTableIfNeeded();
    }

    private void createTableIfNeeded() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS scores (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "score INTEGER NOT NULL," +
                    "timestamp TEXT DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveScore(String name, int score) {
        String sql = "INSERT INTO scores(name, score) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getTopScores(int limit) {
        List<String> scores = new ArrayList<>();
        String sql = "SELECT name, score, timestamp FROM scores ORDER BY score DESC LIMIT ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String entry = rs.getString("name") + " - " + rs.getInt("score") + " pts";
                scores.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }
}
