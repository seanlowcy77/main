package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.event.Event;
import seedu.address.model.module.AcademicYear;
import seedu.address.model.module.Module;
import seedu.address.model.module.ModuleCode;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final Calendar calendar;
    private final UserPrefs userPrefs;
    private final FilteredList<Event> filteredEvents;
    private final FilteredList<Module> filteredModules;
    private FilteredList<? extends Displayable> focusedFilteredDisplayables;
    private Displayable focusedDisplayable;
    private DisplayableType currentDisplayableType;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyCalendar calendar, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(calendar, userPrefs);

        logger.fine("Initializing with calendar: " + calendar + " and user prefs " + userPrefs);
        this.calendar = new Calendar(calendar);
        this.userPrefs = new UserPrefs(userPrefs);


        filteredEvents = new FilteredList<>(this.calendar.getEventList());
        filteredModules = new FilteredList<>(this.calendar.getModuleList());
        setFilteredFocusedList(DisplayableType.EVENT);
    }

    public ModelManager() {
        this(new Calendar(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getCalendarFilePath() {
        return userPrefs.getCalendarFilePath();
    }

    @Override
    public void setCalendarFilePath(Path calendarFilePath) {
        requireNonNull(calendarFilePath);
        userPrefs.setCalendarFilePath(calendarFilePath);
    }

    //=========== Modulo ================================================================================

    public void setView
    @Override
    public void setCalendar(ReadOnlyCalendar calendar) {
        this.calendar.resetData(calendar);
    }

    @Override
    public ReadOnlyCalendar getCalendar() {
        return calendar;
    }

    @Override
    public boolean hasEvent(Event event) {
        requireNonNull(event);
        return calendar.hasEvent(event);
    }

    @Override
    public void deleteEvent(Event target) {
        calendar.removeEvent(target);
    }

    @Override
    public void addEvent(Event calendarItem) {
        calendar.addEvent(calendarItem);
        updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
    }


    @Override
    public void setEvent(Event target, Event editedCalendarItem) {
        requireAllNonNull(target, editedCalendarItem);
        calendar.setEvent(target, editedCalendarItem);
    }

    @Override
    public Event findEvent(Event toFind) {
        requireNonNull(toFind);
        List<Event> eventList = calendar.getEventList();
        for (Event event : eventList) {
            if (event.isSameEvent(toFind)) {
                return event;
            }
        }
        return null;
    }

    @Override
    public List<Event> findAllEvents(Event toFind) {
        requireNonNull(toFind);
        List<Event> eventList = calendar.getEventList();
        List<Event> result = new ArrayList<>();
        System.out.println("event list length: " + eventList.size());
        for (Event event : eventList) {
            if (event.isSameCategoryOfEvents(toFind)) {
                result.add(event);
            }
        }
        return result;
    }

    @Override
    public Event getEvent(Event toFind) {
        requireNonNull(toFind);
        return calendar.getEvent(toFind);
    }

    @Override
    public boolean hasModule(ModuleCode moduleCode, AcademicYear academicYear) {
        requireAllNonNull(moduleCode, academicYear);
        return calendar.hasModule(moduleCode, academicYear);
    }

    @Override
    public void deleteModule(Module target) {
        calendar.removeModule(target);
    }

    @Override
    public void addModule(ModuleCode moduleCode, AcademicYear academicYear) {
        calendar.addModule(moduleCode, academicYear);
        Module addedModule = calendar.getModule(moduleCode, academicYear).get();
        updateFilteredModuleList(PREDICATE_SHOW_ALL_MODULES);
    }

    @Override
    public Optional<Module> getModule(ModuleCode moduleCode, AcademicYear academicYear) {
        return calendar.getModule(moduleCode, academicYear);
    }


    @Override
    public void setModule(Module target, Module editedModule) {
        requireAllNonNull(target, editedModule);

        calendar.setModule(target, editedModule);
    }

    @Override
    public Module findModule(Module toFind) {
        requireNonNull(toFind);
        List<Module> moduleList = calendar.getModuleList();
        for (Module module : moduleList) {
            if (module.matchModule(toFind)) {
                return module;
            }
        }
        return null;
    }

    @Override
    public DisplayableType getCurrentDisplayableType() {
        return currentDisplayableType;
    }

    //=========== Setting Focus =============================================================

    @Override
    public void setFocusedDisplayable(Displayable displayable) {
        boolean hasDisplayable = false;
        if (displayable instanceof Module) {
            hasDisplayable =
                    hasModule(((Module) displayable).getModuleCode(), ((Module) displayable).getAcademicYear());
        } else if (displayable instanceof Event) {
            hasDisplayable = hasEvent((Event) displayable);
        }
        if (!hasDisplayable) {
            return;
        }
        focusedDisplayable = displayable;
    }

    @Override
    public void unsetFocusedDisplayable() {
        focusedDisplayable = null;
    }


    @Override
    public void setFilteredFocusedList(DisplayableType displayableType) {
        if (displayableType == DisplayableType.EVENT) {
            updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
            focusedFilteredDisplayables = filteredEvents;
        } else if (displayableType == DisplayableType.MODULE) {
            updateFilteredModuleList(PREDICATE_SHOW_ALL_MODULES);
            focusedFilteredDisplayables = filteredModules;
        }
    }


    //=========== Filtered Person List Accessors =============================================================

    @Override
    public void updateFilteredDisplayableList(Predicate<Displayable> predicate) {
        requireNonNull(predicate);
        focusedFilteredDisplayables.setPredicate(predicate);
    }

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of {@code
     * versionedAddressBook}
     */
    @Override
    public ObservableList<Event> getFilteredEventList() {
        return filteredEvents;
    }

    @Override
    public void updateFilteredEventList(Predicate<Event> predicate) {
        requireNonNull(predicate);
        filteredEvents.setPredicate(predicate);
        currentDisplayableType = DisplayableType.EVENT;
    }

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of {@code
     * versionedAddressBook}
     */
    @Override
    public ObservableList<Module> getFilteredModuleList() {
        return filteredModules;
    }

    @Override
    public void updateFilteredModuleList(Predicate<Module> predicate) {
        requireNonNull(predicate);
        filteredModules.setPredicate(predicate);
        currentDisplayableType = DisplayableType.MODULE;
    }

    @Override
    public ObservableList<? extends Displayable> getFilteredFocusedList() {
        return focusedFilteredDisplayables;
    }


    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return calendar.equals(other.calendar)
                && userPrefs.equals(other.userPrefs)
                && filteredEvents.equals(other.filteredEvents)
                && filteredModules.equals(other.filteredModules);
    }

    @Override
    public String checkCurrentCalendar() {
        List<Module> modules = calendar.getModuleList();
        List<Event> events = calendar.getEventList();
        StringBuilder sb = new StringBuilder();
        sb.append("Modules: ");
        sb.append("\n");
        for (Module eachModule : modules) {
            sb.append(eachModule.toDebugString());
            sb.append("\n");
        }

        sb.append("Events: ");
        sb.append("\n");
        for (Event eachEvent : events) {
            sb.append(eachEvent.toDebugString());
            sb.append("\n");
        }
        return sb.toString();
    }

}
