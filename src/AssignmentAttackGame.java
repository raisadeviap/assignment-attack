// AssignmentAttackGame.java
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.sound.sampled.*;
import java.io.*;

public class AssignmentAttackGame extends JPanel implements ActionListener, KeyListener {
    DatabaseHandler db = DatabaseHandler.getInstance();
    boolean inputNamaSelesai = false;
    int boardWidth = 750;
    int boardHeight = 250;
    private String name = "";

    boolean skorSudahDisimpan = false;

    Image backgroundImg;
    Image mahasiswaRunImg;
    Image mahasiswaJumpImg;
    Image mahasiswaCrouchImg;
    Image mahasiswaDeadImg;
    Image laptop1Img;
    Image buku_pendekImg;
    Image leadsImg;
    Image laptop_rusakImg;
    Image buku_panjangImg;
    Image jamImg;
    Image judulImg;

    Clip backgroundMusic;

    JButton startButton;
    JButton restartButton;
    boolean gameStarted = false;

    private boolean showInstruction = false;
    private Timer instructionTimer;

    class Block {
        int x, y, width, height;
        Image img;

        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    int mahasiswaWidth = 88;
    int mahasiswaOriginalHeight = 94;
    int mahasiswaCrouchHeight = mahasiswaOriginalHeight * 2 / 3;
    int mahasiswaX = 50;
    int mahasiswaY = 135;

    Block mahasiswa;

    int rintanganGroundY = boardHeight - 70;
    int rintanganAirY = boardHeight - mahasiswaOriginalHeight - 50;

    ArrayList<Block> rintanganArray = new ArrayList<>();

    int velocityX = -12;
    int velocityY = 0;
    int gravity = 1;

    boolean isCrouching = false;
    boolean gameOver = false;
    int score = 0;

    Timer gameLoop;
    Timer placeRintanganTimer;

    public AssignmentAttackGame() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.white);
        setFocusable(true);
        setLayout(null);
        addKeyListener(this);

        backgroundImg = new ImageIcon(getClass().getResource("/img/background.png")).getImage();
        mahasiswaRunImg = new ImageIcon(getClass().getResource("/img/mhs-run.gif")).getImage();
        mahasiswaJumpImg = new ImageIcon(getClass().getResource("/img/mhs-jump.png")).getImage();
        mahasiswaCrouchImg = new ImageIcon(getClass().getResource("/img/mhs-crouch.png")).getImage();
        mahasiswaDeadImg = new ImageIcon(getClass().getResource("/img/mhs-over.png")).getImage();
        laptop1Img = new ImageIcon(getClass().getResource("/img/laptop1.png")).getImage();
        buku_pendekImg = new ImageIcon(getClass().getResource("/img/buku_pendek.png")).getImage();
        leadsImg = new ImageIcon(getClass().getResource("/img/leads.gif")).getImage();
        laptop_rusakImg = new ImageIcon(getClass().getResource("/img/laptop_rusak.png")).getImage();
        buku_panjangImg = new ImageIcon(getClass().getResource("/img/buku_panjang.png")).getImage();
        jamImg = new ImageIcon(getClass().getResource("/img/jam.png")).getImage();
        judulImg = new ImageIcon(getClass().getResource("/img/judul.png")).getImage();

        mahasiswa = new Block(mahasiswaX, mahasiswaY, mahasiswaWidth, mahasiswaOriginalHeight, mahasiswaRunImg);

        gameLoop = new Timer(1000 / 60, this);
        placeRintanganTimer = new Timer(1500, e -> placeRintangan());

