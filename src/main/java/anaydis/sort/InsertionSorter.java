package anaydis.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class InsertionSorter extends AbstractSorter {

    public InsertionSorter() {
        super(SorterType.INSERTION);
    }

    @Override
    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
        final int size = list.size();
        for(int i = 1; i < size; i++){
            for(int j = i - 1; j >= 0; j--){
                final T a = list.get(j+1);
                final T b = list.get(j);
                if(less(a, b, comparator)){
                    swap(list, j, j+1);
                }
                else break;
            }
        }
    }
}
