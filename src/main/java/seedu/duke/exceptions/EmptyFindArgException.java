package seedu.duke.exceptions;

public class EmptyFindArgException extends DukeException {

    public EmptyFindArgException() {
        super("OOPS!!! The keyword to find cannot be empty, e.g. find swim");
    }
}