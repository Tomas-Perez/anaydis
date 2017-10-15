package anaydis.compression;

import anaydis.sort.data.DataSetGenerator;
import anaydis.sort.data.StringDataSetGenerator;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Tomas Perez Molina
 */
public abstract class AbstractCompressorTest {
    private final Compressor compressor;
    private final DataSetGenerator<String> generator;

    public AbstractCompressorTest(Compressor compressor) {
        this.compressor = compressor;
        generator = new StringDataSetGenerator();
    }

    @Test
    public void encodeDecodeParticularTest() throws Exception{
        List<String> strings = Collections.singletonList("ASHLEY");
        encondeDecodeListsTest(strings);
    }

    @Test
    public void largeTextTest() throws Exception{
        List<String> strings = Collections.singletonList("\"Call me Ishmael.  Some years ago--never mind how long\n" +
                "    precisely--having little or no money in my purse, and nothing\n" +
                "    particular to interest me on shore, I thought I would sail about a\n" +
                "    little and see the watery part of the world.  It is a way I have of\n" +
                "    driving off the spleen and regulating the circulation.  Whenever I\n" +
                "    find myself growing grim about the mouth; whenever it is a damp,\n" +
                "    drizzly November in my soul; whenever I find myself involuntarily\n" +
                "    pausing before coffin warehouses, and bringing up the rear of every\n" +
                "    funeral I meet; and especially whenever my hypos get such an upper\n" +
                "    hand of me, that it requires a strong moral principle to prevent me\n" +
                "    from deliberately stepping into the street, and methodically knocking\n" +
                "    people's hats off--then, I account it high time to get to sea as soon\n" +
                "    as I can.  This is my substitute for pistol and ball.  With a\n" +
                "    philosophical flourish Cato throws himself upon his sword; I quietly\n" +
                "    take to the ship.  There is nothing surprising in this.  If they but\n" +
                "    knew it, almost all men in their degree, some time or other, cherish\n" +
                "    very nearly the same feelings towards the ocean with me.\n" +
                "     \n" +
                "    There now is your insular city of the Manhattoes, belted round by\n" +
                "    wharves as Indian isles by coral reefs--commerce surrounds it with\n" +
                "    her surf.  Right and left, the streets take you waterward.  Its\n" +
                "    extreme downtown is the battery, where that noble mole is washed by\n" +
                "    waves, and cooled by breezes, which a few hours previous were out of\n" +
                "    sight of land.  Look at the crowds of water-gazers there.\n" +
                "     \n" +
                "    Circumambulate the city of a dreamy Sabbath afternoon.  Go from\n" +
                "    Corlears Hook to Coenties Slip, and from thence, by Whitehall,\n" +
                "    northward.  What do you see?--Posted like silent sentinels all around\n" +
                "    the town, stand thousands upon thousands of mortal men fixed in ocean\n" +
                "     \n" +
                "    reveries.  Some leaning against the spiles; some seated upon the\n" +
                "    pier-heads; some looking over the bulwarks of ships from China; some\n" +
                "    high aloft in the rigging, as if striving to get a still better\n" +
                "    seaward peep.  But these are all landsmen; of week days pent up in\n" +
                "    lath and plaster--tied to counters, nailed to benches, clinched to\n" +
                "    desks.  How then is this?  Are the green fields gone?  What do they\n" +
                "    here?\n" +
                "     \n" +
                "    But look! here come more crowds, pacing straight for the water, and\n" +
                "    seemingly bound for a dive.  Strange!  Nothing will content them but\n" +
                "    the extremest limit of the land; loitering under the shady lee of\n" +
                "    yonder warehouses will not suffice.  No.  They must get just as nigh\n" +
                "    the water as they possibly can without falling in.  And there they\n" +
                "    stand--miles of them--leagues.  Inlanders all, they come from lanes\n" +
                "    and alleys, streets and avenues--north, east, south, and west.  Yet\n" +
                "    here they all unite.  Tell me, does the magnetic virtue of the\n" +
                "    needles of the compasses of all those ships attract them thither?\n" +
                "    \"\n" +
                "\n" +
                "\n");
        encondeDecodeListsTest(strings);
    }

    @Test
    public void encodeDecodeTestRandom() throws Exception{
        List<String> strings = generator.createRandom(10);
        encondeDecodeListsTest(strings);
    }

    @Test
    public void encodeDecodeDuplicatesTest() throws Exception{
        List<String> strings = Arrays.asList("NNNNNN", "ADADADDADA", "NNDSDDDDDDD");
        encondeDecodeListsTest(strings);
    }

    private void encondeDecodeListsTest(List<String> strings) throws Exception{
        for (String string : strings) {
            InputStream stringInput = new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8.name()));
            ByteArrayOutputStream compressedOutput = new ByteArrayOutputStream();
            compressor.encode(stringInput, compressedOutput);
            byte[] compressedBytes = compressedOutput.toByteArray();

            InputStream compressedInput = new ByteArrayInputStream(compressedBytes);
            ByteArrayOutputStream stringOutput = new ByteArrayOutputStream();
            compressor.decode(compressedInput, stringOutput);

            byte[] expectedStringBytes = string.getBytes();
            byte[] actualStringBytes = stringOutput.toByteArray();

            assertThat(actualStringBytes).isEqualTo(expectedStringBytes);
        }
    }
}