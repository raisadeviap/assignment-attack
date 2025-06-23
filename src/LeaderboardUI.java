import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeaderboardUI extends JFrame {

    private int cloudX1 = 100, cloudX2 = 300, cloudX3 = 500;

    public LeaderboardUI() {
        setTitle("Leaderboard Score");
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

                // Background
                g2d.setColor(new Color(103, 215, 244));
                g2d.fillRect(0, 0, width, height);

                // Awan bergerak
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

                // Podium
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(width / 2 - 60, height - 210, 50, 100); // Juara 1
                g2d.fillRect(width / 2 - 120, height - 170, 50, 60); // Juara 2
                g2d.fillRect(width / 2 + 10, height - 150, 50, 40);  // Juara 3

                // Emoji Piala
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
                g2d.drawString("🏆", width / 2 - 50, height - 220);
                g2d.drawString("🏆", width / 2 - 110, height - 180);
                g2d.drawString("🏆", width / 2 + 20, height - 160);

                // Nama Juara
                g2d.setFont(new Font("Arial", Font.BOLD, 14));
                g2d.setColor(Color.BLACK);
                g2d.drawString("Dayu", width / 2 - 50, height - 265);
                g2d.drawString("Raisa", width / 2 - 128, height - 225);
                g2d.drawString("Shofi", width / 2 + 8, height - 205);
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

        mainPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel titleLabel = new JLabel("LEADERBOARD SCORE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 42));
        titleLabel.setForeground(new Color(0, 90, 40));
        titleLabel.setBounds(30, 20, 740, 50);

        JLabel gameLabel = new JLabel("🎮 Assignment Attack 🎮", SwingConstants.CENTER);
        gameLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 26));
        gameLabel.setForeground(new Color(0, 120, 60));
        gameLabel.setBounds(30, 70, 740, 40);

        String[] columns = {"🏅 Rank", "👤 Player", "🎯 Score"};
        Object[][] data = {
                {"🥇 1", "Dayu", "990"},
                {"🥈 2", "Raisa", "930"},
                {"🥉 3", "Shofi", "890"},
                {"4", "Zelda", "800"},
                {"5", "Lia", "750"},
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        table.setRowHeight(40);
        table.setBackground(new Color(240, 255, 240));
        table.setForeground(Color.BLACK);
        table.setRowSelectionAllowed(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        header.setBackground(new Color(220, 255, 220));
        header.setForeground(new Color(0, 102, 51));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(100, 130, 600, 200);
        scrollPane.getViewport().setBackground(new Color(250, 250, 240));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 0), 5));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        mainPanel.add(titleLabel);
        mainPanel.add(gameLabel);
        mainPanel.add(scrollPane);
        setContentPane(mainPanel);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new LeaderboardUI().setVisible(true));
    }
}







