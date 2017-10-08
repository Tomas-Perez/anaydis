package anaydis.immutable;

import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

/**
 * @author Tomas Perez Molina
 */
public class Node<T> implements List<T>{
    private T head;
    private List<T> tail;

    public static final List NIL = new List() {
        @Override
        public Object head() {
            throw new NoSuchElementException("Calling head on an empty list");
        }

        @NotNull
        @Override
        public List tail() {
            throw new NoSuchElementException("Calling tail on an empty list");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @NotNull
        @Override
        public List reverse() {
            throw new NoSuchElementException("Calling reverse on an empty list");
        }
    };

    public Node(T head, List<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public T head() {
        return head;
    }

    @NotNull
    @Override
    public List<T> tail() {
        return tail;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public List<T> reverse() {
        return reverse_aux(this, (List<T>) NIL);
    }

    private List<T> reverse_aux(List<T> input, List<T> output){
        if(input.isEmpty()) return output;
        return reverse_aux(input.tail(), List.cons(input.head(), output));
    }
}
