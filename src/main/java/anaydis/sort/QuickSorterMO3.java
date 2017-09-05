package anaydis.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class QuickSorterMO3 extends AbstractQuickSorterCutOff{

    public QuickSorterMO3() {
        super(SorterType.QUICK_MED_OF_THREE);
    }

    protected  <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list, final int l, final int r, final int m){
        if(r-l <= m) return;
        swap(list, (l+r)/2, r-1);
        swapIfGreater(list, l, r-1, comparator);
        swapIfGreater(list, l, r, comparator);
        swapIfGreater(list, r-1, r, comparator);
        int pivot = partition(comparator, list, l+1, r-1);
        sort(comparator, list, l, pivot - 1, m);
        sort(comparator, list, pivot + 1, r, m);
    }
}
