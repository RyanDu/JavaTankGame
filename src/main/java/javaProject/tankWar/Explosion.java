package javaProject.tankWar;

import javax.swing.*;
import java.awt.*;

public class Explosion {
    private int x,y;

    Explosion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private int STEP = 0;
    private boolean live = true;

    boolean isLive() {
        return live;
    }

    void setLive(boolean live) {
        this.live = live;
    }

    void draw(Graphics g){
        Image explosion = new ImageIcon("assets/images/"+STEP++ +".gif").getImage();
        if (STEP > 10) {
            this.setLive(false);
            return;
        }
        g.drawImage(explosion,x,y,null);
    }
}
