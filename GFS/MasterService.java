package GFS;

import java.rmi.Remote;
import java.rmi.RemoteException;
public interface MasterService extends Remote {
    void registerChunk(String chunkAddr, String chunkID) throws RemoteException;
    void add (String filename, int shift) throws RemoteException;
    String[] read (String filename, int shift) throws RemoteException;
    void delete (String filename) throws RemoteException;
    String getStatus() throws RemoteException; // 打印 Master 的状态
}
