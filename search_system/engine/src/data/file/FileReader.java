package data.file;

import data.Data;
import data.Reader;
import org.json.JSONArray;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class FileReader implements Reader {

    private FileInputStream inputStream;

    public FileReader(String path) throws FileNotFoundException {
        var inputStream = createInputStream(path);
        setInputStream(inputStream);
    }

    public FileReader(FileInputStream inputStream)
    {
        setInputStream(inputStream);
    }

    private FileInputStream createInputStream(String path) throws FileNotFoundException {
        return new FileInputStream(path);
    }

    private void setInputStream(FileInputStream inputStream)
    {
        this.inputStream = inputStream;
    }

    public ArrayList<Data> getData() throws IOException {
        String dataString = readFromFileStream(this.inputStream);
        JSONArray dataObjects = new JSONArray(dataString);
        ArrayList<Data> dataList = new ArrayList<>();

        for (int i = 0; i < dataObjects.length(); i++)
        {
            var post = dataObjects.getJSONObject(i);
            String title = post.getString("title");
            String body = post.getString("body");
            String id = post.getString("id");

            Data data = new Data(title, body, id);

            dataList.add(data);
        }
        return dataList;
    }

    private String readFromFileStream(FileInputStream inputStream) throws IOException {
        var allBytes = inputStream.readAllBytes();
        var data = new String(allBytes);
        return data;
    }

}
