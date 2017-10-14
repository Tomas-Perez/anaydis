package anaydis.compression;

import static org.junit.Assert.*;

/**
 * @author Tomas Perez Molina
 */
public class RunLengthEncodingTest extends AbstractCompressorTest{
    public RunLengthEncodingTest() {
        super(new RunLengthEncoding());
    }
}