package anaydis.sort;

import anaydis.sort.data.DataSetGenerator;
import anaydis.sort.data.StringDataSetGenerator;
import anaydis.sort.provider.SorterProvider;
import org.junit.Test;

/**
 * Sorter tests should subclass this abstract implementation
 */
abstract class SorterTest extends AbstractSorterTest {

    private SorterType type;

    public SorterTest(SorterType type) {
        this.type = type;
    }

    @Override protected DataSetGenerator<String> createStringDataSetGenerator() {
        return new StringDataSetGenerator();
    }

    @Override protected DataSetGenerator<Integer> createIntegerDataSetGenerator() {
        return new IntegerDataSetGenerator();
    }

    @Override
    protected SorterProvider getSorterProvider() {
        return new SorterProviderImpl();
    }

    @Test
    public void testIntegerSort(){
        final IntegerDataSetGenerator integerDataSetGenerator = new IntegerDataSetGenerator();
        testSorter(integerDataSetGenerator, type, 100);
    }

    @Test public void testStringSort(){
        final StringDataSetGenerator stringDataSetGenerator = new StringDataSetGenerator();
        testSorter(stringDataSetGenerator, type, 100);
    }
}
