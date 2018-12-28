import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
/*
Game view
*/
public class ScreenManager {

    private Window display;
    private JFrame frame;
    private final int WIDTH = 1000;
    private final int HEIGHT = 800;

    public ScreenManager() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setIgnoreRepaint(true);
        frame.setVisible(true);

        // configure frame and init 3 layer buffering strategy 
        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    frame.createBufferStrategy(3);
                }
            });
        } catch (InterruptedException ex) {
        } catch (InvocationTargetException  ex) {  }
    }


    public Window getDisplay() {
	       return display;
    }


    public JFrame getWindow() {
	       return frame;
    }


    public int getHeight() {
        if (frame != null) {
            return frame.getHeight();
        } else {
            return 0;
        }
    }


    public int getWidth() {
        if (frame != null) {
            return frame.getWidth();
        } else {
            return 0;
        }
    }

    // get current graphics 
    public Graphics2D getGraphics() {
        if (frame != null) {
            BufferStrategy strategy = frame.getBufferStrategy();
            return (Graphics2D) strategy.getDrawGraphics();
        } else {
            return null;
        }
    }


    public void refreshDisplay() {
        if (frame != null) frame.dispose();
    }

    // update buffering and display
    public void update() {
        if (frame != null) {
            BufferStrategy strategy = frame.getBufferStrategy();
            if (!strategy.contentsLost()) strategy.show();
        }
    }
}
