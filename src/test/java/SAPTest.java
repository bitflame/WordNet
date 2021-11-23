
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.io.File;
import java.util.stream.Stream;


class SAPTest {
    static File inputFolder = new File("src/test/resources/");
    final static String destFolder = "src/Test/Results";
    static Digraph digraph;
    static SAP sap;

    static class DigraphProvider implements ArgumentsProvider {


        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            int from = 0;
            int to = 0;
            for (File fileEntry : inputFolder.listFiles()) {
                int[] inputs = new int[2];// add from/to to this array or an array list and send it to the test
                String fileName = fileEntry.getName();
                In in = new In(fileName);
                from = in.readInt();
                to = in.readInt();
                digraph = new Digraph(in);
            }
            return Stream.of(Arguments.of(digraph, from, to));
        }
    }

    @ParameterizedTest(name = "{index}=> digraph = {0}, from = {1}, to = {2}")
    @ArgumentsSource(DigraphProvider.class)
    void getPathShouldCalculateMinPath(Digraph digraph, int from, int to) {
        sap = new SAP(digraph);
        int[] expected = {0,1,2};
        int [] actual = new int[3];
        for (int i:sap.getPath(from,to)) {
            actual[i]=i;
        }
        Assertions.assertArrayEquals(actual,expected);
    }
}