import java.io.File;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        File file = new File("./files");
        String[] files = file.list();
        for (int i = 0; i < files.length; i ++) {
            files[i] = "./files/" + files[i];
        }
        Worker worker = new Worker();
        worker.mapPerform(files);
        Map<String, Integer> reduce = worker.reducePerform();
        System.out.println("Reduce result: \n");
        for (Map.Entry<String, Integer> e : reduce.entrySet()) {
            System.out.printf("%s %d \n", e.getKey(), e.getValue());
        }
        worker.shutdown();
    }

}
