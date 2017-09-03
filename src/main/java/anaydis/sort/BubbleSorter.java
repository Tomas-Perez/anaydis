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
        boolean sorted = false;
        for(int i = 0; i < size && !sorted; i++){
            sorted = true;
            for(int j = i+1; j < size; j++){
                if(greater(list, i, j, comparator)){
                    swap(list, i, j);
                    sorted = false;
                }
            }
        }
    }
}
