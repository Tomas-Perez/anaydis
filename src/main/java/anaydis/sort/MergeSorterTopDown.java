package anaydis.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class MergeSorterTopDown extends AbstractMergeSort{
    public MergeSorterTopDown() {
        super(SorterType.MERGE);
    }

    @Override
    public <T> void sort(@NotNull final Comparator<T> comparator, @NotNull final List<T> list) {
        sort(comparator, list, 0, list.size() - 1);
    }

    public <T> void sort(@NotNull final Comparator<T> comparator, @NotNull final List<T> list, final int l, final int r) {
        if(l < r){
            int m = (r + l) / 2;
            sort(comparator, list, l, m);
            sort(comparator, list, m + 1, r);
            merge(list, l, m, r, comparator);
        }
    }
}
