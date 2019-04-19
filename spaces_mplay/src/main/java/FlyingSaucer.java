/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: JavaSpaces Multiplayer Game - FlyingSaucer Class
 * Your name(s):
 */

import com.gigaspaces.annotation.pojo.SpaceId;

import java.awt.*;
import java.util.Random;

public class FlyingSaucer extends Entity {

    private String id;
    private static final double ROTATE_CHANCE = .25;

    public FlyingSaucer() {
        super();
    }

    public FlyingSaucer(Vector location, Vector velocity) {
        super(location, velocity);
    }

    @SpaceId (autoGenerate = true)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void update() {
        super.update();
        Random r = new Random();
        if (r.nextInt(100) < ROTATE_CHANCE * 100)
            super.rotate(r.nextInt(360));
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawOval(location.getX() - radius, location.getY() - radius, radius * 2, radius * 2);
        g.fillOval(location.getX() - radius + 1, location.getY() - radius + 1, radius * 2 - 1, radius * 2 - 1);
        g.setColor(Color.BLACK);
    }

}
