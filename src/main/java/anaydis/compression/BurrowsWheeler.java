package anaydis.compression;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Tomas Perez Molina
 */
public class BurrowsWheeler implements Compressor{
    private final int CONTROL_VAL = 255;

    @Override
    public void encode(@NotNull InputStream input, @NotNull OutputStream output) throws IOException {
        int read_byte = input.read();
        List<Integer> msgBytes = new ArrayList<>();
        while(read_byte != -1){
            msgBytes.add(read_byte);
            read_byte = input.read();
        }
        List<Rotation> rotations = IntStream
                                            .range(0, msgBytes.size())
                                            .boxed()
                                            .map(x -> new Rotation(msgBytes, x))
                                            .collect(Collectors.toList());
        rotations.sort(Rotation::compareTo);
        List<Integer> l = rotations.stream().map(Rotation::getLast).collect(Collectors.toList());
        for (Integer character : l) {
            output.write(character);
        }
        output.write(CONTROL_VAL);
        for (int i = 0; i < rotations.size(); i++) {
            Rotation current = rotations.get(i);
            if(current.number == 1) writeInt(i, output);
        }
        input.close();
        output.close();
    }

    @Override
    public void decode(@NotNull InputStream input, @NotNull OutputStream output) throws IOException {
        List<Pair<Integer, Integer>> lAndSubIndexes = new ArrayList<>();
        HashMap<Integer, Integer> charSubIndexMap = new HashMap<>();
        int read_byte = input.read();
        while(read_byte != CONTROL_VAL){
            Integer character = read_byte;
            charSubIndexMap.merge(character, 0, (a, b) -> a + 1);
            Integer subIndex = charSubIndexMap.get(character);
            lAndSubIndexes.add(new Pair<>(character, subIndex));
            read_byte = input.read();
        }
        int nextIndex = readInt(input);
        List<Pair<Integer, Integer>> fAndSubIndexes = new ArrayList<>(lAndSubIndexes);
        fAndSubIndexes.sort(Comparator.comparingInt(Pair::getKey));
        List<Integer> t = fAndSubIndexes.stream().map(lAndSubIndexes::indexOf).collect(Collectors.toList());
        byte[] msg = new byte[lAndSubIndexes.size()];
        for (int i = 0; i < msg.length; i++) {
            msg[i] = lAndSubIndexes.get(nextIndex).getKey().byteValue();
            nextIndex = t.get(nextIndex);
        }
        output.write(msg);
        input.close();
        output.close();
    }

    private class Rotation implements Comparable<Rotation>{
        private final List<Integer> msg;
        private final int number;
        private final int length;

        Rotation(List<Integer> msg, int number) {
            this.msg = msg;
            this.number = number;
            this.length = msg.size();
        }

        Integer get(int index){
            if (index < 0 || index >= length) {
                throw new StringIndexOutOfBoundsException(index);
            }
            return msg.get((number + index) % length);
        }

        Integer getLast(){
            return get(length - 1);
        }

        @Override
        public int compareTo(@NotNull Rotation o) {
            for (int i = 0; i < length; i++) {
                int comp = get(i) - o.get(i);
                if(comp != 0) return comp;
            }
            return 0;
        }
    }

    private void writeInt(int anInt, OutputStream stream) throws IOException{
        byte[] buffer = new byte[4];
        ByteBuffer.wrap(buffer).putInt(anInt);
        stream.write(buffer);
    }

    private int readInt(InputStream stream) throws IOException{
        byte[] buffer = new byte[4];
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte) stream.read();
        }
        return ByteBuffer.wrap(buffer).getInt();
    }
}
