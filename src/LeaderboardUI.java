import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LeaderboardUI extends JFrame {
    private int cloudX1 = 100, cloudX2 = 300, cloudX3 = 500;

    public LeaderboardUI() {
        setTitle("Assignment Attack - Leaderboard");
        setSize(750, 400); // adjusted to match boardWidth and extra vertical space
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                g2d.setColor(new Color(103, 215, 244));
                g2d.fillRect(0, 0, width, height);

                g2d.setColor(Color.WHITE);
                drawCloud(g2d, cloudX1, 60);
                drawCloud(g2d, cloudX2, 40);
                drawCloud(g2d, cloudX3, 70);

                for (int i = 0; i < width; i += 8) {
                    g2d.setColor(new Color(0, (int)(190 + Math.random() * 40), 0));
                    int blade = 22 + (int)(Math.random() * 10);
                    g2d.fillOval(i, height - 90 - blade, 8, blade);
                }

                g2d.setColor(new Color(102, 51, 0));
                g2d.fillRect(0, height - 90, width, 90);
            }

            private void drawCloud(Graphics2D g2d, int x, int y) {
                g2d.fillOval(x, y, 60, 40);
                g2d.fillOval(x + 30, y - 20, 50, 50);
                g2d.fillOval(x + 50, y + 10, 60, 40);
            }
        };

        Timer timer = new Timer(50, e -> {
            cloudX1 = (cloudX1 + 1) % 850;
            cloudX2 = (cloudX2 + 1) % 850;
            cloudX3 = (cloudX3 + 1) % 850;
            mainPanel.repaint();
        });
        timer.start();

        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        Font gameFont = new Font("Press Start 2P", Font.PLAIN, 12);
        Font gameFontBig = new Font("Press Start 2P", Font.BOLD, 16);
        Font gameFontHuge = new Font("Press Start 2P", Font.BOLD, 20);

        JLabel leaderboardTitle = new JLabel("\uD83C\uDFC6 LEADERBOARD SCORE \uD83C\uDFC6", SwingConstants.CENTER);
        leaderboardTitle.setFont(gameFontHuge);
        leaderboardTitle.setForeground(new Color(0, 70, 0));
        leaderboardTitle.setBounds(0, 10, 750, 40);

        JLabel gameTitle = new JLabel("\uD83C\uDFAE Assignment Attack \uD83C\uDFAE", SwingConstants.CENTER);
        gameTitle.setFont(gameFontBig);
        gameTitle.setForeground(new Color(0, 90, 40));
        gameTitle.setBounds(0, 50, 750, 30);

        String[] columnNames = {"\uD83C\uDFC5 Rank", "\uD83D\uDC64 Player", "\uD83C\uDFAF Score"};
        DatabaseHandler db = DatabaseHandler.getInstance();
        List<String> topScores = db.getTopScores(10);
        Object[][] data = new Object[topScores.size()][3];

        for (int i = 0; i < topScores.size(); i++) {
            String[] parts = topScores.get(i).split(" - ");
            String rankDisplay = switch (i) {
                case 0 -> "\uD83E\uDD47";
                case 1 -> "\uD83E\uDD48";
                case 2 -> "\uD83E\uDD49";
                default -> "\uD83C\uDFC5 " + (i + 1);
            };
            data[i][0] = rankDisplay;
            data[i][1] = parts.length > 0 ? parts[0] : "Unknown";
            data[i][2] = parts.length > 1 ? parts[1].replace(" pts", "") : "0";
        }

        JTable table = new JTable(data, columnNames) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table.setFont(gameFont);
        table.setRowHeight(40);
        table.setShowGrid(true);
        table.setGridColor(new Color(0, 100, 0));
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(new Color(255, 255, 240));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 14));
        header.setBackground(new Color(0, 100, 0));
        header.setForeground(Color.BLACK);
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(100, 35));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(75, 90, 600, 180);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 0), 3));
        scrollPane.getViewport().setBackground(new Color(220, 255, 220));

        JButton mainButton = new JButton("MAIN LAGI");
        mainButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mainButton.setBackground(new Color(34, 139, 34));
        mainButton.setForeground(Color.BLACK);
        mainButton.setFocusPainted(false);
        mainButton.setBounds((750 - 140) / 2, 280, 140, 30);
        mainButton.addActionListener(e -> {
            dispose();
            new App();
        });

        mainPanel.add(leaderboardTitle);
        mainPanel.add(gameTitle);
        mainPanel.add(scrollPane);
        mainPanel.add(mainButton);

        setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new LeaderboardUI().setVisible(true));
    }
}