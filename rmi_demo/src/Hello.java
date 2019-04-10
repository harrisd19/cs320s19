import java.rmi.*;

public interface Hello extends Remote {

    String sayHello() throws RemoteException;

    String sayHello(Person person) throws RemoteException;

    String tellMeAJoke() throws RemoteException;
}
