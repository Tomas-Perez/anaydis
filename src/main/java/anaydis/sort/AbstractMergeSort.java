package anaydis.sort;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
abstract class AbstractMergeSort extends AbstractSorter {
    public AbstractMergeSort(SorterType sorterType) {
        super(sorterType);
    }

    protected <T> void merge(@NotNull final List<T> list, final int l, final int m, final int r, @NotNull final Comparator<T> comparator){
        List<T> aux = new ArrayList<>(r-l);
        for(int i = l; i <= m; i++){
            aux.add(list.get(i));
            notifyCopy(aux.size(), i, true);
        }
        for (int j = r; j > m; j--){
            aux.add(list.get(j));
            notifyCopy(aux.size(), j, true);
        }
        for(int k = l, i = l, j = r; k <= r; k++){
            if(greater(aux, i - l, j - l, comparator)){
                list.set(k, aux.get(j-- - l));
                notifyCopy(k, j, false);
            } else {
                list.set(k, aux.get(i++ - l));
                notifyCopy(k, i, false);
            }
        }
    }
}
