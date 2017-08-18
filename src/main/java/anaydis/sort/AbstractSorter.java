package anaydis.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * Abstract sorter: all sorter implementations should subclass this class.
 */
abstract class AbstractSorter implements Sorter {

    private SorterType sorterType;

    public AbstractSorter(SorterType sorterType) {
        this.sorterType = sorterType;
    }

    public <T> void swap(@NotNull List<T> list, int index1, int index2){
        final T e1 = list.get(index1);
        final T tmp = list.set(index2, e1);
        list.set(index1, tmp);
    }

    public <T> boolean less(@NotNull T a, @NotNull T b, @NotNull Comparator<T> comparator){
        return comparator.compare(a, b) < 0;
    }

    @NotNull
    public SorterType getType() {
        return sorterType;
    }
}
