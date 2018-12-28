import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.util.ArrayList;
import javax.swing.ImageIcon;
/*
Animates player sprite, stores frames of animation, modifies duration, speed, and state of animation
*/
public class AnimatedSprite {
    private ArrayList<AnimFrame> frames;
    private int thisFrameIndex;
    private long animTime;
    private long totalDuration;
    private float speed = 1.0f;
    private boolean completedLoop = false;
    private boolean run = true;
    private int stopFrame = -1;

    public AnimatedSprite() {
        frames = new ArrayList<AnimFrame>();
        totalDuration = 0;
        completedLoop = false;
        start();
    }


    public synchronized void addFrame(Image image, long time) {
        totalDuration += time;
        frames.add(new AnimFrame(image, totalDuration));
    }


    public synchronized void start() {
        animTime = 0;
    	  thisFrameIndex = 0;
    	  completedLoop = false;
    }

    // maintains animation state by checking and modifying frames
    public synchronized void update(long elapsedTime) {
        if (!run) return;
        elapsedTime = (long) (elapsedTime * speed);

        if (frames.size() > 1) {
            animTime += elapsedTime;

            if (animTime >= totalDuration) {

                if (completedLoop) {
                    animTime = animTime % totalDuration;
                    thisFrameIndex = 0;

                } else {
                    animTime = totalDuration;
                }
                completedLoop = true;
            }

        while (animTime > getFrame(thisFrameIndex).endTime) {
          thisFrameIndex++;
        }

        if (thisFrameIndex == stopFrame) {
        	run = false;
        	stopFrame = -1;
        }
      }
    }

    // returns image associated with current frame index
    public synchronized Image getImage() {
      if (frames.size() == 0) return null;
      return getFrame(thisFrameIndex).image;
    }


    private AnimFrame getFrame(int i) {
      return (AnimFrame) frames.get(i);
    }


    public Image getFrameImage(int i) {
      if (i < 0 || i >= frames.size()) return null;
      AnimFrame frame = frames.get(i);
      return frame.image;
    }


    public boolean didComplete() {
      return completedLoop;
    }


    private class AnimFrame {
      Image image;
      long endTime;

      public AnimFrame(Image image, long endTime) {
        this.image = image;
        this.endTime = endTime;
      }
    }
}
