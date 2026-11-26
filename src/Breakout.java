import acm.graphics.GRect;
import acm.program.GraphicsProgram;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import acm.graphics.GOval;
import acm.graphics.GLabel;
import java.awt.Color;



public class Breakout extends GraphicsProgram {
    public static final int APPLICATION_WIDTH = GameConstants.CANVAS_WIDTH;
    public static final int APPLICATION_HEIGHT = GameConstants.CANVAS_HEIGHT;

    private final Paddle paddle = new Paddle();
    private final GRect paddleShape =
            new GRect(GameConstants.PADDLE_WIDTH, GameConstants.PADDLE_HEIGHT);

    private final GRect[][] brickShapes =
            new GRect[GameConstants.BRICK_ROWS][GameConstants.BRICK_COLUMNS];
    private final GameEngine gameEngine = new GameEngine(new InMemoryScoreStorage());
    private final Ball ball = new Ball();
    private final GOval ballShape =
            new GOval(GameConstants.BALL_DIAMETER, GameConstants.BALL_DIAMETER);

    private boolean leftHeld;
    private final GLabel hudLabel = new GLabel("");

    private boolean rightHeld;

    private GameState gameState = GameState.READY;

    public static void main(String[] args) {
        new Breakout().start(args);
    }

    @Override
    public void run() {
        drawBricks();
        paddleShape.setFilled(true);
        add(paddleShape, paddle.getX(), GameConstants.PADDLE_Y);
        ballShape.setFilled(true);
        syncBallWithPaddle();
        add(ballShape);
        updateHud();
        add(hudLabel, GameConstants.HUD_LEFT, GameConstants.HUD_BASELINE);

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
        if (gameState == GameState.GAME_OVER) {
            return;
        }
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
            if (gameEngine.updateBall(ball, paddle)) {
                refreshBricks();
                updateHud();
            }

            if (gameEngine.isBallLost(ball)) {
                handleBallLost();
            } else {
                ballShape.setLocation(ball.getX(), ball.getY());
            }
        }
    }

    private void syncBallWithPaddle() {
        ball.placeAbove(paddle);
        ballShape.setLocation(ball.getX(), ball.getY());
    }
    private void handleBallLost() {
        gameState = gameEngine.loseLife();
        updateHud();

        if (gameState == GameState.READY) {
            syncBallWithPaddle();
            return;
        }

        ballShape.setVisible(false);
        GLabel message = new GLabel("Game Over");
        message.setFont("SansSerif-BOLD-32");

        double x = (APPLICATION_WIDTH - message.getWidth()) / 2.0;
        double y = (APPLICATION_HEIGHT + message.getAscent() - message.getDescent()) / 2.0;
        add(message, x, y);
    }
    private void drawBricks() {
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            for (int col = 0; col < GameConstants.BRICK_COLUMNS; col++) {
                Brick brick = gameEngine.getBrick(row, col);
                GRect shape = new GRect(GameConstants.BRICK_WIDTH, GameConstants.BRICK_HEIGHT);
                shape.setFilled(true);
                shape.setFillColor(Color.ORANGE);
                brickShapes[row][col] = shape;
                add(shape, brick.getX(), brick.getY());
            }
        }
    }
    private void refreshBricks() {
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            for (int col = 0; col < GameConstants.BRICK_COLUMNS; col++) {
                Brick brick = gameEngine.getBrick(row, col);
                brickShapes[row][col].setVisible(!brick.isDestroyed());
            }
        }
    }
    private void updateHud() {
        hudLabel.setLabel("Lives: " + gameEngine.getLives()
                + "   Score: " + gameEngine.getScore()
                + "   High score: " + gameEngine.getHighScore());
    }
}