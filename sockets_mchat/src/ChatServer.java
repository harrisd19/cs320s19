/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructors: Thyago Mota
 * Description: ChatServer class
 * The server should wait for unicast messages; the server should maintain a list of active
 * users; if an active user sends a message, the server uses multicast to forward the message
 * to all users.
 * Your name(s):
 */

/**
 * Protocol Messages:
 *
 * client -> server
 * login,<user>
 * msg,<msg>
 * list,
 * exit,
 *
 * server -> client
 * welcome,<user>
 * msg,<user> says <msg>
 * bye,<user>
 * list,<user>,<user>,...,<user>
 */

import java.io.IOException;
import java.net.*;
import java.util.*;

public class ChatServer {

    static final int    MCAST_PORT    = 50000;
    static final int    UCAST_PORT    = 50002;
    static final String MCAST_ADDRESS = "228.10.10.11";
    static final int    BUFFER_SIZE   = 1024;

    private Map<SocketAddress, String> users;

    private DatagramSocket txSocket;
    private DatagramSocket rxSocket;

    ChatServer() throws SocketException, UnknownHostException {
        users = new HashMap<>();
        txSocket = new DatagramSocket();
        rxSocket = new DatagramSocket(UCAST_PORT, InetAddress.getLocalHost());
    }

    private void txMCastMessage(String msg) throws IOException {
        byte buffer[] = msg.getBytes();
        DatagramPacket txDatagram = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MCAST_ADDRESS), MCAST_PORT);
        txSocket.send(txDatagram);
        System.out.println("-> " + msg);
    }

    void run() throws SocketException, UnknownHostException {
        System.out.println("ChatServer started on " + rxSocket.getLocalSocketAddress());
        System.out.println("Hit ENTER to kill the server at anytime");

        Runnable thread = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        byte buffer[] = new byte[BUFFER_SIZE];
                        DatagramPacket rxDatagram = new DatagramPacket(buffer, BUFFER_SIZE);
                        rxSocket.receive(rxDatagram);
                        String msg = new String(buffer).substring(0, rxDatagram.getLength());
                        System.out.println("<- " + msg);
                        String msgType = msg.substring(0, msg.indexOf(","));
                        if (msgType.equals("login")) {
                            String user = msg.substring(msg.indexOf(",") + 1);
                            if (!users.containsKey(user)) {
                                SocketAddress userSocketAddress = rxDatagram.getSocketAddress();
                                users.put(userSocketAddress, user);
                                txMCastMessage("welcome," + user);
                            }
                        } else if (msgType.equals("exit")) {
                            SocketAddress userSocketAddress = rxDatagram.getSocketAddress();
                            String user = users.get(userSocketAddress);
                            if (user != null) {
                                users.remove(userSocketAddress);
                                txMCastMessage("bye," + user);
                            }
                        } else if (msgType.equals("msg")) {
                            SocketAddress userSocketAddress = rxDatagram.getSocketAddress();
                            String user = users.get(userSocketAddress);
                            if (user != null) {
                                String userSays = msg.substring(msg.indexOf(",") + 1);
                                txMCastMessage("msg," + user + " says " + userSays);
                            }
                        }
                        else if (msgType.equals("list")) {
                            String list = "list,";
                            for (String user: users.values())
                                list += user + ",";
                            list = list.substring(0, list.length() - 1);
                            txMCastMessage(list);
                        }
                        else
                            System.out.println(msg);
                    } catch (IOException e) {
                        break;
                    }
                }
            }
        };
        new Thread(thread).start();

        new Scanner(System.in).nextLine();
        rxSocket.close();
        txSocket.close();
    }

    public static void main(String[] args) throws SocketException, UnknownHostException {
        new ChatServer().run();
    }
}
