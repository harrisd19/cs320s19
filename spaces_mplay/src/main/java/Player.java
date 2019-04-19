/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: JavaSpaces Multiplayer Game - Player Class
 * Your name(s):
 */

import com.gigaspaces.annotation.pojo.SpaceId;
import java.awt.*;
import java.io.Serializable;

public class Player extends Entity {

    private String  name;
    private Color   color;
    private Boolean alive;

    public Player() {}

    public Player(String name, Vector location, Vector velocity, Color color) {
        super(location, velocity);
        this.name = name;
        this.color = color;
        this.radius = MIN_RADIUS;
        alive = true;
    }

    @SpaceId (autoGenerate = false)
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(final Color color) {
        this.color = color;
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

    public void grow() {
        if (radius + 1 <= MAX_RADIUS)
            radius++;
    }

    public void shrink() {
        if (radius - 1 >= MIN_RADIUS)
            radius--;
    }

    public void colisionDetection(final Entity other) {
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
            super.update();
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
