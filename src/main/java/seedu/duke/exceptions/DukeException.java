package seedu.duke.exceptions;

/**
 * Parent class for all Duke related exceptions.
 */
public abstract class DukeException extends Exception {
    private String desc;

    DukeException(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
