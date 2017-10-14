package anaydis.compression;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Tomas Perez Molina
 */
public class RunLengthEncoding implements Compressor{
    private final int CONTROL_VAL = 255;

    @Override
    public void encode(@NotNull InputStream input, @NotNull OutputStream output) throws IOException {
        int read_byte = input.read();
        int current_byte = 256;
        int count = 0;
        while(current_byte != -1){
            if(current_byte != read_byte){
                if(count > 0){
                    if(count > 1){
                        output.write(CONTROL_VAL);
                        output.write(count);
                        output.write(current_byte);
                    }
                    else output.write(current_byte);
                }
                current_byte = read_byte;
                count = 1;
            } else {
                if(count == CONTROL_VAL - 1){
                    output.write(CONTROL_VAL);
                    output.write(CONTROL_VAL - 1);
                    output.write(current_byte);
                    count = 0;
                }
                count++;
            }
            read_byte = input.read();
        }
        input.close();
        output.close();
    }

    @Override
    public void decode(@NotNull InputStream input, @NotNull OutputStream output) throws IOException {
        int read_byte = input.read();
        while(read_byte != -1){
            if(read_byte == CONTROL_VAL){
                int count = input.read();
                int value = input.read();
                for(int i = 0; i < count; i++) output.write(value);
            } else output.write(read_byte);
            read_byte = input.read();
        }
        input.close();
        output.close();
    }
}
