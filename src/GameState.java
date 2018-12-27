/*
map user input to updates 
*/
public class GameState {
    public static final int NORMAL = 0;  // define behavior states associated with input
    public static final int DETECT_INITAL_PRESS_ONLY = 1;
    private static final int STATE_RELEASED = 0;
    private static final int STATE_PRESSED = 1;
    private static final int STATE_WAITING_FOR_RELEASE = 2;

    private String ID;
    private int behavior;
    private int amount;
    private int state;

    public GameState(String ID) {
        this(ID, NORMAL);
    }

    public GameState(String ID, int behavior) {
        this.ID = ID;
        this.behavior = behavior;
        reset();
    }


    public synchronized void tap() {
        press();
        release();
    }


    public synchronized void press() {
        press(1);
    }


    public void reset() {
        state = STATE_RELEASED;
        amount = 0;
    }


    public synchronized void release() {
        state = STATE_RELEASED;
    }


    public synchronized void press(int amount) {
        if (state != STATE_WAITING_FOR_RELEASE) {
            this.amount += amount;
            state = STATE_PRESSED;
        }
    }


    public synchronized boolean isPressed() {
        return (getAmount() != 0);
    }


    public String getID() {
        return ID;
    }


    public synchronized int getAmount() {
        int retVal = amount;
        if (retVal != 0) {
            if (state == STATE_RELEASED) {
                amount = 0;
            } else if (behavior == DETECT_INITAL_PRESS_ONLY) {
                state = STATE_WAITING_FOR_RELEASE;
                amount = 0;
            }
        }
        return retVal;
    }
}
