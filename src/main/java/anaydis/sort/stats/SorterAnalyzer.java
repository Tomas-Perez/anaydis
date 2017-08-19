package anaydis.sort.stats;

import anaydis.sort.gui.SorterListener;

/**
 * @author Tomas Perez Molina
 */
public class SorterAnalyzer implements SorterListener{
    private int swaps;
    private int compares;
    private int equals;
    private int boxs;
    private int copies;


    @Override
    public void box(int from, int to) {
        boxs++;
    }

    @Override
    public void copy(int from, int to, boolean copyToAux) {
        copies++;
    }

    @Override
    public void equals(int i, int j) {
        equals++;
    }

    @Override
    public void greater(int i, int j) {
        compares++;
    }

    @Override
    public void swap(int i, int j) {
        swaps++;
    }

    public int getSwaps() {
        return swaps;
    }

    public int getCompares() {
        return compares;
    }

    public int getEquals() {
        return equals;
    }

    public int getBoxs() {
        return boxs;
    }

    public int getCopies() {
        return copies;
    }

    public void clear(){
        swaps = 0;
        compares = 0;
        equals = 0;
        boxs = 0;
        copies = 0;
    }
}
