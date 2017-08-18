package anaydis.sort;

import org.jetbrains.annotations.NotNull;
import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class BubbleSorter extends AbstractSorter{

    public BubbleSorter() {
        super(SorterType.BUBBLE);
    }

    @Override
    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
        final int size = list.size();
        for(int i = 0; i < size; i++){
            for(int j = i+1; j < size; j++){
                final T a = list.get(i);
                final T b = list.get(j);
                if(less(a, b, comparator)){
                    swap(list, i, j);
                }
            }
        }
    }
}
