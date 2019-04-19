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
    private Entity entities[];

    World() throws IOException {
        setBackground(Color.WHITE);
        entities = null;
    }

    void setEntities(Entity entitites[]) {
        this.entities = entitites;
    }

    @Override
    synchronized public void paintComponent(Graphics g) {
        if (entities != null)
            for (Entity entity: entities)
                entity.paint(g);
    }
}
