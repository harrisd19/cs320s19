/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: JavaSpaces Multiplayer Game - Game Class
 * Your name(s):
 */

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
    private Player       localPlayer;
    private GigaSpace    space;

    Game(final Player localPlayer) throws IOException {
        this.localPlayer = localPlayer;
        setTitle(TITLE);
        setSize(World.WIDTH, World.HEIGHT + TITLE_HEIGHT);
        world = new World();
        setContentPane(world);
        addMouseListener(this);
        addWindowListener(this);
        setVisible(true);

        // JavaSpaces specifics
        GigaSpaceConfigurer conf = new GigaSpaceConfigurer(new UrlSpaceConfigurer(Configuration.SPACE_URL));
        this.space = conf.gigaSpace();
    }

    public void mouseClicked(MouseEvent e) {
        if (!localPlayer.isAlive())
            return;
        if (e.getClickCount() > 1) {
            if (localPlayer.isStopped())
                localPlayer.setVelocity(new Vector(VELOCITY, VELOCITY));
            else
                localPlayer.setVelocity(new Vector());
        }
        else if ((e.getModifiersEx() & CTRL_DOWN_MASK) == CTRL_DOWN_MASK)
            localPlayer.rotate(-45);
        else
            localPlayer.rotate(45);
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
        space.take(localPlayer);
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
        while (true) {
            Player players[] = space.readMultiple(new Player());
            world.setPlayers(players);
            for (Player other: players)
                if (!localPlayer.equals(other))
                        localPlayer.colisionDetection(other);
            localPlayer.update();
            space.write(localPlayer);
            world.repaint();
            try {
                Thread.sleep(Configuration.GUI_REFRESH_TIME);
            }
            catch (InterruptedException ex) {
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
        Color color = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
        Player player = new Player(name, location, new Vector(), color);
        Game game = new Game(player);
        game.run();
    }
}
