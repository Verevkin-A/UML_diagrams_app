package sequenceDiagram;

/**
 * Represents a lifeline activation of an object.
 * @author Marek Dohnal
 * @since 25/04/2022
 */
public class SDActivation {
    private int timeBegin;
    private int timeEnd;

    /**
     * Constructs a new activation block starting at timeBegin and ending at timeEnd
     * @param timeBegin the time at which the block starts
     * @param timeEnd the time at which the block ends
     */
    public SDActivation(int timeBegin, int timeEnd) {
        this.timeBegin = timeBegin;
        this.timeEnd = timeEnd;
    }

    public void setTimeBegin(int timeBegin) {
        this.timeBegin = timeBegin;
    }

    public void setTimeEnd(int timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getTimeBegin() {
        return timeBegin;
    }

    public int getTimeEnd() {
        return timeEnd;
    }
}
