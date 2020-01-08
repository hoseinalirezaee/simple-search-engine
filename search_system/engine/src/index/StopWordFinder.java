package index;

import data.Data;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class StopWordFinder {

    private Analyzer analyzer;
    private int percentThreshold;

    public StopWordFinder(Analyzer analyzer, int percentThreshold)
    {
        this.analyzer = analyzer;
        this.percentThreshold = percentThreshold;
    }

    public ArrayList<String> getStopWords(ArrayList<Data> data) throws IOException {
        var wordFreq = getWordFrequency(data);
        int freqThreshold = data.size() * percentThreshold / 100;
        ArrayList<String> stopWords = new ArrayList<>();
        for (var item : wordFreq.entrySet())
        {
            if (item.getValue() >= freqThreshold)
            {
                stopWords.add(item.getKey());
            }
        }
        return stopWords;
    }

    private Hashtable<String, Integer> getWordFrequency(ArrayList<Data> data) throws IOException {
        Hashtable<String, Integer> table = new Hashtable<>(data.size());
        for (var item : data)
        {
            String bodyText = item.getBody();
            String titleText = item.getTitle();

            TokenStream tokenStream = analyzer.tokenStream(null, bodyText + " " + titleText);
            tokenStream.reset();
            while (tokenStream.incrementToken())
            {
                var word = tokenStream.getAttribute(CharTermAttribute.class).toString();
                var count = table.getOrDefault(word, 0);
                table.put(word, count + 1);
            }
            tokenStream.close();

        }
        return table;
    }
}
