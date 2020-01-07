import data.Reader;
import index.Indexer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Reader reader = ArgsParser.parse(args);
        Indexer indexer = new Indexer(reader);
        String indexDirPath = args[0];
        indexer.createIndex(indexDirPath);
    }
}
