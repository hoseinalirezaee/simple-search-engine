import data.Reader;
import index.Indexer;
import index.StopWordFinder;
import org.apache.lucene.analysis.fa.PersianAnalyzer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Reader reader = ArgsParser.parse(args);
        String indexDirPath = args[0];
        if (args[args.length - 2].equals("-stopword-only"))
        {
            StopWordFinder stopWordFinder = new StopWordFinder(new PersianAnalyzer(), Integer.parseInt(args[args.length - 1]));
            var stopwords = stopWordFinder.getStopWords(reader.getData());
            File file = new File(indexDirPath);
            file.mkdir();
            FileWriter writer = new FileWriter(indexDirPath + "/stopwords.txt");
            for (var stopword : stopwords)
            {
                writer.write(stopword + "\r\n");
            }
            writer.close();
        }
        else
        {
            Indexer indexer = new Indexer(reader);
            indexer.createIndex(indexDirPath);
        }
    }
}
