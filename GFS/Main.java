package GFS;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Main {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        LocateRegistry.createRegistry(1099);
        try {
            Master master = new Master();
            Naming.bind("rmi://127.0.0.1/master", master);
        } catch (RemoteException | AlreadyBoundException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 30; i ++) {
            try {
                Chunk chunk = new Chunk("chunk_" + i);
                Naming.bind("rmi://127.0.0.1/chunk_" + i, chunk);
                MasterService master = (MasterService) Naming.lookup("rmi://127.0.0.1/master");
                master.registerChunk("rmi://127.0.0.1/chunk_" + i, "");
            } catch (RemoteException | AlreadyBoundException | MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        for (int i = 0; i < 30; i ++) {
            ChunkService chunk = (ChunkService) Naming.lookup("rmi://127.0.0.1/chunk_" + i);
            System.out.println(chunk.ping());
        }
        MasterService masterService = (MasterService) Naming.lookup("rmi://127.0.0.1/master");
        System.out.println(masterService.getStatus());
    }
}
