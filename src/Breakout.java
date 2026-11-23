import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class Breakout extends GraphicsProgram {
    public static final int APPLICATION_WIDTH = GameConstants.CANVAS_WIDTH;
    public static final int APPLICATION_HEIGHT = GameConstants.CANVAS_HEIGHT;

    private final Paddle paddle = new Paddle();
    private final GRect paddleShape =
            new GRect(GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);
    private boolean leftHeld;
    private boolean rightHeld;

    public static void main(String[] args) {
        new Breakout().start(args);
    }

    @Override
    public void run() {
        paddleShape.setFilled(true);
        add(paddleShape, paddle.getX(), GameConstants.PADDLE_Y);

        getGCanvas().setFocusable(true);
        addKeyListeners();
        getGCanvas().requestFocusInWindow();

        Timer timer = new Timer(GameConstants.UPDATE_DELAY_MS, this);
        timer.start();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            leftHeld = true;
        } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightHeld = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            leftHeld = false;
        } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightHeld = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (!getGCanvas().isFocusOwner()) {
            leftHeld = false;
            rightHeld = false;
            return;
        }

        if (leftHeld == rightHeld) {
            return; // Both arrows are held, or neither is held.
        }

        if (leftHeld) {
            paddle.moveLeft();
        } else {
            paddle.moveRight();
        }

        paddleShape.setLocation(paddle.getX(), GameConstants.PADDLE_Y);
    }
}