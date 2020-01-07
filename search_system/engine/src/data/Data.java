package data;

public class Data {
    private String title;
    private String body;
    private String id;

    public Data(String title, String body, String id)
    {
        setTitle(title);
        setBody(body);
        setId(id);
    }

    private void setTitle(String title)
    {
        this.title = title;
    }

    private void setBody(String body)
    {
        this.body = body;
    }

    private void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return this.title;
    }
    public String getBody()
    {
        return this.body;
    }
    public String getId()
    {
        return this.id;
    }

}
