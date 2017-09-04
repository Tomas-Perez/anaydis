package anaydis.sort.stats;

import anaydis.sort.*;
import anaydis.sort.gui.ObservableSorter;
import anaydis.sort.provider.SorterProvider;
import java.util.*;

/**
 * @author Tomas Perez Molina
 */
public class SortStatistics {
    private static Map<SorterType, SorterAnalyzer> listenerMap;

    public static void main(String[] args) {
        listenerMap = new EnumMap<>(SorterType.class);
        SorterProvider provider = new SorterProviderImpl();
        IntegerDataSetGenerator integerDataSetGenerator = new IntegerDataSetGenerator();
        for(Sorter sorter: provider.getAllSorters()){
            ObservableSorter abstractSorter = (ObservableSorter) sorter;
            SorterAnalyzer sorterAnalyzer = new SorterAnalyzer();
            abstractSorter.addSorterListener(sorterAnalyzer);
            listenerMap.put(abstractSorter.getType(), sorterAnalyzer);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("N?");
        final int n = scanner.nextInt();
        System.out.println("Repetitions?");
        final int repetitions = scanner.nextInt();

        final List<Integer> ascending = integerDataSetGenerator.createAscending(n);
        final List<Integer> descending = integerDataSetGenerator.createDescending(n);
        final List<Integer> random = integerDataSetGenerator.createRandom(n);
        System.out.println("ASCENDING");
        provider.getAllSorters().forEach(sorter -> sortReport(sorter, repetitions, ascending));
        System.out.println("-------------------------------------");
        System.out.println("DESCENDING");
        provider.getAllSorters().forEach(sorter -> sortReport(sorter, repetitions, descending));
        System.out.println("-------------------------------------");
        System.out.println("RANDOM");
        provider.getAllSorters().forEach(sorter -> sortReport(sorter, repetitions, random));
    }

    private static void sortReport(Sorter sorter, int repetitions, List<Integer> list) {
        long totalTime = 0;
        long firstTime = 0;
        for(int i = 0; i < repetitions; i++) {
            final List<Integer> integers = new ArrayList<>(list);
            long startTime = System.nanoTime();
            sorter.sort(Integer::compareTo, integers);
            long endTime = System.nanoTime();
            long delta = endTime - startTime;
            if(firstTime == 0){
                firstTime += delta;
            } else {
                totalTime += delta;
            }
        }

        System.out.println(sorter.getType().name());
        System.out.println("first time " + firstTime);
        System.out.println("avg Time " + (totalTime / repetitions));
        SorterAnalyzer analyzer = listenerMap.get(sorter.getType());
        System.out.println("swaps " + (analyzer.getSwaps() / repetitions));
        System.out.println("compares " + (analyzer.getCompares() / repetitions));
        System.out.println("----------------\n");
        analyzer.reset();
    }
}
