package sequenceDiagram;

import java.util.ArrayList;

/**
 * A class representing one sequence diagram.
 * @author Marek Dohnal
 * @since 25/04/2022
 */
public class SequenceDiagram {
    private ArrayList<SDObject> objects;
    private ArrayList<SDMessage> messages;

    public SequenceDiagram() {
        this.objects = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public void setMessages(ArrayList<SDMessage> messages) {
        this.messages = messages;
    }

    public void setObjects(ArrayList<SDObject> objects) {
        this.objects = objects;
    }

    public ArrayList<SDMessage> getMessages() {
        return messages;
    }

    public ArrayList<SDObject> getObjects() {
        return objects;
    }
}
