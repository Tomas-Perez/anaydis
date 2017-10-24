package anaydis.compression;

import static org.junit.Assert.*;

/**
 * @author Tomas Perez Molina
 */
public class BurrowsWheelerTest extends AbstractCompressorTest{
    public BurrowsWheelerTest() {
        super(new BurrowsWheeler());
    }
}