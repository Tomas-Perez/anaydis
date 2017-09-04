package anaydis.sort;

import anaydis.sort.AbstractQuickSorter;
import anaydis.sort.SorterType;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class QuickSorter extends AbstractQuickSorter {
    public QuickSorter() {
        super(SorterType.QUICK);
    }

    @Override
    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
        sort(comparator, list, 0, list.size()-1);
    }

    private <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list, final int l, final int r){
        if(l >= r) return;
        int pivot = partition(comparator, list, l, r);
        sort(comparator, list, l, pivot - 1);
        sort(comparator, list, pivot + 1, r);
    }
}
