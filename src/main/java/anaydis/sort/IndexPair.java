package anaydis.sort;

import javafx.util.Pair;

/**
 * @author Tomas Perez Molina
 */
public class IndexPair extends Pair<Integer, Integer> {
    IndexPair(Integer left, Integer right) {
        super(left, right);
    }

    int getLeft(){
        return getKey();
    }

    int getRight(){
        return getValue();
    }
}