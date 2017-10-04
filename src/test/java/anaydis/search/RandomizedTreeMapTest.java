package anaydis.search;

/**
 * @author Tomas Perez Molina
 */
public class RandomizedTreeMapTest extends AbstractMapTester {

    public RandomizedTreeMapTest(){
        super(new RandomizedTreeMap<>(Integer::compareTo));
    }
}
