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

    private List<Tank> enemyTank;

    private List<Walls> walls;

    public List<Walls> getWalls() {
        return walls;
    }

    public List<Tank> getEnemyTank() {
        return enemyTank;
    }

    public GameClient() {
        this.playerTank = new Tank(400, 100, Direction.DOWN);
        this.enemyTank = new ArrayList<>(12);
        this.walls = Arrays.asList(
                new Walls(200,140,true,15),
                new Walls(200,540,true,15),
                new Walls(100,80,false,15),
                new Walls(700,80,false,15)
        );
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                this.enemyTank.add(new Tank(200 + j * 120, 400 + i * 40, true, Direction.UP));
            }
        }
        this.setPreferredSize(new Dimension(800, 600));
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,800,800);
        playerTank.draw(g);
        for(Tank tank: enemyTank){
            tank.draw(g);
        }
        for(Walls wall: walls){
            wall.draw(g);
        }
    }

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        //title bar
        frame.setTitle("Tank War");

//        frame.setIconImage(new ImageIcon("assets/images/tankD.gif").getImage());  // windows user could see icon

        // display configuration
        GameClient client = new GameClient();
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
