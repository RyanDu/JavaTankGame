package javaProject.tankWar;

import javax.swing.*;
import java.awt.*;

public class Walls {
    private int x;
    private int y;
    private boolean horizontal;
    private int bricks;

    public Walls(int x, int y, boolean horizontal, int bricks) {
        this.x = x;
        this.y = y;
        this.horizontal = horizontal;
        this.bricks = bricks;
    }

    public void draw(Graphics g){
            Image brick =  new ImageIcon("assets/images/brick.png").getImage();
            if(horizontal){
                for(int i=0; i<bricks;i++){
                    g.drawImage(brick,x+i*brick.getWidth(null),y,null);
                }
            }else{
                for(int i=0;i<bricks;i++){
                    g.drawImage(brick,x,y+i*brick.getHeight(null),null);
                }
            }
        }
}
