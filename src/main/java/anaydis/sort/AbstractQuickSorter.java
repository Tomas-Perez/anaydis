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
        int i = l-1;
        int j = r;
        while(true){
            while(greater(list, r, ++i, comparator) && i != r);
            while(greater(list, --j, r, comparator) && j != l);

            if(i >= j) break;

            swap(list, i, j);
        }
        swap(list, i, r);
        return i;
    }
}
