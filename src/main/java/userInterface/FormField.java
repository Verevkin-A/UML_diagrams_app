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

    public String getVisibilitySymbol() {
        switch (this.visibility) {
            case "Public":
                return "+";
            case "Private":
                return "–";
            case "Protected":
                return "#";
            case "Package":
                return "~";
        }
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
