import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String host = "localhost";
        Registry registry = LocateRegistry.getRegistry(host);
        Hello stub = (Hello) registry.lookup("Hello");
        // client is making a remote method invocation
        Person person = new Person(46, "Thyago");
        System.out.println("object hash on the client side: " + person);
        System.out.println(stub.sayHello(person));
        System.out.println(stub.tellMeAJoke());
    }
}
