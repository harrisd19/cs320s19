/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: JavaSpaces Multiplayer Game - Configuration Class
 * Your name(s):
 */

public class Configuration {
    static final String SPACE_HOST = "localhost";
    static final String SPACE_NAME = "mplay";
    static final String SPACE_URL  = "jini://" + SPACE_HOST + "/./" + SPACE_NAME;
    static final int    GUI_REFRESH_TIME = 200; // in ms
}
