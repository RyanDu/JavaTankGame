package javaProject.tankWar;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Missile {

    public static final int SPEED = 10 ;
    private int x;
    private int y;
    private final boolean enemy;
    private final Direction direction;
    private boolean live = true;

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public Missile(int x, int y, boolean enemy, Direction direction) {
        this.x = x;
        this.y = y;
        this.enemy = enemy;
        this.direction = direction;
    }

    Image getImage(){
        return direction.getImage("missile");
    }

    private void move(){
        x += direction.xFactor*SPEED;
        y += direction.yFactor*SPEED;
    }

    void draw(Graphics g){
        move();
        if(x<0 || x>800 || y<0 ||y>600){
            this.setLive(false);
            return;
        }
        Rectangle missileRec = this.getRec();
        for(Walls wall: GameClient.getInstance().getWalls()){
            if(missileRec.intersects(wall.getRectangle())){
                this.setLive(false);
                return;
            }
        }
        if(enemy){
            //check missile shoot to user tank
            Tank playertank = GameClient.getInstance().getPlayerTank();
            if(missileRec.intersects(playertank.getRecHit())){
                addExplosion();
                playertank.setHP(playertank.getHP()-20);
                if(playertank.getHP()<=0){
                    playertank.setLive(false);
                }
                this.setLive(false);
            }
        }else{
            //check missle shoot to enemy tank
            for(Tank tank:GameClient.getInstance().getEnemyTank()){
                if(missileRec.intersects(tank.getRecHit())){
                    addExplosion();
                    tank.setLive(false);
                    this.setLive(false);
                    break;
                }
            }
        }
        g.drawImage(getImage(),x,y,null);
    }
    private void addExplosion(){
        GameClient.getInstance().addExplosion(new Explosion(x,y));
        playAudio("assets/audios/explode.wav");
    }
    private void playAudio(String musicFile) {
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
    Rectangle getRec(){
        return new Rectangle(x,y,this.getImage().getWidth(null),this.getImage().getHeight(null));
    }


}
