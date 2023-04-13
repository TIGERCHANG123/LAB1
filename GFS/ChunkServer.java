package GFS;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.Writer;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;

public class ChunkServer extends UnicastRemoteObject implements ChunkService {
    private int status = 1; // 1 for available, 0 for unavailable.
    private int role; // 0 for follower, 1 for primary.
    private String addr; // chunk address.
    private int lease; // current chunk version.
    private List<String> secondaries;
    protected Date leaseTime; // 过期时间，若当前时间超过 leaseTime 则停止租赁。若租赁到期前还存在写指令则延长租赁。
    private String tempData; // 16 M data buffer.
    public ChunkServer(String addr) throws RemoteException {
        this.addr = addr;
    }
    public int getLease () { return lease; }
    public void setRole(int r) { role = r; }
    public void setLeaseTime (Date time) { leaseTime = time; }
    public void setPrimary (List<String> secondaries) { this.secondaries = secondaries; }
    public void setLease(int newVersion) { lease = newVersion; }
    // 上传文件操作包括两步：文件暂存与文件确认。
    public void upload (String chunkID, String data) throws RemoteException {
        if (data.length() > 16) throw new RemoteException();
        tempData = data;
    }
    public String confirm (String chunkID, long offset) throws RemoteException {
        if (role == 1) { // 主服务器
            File f;
            try (Writer fw = new FileWriter((f = new File(chunkID)))) {
                offset = f.length();
                if (offset + tempData.length() > 64) {
                    return "data too long. ";
                }
                fw.write(tempData);
                for (String secondary : secondaries) {
                    ChunkServer cs = (ChunkServer) Naming.lookup(secondary);
                    try {
                        cs.confirm(chunkID, offset);
                    } catch (RemoteException e) { // 出错则返回出错的从服务器
                        throw new RemoteException(secondary);
                    }
                }
            } catch (RemoteException re) {
                throw re;
            } catch (Exception e) {
                throw new RemoteException();
            }
        } else { // 从服务器
            try (RandomAccessFile rw = new RandomAccessFile(new File(chunkID), "rw")) {
                rw.seek(offset);
                rw.write(tempData.getBytes());
            } catch (Exception e) {
                throw new RemoteException();
            }
        }
        return "OK-" + offset; // 返回新添加的数据在本chunk中的偏移
    }
    public String download (String chunkID, long offset, long length) throws RemoteException {
        String data = "";
        try (RandomAccessFile rw = new RandomAccessFile(new File(chunkID), "r")) {
            byte[] tmp = new byte[1024];
            rw.seek(offset);
            rw.read(tmp);
            data = new String(tmp);
        } catch (Exception e) {
            throw new RemoteException();
        }
        return "OK-" + data;
    }
    public String delete (String chunkID) {
        return "OK";
    }
    public String ping () throws RemoteException {
        if (status == 1) {
            return addr + "-OK-" + lease; // 返回版本号
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
