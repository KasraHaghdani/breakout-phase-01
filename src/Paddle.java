public class Paddle {
    private static final int START_X =
            (GameConstants.CANVAS_WIDTH - GameConstants.PADDLE_WIDTH) / 2;
    private int x = START_X;

    public int getX() {
        return x;
    }

    public void moveLeft() {
        x = Math.max(0, x - GameConstants.PADDLE_SPEED);
    }

    public void moveRight() {
        int rightLimit = GameConstants.CANVAS_WIDTH - GameConstants.PADDLE_WIDTH;
        x = Math.min(rightLimit, x + GameConstants.PADDLE_SPEED);
    }
    public void resetPosition() {
        x = START_X;
    }
}