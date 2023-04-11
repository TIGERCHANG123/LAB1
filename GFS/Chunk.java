package GFS;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

public class Chunk  extends UnicastRemoteObject implements ChunkService {
    private int status = 1; // 1 for available, 0 for unavailable.
    protected int state; // 0 for follower, 1 for primary.
    protected String addr; // chunk address.
    protected int version; // current chunk version.
    protected Date leaseTime; // the time stamp of lease.
    protected char[] buffer = new char[16]; // 16 M data buffer.
    protected int endptr; // pointer to the end of the file.
    public Chunk (String addr) throws RemoteException {
        this.addr = addr;
    }

    public void upload (String filename, String data) {

    }
    public String confirm (String filename) throws RemoteException {
        if (status == 1) {
            return "OK";
        } else {
            throw new RemoteException();
        }
    }
    public String download (String filename, int shift) {
        return "OK";
    }
    public String delete (String filename) {
        return "OK";
    }
    public String ping () throws RemoteException {
        if (status == 1) {
            return addr + " OK";
        } else {
            throw new RemoteException();
        }
    }

    public void setAvailable () {
        status = 1;
    }

    public void setUnavailable () {
        status = 0;
    }

}
