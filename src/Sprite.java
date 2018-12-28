import java.awt.Image;
/*
Defines sprite as a game asset
*/
public class Sprite {
    protected AnimatedSprite anim;

    private double x;
    private double y;
    private double velocityX;
    private double velocityY;

    public Sprite(AnimatedSprite anim ) {
        this.anim = anim;
    }

    // update position
    public void update(long elapsedTime) {
        x += velocityX * elapsedTime;
        y += velocityY * elapsedTime;
        anim.update(elapsedTime);
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getWidth() {
        return anim.getImage().getWidth(null);
    }

    public int getHeight() {
        return anim.getImage().getHeight(null);
    }

    public Image getImage() {
        return anim.getImage();
    }   
}
