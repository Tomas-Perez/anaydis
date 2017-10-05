package anaydis.practice.quijote;

import anaydis.search.ArrayMap;
import anaydis.search.Map;
import anaydis.search.RWayTrieMap;
import anaydis.search.RandomizedTreeMap;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Tomas Perez Molina
 */
public class QuixoteReader {
    private static final String QUIXOTE_PATH = "./src/test/resources/books/quijote_parsed.txt";
    private static final String QUIXOTE_REVERSED_PATH = "./src/test/resources/books/quijote_reversed.txt";
    private static final String QUIXOTE_OUTPUT_PATH = "./src/main/java/anaydis/practice/quijote/QuixoteBenchmark.csv";
    private static final int M = 5;

    public static void main(String[] args) {
        PrintWriter writer = null;
        try {
             writer = new PrintWriter(new File(QUIXOTE_OUTPUT_PATH));
        } catch (FileNotFoundException exc){
            System.out.println(exc);
            System.exit(1);
        }
        List<Map<String, Integer>> maps = Arrays.asList(
                new ArrayMap<String, Integer>(String::compareTo),
                new RandomizedTreeMap<String, Integer>(String::compareTo),
                new RWayTrieMap<Integer>());
        List<Integer> nList = Arrays.asList(5000, 50000, 100000, 150000, 200000);
        HashMap<String, Long[][]> mapAndTimes = new HashMap<>();
        maps.forEach(map -> mapAndTimes.put(map.getClass().getSimpleName(), new Long[nList.size()][M]));
        for (int i = 0; i < nList.size(); i++) {
            final Integer n = nList.get(i);
            for (int j = 0; j < M; j++) {
                for (Map<String, Integer> map : maps) {
                    map.clear();
                    csvWords(QUIXOTE_PATH, n).forEach(word -> {
                        Integer amount = map.get(word);
                        if (amount == null) map.put(word, 1);
                        else map.put(word, amount + 1);
                    });
                }
                for (Map<String, Integer> map : maps) {
                    long start = System.nanoTime();
                    csvWords(QUIXOTE_REVERSED_PATH, n).forEach(word -> map.put(word, 0));
                    long end = System.nanoTime();
                    long totalTime = end - start;
                    mapAndTimes.get(map.getClass().getSimpleName())[i][j] = totalTime;
                }
            }
        }
        String joinedNs = nList.stream().map(Object::toString).collect(Collectors.joining(","));
        writer.print("N,");
        writer.println(joinedNs);
        for (String mapName : mapAndTimes.keySet()) {
            writer.print(mapName + ",");
            String longSummaries =
                    Arrays.stream(mapAndTimes.get(mapName))
                    .map(longs -> Arrays.stream(longs)
                    .collect(Collectors.averagingLong(value -> value)))
                    .map(Object::toString)
                    .collect(Collectors.joining(","));
            writer.println(longSummaries);
        }
        writer.close();
    }

    private static Stream<String> csvWords(String path, int n){
        try{
            BufferedReader br = new BufferedReader(new FileReader(path));
            return br.lines().flatMap(line -> Arrays.stream(line.split(","))).limit(n);
        } catch (FileNotFoundException exc){
            System.out.println(exc);
            System.exit(1);
        }
        return null;
    }
}
