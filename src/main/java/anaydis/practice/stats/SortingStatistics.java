package anaydis.practice.stats;

import anaydis.sort.*;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tomas Perez Molina
 */
public class SortingStatistics {

    public static class BasicAllSorterAnalysis {
        public static void main(String[] args) {
            new SortingAnalyzer().basicAnalysis();
        }
    }

    public static class ShellSorterAnalysis {
        public static void main(String[] args) {
            new SortingAnalyzer().shellAnalysis();
        }
    }

    public static class QuickSorterMAnalysis{
        public static void main(String[] args) {
            new SortingAnalyzer().mAnalysis();
        }
    }

    public static class QuickSortersAnalysis{
        public static void main(String[] args) {
            new SortingAnalyzer().analyze(
                    Arrays.asList(new QuickSorter(), new QuickSorter3Way(), new QuickSorterCutOff(), new QuickSorterMO3(), new QuickSorterNR()),
                    Arrays.asList(SortingAnalyzer.Schema.ONE_THOUSAND, SortingAnalyzer.Schema.TEN_THOUSAND, SortingAnalyzer.Schema.HUNDRED_THOUSAND, SortingAnalyzer.Schema.MILLION),
                    Arrays.asList(SortingAnalyzer.Ordering.RANDOM));
        }
    }

    public static class MergeSortersAnalysis{
        public static void main(String[] args) {
            new SortingAnalyzer().analyze(
                    Arrays.asList(new MergeSorterBottomUp(), new MergeSorterTopDown()),
                    Arrays.asList(SortingAnalyzer.Schema.ONE_THOUSAND, SortingAnalyzer.Schema.TEN_THOUSAND, SortingAnalyzer.Schema.HUNDRED_THOUSAND, SortingAnalyzer.Schema.MILLION),
                    Arrays.asList(SortingAnalyzer.Ordering.RANDOM));
        }
    }
}
