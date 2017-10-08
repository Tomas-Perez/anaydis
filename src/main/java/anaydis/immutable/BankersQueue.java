package anaydis.immutable;

import org.jetbrains.annotations.NotNull;

/**
 * @author Tomas Perez Molina
 */
public class BankersQueue<T> implements Queue<T> {
    private List<T> in;
    private List<T> out;

    public BankersQueue(@NotNull List<T> in, @NotNull List<T> out) {
        this.in = in;
        this.out = out;
    }

    @NotNull
    @Override
    public Queue<T> enqueue(@NotNull T value) {
        return new BankersQueue<>(List.cons(value, in), out);
    }

    @NotNull
    @Override
    public Result<T> dequeue() {
        if(out.isEmpty()) {
            Queue<T> resultQueue = new BankersQueue<>(List.nil(), in.reverse());
            return resultQueue.dequeue();
        }
        return new Result<>(out.head(), new BankersQueue<>(in, out.tail()));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
