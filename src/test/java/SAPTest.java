
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class SAPTest {

    @ParameterizedTest
    @CsvFileSource(resources = "digraph25.txt")
    void getPath() {
    }
}