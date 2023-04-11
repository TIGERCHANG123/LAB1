package GFS;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class Master extends UnicastRemoteObject implements MasterService {
    private final Set<String> usedChunks = new HashSet<>();
    private final Set<String> unusedChunks = new HashSet<>();
    private final Map<String, List<String>> fileMap = new HashMap<>(); // filename -> chunkID.
    private final Map<String, Integer> versionMap = new HashMap<>(); // chunkID -> version.
    private final Map<String, List<String>> chunkMap = new HashMap<>(); // chunkID -> chunk address.

    public Master() throws RemoteException {

    }

    public void registerChunk(String chunkAddr, String chunkID) throws RemoteException {
        if ("".equals(chunkID)) { // 若不存在chunkID则为空服务器
            unusedChunks.add(chunkAddr);
        } else { // 若存在chunkID则将其加入列表末尾作为从服务器
            chunkMap.get(chunkID).add(chunkAddr);
        }
    }
    public void add (String filename, int shift) throws RemoteException {

    }
    public String[] read (String filename, int shift) throws RemoteException {
        return new String[] {};
    }
    public void delete (String filename) throws RemoteException {

    }
    public String getStatus() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("--- usedChunks: \n\t");
        for (String chunk : usedChunks) {
            sb.append(chunk);
            sb.append(" \n\t");
        }
        sb.append("\n");

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

        sb.append("--- version map: \n\t");
        for (Map.Entry<String, Integer> e : versionMap.entrySet()) {
            sb.append(e.getKey());
            sb.append(": ");
            sb.append(e.getValue());
            sb.append("\n\t");
        }
        sb.append("\n");

        sb.append("--- chunk Id map: \n\t");
        for (Map.Entry<String, List<String>> e : chunkMap.entrySet()) {
            sb.append(e.getKey());
            sb.append(": ");
            sb.append(e.getValue());
            sb.append("\n\t");
        }
        sb.append("\n");

        return sb.toString();
    }
}
