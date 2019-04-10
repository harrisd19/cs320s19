/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructors: Thyago Mota
 * Description: ChatClient class
 * The client should communicate with the server by sending unicast UDP messages and receiving multicast
 * messages; the client implements a GUI for user interaction
 * Your name(s):
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;

public class ChatClient extends JFrame implements ActionListener, WindowListener, Runnable {

    static final int    MCAST_PORT    = 50000;
    static final int    UCAST_PORT    = 50002;
    static final String MCAST_ADDRESS = "228.10.10.11";
    static final int    BUFFER_SIZE   = 1024;

    static final int    WINDOW_WIDTH  = 800;
    static final int    WINDOW_HEIGHT = 600;
    static final String WINDOW_TITLE  = "CSCI 121 - Multiuser Chat";

    private String      user, server;
    private JTextArea   txfArea;
    private JTextField  txfMsg;

    private DatagramSocket  txSocket;
    private MulticastSocket rxSocket;


    ChatClient(String user, String server) throws IOException {
        this.user = user.toLowerCase();
        this.server = server.toLowerCase();

        // TODO #1: instantiate the unicast and the mcast sockets; have the multicast socket join the multicast group
        txSocket = new DatagramSocket();
        rxSocket = new MulticastSocket(MCAST_PORT);
        rxSocket.joinGroup(InetAddress.getByName(MCAST_ADDRESS));
        // END TODO

        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle(WINDOW_TITLE);

        JPanel pnl = new JPanel(new BorderLayout());
        txfArea = new JTextArea();
        txfArea.setBackground(Color.BLACK);
        txfArea.setForeground(Color.WHITE);
        txfArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txfArea);
        pnl.add(scrollPane, BorderLayout.CENTER);

        txfMsg = new JTextField();
        txfMsg.setEnabled(false);
        pnl.add(txfMsg, BorderLayout.SOUTH);

        setContentPane(pnl);
        addWindowListener(this);

        setVisible(true);
    }

    private void txUCastMessage(String msg) throws IOException {
        byte buffer[] = msg.getBytes();
        InetAddress serverAddress = InetAddress.getByName(server);
        DatagramPacket txDatagram = new DatagramPacket(buffer, buffer.length, serverAddress, UCAST_PORT);
        txSocket.send(txDatagram);
        txfArea.append("-> " + msg + "\n");
    }

    private String rxMCastMessage() throws IOException {
        byte buffer[] = new byte[BUFFER_SIZE];
        DatagramPacket rxDatagram = new DatagramPacket(buffer, BUFFER_SIZE);
        rxSocket.receive(rxDatagram);
        String msg = new String(buffer).substring(0, rxDatagram.getLength());
        txfArea.append("<- " + msg + "\n");
        return msg;
    }

    // TODO #2: implement the code for the thread that is responsible for: 1) send a login message to the server, and 2) loop
    //  indefinitely waiting and processing any multicast message received from the server; welcome messages have special treatment
    @Override
    public void run() {
        try {
            // first send a "login" message to the server
            txUCastMessage("login," + user);

            // process any mcast message received
            while (true) {
                String msg = rxMCastMessage();
                String msgType = msg.substring(0, msg.indexOf(","));
                if (msgType.equals("welcome")) {
                    String user = msg.substring(msg.indexOf(",") + 1);
                    if (user.equals(this.user)) {
                        txfMsg.setEnabled(true);
                        txfMsg.addActionListener(this);
                    }
                }
            }
        } catch (IOException e) {
        }
    }
    // END TODO

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Use: java ChatClient <user> <server>,\n");
            System.out.println("\twhere <user> is the user name and\n");
            System.out.println("\t<server> is the location of the server.");
            System.exit(1);
        }
        String user = args[0];
        String server = args[1];
        ChatClient chatClient = new ChatClient(user, server);
        new Thread(chatClient).start();
    }

    // TODO #3: this method is automatically called when the user enters a text in the JTextField; the method should send the
    // message to the server using the unicast channel; if the message is an exit message, all sockets should be closed and
    // the client program should end
    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = txfMsg.getText();
        txfMsg.setText("");
        if (msg.equals("exit,"))
            txfMsg.setEnabled(false);
        try {
            txUCastMessage(msg);
        } catch (IOException e1) {
            txfArea.append(e1.getMessage());
        }
    }
    // END TODO

    @Override
    public void windowOpened(WindowEvent e) {

    }

    // TODO #4: make sure the client sends an exit message to the server and closes all sockets before quitting the application
    @Override
    public void windowClosing(WindowEvent e) {
        try {
            txUCastMessage("exit,");
        } catch (IOException e1) {
            txfArea.append(e1.getMessage());
        }
        txSocket.close();
        rxSocket.close();
        System.exit(0);
    }
    // END TODO

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
