package anaydis.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class HSorter extends AbstractSorter {
    public HSorter() {
        super(SorterType.H);
    }

    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list){
        sort(comparator, list, 11);
    }

    /**
     * H-Sort list. Basically a BubbleSort in sets of elements separated by h
     */
    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list, int h) {

    }
}

