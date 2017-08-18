package anaydis.sort;

import anaydis.sort.provider.SorterProvider;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

/**
 * @author Tomas Perez Molina
 */
public class SorterProviderImpl implements SorterProvider {
    private EnumMap<SorterType, Sorter> map;

    public SorterProviderImpl() {
        map = new EnumMap<>(SorterType.class);
        map.put(SorterType.BUBBLE, new BubbleSorter());
        map.put(SorterType.SELECTION, new SelectionSorter());
        map.put(SorterType.INSERTION, new InsertionSorter());
    }

    @NotNull
    @Override
    public Iterable<Sorter> getAllSorters() {
        return map.values();
    }

    @NotNull
    @Override
    public Sorter getSorterForType(@NotNull SorterType type) {
        final Sorter result = map.get(type);
        if(result == null) throw new IllegalArgumentException("Not yet implemented!");
        return result;
    }
}
