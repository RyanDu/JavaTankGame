package javaProject.tankWar;

import javax.swing.*;
import java.awt.*;

public class HealthPack {
    private int x,y;
    private boolean live = true;
    private final Image image;
    public HealthPack(int x, int y) {
        this.x = x;
        this.y = y;
        this.image = new ImageIcon("assets/images/blood.png").getImage();
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    void draw(Graphics g){
        g.drawImage(image,x,y,null);
    }

    Rectangle getRectangle(){
        return new Rectangle(x,y,image.getWidth(null),image.getHeight(null));
    }
}
