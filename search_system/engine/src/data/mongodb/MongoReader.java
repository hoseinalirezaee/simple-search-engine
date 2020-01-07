package data.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import data.Data;
import data.Reader;

import java.util.ArrayList;

public class MongoReader implements Reader {

    private MongoClientURI mongoUri;

    // name of database that contains information to index
    private String infoDatabaseName;

    // name of collection that contains information to index
    private String infoCollectionName;

    public MongoReader(
            String hostname, int port, String username, String password, String authDB,
            String infoDatabaseName, String infoCollectionName
    )
    {
        this.mongoUri = createMongoUri(hostname, port, username, password, authDB);
        this.infoDatabaseName = infoDatabaseName;
        this.infoCollectionName = infoCollectionName;
    }

    private MongoClientURI createMongoUri(
            String hostname, int port, String username, String password, String authDB
    )
    {
        String uriString = String.format("mongodb://%s:%s@%s:%d/%s",
                username, password, hostname, port, authDB);
        MongoClientURI mongoClientURI = new MongoClientURI(uriString);
        return mongoClientURI;
    }

    @Override
    public ArrayList<Data> getData() {
        MongoClient client = new MongoClient(this.mongoUri);
        var database = client.getDatabase(this.infoDatabaseName);
        var collection = database.getCollection(this.infoCollectionName);
        var querySet = collection.find();
        ArrayList<Data> dataList = new ArrayList<>();

        for (var item : querySet)
        {
            String title = item.getString("title");
            String body = item.getString("body");
            String id = item.getObjectId("_id").toString();
            Data data = new Data(title, body, id);
            dataList.add(data);
        }

        return dataList;
    }
}
