package javaProject.tankWar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Tank {
    private int x;
    private int y;
    private Direction direction;
    private boolean enemy;

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
        switch (direction) {
            case UP:
                y -= 5;
                break;
            case DOWN:
                y += 5;
                break;
            case LEFT:
                x -= 5;
                break;
            case RIGHT:
                x += 5;
                break;
            case UPLEFT:
                y -= 5;
                x -= 5;
                break;
            case UPRIGHT:
                y -= 5;
                x += 5;
                break;
            case DOWNLEFT:
                y += 5;
                x -= 5;
                break;
            case DOWNRIGHT:
                y += 5;
                x += 5;
                break;
        }
    }

    Image getImage() {
        String prefix = enemy ? "e" : "";
        switch (direction) {
            case UP:
                return new ImageIcon("assets/images/"+prefix+"tankU.gif").getImage();
            case DOWN:
                return new ImageIcon("assets/images/"+prefix+"tankD.gif").getImage();
            case LEFT:
                return new ImageIcon("assets/images/"+prefix+"tankL.gif").getImage();
            case RIGHT:
                return new ImageIcon("assets/images/"+prefix+"tankR.gif").getImage();
            case UPLEFT:
                return new ImageIcon("assets/images/"+prefix+"tankLU.gif").getImage();
            case UPRIGHT:
                return new ImageIcon("assets/images/"+prefix+"tankRU.gif").getImage();
            case DOWNLEFT:
                return new ImageIcon("assets/images/"+prefix+"tankLD.gif").getImage();
            case DOWNRIGHT:
                return new ImageIcon("assets/images/"+prefix+"tankRD.gif").getImage();
        }
        return null;
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
        }
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
                this.direction = Direction.UPLEFT;
            } else if (up && !down && !left && right) {
                this.direction = Direction.UPRIGHT;
            } else if (!up && down && left && !right) {
                this.direction = Direction.DOWNLEFT;
            } else if (!up && down && !left && right) {
                this.direction = Direction.DOWNRIGHT;
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
