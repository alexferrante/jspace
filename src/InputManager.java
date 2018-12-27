import java.awt.Cursor;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
/*
Handle user input
*/
public class InputManager implements KeyListener, MouseListener, MouseMotionListener {
    private JFrame frame;
    private Robot control;
    private boolean modifyMousePos;
    private Point mouseLocation;
    private Point centerPositionation;

    public static final Cursor INVISIBLE_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
                                                  Toolkit.getDefaultToolkit().getImage(""), new Point(0, 0),"invisible");
    // define mouse states to pay attention to
    public static final int MOUSE_MOVE_LEFT = 0;
    public static final int MOUSE_MOVE_RIGHT = 1;
    public static final int MOUSE_MOVE_UP = 2;
    public static final int MOUSE_MOVE_DOWN = 3;
    public static final int MOUSE_BUTTON_1 = 4;
    public static final int MOUSE_BUTTON_2 = 5;
    public static final int MOUSE_BUTTON_3 = 6;

    private static final int MOUSE_CODES = 7;
    private static final int KEY_CODES = 600;

    private GameState[] keyActions = new GameState[KEY_CODES];
    private GameState[] mouseActions = new GameState[MOUSE_CODES];

    // add listeners to screen
    public InputManager(JFrame frame) {
        this.frame = frame;
        mouseLocation = new Point();
        centerPositionation = new Point();

        frame.addKeyListener(this);
        frame.addMouseListener(this);
        frame.addMouseMotionListener( this );
        frame.setFocusTraversalKeysEnabled( false );
    }

    // modify mouse position
    private synchronized void recenterMouse() {
        if (control != null && frame.isShowing()) {
            centerPositionation.x = frame.getWidth() / 2;
            centerPositionation.y = frame.getHeight() / 2;
            SwingUtilities.convertPointToScreen(centerPositionation, frame);
            modifyMousePos = true;
            control.mouseMove(centerPositionation.x, centerPositionation.y);
        }
    }

    // get state of mouse, update position values determine if position needs to be modified
    public synchronized void mouseMoved(MouseEvent e) {
        if (modifyMousePos && centerPositionation.x == e.getX() && centerPositionation.y == e.getY()) {
              modifyMousePos = false;
        } else {
            int dx = e.getX() - mouseLocation.x;
            int dy = e.getY() - mouseLocation.y;
            mouseModifier(MOUSE_MOVE_LEFT, MOUSE_MOVE_RIGHT, dx);
            mouseModifier(MOUSE_MOVE_UP, MOUSE_MOVE_DOWN, dy);
        }
        mouseLocation.x = e.getX();
        mouseLocation.y = e.getY();
    }

    // carry out modifications to mouse positioning, updating game state accordingly
    private void mouseModifier(int codeNeg, int codePos, int amount) {
        GameState GameState;
        if (amount < 0) {
            GameState = mouseActions[codeNeg];
        } else {
            GameState = mouseActions[codePos];
        }
        if (GameState != null) {
            GameState.press(Math.abs(amount));
            GameState.release();
        }
    }

    // update game states based on listeners
    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    public void keyPressed(KeyEvent e) {
        GameState GameState = getKeyAction(e);
        if (GameState != null) {
            GameState.press();
        }
        e.consume();
    }

    public void keyReleased(KeyEvent e) {
        GameState GameState = getKeyAction(e);
        if (GameState != null) {
            GameState.release();
        }
        e.consume();
    }

    public void mouseClicked(MouseEvent e ) {  }

    public void mousePressed(MouseEvent e) {
        GameState GameState = getMouseButtonAction(e);
        if (GameState != null) {
            GameState.press();
        }
    }

    public void mouseReleased(MouseEvent e) {
        GameState GameState = getMouseButtonAction(e);
        if (GameState != null) {
            GameState.release();
        }
    }

    public void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }

    public void mouseExited(MouseEvent e) {
        mouseMoved(e);
    }

    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    // map game state to key
    public void mapToKey(GameState GameState, int keyCode) {
        keyActions[keyCode] = GameState;
    }


    public void setCursor(Cursor cursor) {
        frame.setCursor(cursor);
    }


    public static String getKeyName(int keyCode) {
        return KeyEvent.getKeyText( keyCode );
    }


    public static String getMouseName(int mouseCode) {
        switch (mouseCode) {

            case MOUSE_MOVE_LEFT:
                return "Mouse Left";

            case MOUSE_MOVE_RIGHT:
                return "Mouse Right";

            case MOUSE_MOVE_UP:
                return "Mouse Up";

            case MOUSE_MOVE_DOWN:
                return "Mouse Down";

            case MOUSE_BUTTON_1:
                return "Mouse Button 1";

            case MOUSE_BUTTON_2:
                return "Mouse Button 2";

            case MOUSE_BUTTON_3:
                return "Mouse Button 3";

            default:
                return "Unknown mouse code " + mouseCode;
        }
    }


    private GameState getKeyAction(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode < keyActions.length) {
            return keyActions[keyCode];
        } else {
            return null;
        }
    }


    public static int getMouseButtonCode(MouseEvent e) {
         switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                return MOUSE_BUTTON_1;
            case MouseEvent.BUTTON2:
                return MOUSE_BUTTON_2;
            case MouseEvent.BUTTON3:
                return MOUSE_BUTTON_3;
            default:
                return -1;
        }
    }


    private GameState getMouseButtonAction(MouseEvent e) {
        int mouseCode = getMouseButtonCode(e);
        if (mouseCode != -1) {
             return mouseActions[mouseCode];
        } else {
             return null;
        }
    }


    public int getMouseX() {
        return mouseLocation.x;
    }


    public int getMouseY() {
        return mouseLocation.y;
    }

}
