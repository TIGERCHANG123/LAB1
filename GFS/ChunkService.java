package GFS;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChunkService extends Remote {
    public int getLease (); // 获取序列号
    public void setPrimary (List<String> secondaries) throws RemoteException; // 当前服务器设置为主服务器
    public void setLease(int newVersion) throws RemoteException; // 更新版本号
    void upload (String chunkID, String data) throws RemoteException; // 上传文件暂存
    String confirm (String chunkID, long offset)throws RemoteException; // 上传文件确认，持久化
    String download (String filename, long offset, long length) throws RemoteException;
    String delete (String filename) throws RemoteException;
    String ping () throws RemoteException;

}
