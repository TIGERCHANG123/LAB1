package MapReduce;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

public class MapFunc implements Callable<Map<String, Integer>> {
    String filename;
    public MapFunc(String filename) { this.filename = filename; }
    @Override
    public Map<String, Integer> call() {
        File file = new File(filename);
        Map<String, Integer> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String nxtline;
            while ((nxtline = reader.readLine()) != null) {
                // 使用分词器进行分词
                StringTokenizer st = new StringTokenizer(nxtline);
                String[] words = new String[st.countTokens()];
                int ptr = 0;
                while(st.hasMoreElements()){
                    // 消除单词前后空格以及非子母的字符
                    String t = st.nextToken().trim();
                    words[ptr++] = t.replaceAll("[\\W]+", "");
                }
                // 词频统计
                for (String word : words) {
                    if (word == null || word.length() == 0) continue;
                    map.put(word, map.getOrDefault(word, 0) + 1);
                }
            }
            // 打印当前文件词频信息
            System.out.printf("Maps got from file: %s. \n", filename);
            for (Map.Entry<String, Integer> e : map.entrySet()) {
                System.out.printf("%s %d \n", e.getKey(), e.getValue());
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        return map;
    }
}
