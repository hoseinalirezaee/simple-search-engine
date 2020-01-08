package search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Searcher {

    private String indexDirPath;

    public Searcher(String indexDirPath)
    {
        setIndexDirPath(indexDirPath);
    }

    private void setIndexDirPath(String indexDirPath)
    {
        this.indexDirPath = indexDirPath;
    }

    public ArrayList<String> search(String queryString) throws IOException, ParseException {
        IndexSearcher indexSearcher = createIndexSearcher(this.indexDirPath);
        Query query = prepareQuery(queryString);
        TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
        var docsId = getDocsId(topDocs, indexSearcher);
        return docsId;
    }

    private ArrayList<String> getDocsId(TopDocs topDocs, IndexSearcher searcher) throws IOException {
        var docsId = topDocs.scoreDocs;
        var docsIdList = new ArrayList<String>();
        for (var item : docsId)
        {
            Document document = searcher.doc(item.doc);
            docsIdList.add(document.get("id"));
        }
        return docsIdList;
    }

    private Query prepareQuery(String query) throws ParseException {
        Analyzer analyzer = new StandardAnalyzer();
        var fieldsToSearch = new String[] {"title"};
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(
                fieldsToSearch,
                analyzer
        );

        return queryParser.parse(query);
    }
    private IndexSearcher createIndexSearcher(String indexDirPath) throws IOException {

        var indexDir = FSDirectory.open(Paths.get(indexDirPath));

        IndexReader indexReader = DirectoryReader.open(indexDir);

        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        return indexSearcher;
    }

}
