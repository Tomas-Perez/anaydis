package anaydis.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class SelectionSorter extends AbstractSorter {

    public SelectionSorter() {
        super(SorterType.SELECTION);
    }

    @Override
    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
        final int size = list.size();
        for(int i = 0; i < size; i++){
            int min = i;
            for(int j = i + 1; j < size; j++){
                final T minE = list.get(min);
                final T currentE = list.get(j);
                if(less(currentE, minE, comparator)) min = j;
            }
            swap(list, min, i);
        }
    }
}
