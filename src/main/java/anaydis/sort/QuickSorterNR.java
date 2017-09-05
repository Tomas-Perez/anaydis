package anaydis.sort;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.Stack;

/**
 * @author Tomas Perez Molina
 */
public class QuickSorterNR extends AbstractQuickSorter{
    public QuickSorterNR() {
        super(SorterType.QUICK_NON_RECURSIVE);
    }

    @Override
    public <T> void sort(@NotNull Comparator<T> comparator, @NotNull List<T> list) {
        Stack<IndexPair> s = new Stack<>();
        s.push(new IndexPair(0, list.size()-1));
        while(!s.empty()){
            IndexPair indexPair = s.pop();
            int left = indexPair.getLeft();
            int right = indexPair.getRight();

            if(left < right){
                int pivot = partition(comparator, list, left, right);
                if(pivot - left < right - pivot){
                    s.push(new IndexPair(left, pivot - 1));
                }
                s.push(new IndexPair(pivot + 1, right));
                if(pivot - left >= right - pivot){
                    s.push(new IndexPair(left, pivot - 1));
                }
            }
        }
    }

}
