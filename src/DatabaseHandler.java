import java.io.File;
import java.sql.*;
import java.util.*;

public class DatabaseHandler {
    private static final String DB_FOLDER = "C:/sqllite";
    private static final String DB_PATH = DB_FOLDER + "/skor_pengguna.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;
    private static DatabaseHandler instance;

    private DatabaseHandler() {
        ensureDatabaseFolderExists();
        createTableIfNeeded();
    }

    public static synchronized DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    private void ensureDatabaseFolderExists() {
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            if (created) {
                System.out.println("📁 Folder database berhasil dibuat: " + DB_FOLDER);
            } else {
                System.err.println("❌ Gagal membuat folder database: " + DB_FOLDER);
            }
        }
    }

    private void createTableIfNeeded() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL");
            String sql = "CREATE TABLE IF NOT EXISTS scores (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nama VARCHAR(255)," +
                    "skor INT," +
                    "timestamp TEXT DEFAULT CURRENT_TIMESTAMP)";
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void saveScore(String nama, int skor) {
        String sql = "INSERT INTO scores(nama, skor) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nama);
            pstmt.setInt(2, skor);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Gagal menyimpan skor:");
            e.printStackTrace();
        }
    }

    public synchronized List<String> getTopScores(int limit) {
        List<String> scores = new ArrayList<>();
        String sql = "SELECT nama, skor, timestamp FROM scores ORDER BY skor DESC LIMIT ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String entry = rs.getString("nama") + " - " + rs.getInt("skor") + " pts";
                    scores.add(entry);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Gagal mengambil skor:");
            e.printStackTrace();
        }
        return scores;
    }
}
