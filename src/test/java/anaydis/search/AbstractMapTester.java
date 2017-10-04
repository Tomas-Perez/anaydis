package anaydis.search;

import anaydis.sort.IntegerDataSetGenerator;
import anaydis.sort.data.DataSetGenerator;
import anaydis.sort.data.StringDataSetGenerator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Tomas Perez Molina
 */
public abstract class AbstractMapTester {
    protected Map<String, Integer> map;

    public AbstractMapTester(Map<String, Integer> map){
        this.map = map;
    }

    @Test
    public void size() throws Exception{
        map.clear();
        DataSetGenerator<Integer> valueGenerator = new IntegerDataSetGenerator();
        DataSetGenerator<String> keyGenerator = new StringDataSetGenerator();
        List<String> keys = keyGenerator.createRandom(10);
        List<Integer> values = valueGenerator.createRandom(10);
        for(int i = 0; i < keys.size(); i++){
            map.put(keys.get(i), values.get(i));
        }
        assertEquals(keys.stream().distinct().count(), map.size());
        assertTrue(values.size() >= map.size());
    }

    @Test
    public void sizePerverse() throws Exception{
        map.clear();
        DataSetGenerator<Integer> valueGenerator = new IntegerDataSetGenerator();
        DataSetGenerator<String> keyGenerator = new StringDataSetGenerator();
        List<String> keys = Arrays.asList("ANNIE", "ANN", "AMY", "ALICE", "ANNA", "AMANDA", "ANGELA", "ASHLEY", "ANNE", "ANDREA");
        List<Integer> values = valueGenerator.createRandom(10);
        for(int i = 0; i < keys.size(); i++){
            map.put(keys.get(i), values.get(i));
        }
        assertEquals(keys.stream().distinct().count(), map.size());
        assertTrue(values.size() >= map.size());
    }

    @Test
    public void duplicateKey() throws Exception {
        map.clear();
        assertEquals(0, map.size());
        map.put("5", 5);
        assertEquals(new Integer(5), map.get("5"));
        assertEquals(1, map.size());
        map.put("5", 10);
        assertEquals(new Integer(10), map.get("5"));
        assertEquals(1, map.size());
    }

    @Test
    public void get() throws Exception {
        map.clear();
        DataSetGenerator<Integer> valueGenerator = new IntegerDataSetGenerator();
        DataSetGenerator<String> keyGenerator = new StringDataSetGenerator();
        List<String> keys = keyGenerator.createRandom(10);
        List<Integer> values = valueGenerator.createRandom(10);
        for(int i = 0; i < keys.size(); i++){
            map.put(keys.get(i), values.get(i));
        }
        keys.forEach(key -> assertNotNull(map.get(key)));
        keys.forEach(key -> assertTrue(values.contains(map.get(key))));
    }

    @Test
    public void getMissingKeys() throws Exception {
        map.clear();
        DataSetGenerator<Integer> valueGenerator = new IntegerDataSetGenerator();
        List<String> keys = Arrays.asList("UNO", "DOS", "TRES", "CUATRO", "CINCO");
        List<Integer> values = valueGenerator.createRandom(5);
        for(int i = 0; i < keys.size(); i++){
            map.put(keys.get(i), values.get(i));
        }
        assertFalse(map.containsKey("U"));
        assertFalse(map.containsKey("D"));
        assertFalse(map.containsKey("SIETE"));
        assertFalse(map.containsKey("O"));
        assertFalse(map.containsKey("UATRO"));
    }

    @Test
    public void keys() throws Exception{
        map.clear();
        DataSetGenerator<Integer> valueGenerator = new IntegerDataSetGenerator();
        DataSetGenerator<String> keyGenerator = new StringDataSetGenerator();
        List<String> keys = keyGenerator.createRandom(10);
        List<Integer> values = valueGenerator.createRandom(10);
        for(int i = 0; i < values.size(); i++){
            map.put(keys.get(i), values.get(i));
        }
        Iterator<String> iterator = map.keys();
        int size = 0;
        while(iterator.hasNext()){
            assertTrue(keys.contains(iterator.next()));
            size++;
        }
        assertEquals(keys.size(), size);
    }
}
