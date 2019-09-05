package javaProject.tankWar;

import javax.swing.*;
import java.awt.*;

public class Walls {
    private int x;
    private int y;
    private boolean horizontal;
    private int bricks;
    private final Image brickImage;

    public Walls(int x, int y, boolean horizontal, int bricks) {

        this.brickImage = new ImageIcon("assets/images/brick.png").getImage();
        this.x = x;
        this.y = y;
        this.horizontal = horizontal;
        this.bricks = bricks;
    }

    public Rectangle getRectangle(){
        return horizontal? new Rectangle(x,y,
                bricks*brickImage.getWidth(null),brickImage.getHeight(null)):
                new Rectangle(x,y,brickImage.getHeight(null),bricks*brickImage.getWidth(null));
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
