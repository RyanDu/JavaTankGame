package javaProject.tankWar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameClient extends JComponent {

    private Tank playerTank;

    private List<Tank> enemyTank;

    private List<Walls> walls;

    public GameClient() {
        this.playerTank = new Tank(400, 100, Direction.DOWN);
        this.enemyTank = new ArrayList<>(12);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                this.enemyTank.add(new Tank(200 + j * 120, 400 + i * 40, true, Direction.UP));
            }
        }
        this.setPreferredSize(new Dimension(800, 600));
    }

    @Override
    public void paintComponent(Graphics g) {
        playerTank.draw(g);
        for(Tank tank: enemyTank){
            tank.draw(g);
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