        startButton = new JButton(new ImageIcon(getClass().getResource("/img/start.png")));
        startButton.setBounds(boardWidth / 2 - 75, boardHeight / 2 - 30, 150, 60);
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> startGame());
        add(startButton);

        restartButton = new JButton(new ImageIcon(getClass().getResource("/img/restart.png")));
        restartButton.setBounds(boardWidth / 2 - 75, boardHeight / 2 + 20, 150, 60);
        restartButton.setBorderPainted(false);
        restartButton.setContentAreaFilled(false);
        restartButton.setFocusPainted(false);
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> restartGame());
        add(restartButton);

        playBackgroundMusic("/audio/RobotCity.wav");
    }

    private void startGame() {
        gameStarted = true;
        showInstruction = true;
        startButton.setVisible(false);

        gameLoop.start();
        placeRintanganTimer.start();

        instructionTimer = new Timer(1000, e -> {
            showInstruction = false;
            instructionTimer.stop();
        });
        instructionTimer.setRepeats(false);
        instructionTimer.start();
    }

    private void restartGame() {
        resetGame();
        restartButton.setVisible(false);

        showInstruction = true;
        instructionTimer = new Timer(4000, e -> {
            showInstruction = false;
            instructionTimer.stop();
        });
        instructionTimer.setRepeats(false);
        instructionTimer.start();
    }

    void placeRintangan() {
        if (gameOver) return;
        double chance = Math.random();
        if (chance > 0.8333) rintanganArray.add(new Block(750, rintanganGroundY, 45, 45, laptop1Img));
        else if (chance > 0.6666) rintanganArray.add(new Block(750, rintanganGroundY, 45, 45, buku_pendekImg));
        else if (chance > 0.5) rintanganArray.add(new Block(750, rintanganAirY, 50, 50, leadsImg));
        else if (chance > 0.3333) rintanganArray.add(new Block(750, rintanganGroundY, 45, 45, laptop_rusakImg));
        else if (chance > 0.1666) rintanganArray.add(new Block(750, rintanganGroundY, 45, 45, buku_panjangImg));
        else rintanganArray.add(new Block(750, rintanganGroundY, 45, 45, jamImg));

        if (rintanganArray.size() > 10) rintanganArray.remove(0);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(mahasiswa.img, mahasiswa.x, mahasiswa.y, mahasiswa.width, mahasiswa.height, null);

        for (Block r : rintanganArray) {
            g.drawImage(r.img, r.x, r.y, r.width, r.height, null);
        }

        g.setColor(Color.black);
        g.setFont(new Font("DialogInput", Font.BOLD, 16));
        if (gameOver) {
            g.drawString("Game Over: " + score, 10, 35);
        } else {
            g.drawString("Score: " + score, 10, 35);
        }

        if (showInstruction) {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, boardHeight / 2 - 30, boardWidth, 60);
            g.setColor(Color.WHITE);
            g.setFont(new Font("DialogInput", Font.BOLD, 18));
            String info = "Gunakan ↑ / Spasi untuk lompat, ↓ untuk menunduk. Hindari rintangan!";
            int textWidth = g.getFontMetrics().stringWidth(info);
            g.drawString(info, (boardWidth - textWidth) / 2, boardHeight / 2 + 5);
        }

        if (!gameStarted) {
            int judulWidth = 400;
            int judulHeight = 100;
            int judulX = (boardWidth - judulWidth) / 2;
            int judulY = boardHeight / 4 - 50;
            g.drawImage(judulImg, judulX, judulY, judulWidth, judulHeight, null);
        }

    }

    public void playBackgroundMusic(String path) {
        try {
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(getClass().getResource(path));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInput);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void move() {
        velocityY += gravity;
        mahasiswa.y += velocityY;

        int groundY = mahasiswaY + (isCrouching ? (mahasiswaOriginalHeight - mahasiswaCrouchHeight) : 0);
        if (mahasiswa.y > groundY) {
            mahasiswa.y = groundY;
            velocityY = 0;
            mahasiswa.img = isCrouching ? mahasiswaCrouchImg : mahasiswaRunImg;
        }

        for (Block r : rintanganArray) {
            r.x += velocityX;
            if (collision(mahasiswa, r)) {
                gameOver = true;
                mahasiswa.img = mahasiswaDeadImg;
                if (backgroundMusic != null && backgroundMusic.isRunning()) backgroundMusic.stop();
            }
        }
        score++;
    }

    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameStarted) return;
        move();
        repaint();
        if (gameOver) {
            placeRintanganTimer.stop();
            gameLoop.stop();
            if (!skorSudahDisimpan) {
                JTextField nameField = new JTextField(15);
                nameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

                JPanel panel = new JPanel(new BorderLayout(5, 5));
                panel.add(new JLabel("Game Over!"), BorderLayout.NORTH);
                panel.add(new JLabel("Masukkan Nama Anda:"), BorderLayout.CENTER);
                panel.add(nameField, BorderLayout.SOUTH);

                int result = JOptionPane.showConfirmDialog(this, panel, "Skor Game", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String inputNama = nameField.getText();
                    if (inputNama != null && !inputNama.trim().isEmpty() && inputNama.length() <= 20) {
                        db.saveScore(inputNama.trim(), score);
                        skorSudahDisimpan = true;
                        SwingUtilities.invokeLater(() -> new LeaderboardUI().setVisible(true));
                    } else {
                        JOptionPane.showMessageDialog(this, "Nama tidak valid. Skor tidak disimpan.");
                        skorSudahDisimpan = true;
                    }
                } else {
                    skorSudahDisimpan = true;
                }
            }
            restartButton.setVisible(true);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) && mahasiswa.y == mahasiswaY) {
            velocityY = -17;
            mahasiswa.img = mahasiswaJumpImg;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN && mahasiswa.y == mahasiswaY && !isCrouching) {
            isCrouching = true;
            mahasiswa.img = mahasiswaCrouchImg;
            int diff = mahasiswaOriginalHeight - mahasiswaCrouchHeight;
            mahasiswa.y += diff;
            mahasiswa.height = mahasiswaCrouchHeight;
        }

        if (gameOver && (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP)) {
            restartGame();
        }
    }

    @Override public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN && isCrouching) {
            isCrouching = false;
            mahasiswa.img = mahasiswaRunImg;
            int diff = mahasiswaOriginalHeight - mahasiswaCrouchHeight;
            mahasiswa.y -= diff;
            mahasiswa.height = mahasiswaOriginalHeight;
        }
    }

    @Override public void keyTyped(KeyEvent e) {}

    public void setPlayerName(String name) {
        this.name = name;
    }

    public void resetGame() {
        mahasiswa.y = mahasiswaY;
        mahasiswa.img = mahasiswaRunImg;
        mahasiswa.height = mahasiswaOriginalHeight;
        velocityY = 0;
        rintanganArray.clear();
        score = 0;
        gameOver = false;
        skorSudahDisimpan = false;
        inputNamaSelesai = false;
        gameLoop.start();
        placeRintanganTimer.start();

        if (backgroundMusic != null) {
            backgroundMusic.setFramePosition(0);
            backgroundMusic.start();
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
