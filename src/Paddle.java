public class Paddle {
    private int x = (GameConstants.CANVAS_WIDTH - GameConstants.PADDLE_WIDTH) / 2;

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
}