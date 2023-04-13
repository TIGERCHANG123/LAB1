package GFS;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Master extends UnicastRemoteObject implements MasterService {
    private final Set<String> unusedChunks = new HashSet<>();
    private final Map<String, List<String>> fileMap = new HashMap<>(); // filename -> list of chunkID.
    private final Map<String, ChunkInformation> chunkMap = new HashMap<>(); // chunkID -> chunk information.

    public Master() throws RemoteException {
        new Thread(() -> { // 扫描不可用 chunk servers
            for (List<String> chunkIDs : fileMap.values()) {
                for (String chunkID : chunkIDs) {
                    ChunkInformation ci = chunkMap.get(chunkID);
                    for (String addr : ci.getChunks()) {
                        try {
                            ChunkServer cs = (ChunkServer) Naming.lookup(addr);
                            cs.ping(); // chunk 下线
                            if (cs.getLease() < ci.getLeaseNumber()) { // 序列号过期
                                throw new RemoteException();
                            }

                        } catch (MalformedURLException | NotBoundException | RemoteException e) {
                            if (e instanceof RemoteException) { // 不可用
                                // 为此 chunkid 分配新的 chunk 并同步数据

                                // 不可用的 chunk 进行垃圾回收
                            }
                            System.out.println(e.toString());
                        }
                    }
                }
            }
        }).start();


    }

    public void requestNewChunk (String filename) throws RemoteException {
        synchronized (this) {
            if (unusedChunks.size() < 3) {
                throw new RemoteException("Insufficient chunks. ");
            } else {
                fileMap.putIfAbsent(filename, new ArrayList<>());
                String chunkID = String.valueOf(UUID.randomUUID()); // 新 chunk 的 id
                fileMap.get(filename).add(chunkID); // 文件的 chunk 队列添加新chunkid
                chunkMap.put(chunkID, new ChunkInformation()); // 注册 chunkid
                Iterator<String> ite = unusedChunks.iterator();
                Set<String> tmp = new HashSet<> ();
                for (int i = 0; i < 3; i ++) {
                    String nxt = ite.next();
                    tmp.add(nxt);
                    chunkMap.get(chunkID).addChunk(nxt); // 为新的 chunkid 添加 chunk
                }
                unusedChunks.removeAll(tmp);
            }
        }
    }
    public void registerChunk(String chunkAddr, String chunkID) throws RemoteException {
        if ("".equals(chunkID)) { // 若不存在chunkID则为空服务器
            unusedChunks.add(chunkAddr);
        } else { // 若存在chunkID则将其加入列表末尾作为从服务器
            if (chunkMap.containsKey(chunkID)) {
                ChunkInformation ci = chunkMap.get(chunkID);
                ci.addChunk(chunkAddr);
            } else {
                List<String> ls = new ArrayList<>();
                ls.add(chunkAddr);
                ChunkInformation ci = ChunkInformation.builder()
                        .setChunks(ls)
                        .build();
                chunkMap.put(chunkID, ci);
            }
        }
    }
    public List<String> append (String filename) throws RemoteException {
        List<String> clist = fileMap.get(filename);
        String chunkid = clist.get(clist.size() - 1);
        return chunkMap.get(chunkid).getChunks();
    }
    public List<String> read (String filename, int index) throws RemoteException {
        List<String> clist = fileMap.get(filename);
        String chunkid = clist.get(index);
        return chunkMap.get(chunkid).getChunks();
    }
    public void delete (String filename) throws RemoteException {

    }
    public String getStatus() throws RemoteException {
        StringBuilder sb = new StringBuilder();

        sb.append("--- unusedChunks: \n\t");
        for (String chunk : unusedChunks) {
            sb.append(chunk);
            sb.append(" \n\t");
        }
        sb.append("\n");

        sb.append("--- file map: \n\t");
        for (Map.Entry<String, List<String>> e : fileMap.entrySet()) {
            sb.append(e.getKey());
            sb.append(": ");
            sb.append(e.getValue());
            sb.append("\n\t ");
        }
        sb.append("\n");

        sb.append("--- chunk  map: \n\t");
        for (Map.Entry<String, ChunkInformation> e : chunkMap.entrySet()) {
            sb.append(e.getKey());
            sb.append(": ");
            sb.append(e.getValue());
            sb.append("\n\t");
        }
        sb.append("\n");

        return sb.toString();
    }
}
