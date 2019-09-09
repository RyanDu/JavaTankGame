package javaProject.tankWar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;
import javafx.scene.media.*;


public class Tank {
    private int x;
    private int y;
    private Direction direction;
    private boolean enemy;
    private boolean live = true;
    private static final int Max_HP = 100;
    private int HP = Max_HP;
    boolean isDying(){
        return this.getHP() <= Max_HP*0.2;
    }

    int getHP() {
        return HP;
    }

    void setHP(int HP) {
        this.HP = HP;
    }

    boolean isLive() {
        return live;
    }

    void setLive(boolean live) {
        this.live = live;
    }

    public boolean isEnemy() {
        return enemy;
    }
    private final Random random = new Random();
    private int step = random.nextInt(12) + 3;

    public static final int SPEED = 5;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    Tank(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    Tank(int x, int y, boolean enemy, Direction direction) {
        this.x = x;
        this.y = y;
        this.enemy = enemy;
        this.direction = direction;
    }


    void move() {
        if (this.stop) return;
        x += direction.xFactor*SPEED;
        y += direction.yFactor*SPEED;
    }

    private Image getImage() {
        String prefix = enemy ? "e" : "";
        return direction.getImage(prefix+"tank");
    }

    void draw(Graphics g) {
        int oldX = x, oldY = y;
        if(!this.enemy){
            this.determineDirection();
        }
        this.move();
        //tank can't move out of the display;
        if(x<0) x=0;
        else if(x>800 - this.getImage().getWidth(null)) x=800 - this.getImage().getWidth(null);
        if (y<0) y=0;
        else if (y>600 - this.getImage().getHeight(null)) y=600 - this.getImage().getHeight(null) ;

        //tank can't move through the wall

        Rectangle tankRec = this.getRectan();
        for(Walls wall:GameClient.getInstance().getWalls()){

            if(tankRec.intersects(wall.getRectangle())){
                x = oldX;
                y = oldY;
                break;
            }

        }

        // tank can't move through enemy tank
        for(Tank eTank: GameClient.getInstance().getEnemyTank()){

            if(eTank!=this &&tankRec.intersects(eTank.getRectan())){
                x = oldX;
                y = oldY;
                break;
            }
        }

        if(this.enemy && tankRec.intersects(GameClient.getInstance().getPlayerTank().getRectan())){
            x = oldX;
            y = oldY;
        }

        if(!enemy){
            HealthPack healthPack = GameClient.getInstance().getHealthPack();
            if(healthPack.isLive() && tankRec.intersects(healthPack.getRectangle())){
                this.HP = Max_HP;
                playAudio("assets/audios/revive.wav");
                GameClient.getInstance().getHealthPack().setLive(false);
            }
            // HP display
            g.setColor(Color.white);
            g.fillRect(x,y-10,this.getImage().getWidth(null),10);

            g.setColor(Color.red);
            int width = HP * this.getImage().getWidth(null)/Max_HP;
            g.fillRect(x,y-10,width,10);
            // pet camel
            Image pet = new ImageIcon("assets/images/pet-camel.gif").getImage();
            g.drawImage(pet,this.x-pet.getWidth(null)-4,this.y,null);
        }

        g.drawImage(this.getImage(), this.x, this.y, null);
    }

    private static final int DISTANCE_TO_PET = 4;
    // Rec to check if hit the wall
    Rectangle getRectan(){
        if(enemy) {
            return new Rectangle(x, y, this.getImage().getWidth(null), this.getImage().getHeight(null));
        }else{
            Image pet = new ImageIcon("assets/images/pet-camel.gif").getImage();
            int delta = pet.getWidth(null)+DISTANCE_TO_PET;
            return new Rectangle(x-delta,y,this.getImage().getWidth(null)+delta,this.getImage().getHeight(null));
        }
    }
    // Rectangle that used to check the hit with bullets
    Rectangle getRecHit(){
        return new Rectangle(x, y, this.getImage().getWidth(null), this.getImage().getHeight(null));
    }

    private boolean up, down, left, right, stop;

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
            case KeyEvent.VK_CONTROL:
                fire();
                break;
            case KeyEvent.VK_A:
                superFire();
                break;
            case KeyEvent.VK_F2:
                GameClient.getInstance().restart();
        }
    }

    private void fire(){
        Missile missile = new Missile(x+this.getImage().getWidth(null)/2-6,
                y+this.getImage().getHeight(null)/2-6,enemy,direction);
        GameClient.getInstance().getMissiles().add(missile);
        //Sound play
        String musicFile = "assets/audios/shoot.wav";
        playAudio(musicFile);
    }

    private void superFire(){
        for(Direction direction: Direction.values()) {
            Missile missile = new Missile(x + this.getImage().getWidth(null) / 2 - 6,
                    y + this.getImage().getHeight(null) / 2 - 6, enemy, direction);
            GameClient.getInstance().getMissiles().add(missile);
        }
        //Sound play
        String musicFile = new Random().nextBoolean() ? "assets/audios/supershoot.wav": "assets/audios/supershoot.aiff";
        playAudio(musicFile);
    }
    // play sound
    private void playAudio(String musicFile) {
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
    // Make tanks act randomly
    void actRandomly(){
        Direction[] directions = Direction.values();
        if(step == 0){
            step = random.nextInt(12) +3;
            this.direction = directions[random.nextInt(8)];
            if(random.nextBoolean()){
                this.fire();
            }
        }
        step--;
    }

    private void determineDirection() {

        if (!up && !down && !left && !right) {
            this.stop = true;
        } else {
            if (up && !down && !left && !right) {
                this.direction = Direction.UP;
            } else if (!up && down && !left && !right) {
                this.direction = Direction.DOWN;
            } else if (!up && !down && left && !right) {
                this.direction = Direction.LEFT;
            } else if (!up && !down && !left && right) {
                this.direction = Direction.RIGHT;
            } else if (up && !down && left && !right) {
                this.direction = Direction.LEFT_UP;
            } else if (up && !down && !left && right) {
                this.direction = Direction.RIGHT_UP;
            } else if (!up && down && left && !right) {
                this.direction = Direction.LEFT_DOWN;
            } else if (!up && down && !left && right) {
                this.direction = Direction.RIGHT_DOWN;
            }
            this.stop = false;
        }
    }

    public void keyReleased(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
        }
    }

}
