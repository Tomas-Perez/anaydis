package anaydis.search;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Tomas Perez Molina
 */
public class ArrayMapTest extends AbstractMapTester {

    public ArrayMapTest(){
        super(new ArrayMap<>(Integer::compareTo));
    }

    @Test
    public void find() throws Exception {
        ArrayMap<Integer, Integer> map = new ArrayMap<>(Integer::compareTo);
        List<Integer> values = Arrays.asList(10, 5, 10, 20, 2);
        List<Integer> keys = Arrays.asList(10, 5, 10, 20, 2);
        for(int i = 0; i < values.size(); i++){
            map.put(values.get(i), keys.get(i));
        }
        assertEquals(0, map.find(2, 0, map.size()-1));
        assertEquals(1, map.find(5, 0, map.size()-1));
        assertEquals(2, map.find(10, 0, map.size()-1));
        assertEquals(3, map.find(20, 0, map.size()-1));
        assertEquals(-5, map.find(50, 0, map.size()-1));
        assertEquals(-1, map.find(-5, 0, map.size()-1));
    }

    @Test
    public void indexOf() throws Exception {
        ArrayMap<Integer, Integer> map = new ArrayMap<>(Integer::compareTo);
        List<Integer> values = Arrays.asList(10, 5, 10, 20, 2);
        List<Integer> keys = Arrays.asList(10, 5, 10, 20, 2);
        for(int i = 0; i < values.size(); i++){
            map.put(values.get(i), keys.get(i));
        }
        assertEquals(0, map.indexOf(2));
        assertEquals(1, map.indexOf(5));
        assertEquals(2, map.indexOf(10));
        assertEquals(3, map.indexOf(20));
        assertEquals(-1, map.indexOf(50));
        assertEquals(-1, map.indexOf(-5));
    }
}