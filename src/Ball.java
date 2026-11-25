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
        double maxAngle = GameConstants.MAX_BALL_LAUNCH_ANGLE;
        double degrees = Math.random() * (2 * maxAngle) - maxAngle;
        double angle = Math.toRadians(degrees);

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
    public boolean isMovingDown() {
        return vy > 0;
    }

    public void bounceOffPaddle(Paddle paddle) {
        double halfPaddle = GameConstants.PADDLE_WIDTH / 2.0;
        double paddleCenter = paddle.getX() + halfPaddle;
        double ballCenter = x + GameConstants.BALL_RADIUS;

        double offset = (ballCenter - paddleCenter) / halfPaddle;
        offset = Math.max(-1.0, Math.min(1.0, offset));

        double angle = Math.toRadians(offset * GameConstants.MAX_PADDLE_BOUNCE_ANGLE);
        double speed = Math.hypot(vx, vy);
        vx = speed * Math.sin(angle);
        vy = -speed * Math.cos(angle);

        y = GameConstants.PADDLE_Y - GameConstants.BALL_DIAMETER;
    }
}