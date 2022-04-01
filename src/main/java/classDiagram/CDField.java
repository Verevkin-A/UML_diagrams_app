package classDiagram;

/**
 * @author Marek Dohnal (xdohna48)
 * @since 2022-03-26
 */
public class CDField {
    private String name;
    private Visibility visibility;

    public CDField(String name, Visibility visibility) {
        this.name = name;
        this.visibility = visibility;
    }

    public String getName() {
        return this.name;
    }

    public String getVisibilityAsString() {
        return this.visibility.getSymb();
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

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
