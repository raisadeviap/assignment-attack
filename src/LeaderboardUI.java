import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class LeaderboardUI extends JFrame {

    public LeaderboardUI() {
        setTitle("Assignment Attack - Leaderboard");
        setSize(600, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, new Color(180, 255, 180), 0, getHeight(), new Color(60, 150, 60)));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Font gameFont = new Font("Press Start 2P", Font.PLAIN, 14);
        Font gameFontBig = new Font("Press Start 2P", Font.BOLD, 20);
        Font gameFontHuge = new Font("Press Start 2P", Font.BOLD, 28);

        JLabel leaderboardTitle = new JLabel("\uD83C\uDFC6 LEADERBOARD SCORE \uD83C\uDFC6", SwingConstants.CENTER);
        leaderboardTitle.setFont(gameFontHuge);
        leaderboardTitle.setForeground(new Color(0, 70, 0));
        leaderboardTitle.setBorder(new EmptyBorder(10, 0, 20, 0));

        JLabel gameTitle = new JLabel("\uD83C\uDFAE Assignment Attack \uD83C\uDFAE", SwingConstants.CENTER);
        gameTitle.setFont(gameFontBig);
        gameTitle.setForeground(new Color(0, 90, 40));
        gameTitle.setBorder(new EmptyBorder(10, 0, 20, 0));

        String[] columnNames = {"🏅 Rank", "👤 Player", "🎯 Score"};

        DatabaseHandler db = DatabaseHandler.getInstance();
        List<String> topScores = db.getTopScores(10);
        Object[][] data = new Object[topScores.size()][3];

        for (int i = 0; i < topScores.size(); i++) {
            String[] parts = topScores.get(i).split(" - ");
            String rankDisplay = switch (i) {
                case 0 -> "🥇";
                case 1 -> "🥈";
                case 2 -> "🥉";
                default -> "🏅 " + (i + 1);
            };

            data[i][0] = rankDisplay; // ✅ diperbaiki agar tidak double
            data[i][1] = parts.length > 0 ? parts[0] : "Unknown";
            data[i][2] = parts.length > 1 ? parts[1].replace(" pts", "") : "0";
        }

        JTable table = new JTable(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setFont(gameFont);
        table.setRowHeight(60);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(new Color(255, 255, 240));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, row, col);
                c.setForeground(Color.BLACK);
                c.setFont(gameFont);
                setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(0, 100, 0)));
                if (row == 0) {
                    c.setBackground(new Color(255, 230, 150));
                } else if (row == 1) {
                    c.setBackground(new Color(210, 210, 210));
                } else if (row == 2) {
                    c.setBackground(new Color(220, 180, 130));
                } else {
                    c.setBackground(new Color(240, 255, 240));
                }
                return c;
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setFont(gameFont);
        header.setBackground(new Color(0, 100, 50));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(100, 45));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 0), 4));
        scrollPane.getViewport().setBackground(new Color(220, 255, 220));
        scrollPane.setPreferredSize(new Dimension(520, 350));

        mainPanel.add(leaderboardTitle, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(gameTitle, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            LeaderboardUI frame = new LeaderboardUI();
            frame.setVisible(true);
        });
    }
}
