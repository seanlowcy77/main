package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.event.Event;
import seedu.address.model.event.UniqueEventList;
import seedu.address.model.module.AcademicYear;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleCode;
import seedu.address.model.module.UniqueModuleList;

/**
 * Wraps all data at the calendar level Duplicates are not allowed (by .isSameModule comparison)
 */
public class Calendar implements ReadOnlyCalendar {

    private final UniqueEventList events;
    private final UniqueModuleList modules;

    /*
     *  The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     *  between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     *  Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *  among constructors.
     */
    {
        events = new UniqueEventList();
        modules = new UniqueModuleList();
    }

    public Calendar() {
    }

    /**
     * Creates a Calendar using the items in the {@code toBeCopied}
     */
    public Calendar(ReadOnlyCalendar toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code modules}. {@code modules} must not contain duplicate
     * modules.
     */
    public void setModules(List<Module> modules) {
        this.modules.setModules(modules);
    }

    /**
     * Replaces the contents of the person list with {@code persons}. {@code persons} must not contain duplicate
     * persons.
     */
    public void setEvents(List<Event> events) {
        this.events.setEvents(events);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyCalendar newData) {
        requireNonNull(newData);

        setEvents(newData.getEventList());
        setModules(newData.getModuleList());
    }

    // module-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasModule(ModuleCode moduleCode, AcademicYear academicYear) {
        requireAllNonNull(moduleCode, academicYear);
        return modules.contains(moduleCode, academicYear);
    }

    public Module addModule(ModuleCode moduleCode, AcademicYear academicYear) {
        return modules.getModule(moduleCode, academicYear);
    }

    public Module getModule(ModuleCode moduleCode, AcademicYear academicYear) {
        return modules.getModule(moduleCode, academicYear);
    }

    /**
     * Adds a person to the address book. The person must not already exist in the address book.
     */
    public void addModuleFromStorage(Module module) {
        Module actualModule = modules.getModule(module.getModuleCode(), module.getAcademicYear());
        for (Event event : module.getEvents()) {
            Event actualEvent = new Event(event.getName(), event.getEventType(), event.getEventStart(),
                    event.getEventEnd(), actualModule, event.getDeadlines());
            actualModule.addEvent(actualEvent);
            addEvent(actualEvent);
        }
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}. {@code target} must exist in the
     * address book. The person identity of {@code editedPerson} must not be the same as another existing person in the
     * address book.
     */
    public void setModule(Module target, Module editedModule) {
        requireNonNull(editedModule);

        modules.setModule(target, editedModule);
    }

    /**
     * Removes {@code key} from the {@code Calendar}. {@code key} must exist in the calendar.
     */
    public void removeModule(Module key) {
        modules.remove(key);
    }


    // event-level operations

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}. {@code target} must exist in the
     * address book. The person identity of {@code editedPerson} must not be the same as another existing person in the
     * address book.
     */
    public void setEvent(Event target, Event editedEvent) {
        requireNonNull(editedEvent);
        events.setEvent(target, editedEvent);
    }

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        // System.out.println("Calendar item !!!!!!!!!!!!!!!"+ calendarItem);
        return events.contains(event);
    }

    /**
     * Adds a person to the address book. The person must not already exist in the address book.
     */
    public void addEvent(Event event) {
        events.add(event);
    }


    /**
     * Removes {@code key} from this {@code AddressBook}. {@code key} must exist in the address book.
     */
    public void removeEvent(Event key) {
        events.remove(key);
    }

    public Event getEvent(Event event) {
        return events.getEvent(event);
    }

    //// util methods

    @Override
    public String toString() {
        return events.asUnmodifiableObservableList().size() + " events";
        // TODO: refine later
    }

    @Override
    public ObservableList<Event> getEventList() {
        return events.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Module> getModuleList() {
        return modules.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Calendar // instanceof handles nulls
                && events.equals(((Calendar) other).events))
                && modules.equals(((Calendar) other).modules);
    }

    @Override
    public int hashCode() {
        return events.hashCode();
    }
}
