/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: JavaSpaces Multiplayer Game - Vector Class
 * Your name(s):
 */

import java.io.Serializable;

public class Vector implements Serializable {

    private Integer x;
    private Integer y;

    public Vector() {
        x = y = 0;
    }

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public int getLength() {
        return (int) (Math.sqrt(x * x + y * y));
    }

    public void enlarge() {
        if (x < 0)
            x--;
        else if (x > 0)
            x++;
        if (y < 0)
            y--;
        else if (y > 0)
            y++;
    }

    public void add(final Vector other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void multiply(int xScalar, int yScalar) {
        this.x *= xScalar;
        this.y *= yScalar;
    }

    @Override
    public boolean equals(Object obj) {
        Vector other = (Vector) obj;
        return (x.equals(other.getX()) && y.equals(other.getY()));
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}