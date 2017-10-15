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