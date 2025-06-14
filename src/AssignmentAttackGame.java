// AssignmentAttackGame.java
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class AssignmentAttackGame extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 750;
    int boardHeight = 250;

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
    int mahasiswaHeight = 94;
    int mahasiswaX = 50;
    int mahasiswaY = 135;

    Block mahasiswa;

    int rintanganGroundY = boardHeight - 70;
    int rintanganAirY = boardHeight - mahasiswaHeight - 100;

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
        addKeyListener(this);

        backgroundImg = new ImageIcon(getClass().getResource("/img/background.png")).getImage();
        mahasiswaRunImg = new ImageIcon(getClass().getResource("/img/mhs-run.gif")).getImage();
        mahasiswaJumpImg = new ImageIcon(getClass().getResource("/img/mhs.png")).getImage();
        mahasiswaCrouchImg = new ImageIcon(getClass().getResource("/img/mhs.png")).getImage();
        mahasiswaDeadImg = new ImageIcon(getClass().getResource("/img/mhs.png")).getImage();
        laptop1Img = new ImageIcon(getClass().getResource("/img/laptop.png")).getImage();
        buku_pendekImg = new ImageIcon(getClass().getResource("/img/buku.png")).getImage();
        leadsImg = new ImageIcon(getClass().getResource("/img/buku.png")).getImage();
        laptop_rusakImg = new ImageIcon(getClass().getResource("/img/laptop.png")).getImage();
        buku_panjangImg = new ImageIcon(getClass().getResource("/img/buku.png")).getImage();
        jamImg = new ImageIcon(getClass().getResource("/img/jam.png")).getImage();


        mahasiswa = new Block(mahasiswaX, mahasiswaY, mahasiswaWidth, mahasiswaHeight, mahasiswaRunImg);

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        placeRintanganTimer = new Timer(1500, e -> placeRintangan());
        placeRintanganTimer.start();
    }

    void placeRintangan() {
        if (gameOver) return;
        double chance = Math.random();
        if (chance > 0.8333) {
            rintanganArray.add(new Block(750, rintanganGroundY, 40, 40, laptop1Img)); // Rintangan 1
        } else if (chance > 0.6666) {
            rintanganArray.add(new Block(750, rintanganGroundY, 40, 40, buku_pendekImg)); // Rintangan 2
        } else if (chance > 0.5) {
            rintanganArray.add(new Block(750, rintanganAirY, 40, 40, leadsImg)); // Rintangan 3
        } else if (chance > 0.3333) {
            rintanganArray.add(new Block(750, rintanganGroundY, 40, 40, laptop_rusakImg)); // Rintangan 4
        } else if (chance > 0.1666) {
            rintanganArray.add(new Block(750, rintanganGroundY, 40, 40, buku_panjangImg)); // Rintangan 5
        } else {
            rintanganArray.add(new Block(750, rintanganAirY, 40, 40, jamImg)); // Rintangan 6
        }

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
        g.setFont(new Font("Courier", Font.PLAIN, 24));
        if (gameOver) {
            g.drawString("Game Over: " + score, 10, 35);
        } else {
            g.drawString("Score: " + score, 10, 35);
        }
    }

    public void move() {
        velocityY += gravity;
        mahasiswa.y += velocityY;

        if (mahasiswa.y > mahasiswaY) {
            mahasiswa.y = mahasiswaY;
            velocityY = 0;
            if (isCrouching) {
                mahasiswa.img = mahasiswaCrouchImg;
            } else {
                mahasiswa.img = mahasiswaRunImg;
            }
        }

        for (Block r : rintanganArray) {
            r.x += velocityX;
            if (collision(mahasiswa, r)) {
                gameOver = true;
                mahasiswa.img = mahasiswaDeadImg;
            }
        }
        score++;
    }

    boolean collision(Block a, Block b) {
        return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            placeRintanganTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && mahasiswa.y == mahasiswaY) {
            velocityY = -17;
            mahasiswa.img = mahasiswaJumpImg;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN && mahasiswa.y == mahasiswaY) {
            isCrouching = true;
            mahasiswa.img = mahasiswaCrouchImg;
        }

        if (gameOver && e.getKeyCode() == KeyEvent.VK_SPACE) {
            mahasiswa.y = mahasiswaY;
            mahasiswa.img = mahasiswaRunImg;
            velocityY = 0;
            rintanganArray.clear();
            score = 0;
            gameOver = false;
            gameLoop.start();
            placeRintanganTimer.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN && mahasiswa.y == mahasiswaY) {
            isCrouching = false;
            mahasiswa.img = mahasiswaRunImg;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
