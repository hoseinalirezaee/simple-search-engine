package index;

import data.Data;
import data.Reader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.fa.PersianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Indexer {

    private ArrayList<Data> dataList;
    public Indexer(ArrayList<Data> dataList)
    {
        setDataList(dataList);
    }
    public Indexer(Reader reader) throws IOException {
        setDataList(reader.getData());
    }

    private void setDataList(ArrayList<Data> dataList)
    {
        this.dataList = dataList;
    }

    public void createIndex(String indexDirPath) throws IOException {

        StopWordFinder stopWordFinder = new StopWordFinder(new PersianAnalyzer(), 95);
        var stopWords = stopWordFinder.getStopWords(this.dataList);
        var temp = new CharArraySet(stopWords.size(), true);
        temp.addAll(stopWords);
        var indexWriter = createIndexWrite(indexDirPath, temp);
        var documents = createDocumentsList(this.dataList);
        indexWriter.addDocuments(documents);
        indexWriter.close();
    }

    private ArrayList<Document> createDocumentsList(ArrayList<Data> dataList)
    {
        ArrayList<Document> documentsList = new ArrayList<>();
        for (var item : dataList)
        {
            Document document = new Document();
            document.add(new TextField("title", item.getTitle(), Field.Store.NO));
            document.add(new TextField("body", item.getBody(), Field.Store.NO));
            document.add(new TextField("id", item.getId(), Field.Store.YES));

            documentsList.add(document);
        }
        return documentsList;
    }

    private IndexWriter createIndexWrite(String indexDirPath, CharArraySet stopWords) throws IOException {
        var path = Paths.get(indexDirPath);
        Directory indexDir = FSDirectory.open(path);
        Analyzer analyzer = new PersianAnalyzer(stopWords);

        IndexWriterConfig icfg = new IndexWriterConfig(analyzer);
        icfg.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        IndexWriter indexWriter = new IndexWriter(indexDir, icfg);
        return indexWriter;
    }
}

