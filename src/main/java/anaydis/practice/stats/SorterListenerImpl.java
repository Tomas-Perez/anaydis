package anaydis.practice.stats;

import anaydis.sort.gui.SorterListener;

/**
 * @author Tomas Perez Molina
 */
public class SorterListenerImpl implements SorterListener{
    private int swaps;
    private int compares;
    private int equals;
    private int boxes;
    private int copies;
    private long startTime;
    private long finishTime;

    @Override
    public void box(int from, int to) {
        boxes++;
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

    public int getBoxes() {
        return boxes;
    }

    public int getCopies() {
        return copies;
    }

    public void start(){
        startTime = System.nanoTime();
    }

    public void stop(){
        finishTime = System.nanoTime();
    }

    public long getElapsedTime() {
        return finishTime - startTime;
    }

    public void reset(){
        swaps = 0;
        compares = 0;
        equals = 0;
        boxes = 0;
        copies = 0;
    }
}
