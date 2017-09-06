package anaydis.practice.stats;

import anaydis.sort.*;
import anaydis.sort.data.DataSetGenerator;
import anaydis.sort.gui.ObservableSorter;
import anaydis.sort.gui.SorterListener;
import anaydis.sort.provider.SorterProvider;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;


public class SortingAnalyzer {

    private static final int RUNS = 10;

    private enum DataUnit {
        SWAPS,
        COMPARISONS,
        TIME
    }

    private enum ShellSequence {
        SHELL_ONE(Arrays.asList(16577, 4193, 1073, 281, 77, 23, 8, 1)),
        SHELL_TWO(Arrays.asList(9841, 3280, 1093, 364, 121, 40, 13, 4, 1));

        List<Integer> sequence;

        ShellSequence(List<Integer> sequence){
            this.sequence = sequence;
        }
    }

    private enum M{
        FIVE(5),
        TEN(10),
        FIFTEEN(15),
        TWENTY(20),
        TWENTY_FIVE(25),
        THIRTY(30);

        int number;
        M(int number)  {this.number = number;}
    }

    enum Schema {
        ONE_HUNDRED(100),
        FIVE_HUNDRED(500),
        ONE_THOUSAND(1000),
        TEN_THOUSAND(10000),
        HUNDRED_THOUSAND(100000),
        MILLION(1000000);

        int size;

        Schema(int size) {
            this.size = size;
        }
    }

    enum Ordering {
        ASCENDING {
            @Override
            public <T> List<T> create(@NotNull DataSetGenerator<T> generator, Schema schema) {
                return generator.createAscending(schema.size);
            }
        },
        DESCENDING {
            @Override
            public <T> List<T> create(@NotNull DataSetGenerator<T> generator, Schema schema) {
                return generator.createDescending(schema.size);
            }
        },
        RANDOM {
            @Override
            public <T> List<T> create(@NotNull DataSetGenerator<T> generator, Schema schema) {
                return generator.createRandom(schema.size);
            }
        };

        abstract <T> List<T> create(@NotNull DataSetGenerator<T> generator, Schema schema);
    }

    private class Cube {
        private final SuperCell[][] data = new SuperCell[Ordering.values().length][Schema.values().length];

        Cube() {
            init();
        }

        void submit(final Schema schema, final Ordering ordering, final SorterListenerImpl listener) {
            final SuperCell cell = data[ordering.ordinal()][schema.ordinal()];
            cell.submit(listener);
        }

        private void init() {
            for (int i = 0; i < Ordering.values().length; i++) {
                for (int j = 0; j < Schema.values().length; j++) {
                    data[i][j] = new SuperCell();
                }
            }
        }

        SuperCell[] schemas(Ordering ordering) {
            return data[ordering.ordinal()];
        }
    }

    private class SuperCell {
        private final Cell[] data = new Cell[DataUnit.values().length];

        SuperCell() {
            init();
        }

        private void submit(SorterListenerImpl listener) {
            data[DataUnit.SWAPS.ordinal()].submit(listener.getSwaps());
            data[DataUnit.COMPARISONS.ordinal()].submit(listener.getCompares());
            data[DataUnit.TIME.ordinal()].submit(listener.getElapsedTime());
        }

        private void init() {
            for (int i = 0; i < DataUnit.values().length; i++) {
                data[i] = new Cell();
            }
        }

        private Cell cell(DataUnit unit) {
            return data[unit.ordinal()];
        }
    }

    private class Cell {
        private final List<Long> data = new ArrayList<>();

        private void submit(long value) {
            data.add(value);
        }
    }

    private Map<SorterType, Cube> cubes() {
        final Map<SorterType, Cube> result = new EnumMap<>(SorterType.class);
        final SorterProvider sorters = new SorterProviderImpl();
        sorters.getAllSorters().forEach(s -> result.put(s.getType(), cube(s)));
        return result;
    }

    private Map<SorterType, Cube> cubes(@NotNull final List<Sorter> sorters, @NotNull final List<Schema> schemas, @NotNull final List<Ordering> orderings) {
        final Map<SorterType, Cube> result = new EnumMap<>(SorterType.class);
        sorters.forEach(s -> result.put(s.getType(), cube(s, schemas, orderings)));
        return result;
    }

    private Map<ShellSequence, Cube> shellCubes(@NotNull final List<Schema> schemas, @NotNull final List<Ordering> orderings) {
        ShellSorter sorter = new ShellSorter();
        final Map<ShellSequence, Cube> result = new EnumMap<>(ShellSequence.class);
        Arrays.stream(ShellSequence.values()).forEach(s -> result.put(s, shellCube(sorter, schemas, orderings, s.sequence)));
        return result;
    }

    private Map<M, Cube> mCubes(@NotNull final List<Schema> schemas){
        QuickSorterCutOff sorter = new QuickSorterCutOff();
        final Map<M, Cube> result = new EnumMap<>(M.class);
        Arrays.stream(M.values()).forEach(m -> result.put(m, mCube(sorter, schemas, m.number)));
        return result;
    }

    @NotNull private Cube cube(@NotNull final Sorter sorter) {
        final Cube cube = new Cube();
        final IntegerDataSetGenerator generator = new IntegerDataSetGenerator();

        for (final Schema schema : Schema.values()) {
            for (final Ordering ordering : Ordering.values()) {
                run(sorter, generator, schema, ordering, cube);
            }
        }
        return cube;
    }

    @NotNull private Cube cube(@NotNull final Sorter sorter, @NotNull final List<Schema> schemas, @NotNull final List<Ordering> orderings) {
        final Cube cube = new Cube();
        final IntegerDataSetGenerator generator = new IntegerDataSetGenerator();

        for (final Schema schema : schemas) {
            for (final Ordering ordering : orderings) {
                run(sorter, generator, schema, ordering, cube);
            }
        }
        return cube;
    }

