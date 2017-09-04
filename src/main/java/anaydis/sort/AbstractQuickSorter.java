package anaydis.sort;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * @author Tomas Perez Molina
 */
abstract class AbstractQuickSorter extends AbstractSorter{

    public AbstractQuickSorter(SorterType sorterType) {
        super(sorterType);
    }

    protected <T> int partition(@NotNull Comparator<T> comparator, @NotNull List<T> list, final int l, final int r){
        int i = l;
        int j = r-1;
        while(true){
            while(!greater(list, i, r, comparator) && i != r)
                i++;
            while(!greater(list, r, j, comparator) && j != l)
                j--;

            if(i >= j) break;

            swap(list, i, j);
        }
        swap(list, i, r);
        return i;
    }
}
