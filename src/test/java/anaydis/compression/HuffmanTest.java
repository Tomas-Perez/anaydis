package anaydis.compression;

import static org.junit.Assert.*;

/**
 * @author Tomas Perez Molina
 */
public class HuffmanTest extends AbstractCompressorTest{
    public HuffmanTest() {
        super(new Huffman());
    }
}