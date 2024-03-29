package javaProject.tankWar;

import javax.swing.*;
import java.awt.*;

public enum  Direction {

    UP("U",0,-1),
    DOWN("D",0,1),
    RIGHT("R",1,0),
    LEFT("L",-1,0),
    LEFT_UP("LU",-1,-1),
    RIGHT_UP("RU",1,-1),
    LEFT_DOWN("LD",-1,1),
    RIGHT_DOWN("RD",1,1);

    private final String abbrev;
    final int xFactor,yFactor;

    Direction(String abbrev,int xFactor, int yFactor){
        this.abbrev = abbrev;
        this.xFactor = xFactor;
        this.yFactor = yFactor;
    }

    Image getImage(String prefix){
        return new ImageIcon("assets/images/"+prefix+abbrev+".gif").getImage();
    }
}
