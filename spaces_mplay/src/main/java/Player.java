/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: JavaSpaces Multiplayer Game - Player Class
 * Your name(s):
 */

import com.gigaspaces.annotation.pojo.SpaceId;
import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {

    private String  name;
    private Vector  location;
    private Vector  velocity;
    private Color   color;
    private Integer radius;
    private Boolean alive;

    public static  final Integer MIN_RADIUS = 7;
    private static  final Integer MAX_RADIUS = 5 * MIN_RADIUS;

    public Player() {}

    public Player(String name, Vector location, Vector velocity, Color color) {
        this.name = name;
        this.location = location;
        this.velocity = velocity;
        this.color = color;
        radius = MIN_RADIUS;
        alive = true;
    }

    @SpaceId (autoGenerate = false)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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

    public Color getColor() {
        return color;
    }

    public void setColor(final Color color) {
        this.color = color;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(final Integer radius) {
        this.radius = radius;
    }

    public Boolean isAlive() {
        return alive;
    }

    synchronized public void setAlive(final Boolean alive) {
        this.alive = alive;
        if (!alive) {
            setColor(new Color(255, 255, 255));
            setVelocity(new Vector());
        }
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

    public void grow() {
        if (radius + 1 <= MAX_RADIUS)
            radius++;
    }

    public void shrink() {
        if (radius - 1 >= MIN_RADIUS)
            radius--;
    }

    public void colisionDetection(final Player other) {
        double distance = Math.sqrt(Math.pow(location.getX() - other.getLocation().getX(), 2.) + Math.pow(location.getY() - other.getLocation().getY(), 2.));
        if (distance < radius + other.getRadius()) {
            if (radius < other.getRadius())
                setAlive(false);
        }
    }

    public void update() {
        if (!isAlive())
            return;
        if (isStopped())
            grow();
        else {
            shrink();
            location.add(velocity);
            if (location.getY() < radius || location.getY() >= World.HEIGHT - Game.TITLE_HEIGHT - radius)
                reverseY();
            else if (location.getX() < radius || location.getX() >= World.WIDTH - radius)
                reverseX();
        }
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawOval(location.getX() - radius, location.getY() - radius, radius * 2, radius * 2);
        g.setColor(getColor());
        g.fillOval(location.getX() - radius + 1, location.getY() - radius + 1, radius * 2 - 1, radius * 2 - 1);
        g.setColor(Color.BLACK);
        g.drawChars(name.toCharArray(), 0, name.length(), location.getX() - radius / 2, location.getY());
    }

    @Override
    public boolean equals(Object obj) {
        Player other = (Player) obj;
        return name.equalsIgnoreCase(other.name);
    }

    @Override
    public String toString() {
        return "Player{" +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", velocity=" + velocity +
                ", color=" + color +
                ", radius=" + radius +
                ", alive=" + alive +
                '}';
    }
}
