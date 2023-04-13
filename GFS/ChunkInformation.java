package GFS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChunkInformation {
    private List<String> chunks; // chunk 列表
    private int leaseNumber; // 当前序列号
    private String primaryChunk; // primary chunk
    private Date leaseTimeStamp; // 当前租期的开始时间
    public ChunkInformation() {
        chunks = new ArrayList<>();
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("chunks: \t\n");
        for (String s : chunks) {
            sb.append(s);
            sb.append("\t\n");
        }
        sb.append("leaseNumber: ").append(leaseNumber).append("\t\n");
        sb.append("primary chunk: ").append(primaryChunk).append("\t\n");
        sb.append("lease time stamp: ").append(leaseTimeStamp).append("\t\n");
        return sb.toString();
    }
    public List<String> getChunks () { return chunks; }
    public int getLeaseNumber () { return leaseNumber; }
    public String getPrimaryChunk() { return primaryChunk; }
    public Date getLeaseTimeStamp() { return leaseTimeStamp; }
    public void setLeaseNumber(int number) { leaseNumber = number; }
    public void setLeaseTimeStamp (Date date) { leaseTimeStamp = date; }
    public void setPrimaryChunk (String chunk) { primaryChunk = chunk; }
    public void addChunk(String newChunk) {
        chunks.add(newChunk);
    }
    public void removeChunk(String chunk) {
        chunks.remove(chunk);
    }
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private ChunkInformation chunkInformation;
        public Builder() {}
        public Builder setChunks(List<String> chunks) {
            chunkInformation.chunks = chunks;
            return this;
        }

        public Builder setLeaseNumber(int leaseNumber) {
            chunkInformation.leaseNumber = leaseNumber;
            return this;
        }

        public Builder setPrimaryChunk(String primaryChunk) {
            chunkInformation.primaryChunk = primaryChunk;
            return this;
        }

        public Builder setLeaseTimeStamp(Date date) {
            chunkInformation.leaseTimeStamp = date;
            return this;
        }

        public ChunkInformation build() {
            return chunkInformation;
        }
    }
}
