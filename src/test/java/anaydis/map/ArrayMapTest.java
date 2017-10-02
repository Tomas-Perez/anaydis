package anaydis.map;

import anaydis.search.Map;
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


}