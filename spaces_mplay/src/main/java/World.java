/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: JavaSpaces Multiplayer Game - World Class
 * Your name(s):
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class World extends JPanel {

    public static final int WIDTH  = 800;
    public static final int HEIGHT = 600;
    private Player players[];

    World() throws IOException {
        setBackground(Color.WHITE);
        players = null;
    }

    void setPlayers(Player players[]) {
        this.players = players;
    }

    @Override
    synchronized public void paintComponent(Graphics g) {
        if (players != null)
            for (Player player: players)
                player.paint(g);
    }
}
