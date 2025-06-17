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
        setSize(800, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();

                // Background langit
                g2d.setColor(new Color(103, 215, 244));
                g2d.fillRect(0, 0, width, height);

                // Awan
                g2d.setColor(Color.WHITE);
                drawCloud(g2d, cloudX1, 60);
                drawCloud(g2d, cloudX2, 40);
                drawCloud(g2d, cloudX3, 70);

                // Rumput
                for (int i = 0; i < width; i += 8) {
                    g2d.setColor(new Color(0, (int)(190 + Math.random() * 40), 0));
                    int blade = 22 + (int)(Math.random() * 10);
                    g2d.fillOval(i, height - 110 - blade, 8, blade);
                }

                // Tanah
                g2d.setColor(new Color(102, 51, 0));
                g2d.fillRect(0, height - 110, width, 110);
            }

            private void drawCloud(Graphics2D g2d, int x, int y) {
                g2d.fillOval(x, y, 60, 40);
                g2d.fillOval(x + 30, y - 20, 50, 50);
                g2d.fillOval(x + 50, y + 10, 60, 40);
            }
        };

        Timer timer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cloudX1 = (cloudX1 + 1) % 900;
                cloudX2 = (cloudX2 + 1) % 900;
                cloudX3 = (cloudX3 + 1) % 900;
                mainPanel.repaint();
            }
        });
        timer.start();

        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        Font gameFont = new Font("Press Start 2P", Font.PLAIN, 14);
        Font gameFontBig = new Font("Press Start 2P", Font.BOLD, 20);
        Font gameFontHuge = new Font("Press Start 2P", Font.BOLD, 28);

        JLabel leaderboardTitle = new JLabel("🏆 LEADERBOARD SCORE 🏆", SwingConstants.CENTER);
        leaderboardTitle.setFont(gameFontHuge);
        leaderboardTitle.setForeground(new Color(0, 70, 0));
        leaderboardTitle.setBounds(30, 20, 740, 50);

        JLabel gameTitle = new JLabel("🎮 Assignment Attack 🎮", SwingConstants.CENTER);
        gameTitle.setFont(gameFontBig);
        gameTitle.setForeground(new Color(0, 90, 40));
        gameTitle.setBounds(30, 70, 740, 40);

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

            data[i][0] = rankDisplay;
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
        scrollPane.setBounds(100, 130, 600, 350);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 0), 4));
        scrollPane.getViewport().setBackground(new Color(220, 255, 220));

        mainPanel.add(leaderboardTitle);
        mainPanel.add(gameTitle);
        mainPanel.add(scrollPane);
        setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new LeaderboardUI().setVisible(true));
    }
}
