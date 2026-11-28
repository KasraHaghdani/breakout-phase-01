public class Brick {
    private final int x;
    private final int y;
    private int hitsRemaining;

    public Brick(int x, int y, int hitsRemaining) {
        this.x = x;
        this.y = y;
        this.hitsRemaining = hitsRemaining;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHitsRemaining() {
        return hitsRemaining;
    }

    public boolean isDestroyed() {
        return hitsRemaining == 0;
    }

    public void hit() {
        if (hitsRemaining > 0) {
            hitsRemaining--;
        }
    }
}