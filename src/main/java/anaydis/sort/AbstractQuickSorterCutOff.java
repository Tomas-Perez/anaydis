package anaydis.sort;

import anaydis.sort.gui.SorterListener;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
abstract class AbstractQuickSorterCutOff extends AbstractQuickSorter{
    private final InsertionSorter insertionSorter;
    private final int defaultM;

    public AbstractQuickSorterCutOff(SorterType sorterType) {
        super(sorterType);
        insertionSorter = new InsertionSorter();
        defaultM = 9;
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
        sort(comparator, list, defaultM);
    }

    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list, final int m) {
        sort(comparator, list, 0, list.size()-1, m);
        insertionSorter.sort(comparator, list);
    }

    protected abstract <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list, final int l, final int r, final int m);
}
