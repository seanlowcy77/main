package seedu.address.model.deadline;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDateTime;

import seedu.address.model.Name;
import seedu.address.model.event.Event;


/**
 * Represents a deadline in Modulo. Guarantees: details are present and not null, field values are validated, fields are
 * immutable.
 */
public class Deadline {

    // Identity fields
    private Name name;
    private LocalDateTime dueTime;

    // Data fields
    private boolean isCompleted;

    public Deadline(Name name, Event parentEvent) {
        requireAllNonNull(name, parentEvent);
        this.name = name;
        this.dueTime = parentEvent.getEventStart();
        this.isCompleted = false;
    }

    public Deadline(Name name, LocalDateTime dueTime, boolean isCompleted) {
        requireAllNonNull(name, dueTime);
        this.name = name;
        this.dueTime = dueTime;
        this.isCompleted = isCompleted;
    }

    public Name getName() {
        return name;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public LocalDateTime getDueTime() {
        return dueTime;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    /**
     * Returns a string for debugging purposes.
     *
     * @return String for debugging purposes.
     */
    public String toDebugString() {
        return " | " + "DEADLINE" + " | " + name + " || ";
    }

    /**
     * Returns true if both deadlines have the same identity.
     *
     * @param otherDeadline The deadline to compare to.
     * @return boolean value denoting whether the two deadlines are the same deadline.
     */
    public boolean isSameDeadline(Deadline otherDeadline) {
        if (otherDeadline == this) {
            return true;
        }
        return otherDeadline.getName().equals(getName());
    }

    /**
     * Returns true if both deadlines have the same identity and data fields. This defines a stronger notion of equality
     * between two deadlines.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Deadline)) {
            return false;
        }

        Deadline otherDeadline = (Deadline) other;
        return otherDeadline.getName().equals(getName())
                && otherDeadline.isCompleted() == isCompleted();
    }

    @Override
    public String toString() {
        return this.name.toString();
    }
}
