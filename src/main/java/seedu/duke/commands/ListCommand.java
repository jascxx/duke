package seedu.duke.commands;

import seedu.duke.TaskList;
import seedu.duke.exceptions.DukeException;

public class ListCommand extends Command {
    public ListCommand(TaskList taskList) {
        super(null, taskList);
    }

    @Override
    public String execute() throws DukeException {
        return list();
    }

    /**
     * Lists all the tasks.
     * @return The list.
     */
    private String list() {
        return "Here are the tasks in your list:\n" +
                taskList.toString() + "\n";
    }
}