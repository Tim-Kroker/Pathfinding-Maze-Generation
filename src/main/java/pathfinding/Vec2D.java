package pathfinding;

public record Vec2D(int x, int y) {
    public String toString() {
        return "Vec2D[x=" + x + ", y=" + y + "]";
    }
}
