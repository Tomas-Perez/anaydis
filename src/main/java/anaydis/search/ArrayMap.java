package anaydis.search;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class ArrayMap<K,V> implements Map<K,V>{
    private final List<K> keys = new ArrayList<>();
    private final List<V> values = new ArrayList<>();
    private final Comparator<K> keyComp;
    private int size;

    public ArrayMap(Comparator<K> keyComparator){
        keyComp = keyComparator;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(@NotNull K key) {
        return indexOf(key) == -1;
    }

    @Override
    public @Nullable V get(@NotNull K key) {
        int index = indexOf(key);
        return index == -1? null : values.get(index);
    }

    @Override
    public @Nullable V put(@NotNull K key, V value) {
        int index = find(key, 0, size - 1);
        if (index < 0) {
            index = -index - 1;
            if(index >= size ){
                values.add(value);
                keys.add(key);
                size++;
                return null;
            }
            values.add(values.get(size - 1));
            keys.add(keys.get(size - 1));
            for (int i = size - 1; i > index; i--) {
                values.set(i, values.get(i - 1));
                keys.set(i, keys.get(i - 1));
            }
            size++;
            keys.set(index, key);
        }
        return values.set(index, value);
    }

    @Override
    public void clear() {
        keys.clear();
        values.clear();
        size = 0;
    }

    @Override
    public Iterator<K> keys() {
        return keys.iterator();
    }

    int indexOf(@NotNull K key){
        int index = find(key, 0, size - 1);
        return index < 0 ? -1 : index;
    }

    int find(@NotNull K key, int l, int r){
        if(l > r) return - (l+1);

        int m = (l + r) / 2;
        int comp = keyComp.compare(key, keys.get(m));
        if(comp == 0) return m;
        else if(comp > 0) return find(key, m + 1, r);
        else return find(key, l, m - 1);
    }
}
