package anaydis.compression;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Tomas Perez Molina
 */
public class BurrowsWheeler implements Compressor{
    @Override
    public void encode(@NotNull InputStream input, @NotNull OutputStream output) throws IOException {
        int read_byte = input.read();
        List<Integer> msgBytes = new ArrayList<>();
        while(read_byte != -1){
            msgBytes.add(read_byte);
            read_byte = input.read();
        }
        List<Rotation> rotations = IntStream
                                            .range(0, msgBytes.size() + 1)
                                            .boxed()
                                            .map(x -> new Rotation(msgBytes, x))
                                            .collect(Collectors.toList());
        rotations.sort(Rotation::compareTo);
        List<Integer> l = rotations.stream().map(Rotation::getLast).collect(Collectors.toList());
        for (Integer character : l) {
            output.write(character);
        }
        for (int i = 0; i < rotations.size(); i++) {
            Rotation current = rotations.get(i);
            if(current.number == 1) output.write(i);
        }
        input.close();
        output.close();
    }

    @Override
    public void decode(@NotNull InputStream input, @NotNull OutputStream output) throws IOException {
        List<Integer> l = new ArrayList<>();
        int read_byte = input.read();
        while(read_byte != -1){
            l.add(read_byte);
            read_byte = input.read();
        }
        List<Integer> f = l.subList(0, l.size());
        f.sort(Integer::compareTo);
        List<Integer> t = l.stream().map(f::indexOf).collect(Collectors.toList());

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
}
