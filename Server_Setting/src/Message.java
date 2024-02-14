import java.io.Serializable;

public class Message implements Serializable {
    //region fields
    private final String from;
    private final String name;
    private final String text;
    //endregion

    //region Constructor
    public Message(String from, String name, String text) {
        this.from = from;
        this.name = name;
        this.text = text;
    }
    //endregion

    //region methods
    public String getFrom() {
        return from;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " " + text;
    }
    //endregion
}
