import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Hello {

    @Override
    public String sayHello() throws RemoteException {
        return "Hello!";
    }

    @Override
    public String sayHello(Person person) throws RemoteException {
        System.out.println("object hash on the server side: " + person);
        return "Hello " + person + "!";
    }

    @Override
    public String tellMeAJoke() throws RemoteException {
        return "No, I'm busy!";
    }

    public static void main(String[] args) throws RemoteException {

        String host = "localhost"; // where the directory service is located
        Server obj = new Server();
        Hello skeleton = (Hello) UnicastRemoteObject.exportObject(obj, 0);
        Registry registry = LocateRegistry.getRegistry(host);
        registry.rebind("Hello", skeleton);
        System.out.println("Server ready!");
    }
}
