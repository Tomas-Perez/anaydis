package anaydis.sort;

import anaydis.sort.gui.SorterListener;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class QuickSorterCutOff extends AbstractQuickSorterCutOff{

    public QuickSorterCutOff() {
        super(SorterType.QUICK_CUT);
    }

    protected <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list, final int l, final int r, final int m){
        if(r-l <= m) return;
        int pivot = partition(comparator, list, l, r);
        sort(comparator, list, l, pivot - 1, m);
        sort(comparator, list, pivot + 1, r, m);
    }
}
