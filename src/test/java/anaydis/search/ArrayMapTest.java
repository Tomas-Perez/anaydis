package anaydis.search;

import anaydis.sort.IntegerDataSetGenerator;
import anaydis.sort.data.DataSetGenerator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Tomas Perez Molina
 */
public class ArrayMapTest {
    @Test
    public void find() throws Exception {
        ArrayMap<Integer, Integer> map = new ArrayMap<>(Integer::compareTo);
        List<Integer> values = Arrays.asList(10, 5, 10, 20, 2);
        List<Integer> keys = Arrays.asList(10, 5, 10, 20, 2);
        for(int i = 0; i < values.size(); i++){
            map.put(values.get(i), keys.get(i));
        }
        assertEquals(0, map.find(2, 0, map.size()));
        assertEquals(1, map.find(5, 0, map.size()));
        assertEquals(2, map.find(10, 0, map.size()));
        assertEquals(3, map.find(20, 0, map.size()));
    }

    @Test
    public void duplicateKey() throws Exception {
        ArrayMap<Integer, Integer> map = new ArrayMap<>(Integer::compareTo);
        assertEquals(0, map.size());
        map.put(5, 5);
        assertEquals(new Integer(5), map.get(5));
        assertEquals(1, map.size());
        map.put(5, 10);
        assertEquals(new Integer(10), map.get(5));
        assertEquals(1, map.size());
    }

    @Test
    public void get() throws Exception {
        ArrayMap<Integer, Integer> map = new ArrayMap<>(Integer::compareTo);
        DataSetGenerator<Integer> dataSetGenerator = new IntegerDataSetGenerator();
        List<Integer> keys = dataSetGenerator.createRandom(10);
        List<Integer> values = dataSetGenerator.createRandom(10);
        for(int i = 0; i < keys.size(); i++){
            map.put(keys.get(i), values.get(i));
        }
        keys.forEach(key -> assertNotNull(map.get(key)));
        keys.forEach(key -> assertTrue(values.contains(map.get(key))));
    }
}