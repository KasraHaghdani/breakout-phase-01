public class Ball {
    private double x;
    private double y;

    private double vx;
    private double vy;

    public void placeAbove(Paddle paddle) {
        double paddleCenter = paddle.getX() + GameConstants.PADDLE_WIDTH / 2.0;
        x = paddleCenter - GameConstants.BALL_RADIUS;
        y = GameConstants.PADDLE_Y - GameConstants.BALL_DIAMETER;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public void launch() {
        double angle = Math.toRadians(GameConstants.BALL_LAUNCH_ANGLE);
        vx = GameConstants.BALL_SPEED * Math.sin(angle);
        vy = -GameConstants.BALL_SPEED * Math.cos(angle);
    }

    public void move() {
        x += vx;
        y += vy;
    }
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void reverseX() {
        vx = -vx;
    }

    public void reverseY() {
        vy = -vy;
    }
}