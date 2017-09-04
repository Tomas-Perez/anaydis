package anaydis.sort;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author Tomas Perez Molina
 */
public class ShellSorterTest extends SorterTest{
    public ShellSorterTest() {
        super(SorterType.SHELL);
    }
}
