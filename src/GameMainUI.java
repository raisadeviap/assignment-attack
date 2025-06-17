import javax.swing.*;

public class GameMainUI extends JFrame {
    public GameMainUI() {
        setTitle("Assignment Attack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        AssignmentAttackGame gamePanel = new AssignmentAttackGame();
        add(gamePanel);

        pack();
        setLocationRelativeTo(null); // Center screen
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameMainUI());
    }
}
