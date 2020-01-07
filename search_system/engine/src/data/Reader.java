package data;

import java.io.IOException;
import java.util.ArrayList;

public interface Reader {
    public ArrayList<Data> getData() throws IOException;
}
