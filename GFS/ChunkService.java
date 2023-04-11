package GFS;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChunkService extends Remote {
    void upload (String filename, String data) throws RemoteException;
    String confirm (String filename) throws RemoteException;
    String download (String filename, int shift) throws RemoteException;
    String delete (String filename) throws RemoteException;
    String ping () throws RemoteException;

}
