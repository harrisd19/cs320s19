import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Screenshot extends Remote {

    byte[] getScreenshot() throws RemoteException;

}
