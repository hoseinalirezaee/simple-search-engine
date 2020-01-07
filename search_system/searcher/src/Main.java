import org.apache.lucene.queryparser.classic.ParseException;
import search.Searcher;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        String indexDirPath = args[0];
        String query = args[1];

        Searcher searcher = new Searcher(indexDirPath) ;
        var result = searcher.search(query);
        for(var id : result)
        {
            System.out.println(id);
        }
    }
}
