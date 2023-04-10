package MapReduce;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ReduceFunc implements Callable<Map<String, Integer>> {
    List<Map<String, Integer>> intermediateOutputs;
    public ReduceFunc (List<Map<String, Integer>> intermediateOutputs) { this.intermediateOutputs = intermediateOutputs; }
    @Override
    public Map<String, Integer> call() throws Exception {
        Map<String, Integer> map = new HashMap<>();
        // 词频合并
        for (Map<String, Integer> output : intermediateOutputs) {
            for (Map.Entry<String, Integer> e : output.entrySet()) {
                map.put(e.getKey(), map.getOrDefault(e.getKey(), 0) + e.getValue());
            }
        }
        return map;
    }
}
