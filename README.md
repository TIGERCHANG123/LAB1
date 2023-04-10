# LAB1 

This is a java realization of the MapReduce frame. 

The course "MIT 6.824" is available on youtube. https://www.youtube.com/@6.824/videos

# Explain 


## MapFunc

读取文件，进行分词与词频统计并保存为中间结果。

```java
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

```

## ReduceFunc

输入中间结果并进行Reduce操作。

```java
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
```

## Worker 

利用线程池模拟Map的分布式操作并保存其中间结果。

```java
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    List<Map<String, Integer>> intermediateOutputs = new ArrayList<>();
```