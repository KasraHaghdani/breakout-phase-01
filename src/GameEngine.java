public class GameEngine {
    private final HighScoreStorage highScoreStorage;
    private int score;
    private int lives = GameConstants.STARTING_LIVES;
    private final Brick[][] bricks =
            new Brick[GameConstants.BRICK_ROWS][GameConstants.BRICK_COLUMNS];

    public GameEngine(HighScoreStorage highScoreStorage) {
        this.highScoreStorage = highScoreStorage;
        createBricks();
    }

    public void resetRound() {
        score = 0;
        lives = GameConstants.STARTING_LIVES;
        createBricks();
    }

    private void createBricks() {
        for (int row = 0; row < GameConstants.BRICK_ROWS; row++) {
            for (int col = 0; col < GameConstants.BRICK_COLUMNS; col++) {
                int x = GameConstants.BRICK_START_X
                        + col * (GameConstants.BRICK_WIDTH + GameConstants.BRICK_GAP);
                int y = GameConstants.BRICK_START_Y
                        + row * (GameConstants.BRICK_HEIGHT + GameConstants.BRICK_GAP);
                bricks[row][col] = new Brick(x, y);
            }
        }
    }

    public Brick getBrick(int row, int col) {
        return bricks[row][col];
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public int getHighScore() {
        return highScoreStorage.loadHighScore();
    }
    public boolean hasWon() {
        for (Brick[] row : bricks) {
            for (Brick brick : row) {
                if (!brick.isDestroyed()) {
                    return false;
                }
            }
        }
        return true;
    }
    private void recordHighScore() {
        if (score > getHighScore()) {
            highScoreStorage.saveHighScore(score);
        }
    }
    public GameState loseLife() {
        if (lives > 0) {
            lives--;
        }

        if (lives == 0) {
            recordHighScore();
            return GameState.GAME_OVER;
        }

        return GameState.READY;
    }
    public boolean updateBall(Ball ball, Paddle paddle) {
        double previousBottom = ball.getY() + GameConstants.BALL_DIAMETER;
        ball.move();

        int rightLimit = GameConstants.CANVAS_WIDTH - GameConstants.BALL_DIAMETER;
        if (ball.getX() < 0) {
            ball.setPosition(0, ball.getY());
            ball.reverseX();
        } else if (ball.getX() > rightLimit) {
            ball.setPosition(rightLimit, ball.getY());
            ball.reverseX();
        }

        if (ball.getY() < 0) {
            ball.setPosition(ball.getX(), 0);
            ball.reverseY();
        }

        checkPaddleHit(ball, paddle, previousBottom);
        return checkBrickHits(ball);
    }

    public boolean isBallLost(Ball ball) {
        return ball.getY() + GameConstants.BALL_DIAMETER > GameConstants.CANVAS_HEIGHT;
    }

    private void checkPaddleHit(Ball ball, Paddle paddle, double previousBottom) {
        double ballBottom = ball.getY() + GameConstants.BALL_DIAMETER;
        int paddleLeft = paddle.getX();
        int paddleRight = paddleLeft + GameConstants.PADDLE_WIDTH;

        boolean reachesPaddle = previousBottom <= GameConstants.PADDLE_Y
                && ballBottom >= GameConstants.PADDLE_Y;
        boolean overlaps = ball.getX() < paddleRight
                && ball.getX() + GameConstants.BALL_DIAMETER > paddleLeft;

        if (ball.isMovingDown() && reachesPaddle && overlaps) {
            ball.bounceOffPaddle(paddle);
        }
    }
    private boolean checkBrickHits(Ball ball) {
        for (Brick[] row : bricks) {
            for (Brick brick : row) {
                if (!brick.isDestroyed() && bounceOffBrick(ball, brick)) {
                    brick.hit();
                    score += GameConstants.POINTS_PER_BRICK;

                    if (hasWon()) {
                        recordHighScore();
                    }

                    return true;
                }
            }
        }
        return false;
    }

    private boolean bounceOffBrick(Ball ball, Brick brick) {
        int size = GameConstants.BALL_DIAMETER;
        double x = ball.getX();
        double y = ball.getY();

        double left = x + size - brick.getX();
        double right = brick.getX() + GameConstants.BRICK_WIDTH - x;
        double up = y + size - brick.getY();
        double down = brick.getY() + GameConstants.BRICK_HEIGHT - y;

        double overlapX = Math.min(left, right);
        double overlapY = Math.min(up, down);

        if (overlapX <= 0 || overlapY <= 0) {
            return false;
        }

        if (overlapX < overlapY) {
            ball.setPosition(left < right ? x - left : x + right, y);
            ball.reverseX();
        } else {
            ball.setPosition(x, up < down ? y - up : y + down);
            ball.reverseY();
        }
        return true;
    }
}