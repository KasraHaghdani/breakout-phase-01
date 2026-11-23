import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import acm.graphics.GOval;

public class Breakout extends GraphicsProgram {
    public static final int APPLICATION_WIDTH = GameConstants.CANVAS_WIDTH;
    public static final int APPLICATION_HEIGHT = GameConstants.CANVAS_HEIGHT;

    private final Paddle paddle = new Paddle();
    private final GRect paddleShape =
            new GRect(GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);

    private final GameEngine gameEngine = new GameEngine();
    private final Ball ball = new Ball();
    private final GOval ballShape =
            new GOval(GameConstants.BALL_DIAMETER, GameConstants.BALL_DIAMETER);

    private boolean leftHeld;
    private boolean rightHeld;

    private GameState gameState = GameState.READY;

    public static void main(String[] args) {
        new Breakout().start(args);
    }

    @Override
    public void run() {
        paddleShape.setFilled(true);
        add(paddleShape, paddle.getX(), GameConstants.PADDLE_Y);
        ballShape.setFilled(true);
        syncBallWithPaddle();
        add(ballShape);

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
        } else if (event.getKeyCode() == KeyEvent.VK_SPACE && gameState == GameState.READY) {
            ball.launch();
            gameState = GameState.RUNNING;
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
        }

        if (leftHeld != rightHeld) {
            if (leftHeld) {
                paddle.moveLeft();
            } else {
                paddle.moveRight();
            }
            paddleShape.setLocation(paddle.getX(), GameConstants.PADDLE_Y);
        }

        if (gameState == GameState.READY) {
            syncBallWithPaddle();
        } else if (gameState == GameState.RUNNING) {
            gameEngine.updateBall(ball);

            if (gameEngine.isBallLost(ball)) {
                gameState = GameState.READY;
                syncBallWithPaddle();
            } else {
                ballShape.setLocation(ball.getX(), ball.getY());
            }
        }
    }

    private void syncBallWithPaddle() {
        ball.placeAbove(paddle);
        ballShape.setLocation(ball.getX(), ball.getY());
    }
}