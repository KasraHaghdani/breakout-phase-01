public class GameEngine {
    public void updateBall(Ball ball) {
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
    }

    public boolean isBallLost(Ball ball) {
        return ball.getY() + GameConstants.BALL_DIAMETER > GameConstants.CANVAS_HEIGHT;
    }
}