package classDiagram;

/**
 * Represents a field or a method of a class.
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public class CDField {
    private String name;
    private Visibility visibility;

    /**
     * Constructs a filled field.
     * @param name the name of the field
     * @param visibility the visibility of the field
     */
    public CDField(String name, Visibility visibility) {
        this.name = name;
        this.visibility = visibility;
    }

    /**
     *
     * @return the name of the field
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return the visibility of the field as string
     */
    public String getVisibilityAsString() {
        return this.visibility.getSymb();
    }

    /**
     *
     * @return the visibility of the field
     */
    public Visibility getVisibility() {
        return this.visibility;
    }

    /**
     * Two fields equal if and only if
     * they have the same name and the same visibility
     * @param obj an object to compare to
     * @return true if the objects equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final CDField other = (CDField) obj;

        return this.name.equals(other.name) && this.visibility == other.visibility;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 53 * hash + this.getVisibilityAsString().hashCode();
        return hash;
    }
}
