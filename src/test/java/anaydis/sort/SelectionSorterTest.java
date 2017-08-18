package anaydis.sort;

import anaydis.sort.data.StringDataSetGenerator;
import anaydis.sort.provider.SorterProvider;
import org.junit.Test;

/**
 * @author Tomas Perez Molina
 */
public class SelectionSorterTest extends SorterTest {
    public SelectionSorterTest() {
        super(SorterType.SELECTION);
    }
}
