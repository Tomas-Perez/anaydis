package anaydis.sort.stats;

import anaydis.sort.IntegerDataSetGenerator;
import anaydis.sort.Sorter;
import anaydis.sort.SorterProviderImpl;
import anaydis.sort.SorterType;
import anaydis.sort.data.DataSetGenerator;
import anaydis.sort.gui.ObservableSorter;
import anaydis.sort.gui.SorterListener;
import anaydis.sort.provider.SorterProvider;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;


public class SortingAnalysis {

    private static final int RUNS = 10;

    private enum DataUnit {
        SWAPS,
        COMPARISONS,
        TIME
    }

    private enum Schema {
        ONE(100),
        TWO(500),
        THREE(1000);

        int size;

        Schema(int size) {
            this.size = size;
        }
    }

    private enum Ordering {
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

        void submit(final Schema schema, final Ordering ordering, final SorterAnalyzer listener) {
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

        private void submit(SorterAnalyzer listener) {
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

    public static void main(String[] args) {
        final SortingAnalysis analysis = new SortingAnalysis();
        final Map<SorterType, Cube> cubes = analysis.cubes();
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
                        final LongSummaryStatistics statistics =
                                cell.data.stream().collect(Collectors.summarizingLong(value -> value));
                        System.out.println("\t\t\t\t " + statistics);
                    }
                }
            }
        });
    }

    private <T> void run(final Sorter sorter, final DataSetGenerator<T> generator, Schema schema, final Ordering ordering, final Cube cube) {
        final SorterAnalyzer listener = new SorterAnalyzer();
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