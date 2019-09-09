package javaProject.tankWar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GameClient extends JComponent {

    private static final GameClient INSTANCE = new GameClient();

    public static GameClient getInstance(){
        return INSTANCE;
    }

    private Tank playerTank;
    public Tank getPlayerTank() {
        return playerTank;
    }

    private List<Tank> enemyTank;

    private List<Walls> walls;

    private List<Missile> missiles;

    public List<Missile> getMissiles() {
        return missiles;
    }
    void removeMissile(Missile missile){
        missiles.remove(missile);
    }

    public List<Walls> getWalls() {
        return walls;
    }

    public List<Tank> getEnemyTank() {
        return enemyTank;
    }

    private List<Explosion> explosions;
    void addExplosion(Explosion explosion){
            explosions.add(explosion);
    }

    public GameClient() {
        this.playerTank = new Tank(400, 100, Direction.DOWN);
        this.missiles = new ArrayList<>();
        this.explosions = new ArrayList<>();
        this.walls = Arrays.asList(
                new Walls(200,140,true,15),
                new Walls(200,540,true,15),
                new Walls(100,80,false,15),
                new Walls(700,80,false,15)
        );
        initEnemyTank();
        this.setPreferredSize(new Dimension(800, 600));
    }

    private void initEnemyTank() {
        this.enemyTank = new ArrayList<>(12);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                this.enemyTank.add(new Tank(200 + j * 120, 400 + i * 40, true, Direction.UP));
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,800,800);
        playerTank.draw(g);
        enemyTank.removeIf(tank -> !tank.isLive());
        if(enemyTank.isEmpty()){
            initEnemyTank();
        }
        for(Tank tank: enemyTank){
            tank.draw(g);
        }
        for(Walls wall: walls){
            wall.draw(g);
        }
        missiles.removeIf(missile -> !missile.isLive());
        for(Missile missile: missiles){
            missile.draw(g);
        }
        explosions.removeIf(explosion -> !explosion.isLive());
        for(Explosion explosion: explosions){
            explosion.draw(g);
        }
    }

    public static void main(String[] args) {

        com.sun.javafx.application.PlatformImpl.startup(()->{});

        JFrame frame = new JFrame();
        //title bar
        frame.setTitle("Tank War");

//        frame.setIconImage(new ImageIcon("assets/images/tankD.gif").getImage());  // windows user could see icon

        // display configuration
        GameClient client = GameClient.getInstance();
        client.repaint();
        frame.add(client);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                client.playerTank.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {

                client.playerTank.keyReleased(e);

            }
        });
        frame.setVisible(true);
//
        while (true) {
            client.repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
