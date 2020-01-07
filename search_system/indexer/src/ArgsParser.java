import data.Reader;
import data.file.FileReader;
import data.mongodb.MongoReader;

import java.io.FileNotFoundException;

public class ArgsParser {

    public static Reader parse(String[] args) throws FileNotFoundException {

        if (args[1].equals("file"))
        {
            String dataFileName = args[2];
            return new FileReader(dataFileName);
        }
        else if (args[1].equals("mongodb"))
        {
            String hostname = args[2];
            String port = args[3];
            String username = args[4];
            String password = args[5];
            String authDB = args[6];
            String infoDB = args[7];
            String infoCollection = args[8];

            return new MongoReader(
                    hostname, Integer.parseInt(port), username, password, authDB, infoDB, infoCollection
            );
        }
        return null;
    }
}
