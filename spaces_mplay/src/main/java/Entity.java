/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: JavaSpaces Multiplayer Game - Entity Class
 * Your name(s):
 */

import com.gigaspaces.annotation.pojo.SpaceId;
import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public abstract class Entity implements Serializable {

    protected Vector  location;
    protected Vector  velocity;
    protected Integer radius;

    public static  final Integer MIN_RADIUS = 7;
    public static  final Integer MAX_RADIUS = 5 * MIN_RADIUS;

    public Entity() {}

    public Entity(Vector location, Vector velocity) {
        this.location = location;
        this.velocity = velocity;
        radius = new Random().nextInt(MAX_RADIUS - MIN_RADIUS) + MIN_RADIUS;
    }

    public Vector getLocation() {
        return location;
    }

    public void setLocation(final Vector location) {
        this.location = location;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(final Vector velocity) {
        this.velocity = velocity;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(final Integer radius) {
        this.radius = radius;
    }

    public boolean isStopped() {
        return velocity.equals(new Vector());
    }

    synchronized public void reverseY() {
        velocity.multiply(1, -1);
    }

    synchronized public void reverseX() {
        velocity.multiply(-1, 1);
    }

    synchronized public void rotate(double angle) {
        int lengthBefore = velocity.getLength();
        double angleRadians = Math.toRadians(angle);
        int x2 = (int) (Math.cos(angleRadians) * velocity.getX() - Math.sin(angleRadians) * velocity.getY());
        int y2 = (int) (Math.sin(angleRadians) * velocity.getX() + Math.cos(angleRadians) * velocity.getY());
        velocity = new Vector(x2, y2);
        while (velocity.getLength() < lengthBefore)
            velocity.enlarge();
    }

    public void update() {
        location.add(velocity);
        if (location.getY() < 2 * radius || location.getY() >= World.HEIGHT - Game.TITLE_HEIGHT - 2 * radius)
            reverseY();
        else if (location.getX() < 2 * radius || location.getX() >= World.WIDTH - 2 * radius)
            reverseX();
    }

    public abstract void paint(Graphics g);
}
