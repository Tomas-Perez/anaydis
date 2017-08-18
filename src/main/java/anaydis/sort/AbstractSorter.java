package anaydis.sort;

import anaydis.sort.gui.ObservableSorter;
import anaydis.sort.gui.SorterListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Abstract sorter: all sorter implementations should subclass this class.
 */
abstract class AbstractSorter implements Sorter, ObservableSorter {

    private SorterType sorterType;
    private List<SorterListener> listeners;

    public AbstractSorter(SorterType sorterType) {
        listeners = new ArrayList<>();
        this.sorterType = sorterType;
    }

    @Override
    public void addSorterListener(@NotNull SorterListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeSorterListener(@NotNull SorterListener listener) {
        listeners.remove(listener);
    }

    protected  <T> void swap(@NotNull List<T> list, int index1, int index2){
        listeners.forEach(listener -> listener.swap(index1, index2));
        final T e1 = list.get(index1);
        final T tmp = list.set(index2, e1);
        list.set(index1, tmp);
    }

    protected <T> boolean less(@NotNull List<T> list, int index1, int index2, @NotNull Comparator<T> comparator){
        listeners.forEach(listener -> listener.greater(index1, index2));
        return comparator.compare(list.get(index1), list.get(index2)) < 0;
    }

    @NotNull
    public SorterType getType() {
        return sorterType;
    }
}
