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
                box(0, j);
                if(greater(list, j, j+1, comparator)){
                    swap(list, j, j+1);
                }
                else break;
            }
        }
    }
}
