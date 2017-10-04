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
public class RandomizedTreeMapTest {

    @Test
    public void duplicateKey() throws Exception {
        RandomizedTreeMap<Integer, Integer> map = new RandomizedTreeMap<>(Integer::compareTo);
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
        RandomizedTreeMap<Integer, Integer> map = new RandomizedTreeMap<>(Integer::compareTo);
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
