import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
/*
 Configures the game state, updating methods & frames
*/
public abstract class GameFramework {
    private boolean isRunning;
    protected ScreenManager screen;

    public void run() {
        try {
            init();
            gameLoop();
        } finally {
            screen.refreshDisplay();
            threadManage();
        }
    }

    // init frames and display
    public void init() {
        screen = new ScreenManager();
        Window window = screen.getWindow();
        window.add(new JLabel(new ImageIcon(loadImage("menu.png"))));
        window.setVisible(true);
        isRunning = true;
    }

    // manage and update FPS 
    public void gameLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        while (isRunning) {
            long elapsedTime = System.currentTimeMillis() - currTime;
            currTime += elapsedTime;
            update(elapsedTime);
            Graphics2D g = screen.getGraphics();
            draw(g);
            g.dispose();
            screen.update();
        }
    }

    // handle loading/ buffering errors 
    public void threadManage() {
        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) { }
                    System.exit(0);
                }
        };
        thread.setDaemon(true);
        thread.start();
    }


    public void stop() {
        isRunning = false;
    }


    public Image loadImage(String filename) {
        return new ImageIcon( getClass().getResource("Assets/images/" + filename)).getImage();
    }

    // methods provided to game manager
    public void update(long elapsedTime) {  }

    public abstract void draw(Graphics2D g);

}
