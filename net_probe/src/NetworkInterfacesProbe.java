/*
 * CSCI320 - Networking & DS - Spring 2019
 * Instructor: Thyago Mota
 * Description: In-class 01 - Network Interfaces
 * Your name(s):
 */
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkInterfacesProbe {

    static String formatMac(final byte[] mac) {
        String macStr = "";
        for (int i = 0; i < mac.length; i++)
            macStr += String.format("%02X", mac[i]) + ":";
        return macStr.substring(0, macStr.length() - 1);
    }

    static String formatIPv4(final byte[] ipAddr) {
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++)
            if (ipAddr[i] < 0)
                ipAddrStr += String.format("%d", ipAddr[i] + 256) + ".";
            else
                ipAddrStr += String.format("%d", ipAddr[i]) + ".";
        return ipAddrStr.substring(0, ipAddrStr.length() - 1);
    }

    public static void main(String[] args) throws SocketException {
        Enumeration<NetworkInterface> ifaces;
        ifaces = NetworkInterface.getNetworkInterfaces();
        while (ifaces.hasMoreElements()) {
            NetworkInterface iface = ifaces.nextElement();
            System.out.print(iface);
            if (iface.isLoopback())
                System.out.print(", loopback");
            if (iface.isVirtual())
                System.out.print(", is virtual");

            if (iface.isUp())
                System.out.print(", is UP");
            else
                System.out.print(", is DOWN");
            byte[] mac = iface.getHardwareAddress();
            if (mac != null)
                System.out.print(", MAC is " + formatMac(mac));
            Enumeration<InetAddress> inetAddresses = iface.getInetAddresses();
            while (inetAddresses.hasMoreElements()) {
                InetAddress inetAddress = inetAddresses.nextElement();
                //System.out.println(inetAddress);
                if (inetAddress instanceof Inet4Address) {
                    byte[] addr = ((Inet4Address) inetAddress).getAddress();
                    System.out.print(", IPv4 is " + formatIPv4(addr));
                }
            }
            System.out.println();
        }
    }
}
