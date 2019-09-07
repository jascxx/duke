package seedu.duke;

import seedu.duke.exceptions.ArgumentNotNumberException;
import seedu.duke.exceptions.DukeException;
import seedu.duke.exceptions.EmptyDeadlineArgException;
import seedu.duke.exceptions.EmptyDeadlineDescException;
import seedu.duke.exceptions.EmptyEventArgException;
import seedu.duke.exceptions.EmptyEventDescException;
import seedu.duke.exceptions.EmptyFindArgException;
import seedu.duke.exceptions.EmptyTodoDescException;
import seedu.duke.exceptions.TaskDoesNotExistException;
import seedu.duke.exceptions.TaskNotSpecifiedException;
import seedu.duke.exceptions.UnknownCommandException;
import seedu.duke.task.Deadline;
import seedu.duke.task.Event;
import seedu.duke.task.Task;
import seedu.duke.task.Todo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Parses string commands and execute them.
 */
public class Parser {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String DONE_COMMAND = "done";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";

    private TaskList taskList;

    Parser(Storage storage) {
        taskList = new TaskList(storage);
        taskList.addAll(storage.load());
    }

    /**
     * Processes the command and execute it.
     * @param input The user input
     * @return The response. If the program is to be finished, returns null.
     */
    String process(String input) {
        input = input.trim();
        if (input.length() == 0) {
            return "Yo say something...";
        }
        try {
            String[] strs = input.split(" ", 2);
            String command = strs[0];
            String arg = "";
            if (strs.length > 1) { // there are more words
                arg = strs[1].trim();
            }
            switch (command) {
            case TODO_COMMAND:
                return addTodo(arg);
            case DEADLINE_COMMAND:
                return addDeadline(arg);
            case EVENT_COMMAND:
                return addEvent(arg);
            case BYE_COMMAND:
                return null;
            case LIST_COMMAND:
                return list();
            case DONE_COMMAND:
                return done(arg);
            case DELETE_COMMAND:
                return delete(arg);
            case FIND_COMMAND:
                return find(arg);
            default:
                throw new UnknownCommandException();
            }
        } catch (DukeException e) {
            return e.toString();
        }
    }

    /**
     * Finds and lists the tasks containing {@code keyword}
     * @param keyword The keyword to find.
     * @return The response.
     */
    private String find(String keyword) throws DukeException {
        assert keyword != null;
        if (keyword.isEmpty()) {
            throw new EmptyFindArgException();
        }
        List<Task> list = taskList
                .stream()
                .filter(x -> x.getDescription().contains(keyword))
                .collect(Collectors.toList());
        TaskList matchTaskList = new TaskList(null);
        matchTaskList.addAll(list);
        return "Here are the matching tasks in your list:\n" +
                matchTaskList.toString() + "\n";
    }

    /**
     * Marks a task as done.
     * @param s The index of the task to be marked as done, represented as a String
     * @return The response.
     * @throws DukeException If {@code s} cannot be parsed to an Integer, or if the integer as an index is not
     *         within the {@code TaskList} bound.
     */
    private String done(String s) throws DukeException {
        assert s != null;
        try {
            if (s.isEmpty()) {
                throw new TaskNotSpecifiedException();
            }
            int i = Integer.parseInt(s);
            Task task = taskList.get(i);
            task.markAsDone();
            taskList.notifyChange();
            StringBuilder sb = new StringBuilder();
            sb.append("Nice! I've marked this task as done:\n");
            String taskString = "  " + task.toString() + "\n";
            sb.append(taskString);
            return sb.toString();
        } catch (NumberFormatException e) {
            throw new ArgumentNotNumberException();
        } catch (IndexOutOfBoundsException e) {
            throw new TaskDoesNotExistException();
        }
    }

    /**
     * Deletes a task.
     * @param s The index of the task to be deleted, represented as a String.
     * @return The response.
     * @throws DukeException If {@code s} cannot be parsed to an Integer, or if the integer as an index is not
     *         within the {@code TaskList} bound.
     */
    private String delete(String s) throws DukeException {
        assert s != null;
        try {
            if (s.isEmpty()) {
                throw new TaskNotSpecifiedException();
            }
            int i = Integer.parseInt(s);
            Task task = taskList.get(i);
            taskList.remove(i);
            StringBuilder sb = new StringBuilder();
            sb.append("Noted. I've removed this task:\n");
            String taskString = "  " + task.toString() + "\n";
            sb.append(taskString);
            sb.append(getListSizeMsg());
            return sb.toString();
        } catch (NumberFormatException e) {
            throw new ArgumentNotNumberException();
        } catch (IndexOutOfBoundsException e) {
            throw new TaskDoesNotExistException();
        }
    }

    /**
     * Lists all the tasks.
     * @return The list.
     */
    private String list() {
        return "Here are the tasks in your list:\n" +
                taskList.toString() + "\n";
    }

    /**
     * Adds a Todo to the list of tasks.
     * @param desc The description of the todo
     * @return The response.
     * @throws DukeException If {@code desc} is empty.
     */
    private String addTodo(String desc) throws DukeException {
        assert desc != null;
        if (desc.isEmpty()) {
            throw new EmptyTodoDescException();
        }
        Task task = new Todo(desc);
        taskList.add(task);
        return getAddedMsg(task);
    }

    /**
     * Adds a Deadline to the list of tasks.
     * @param desc The description of the Deadline
     * @return The response.
     * @throws DukeException If {@code desc} is empty, or does not contain the second argument separated by
     *         the regex "/by"
     */
    private String addDeadline(String desc) throws DukeException {
        assert desc != null;
        try {
            if (desc.isEmpty()) {
                throw new EmptyDeadlineDescException();
            }
            String[] strs = desc.split(Deadline.REGEX);
            Task task = new Deadline(strs[0], strs[1]);
            taskList.add(task);
            return getAddedMsg(task);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EmptyDeadlineArgException();
        }
    }

    /**
     * Adds an Event to the list of tasks.
     * @param desc The description of the Deadline
     * @return The response.
     * @throws DukeException If {@code desc} is empty, or does not contain the second argument separated by
     *         the regex "/at"
     */
    private String addEvent(String desc) throws DukeException {
        assert desc != null;
        try {
            if (desc.isEmpty()) {
                throw new EmptyEventDescException();
            }
            String[] strs = desc.split(Event.REGEX);
            Task task = new Event(strs[0], strs[1]);
            taskList.add(task);
            return getAddedMsg(task);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EmptyEventArgException();
        }
    }

    private String getAddedMsg(Task task) {
        assert task != null;
        String taskString = "  " + task + "\n";
        return "Got it. I've added this task:\n"
                + taskString
                + getListSizeMsg();
    }

    private String getListSizeMsg() {
        return "Now you have " + taskList.size() + " tasks in the list.\n";
    }
}
