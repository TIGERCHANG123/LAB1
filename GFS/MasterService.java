package GFS;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MasterService extends Remote {
    void requestNewChunk (String filename) throws RemoteException; // 客户端请求新的 chunk
    void registerChunk(String chunkAddr, String chunkID) throws RemoteException; // 注册新的 chunk
    List<String> append (String filename) throws RemoteException; // 返回 chunk 列表，第一个是主服务器（可能重复）
    List<String> read (String filename, int index) throws RemoteException; // 返回第 index chunkid 的 chunk 列表
    void delete (String filename) throws RemoteException;
    String getStatus() throws RemoteException; // 打印 Master 的状态
}
