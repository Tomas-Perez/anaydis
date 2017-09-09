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
            notifyBox(i, size);
            int min = i;
            for(int j = i + 1; j < size; j++){
                if(greater(list, min, j, comparator)) min = j;
            }
            swap(list, min, i);
        }
    }
}
