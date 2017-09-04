package anaydis.sort;

import anaydis.sort.gui.SorterListener;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class QuickSorterCutOff extends AbstractQuickSorter{
    private InsertionSorter insertionSorter;

    public QuickSorterCutOff() {
        super(SorterType.QUICK_CUT);
        insertionSorter = new InsertionSorter();
    }

    @Override
    public void addSorterListener(@NotNull SorterListener listener) {
        super.addSorterListener(listener);
        insertionSorter.addSorterListener(listener);
    }

    @Override
    public void removeSorterListener(@NotNull SorterListener listener) {
        super.removeSorterListener(listener);
        insertionSorter.removeSorterListener(listener);
    }

    @Override
    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
        sort(comparator, list, 10);
    }

    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list, final int m) {
        sort(comparator, list, 0, list.size()-1, m);
        insertionSorter.sort(comparator, list);
    }

    private <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list, final int l, final int r, final int m){
        if(r-l <= m) return;
        int pivot = partition(comparator, list, l, r);
        sort(comparator, list, l, pivot - 1, m);
        sort(comparator, list, pivot + 1, r, m);
    }
}
