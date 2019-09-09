package javaProject.tankWar;

import com.alibaba.fastjson.JSON;
import com.sun.javafx.reflect.FieldUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.io.*;


public class GameClient extends JComponent {

    private static final GameClient INSTANCE = new GameClient();
    public static final String GAME_SAV = "game.sav";

    public static GameClient getInstance(){
        return INSTANCE;
    }

    private Tank playerTank;
    public Tank getPlayerTank() {
        return playerTank;
    }

    private List<Tank> enemyTank;

    private AtomicInteger enemyKilled = new AtomicInteger(0);

    private List<Walls> walls;

    private HealthPack healthPack;

    public HealthPack getHealthPack() {
        return healthPack;
    }

    private List<Missile> missiles;

    public List<Missile> getMissiles() {
        return missiles;
    }

    void add(Missile missile){
        missiles.add(missile);
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
        this.missiles = new CopyOnWriteArrayList<>();
        this.explosions = new ArrayList<>();
        this.healthPack = new HealthPack(400,250);
        this.walls = Arrays.asList(
                new Walls(200,140,true,12),
                new Walls(200,540,true,12),
                new Walls(100,160,false,12),
                new Walls(700,160,false,12)
        );
        initEnemyTank();
        this.setPreferredSize(new Dimension(800, 600));
    }

    private void initEnemyTank() {
        this.enemyTank = new CopyOnWriteArrayList<>();
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
        if(!playerTank.isLive()){
            g.setColor(Color.red);
            g.setFont(new Font(null,Font.BOLD,100));
            g.drawString("Game over",100,200);
            g.setFont(new Font(null,Font.BOLD,60));
            g.drawString("Press F2 to start over",60,360);
        }else {
            g.setColor(Color.white);
            g.setFont(new Font(null,Font.BOLD,16));
            g.drawString("Missiles: "+missiles.size(),10,50);
            g.drawString("Explosions: "+explosions.size(),10,70);
            g.drawString("Player Tank HP: "+playerTank.getHP(),10,90);
            g.drawString("Enemy left: "+enemyTank.size(),10,110);
            g.drawString("Enemy killed: "+enemyKilled.get(),10,130);
            g.drawImage(new ImageIcon("assets/images/tree.png").getImage(),720,10,null);
            g.drawImage(new ImageIcon("assets/images/tree.png").getImage(),10,520,null);

            int count = enemyTank.size();
            if(playerTank.isDying()&&new Random().nextInt(4) < 2){
                healthPack.setLive(true);
            }
            if(healthPack.isLive()){
                healthPack.draw(g);
            }

            playerTank.draw(g);
            enemyTank.removeIf(tank -> !tank.isLive());
            enemyKilled.addAndGet(count - enemyTank.size());
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
    }

    public static void main(String[] args) {

        com.sun.javafx.application.PlatformImpl.startup(()->{});

        JFrame frame = new JFrame();
        //title bar
        frame.setTitle("Tank War");

//        frame.setIconImage(new ImageIcon("assets/images/tankD.gif").getImage());  // windows user could see icon

        // display configuration
        final GameClient client = GameClient.getInstance();
        frame.add(client);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    client.save();
                    System.exit(0);
                }catch (IOException ex){
                    JOptionPane.showMessageDialog(null,"Failed to save the game",
                            "Oops! Error occured",JOptionPane.ERROR_MESSAGE);
                }
                System.exit(4);
            }
        });
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
            try {
                client.repaint();
                if(client.playerTank.isLive()) {
                    for(Tank tank:client.enemyTank){
                        tank.actRandomly();
                    }
                }
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private void load(){
        File file = new File(GAME_SAV);
        if(file.exists() && file.isFile()){
            String json = FileUtils.readFileToString(file,StandardCharsets,UTF_8);
        }
    }

    private void save() throws IOException{
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(GAME_SAV)))){
            out.println(JSON.toJSONString(playerTank,true));
        }
    }

    void restart(){
        if(!playerTank.isLive()){
            playerTank = new Tank(400, 100, Direction.DOWN);
        }
        this.initEnemyTank();
    }
}
