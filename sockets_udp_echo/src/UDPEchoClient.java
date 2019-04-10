import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPEchoClient {

    static final int PORT = 80;

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Server address? ");
        String server = sc.nextLine();
        InetAddress serverAddress = InetAddress.getByName(server);
        System.out.println(UDPEchoClient.class + " sending message to " + serverAddress);
        SocketAddress serverSocketAddress = new InetSocketAddress(serverAddress, PORT);
        System.out.print("Enter a message: ");
        String msg = sc.nextLine();
        byte buffer[] = msg.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, serverSocketAddress);
        DatagramSocket datagramSocket = new DatagramSocket();
        datagramSocket.send(datagramPacket);
        System.out.println("Message was sent!");
        datagramSocket.close();
    }
}