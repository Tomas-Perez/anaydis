package anaydis.sort;

import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Tomas Perez Molina
 */
public class QuickSorterTest extends SorterTest {
    public QuickSorterTest() {
        super(SorterType.QUICK);
    }

    @Test
    public void testPartition(){
        QuickSorter sorter = new QuickSorter();
        List<Integer> list = new IntegerDataSetGenerator().createRandom(10);
        int expected = list.get(list.size()-1);
        int actualPivotIndex = sorter.partition(Integer::compareTo, list, 0, list.size()-1);
        int actualPivot = list.get(actualPivotIndex);
        assertThat(expected).isEqualTo(actualPivot);

        IntStream.range(0, actualPivotIndex).forEach(minorIndex -> assertThat(list.get(minorIndex)).isLessThan(actualPivot));
        IntStream.range(actualPivotIndex+1, list.size()).forEach(mayorIndex -> assertThat(list.get(mayorIndex)).isGreaterThan(actualPivot));
    }
}
