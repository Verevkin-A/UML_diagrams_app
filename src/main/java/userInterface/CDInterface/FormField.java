package userInterface.CDInterface;

/**
 * Method/Field structure in class form
 * @author Aleksandr Verevkin (xverev00)
 * @since 2022-04-02
 */
public class FormField {
    private String name;
    private String type;
    private String visibility;

    /**
     * FormField constructor
     *
     * @param name field name
     * @param type field type
     * @param visibility field visibility
     */
    public FormField(String name, String type, String visibility) {
        this.name = name;
        this.type = type;
        this.visibility = visibility;
    }

    /**
     * Field name getter
     *
     * @return field name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Field type getter
     *
     * @return field type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Field visibility getter
     *
     * @return field visibility
     */
    public String getVisibility() {
        return this.visibility;
    }

    /**
     * Visibility as symbol getter
     *
     * @return visibility symbol
     */
    public String getVisibilitySymbol() {
        switch (this.visibility) {
            case "Public":
                return "+";
            case "Private":
                return "-";
            case "Protected":
                return "#";
            case "Package":
                return "~";
        }
        return null;
    }

    /**
     * Field name setter
     *
     * @param name field name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Field type setter
     *
     * @param type field type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Field visibility setter
     *
     * @param visibility field visibility
     */
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
}
