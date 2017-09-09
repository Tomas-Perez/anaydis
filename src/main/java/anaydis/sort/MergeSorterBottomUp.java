package anaydis.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class MergeSorterBottomUp extends AbstractMergeSort{

    public MergeSorterBottomUp() {
        super(SorterType.MERGE_BOTTOM_UP);
    }

    @Override
    public <T> void sort(@NotNull final Comparator<T> comparator, @NotNull final List<T> list) {
        sort(comparator, list, 0, list.size() - 1);
    }

    public <T> void sort(@NotNull final Comparator<T> comparator, @NotNull final List<T> list, final int l, final int r) {
        for(int m = 1; m <= r - l; m *= 2){
            final int mX2 = m * 2;
            for(int i = l; i <= r - l; i += mX2){
                final int j = Math.min(i - l + mX2 - 1, r);
                if(j - i >= m) {
                    merge(list, i, i + m - 1, j, comparator);
                }
            }
        }
    }
}
