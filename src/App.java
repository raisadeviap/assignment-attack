// App.java
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        int boardWidth = 750;
        int boardHeight = 250;

        JFrame frame = new JFrame("Assignment Attack");
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AssignmentAttackGame gamePanel = new AssignmentAttackGame();
        frame.add(gamePanel);
        frame.pack();
        gamePanel.requestFocus();
        frame.setVisible(true);
    }
}
