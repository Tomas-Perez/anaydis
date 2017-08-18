package anaydis.sort;

import anaydis.sort.data.DataSetGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Tomas Perez Molina
 */
public class IntegerDataSetGenerator implements DataSetGenerator<Integer> {

    @NotNull
    @Override
    public List<Integer> createAscending(int length) {
        return IntStream
                .range(0, length)
                .boxed()
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    public List<Integer> createDescending(int length) {
        return IntStream
                .iterate(length, i -> i - 1)
                .limit(length)
                .boxed()
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    public List<Integer> createRandom(int length) {
        return new Random()
                .ints(length)
                .boxed()
                .collect(Collectors.toList());
    }

    @NotNull
    @Override
    public Comparator<Integer> getComparator() {
        return Integer::compareTo;
    }
}
