package anaydis.compression;

import javafx.util.Pair;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Tomas Perez Molina
 */
public class HuffmanTableTest {
    @Test
    public void test() throws Exception{
        List<HuffmanTable.CharacterFreqPair> list = Arrays.asList(
                new HuffmanTable.CharacterFreqPair(25, 'A'),
                new HuffmanTable.CharacterFreqPair(25, 'B'),
                new HuffmanTable.CharacterFreqPair(20, 'C'),
                new HuffmanTable.CharacterFreqPair(15, 'D'),
                new HuffmanTable.CharacterFreqPair(10, 'E'),
                new HuffmanTable.CharacterFreqPair(5, 'F')
        );
        HuffmanTable table = new HuffmanTable(list);
        List<Pair<Integer, Character>> expected = Arrays.asList(
                new Pair<>(0b0, 'C'),
                new Pair<>(0b10, 'B'),
                new Pair<>(0b01, 'A'),
                new Pair<>(0b011, 'D'),
                new Pair<>(0b0111, 'F'),
                new Pair<>(0b1111, 'E')
        );
        assertThat(table.getKeyValuePairs()).containsAll(expected);
    }
}