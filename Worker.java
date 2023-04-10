import MapReduce.MapFunc;
import MapReduce.ReduceFunc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Worker {
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    List<Map<String, Integer>> intermediateOutputs = new ArrayList<>();
    public Worker() {}
    // map 函数运行。
    public void mapPerform (String[] filenames) {
        List<FutureTask<Map<String, Integer>>> futureTasks = new ArrayList<>();
        for (String filename : filenames) {
            FutureTask<Map<String, Integer>> futureTask = new FutureTask<>(new MapFunc(filename));
            futureTasks.add(futureTask);
            executorService.submit(futureTask);
        }
        for (int i = 0; i < filenames.length; i ++) {
            try {
                intermediateOutputs.add(futureTasks.get(i).get());
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.toString());
            }
        }
    }
    // reduce 函数运行
    public Map<String, Integer> reducePerform () {
        FutureTask<Map<String, Integer>> futureTask = new FutureTask<>(new ReduceFunc(intermediateOutputs));
        executorService.submit(futureTask);
        Map<String, Integer> map = null;
        try {
            map = futureTask.get();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println(e.toString());
        }
        return map;
    }

    public void shutdown() {
        executorService.shutdown();
    }


}
