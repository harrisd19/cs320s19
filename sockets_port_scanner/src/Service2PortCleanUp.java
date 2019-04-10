import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Service2PortCleanUp {

    static final String INPUT_FILE_NAME = "service-names-port-numbers.csv";
    static final String OUTPUT_FILE_NAME = "port_service.csv";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new FileInputStream(INPUT_FILE_NAME));
        PrintStream out = new PrintStream(new FileOutputStream(OUTPUT_FILE_NAME));

        in.nextLine();
        out.println("port,transport,service,description");
        while (in.hasNextLine()) {
            String line = in.nextLine();
//            System.out.println(line);
            String data[] = line.split(",");
            String service = data[0];
            if (service.isBlank())
                continue;
            String description = "";
            if (data.length >= 4)
                description = data[3];
            int port = 0;
            try {
                port = Integer.parseInt(data[1]);
                if (port > 1023)
                    continue;
            }
            catch (Exception ex) {
                continue;
            }
            String transport = data[2].toLowerCase();
            if (!transport.equals("tcp") && !transport.equals("udp"))
                continue;
            out.println(port + "," + transport + "," + service + "," + description);
        }
        in.close();
        out.close();
    }
}
