/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: JavaSpaces Multiplayer Game - Game Class
 * Your name(s):
 */

import com.gigaspaces.query.IdQuery;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import static java.awt.event.InputEvent.*;

public class Game extends JFrame implements MouseListener, WindowListener {

    static final int    TITLE_HEIGHT  = 15;
    static final String TITLE         = "CSCI 320 - JavaSpaces MPlayerGame";
    static final int    VELOCITY      = 15;

    private World        world;
    private Entity       entity;
    private GigaSpace    space;

    Game(final Entity entity) throws IOException {
        this.entity = entity;
        setTitle(TITLE);
        setSize(World.WIDTH, World.HEIGHT + TITLE_HEIGHT);
        world = new World();
        setContentPane(world);
        if (entity instanceof Player)
            addMouseListener(this);
        addWindowListener(this);
        setVisible(true);

        // JavaSpaces specifics
        GigaSpaceConfigurer conf = new GigaSpaceConfigurer(new UrlSpaceConfigurer(Configuration.SPACE_URL));
        this.space = conf.gigaSpace();
    }

    public void mouseClicked(MouseEvent e) {
        if (entity instanceof Player) {
            Player localPlayer = (Player) entity;
            if (!localPlayer.isAlive())
                return;
            if (e.getClickCount() > 1) {
                if (localPlayer.isStopped())
                    localPlayer.setVelocity(new Vector(VELOCITY, VELOCITY));
                else
                    localPlayer.setVelocity(new Vector());
            } else if ((e.getModifiersEx() & CTRL_DOWN_MASK) == CTRL_DOWN_MASK)
                localPlayer.rotate(-45);
            else
                localPlayer.rotate(45);
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {
        System.out.println("Windows closing called!");
        if (entity instanceof Player)
            space.takeById(Player.class, ((Player) entity).getName());
        else
            space.takeById(FlyingSaucer.class, ((FlyingSaucer) entity).getId());
        System.exit(1);
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }

    // TODO: in the while true loop, query the space for ALL players and inform the world (hint: use setPlayers method);
    //  then, check if the local player is colliding with ANY of the other players;
    //  update the local player and write the object into the space;
    //  finally, repaint the world, sleeping for GUI_REFRESH_TIME after repaint
    void run() {
        if (entity instanceof Player) {
            Player localPlayer = (Player) entity;
            while (true) {
                // get all players
                Player players[] = space.readMultiple(new Player());
                for (Player player : players)
                    if (!localPlayer.equals(player))
                        localPlayer.colisionDetection(player);
                // get all flying saucers
                FlyingSaucer flyingSaucers[] = space.readMultiple(new FlyingSaucer());
                for (FlyingSaucer flyingSaucer : flyingSaucers)
                    localPlayer.colisionDetection(flyingSaucer);
                // setting the world's entities
                Entity entities[] = new Entity[players.length + flyingSaucers.length];
                int i = 0;
                for (i = 0; i < players.length; i++)
                    entities[i] = players[i];
                for (int j = 0; j < flyingSaucers.length; j++)
                    entities[i++] = flyingSaucers[j];
                world.setEntities(entities);
                // update local player
                localPlayer.update();
                space.write(localPlayer);
                // repaint world and sleep
                world.repaint();
                try {
                    Thread.sleep(Configuration.GUI_REFRESH_TIME);
                } catch (InterruptedException ex) {
                }
            }
        } else {
            while (true) {
                FlyingSaucer flyingSaucer = (FlyingSaucer) entity;
                flyingSaucer.update();
                space.write(flyingSaucer);
                try {
                    Thread.sleep(Configuration.GUI_REFRESH_TIME);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Use: java " + Game.class + " <player's name>");
            System.exit(1);
        }
        String name = args[0];
        Random r = new Random();
        int x = r.nextInt(World.WIDTH);
        int y = r.nextInt(World.HEIGHT);
        Vector location = new Vector(x, y);
        Entity entity = null;
        if (name.equals("saucer"))
            entity = new FlyingSaucer(location, new Vector(r.nextInt(21) - 10, r.nextInt(21) - 10));
        else {
            Color color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
            entity = new Player(name, location, new Vector(), color);
        }
        Game game = new Game(entity);
        game.run();
    }
}
