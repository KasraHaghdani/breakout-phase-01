public class GameEngine {
    private int lives = GameConstants.STARTING_LIVES;

    public int getLives() {
        return lives;
    }

    public GameState loseLife() {
        if (lives > 0) {
            lives--;
        }

        if (lives == 0) {
            return GameState.GAME_OVER;
        }

        return GameState.READY;
    }
    public void updateBall(Ball ball, Paddle paddle) {
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
}