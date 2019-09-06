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

    public Tank(int x, int y, Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public Tank(int x, int y, boolean enemy, Direction direction) {
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
        this.determineDirection();
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

            if(tankRec.intersects(eTank.getRectan())){
                x = oldX;
                y = oldY;
                break;
            }
        }


        g.drawImage(this.getImage(), this.x, this.y, null);
    }

    public Rectangle getRectan(){
        return new Rectangle(x,y,this.getImage().getWidth(null),this.getImage().getHeight(null));
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
