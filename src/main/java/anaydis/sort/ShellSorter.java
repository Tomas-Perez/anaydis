package anaydis.sort;

import anaydis.sort.gui.SorterListener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Tomas Perez Molina
 */
public class ShellSorter extends AbstractSorter {
    private final HSorter hSorter;

    public ShellSorter() {
        super(SorterType.SHELL);
        hSorter = new HSorter();
    }

    @Override
    public void addSorterListener(@NotNull SorterListener listener) {
        super.addSorterListener(listener);
        hSorter.addSorterListener(listener);
    }

    @Override
    public void removeSorterListener(@NotNull SorterListener listener) {
        super.removeSorterListener(listener);
        hSorter.removeSorterListener(listener);
    }

    @Override
    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
        int h = 1;
        List<Integer> sequence = new ArrayList<>();
        for(; h <= list.size() / 9; h = 3*h+1);
        for(; h > 0; h/= 3){
            sequence.add(h);
        }
        sort(comparator, list, sequence);
    }

    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list, @NotNull List<Integer> sequence) {
        sequence.forEach(h -> hSorter.sort(comparator, list, h));
    }
}
