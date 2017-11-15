package anaydis.search;

import anaydis.sort.IntegerDataSetGenerator;
import anaydis.sort.data.DataSetGenerator;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Tomas Perez Molina
 */
public class TSTTrieMapTest extends AbstractMapTester{
    @Test
    public void emptyIterator() throws Exception{
        TSTTrieMap<Integer> trie = new TSTTrieMap<>();
        Iterator<String> iterator = trie.keys();
        while(iterator.hasNext()){
            assertThat(iterator.next()).isNotNull();
        }
    }

    @Test
    public void wildcardKeys() throws Exception {
        TSTTrieMap<Integer> trie = new TSTTrieMap<>();

        List<String> keys = Arrays.asList("Dado", "Dido", "Dodo", "Dudo", "Dato", "Pato", "Da");
        DataSetGenerator<Integer> valueGenerator = new IntegerDataSetGenerator();
        List<Integer> values = valueGenerator.createRandom(keys.size());
        for(int i = 0; i < keys.size(); i++){
            trie.put(keys.get(i), values.get(i));
        }

        List<String> expected1 = Arrays.asList("Dado", "Dato");
        String pattern1 = "Da*o";
        List<String> actual1 = trie.wildcardKeys(pattern1);

        assertThat(actual1).hasSameSizeAs(expected1);
        assertThat(actual1).containsExactlyElementsOf(expected1);

        List<String> expected2 = Arrays.asList("Dado", "Dido", "Dodo", "Dudo", "Dato", "Pato");
        expected2.sort(String::compareTo);
        String pattern2 = "****";
        List<String> actual2 = trie.wildcardKeys(pattern2);

        assertThat(actual2).hasSameSizeAs(expected2);
        assertThat(actual2).containsExactlyElementsOf(expected2);

        List<String> expected3 = Arrays.asList("Dato", "Pato");
        String pattern3 = "*ato";
        List<String> actual3 = trie.wildcardKeys(pattern3);

        assertThat(actual3).hasSameSizeAs(expected3);
        assertThat(actual3).containsExactlyElementsOf(expected3);

        List<String> expected4 = Collections.singletonList("Da");
        String pattern4 = "**";
        List<String> actual4 = trie.wildcardKeys(pattern4);

        assertThat(actual4).hasSameSizeAs(expected4);
        assertThat(actual4).containsExactlyElementsOf(expected4);
    }

    public TSTTrieMapTest() {
        super(new TSTTrieMap<>());
    }
}