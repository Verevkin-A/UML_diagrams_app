package userInterface;

public class FormField {
    private String name;
    private String type;
    private String visibility;

    public FormField(String name, String type, String visibility) {
        this.name = name;
        this.type = type;
        this.visibility = visibility;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public String getVisibility() {
        return this.visibility;
    }
}
