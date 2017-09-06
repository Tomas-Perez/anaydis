package anaydis.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class QuickSorter3Way extends AbstractQuickSorter{
    public QuickSorter3Way() {
        super(SorterType.QUICK_THREE_PARTITION);
    }

    @Override
    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
        sort(comparator, list, 0, list.size() - 1);
    }

    private <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list, final int l, final int r){
        if(l >= r) return;
        IndexPair pair = partition(comparator, list, new IndexPair(l, r));
        sort(comparator, list, l, pair.getLeft() - 1);
        sort(comparator, list, pair.getRight() + 1, r);
    }

    private <T> IndexPair partition(@NotNull Comparator<T> comparator, @NotNull List<T> list, IndexPair pair) {
        int less = pair.getLeft(), greater = pair.getRight(), i = less;
        T pivot = list.get(less);
        while (i <= greater) {
            int cmp = comparator.compare(list.get(i), pivot);
            greater(list, i, less, comparator);
            if(cmp < 0) swap(list, less++, i++);
            else if (cmp > 0) swap(list, i, greater--);
            else i++;
        }
        return new IndexPair(less, greater);
    }
}
