/*
 * CSCI121 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: Hwk 02 - PortScanner
 * Your name(s):
 */

import java.io.*;
import java.net.*;
import java.util.*;

/*
Write a program that lists all TCP/UDP ports < 1024 that are currently being used by a network service on the computer that it runs.  If a port is being used, your program should list the port, the service short name, and the service longer description. The list of known TCP/UDP services is provided to you as a CSV file.
*/
public class PortScanner {

    static final String KNOWN_PORTS = "port_service.csv";
    static final String INPUT_FILE_NAME = "port_service.csv";
    static Map<Integer, String> udpPorts, tcpPorts;

    public static void main(String[] args) throws FileNotFoundException, UnknownHostException {
        udpPorts = new TreeMap<>();
        tcpPorts = new TreeMap<>();

        Scanner in = new Scanner(new FileInputStream(INPUT_FILE_NAME));
        in.nextLine(); // ignore 1st line
        while (in.hasNext()) {
            String line = in.nextLine();
            String data[] = line.split(",");
            int port = Integer.parseInt(data[0]);
            String transport = data[1];
            String service = data[2];
            String description = "NA";
            if (data.length > 3)
                description = data[3];
            if (transport.equals("udp"))
                udpPorts.put(port, service + ", " + description);
            else
                tcpPorts.put(port, service + ", " + description);
        }
        in.close();
//        System.out.println(udpPorts);

        // beginning UDP port scanner
        System.out.println("Port scan host: " + InetAddress.getLocalHost());
        System.out.println("\nUDP port scan started...");
        System.out.println("Services found: ");
        for (int port : udpPorts.keySet()) {
            DatagramSocket datagramSocket;
            try {
                datagramSocket = new DatagramSocket(port);
                datagramSocket.close();
            }
            catch (SocketException ex) {
                System.out.println("\t" + port + ":" + udpPorts.get(port));
            }
        }
        System.out.println("UDP port scan completed!");

        // beginning TCP port scanner
        System.out.println("\nTCP port scan started...");
        System.out.println("Services found: ");
        for (int port : tcpPorts.keySet()) {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                serverSocket.close();
            }
            catch (IOException ex) {
                System.out.println("\t" + port + ":" + tcpPorts.get(port));
            }
        }
        System.out.println("TCP port scan completed!");
    }
}
