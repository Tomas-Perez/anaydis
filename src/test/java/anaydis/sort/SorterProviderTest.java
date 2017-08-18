package anaydis.sort;

import anaydis.sort.provider.SorterProvider;

/**
 * Sorter provider test
 */
public class SorterProviderTest extends AbstractSorterProviderTest {

    @Override protected SorterProvider createSorterProvider() {
        return new SorterProviderImpl();
    }
}
