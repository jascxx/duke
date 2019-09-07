package seedu.duke;

import seedu.duke.task.Task;

import java.util.ArrayList;

/**
 * Represents a list of tasks. Every time the data is changed, the new data will be saved.
 */
public class TaskList extends ArrayList<Task> {
    private Storage storage;

    /**
     * Constructor.
     * @param storage To save the data everytime a change happens. {@code null} if not associated
     *                to any {@code storage}
     */
    public TaskList(Storage storage) {
        this.storage = storage;
    }

    @Override
    public boolean add(Task task) {
        boolean added = super.add(task);
        if (storage != null) {
            storage.save(this);
        }
        return added;
    }

    @Override
    public Task remove(int index) {
        Task removed = super.remove(index - 1);
        if (storage != null) {
            storage.save(this);
        }
        return removed;
    }

    @Override
    public Task get(int index) {
        return super.get(index - 1);
    }

    /**
     * Notifies the change of the tasks such that the tasks can be saved.
     */
    public void notifyChange() {
        storage.save(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= size(); ++i) {
            String info = i + ". " + get(i) + "\n";
            sb.append(info);
        }
        return sb.toString();
    }
}