    @NotNull private Cube shellCube(@NotNull final ShellSorter sorter, @NotNull final List<Schema> schemas, @NotNull final List<Ordering> orderings, List<Integer> hList) {
        final Cube cube = new Cube();
        final IntegerDataSetGenerator generator = new IntegerDataSetGenerator();

        for (final Schema schema : schemas) {
            for (final Ordering ordering : orderings) {
                runShellTest(sorter, generator, schema, ordering, cube, hList);
            }
        }
        return cube;
    }

    @NotNull private Cube mCube(@NotNull final QuickSorterCutOff sorter, @NotNull final List<Schema> schemas, int m) {
        final Cube cube = new Cube();
        final IntegerDataSetGenerator generator = new IntegerDataSetGenerator();

        for (final Schema schema : schemas) {
            runMTest(sorter, generator, schema, Ordering.RANDOM, cube, m);
        }
        return cube;
    }

    private <T> void analyze(@NotNull final Map<T, Cube> cubes){
        cubes.forEach((sorterType, cube) -> {
            System.out.println("SORTER = " + sorterType);
            for (Ordering ordering : Ordering.values()) {
                System.out.println("\tORDERING = " + ordering);
                final SuperCell[] schemas = cube.schemas(ordering);
                for (final Schema schema : Schema.values()) {
                    System.out.println("\t\tSCHEMA = " + schema);
                    final SuperCell s = schemas[schema.ordinal()];
                    for (DataUnit unit : DataUnit.values()) {
                        System.out.println("\t\t\tUNIT = " + unit);
                        final Cell cell = s.cell(unit);
                        if(cell.data.size() > 0) {
                            final LongSummaryStatistics statistics =
                                    cell.data.stream().collect(Collectors.summarizingLong(value -> value));
                            System.out.println("\t\t\t\t " + statistics);
                        }
                    }
                }
            }
        });
    }

    public void analyzeAll() {
        final Map<SorterType, Cube> cubes = cubes();
        analyze(cubes);
    }

    public void shellAnalysis(){
        final Map<ShellSequence, Cube> cubes = shellCubes(Arrays.asList(Schema.ONE_HUNDRED, Schema.ONE_THOUSAND, Schema.TEN_THOUSAND),
                Arrays.asList(Ordering.values()));
        analyze(cubes);
    }

    public void mAnalysis(){
        final Map<M, Cube> cubes = mCubes(Arrays.asList(Schema.ONE_HUNDRED, Schema.ONE_THOUSAND, Schema.TEN_THOUSAND, Schema.HUNDRED_THOUSAND, Schema.MILLION));
        analyze(cubes);
    }

    public void basicAnalysis(){
        List<Sorter> sorters = new ArrayList<>();
        new SorterProviderImpl().getAllSorters().forEach(sorters::add);
        final Map<SorterType, Cube> cubes = cubes(sorters,
                Arrays.asList(Schema.ONE_HUNDRED, Schema.FIVE_HUNDRED, Schema.ONE_THOUSAND),
                Arrays.asList(Ordering.values()));
        analyze(cubes);
    }

    public void analyze(List<Sorter> sorters, List<Schema> schemas, List<Ordering> orderings){
        final Map<SorterType, Cube> cubes = cubes(sorters, schemas, orderings);
        analyze(cubes);
    }

    private <T> void run(final Sorter sorter, final DataSetGenerator<T> generator, Schema schema, final Ordering ordering, final Cube cube) {
        final SorterListenerImpl listener = new SorterListenerImpl();
        addListener(sorter, listener);
        final List<T> datum = ordering.create(generator, schema);
        for (int i = 0; i < RUNS; i++) {
            final List<T> copy = new ArrayList<>(datum);
            listener.start();
            sorter.sort(generator.getComparator(), copy);
            listener.stop();
            cube.submit(schema, ordering, listener);
            listener.reset();
        }
        removeListener(sorter, listener);
    }

    private <T> void runShellTest(final ShellSorter sorter, final DataSetGenerator<T> generator, Schema schema, final Ordering ordering, final Cube cube, List<Integer> seq) {
        final SorterListenerImpl listener = new SorterListenerImpl();
        addListener(sorter, listener);
        final List<T> datum = ordering.create(generator, schema);
        for (int i = 0; i < RUNS; i++) {
            final List<T> copy = new ArrayList<>(datum);
            listener.start();
            sorter.sort(generator.getComparator(), copy, seq);
            listener.stop();
            cube.submit(schema, ordering, listener);
            listener.reset();
        }
        removeListener(sorter, listener);
    }

    private <T> void runMTest(final QuickSorterCutOff sorter, final DataSetGenerator<T> generator, Schema schema, final Ordering ordering, final Cube cube, int m) {
        final SorterListenerImpl listener = new SorterListenerImpl();
        addListener(sorter, listener);
        final List<T> datum = ordering.create(generator, schema);
        for (int i = 0; i < RUNS; i++) {
            final List<T> copy = new ArrayList<>(datum);
            listener.start();
            sorter.sort(generator.getComparator(), copy, m);
            listener.stop();
            cube.submit(schema, ordering, listener);
            listener.reset();
        }
        removeListener(sorter, listener);
    }

    private void removeListener(Sorter sorter, SorterListener listener) {
        if(sorter instanceof ObservableSorter) {
            ((ObservableSorter) sorter).removeSorterListener(listener);
        }
    }

    private void addListener(Sorter sorter, SorterListener listener) {
        if(sorter instanceof ObservableSorter) {
            ((ObservableSorter) sorter).addSorterListener(listener);
        }
    }


}