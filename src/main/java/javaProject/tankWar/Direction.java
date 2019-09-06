package javaProject.tankWar;

import javax.swing.*;
import java.awt.*;

public enum  Direction {

    UP("U"),
    DOWN("D"),
    RIGHT("R"),
    LEFT("L"),
    LEFT_UP("LU"),
    RIGHT_UP("RU"),
    LEFT_DOWN("LD"),
    RIGHT_DOWN("RD");

    private final String abbrev;

    Direction(String abbrev){
        this.abbrev = abbrev;
    }

    Image getImage(String prefix){
        return new ImageIcon("assets/images/"+prefix+abbrev+".gif").getImage();
    }
}
