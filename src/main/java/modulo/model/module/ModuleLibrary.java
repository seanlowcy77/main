package modulo.model.module;

import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import modulo.MainApp;
import modulo.logic.commands.AddEventCommand;
import modulo.model.Name;
import modulo.model.event.Event;
import modulo.model.event.EventType;
import modulo.model.event.Location;
import modulo.model.event.exceptions.EventNotFoundException;
import modulo.model.module.exceptions.ModuleNotFoundException;

/**
 * Interfacing class between the app and the module JSON files.
 */
public class ModuleLibrary {
    /**
     * Creates a module based on the code and academic year given, with data from the module json files.
     *
     * @param moduleCode   Code of the module.
     * @param academicYear AY for the module.
     * @return New module created from save file.
     * @throws ModuleNotFoundException Fail to read data from the module.
     */
    public static Module createAndReturnModule(ModuleCode moduleCode, AcademicYear academicYear)
            throws ModuleNotFoundException {
        try {
            JsonObject moduleNeeded = getModule(moduleCode);
            Name name = new Name(moduleNeeded.get("title").getAsString());
            String description = moduleNeeded.get("description").getAsString();
            return new Module(moduleCode, name, academicYear, description);
        } catch (IOException e) {
            throw new ModuleNotFoundException();
        }
    }

    /**
     * Returns a list of event types for the given module.
     *
     * @param module Module to search for.
     * @return List of event types the module possesses.
     * @throws ModuleNotFoundException If the module cannot be found, which is likely impossible.
     */
    public static List<EventType> getEventTypesOfModule(Module module) throws ModuleNotFoundException {
        try {
            JsonArray timetable = getTimetable(module);
            List<EventType> eventTypes = new ArrayList<>();
            for (int i = 0; i < timetable.size(); i++) {
                JsonObject lesson = timetable.get(i).getAsJsonObject();
                String lessonType = lesson.get("lessonType").getAsString();
                EventType eventType = EventType.parseEventType(lessonType);
                if (!eventTypes.contains(eventType)) {
                    eventTypes.add(eventType);
                }
            }
            return eventTypes;
        } catch (IOException e) {
            throw new ModuleNotFoundException();
        }
    }

    public static AddEventCommand getAddEventCommandToExecute(Module module, EventType eventType,
                                                              String eventSlot) throws EventNotFoundException {
        try {
            JsonArray timetable = getTimetable(module);
            JsonObject lesson = null;
            for (int i = 0; i < timetable.size(); i++) {
                JsonObject selectLesson = timetable.get(i).getAsJsonObject();
                String lessonType = selectLesson.get("lessonType").getAsString();
                String classNumber = selectLesson.get("classNo").getAsString();
                if (EventType.parseEventType(lessonType) == eventType && areSameEventSlot(classNumber, eventSlot)) {
                    lesson = selectLesson;
                    break;
                }
            }
            if (lesson == null) {
                String sampleClassNumber = null;
                for (int i = 0; i < timetable.size(); i++) {
                    JsonObject selectLesson = timetable.get(i).getAsJsonObject();
                    String lessonType = selectLesson.get("lessonType").getAsString();
                    String classNumber = selectLesson.get("classNo").getAsString();
                    if (EventType.parseEventType(lessonType) == eventType) {
                        sampleClassNumber = classNumber;
                        break;
                    }
                }
                throw new EventNotFoundException(sampleClassNumber == null
                        ? ""
                        : "A sample slot input would be: " + sampleClassNumber);
            }
            JsonArray weeks = lesson.getAsJsonArray("weeks");
            String day = lesson.get("day").getAsString().toUpperCase();
            String startTime = lesson.get("startTime").getAsString();
            String endTime = lesson.get("endTime").getAsString();
            String location = lesson.get("venue").getAsString();
            AcademicYear academicYear = module.getAcademicYear();
            LocalDate eventDay = academicYear.getStartDate()
                    .plusWeeks(weeks.get(0).getAsInt() - 1)
                    .with(TemporalAdjusters.nextOrSame(DayOfWeek.valueOf(day)));
            LocalDateTime eventStart = eventDay.atTime(Integer.parseInt(startTime.substring(0, 2)),
                    Integer.parseInt(startTime.substring(2, 4)));
            LocalDateTime eventEnd = eventDay.atTime(Integer.parseInt(endTime.substring(0, 2)),
                    Integer.parseInt(endTime.substring(2, 4)));
            boolean isRepeated = false;
            TemporalAmount frequency = null;
            LocalDate endRepeatDate = eventDay;
            if (weeks.size() > 1) {
                frequency = Period.ofWeeks(weeks.get(1).getAsInt() - weeks.get(0).getAsInt());
                isRepeated = true;
                endRepeatDate = endRepeatDate.plusWeeks(weeks.get(weeks.size() - 1).getAsInt()
                        - weeks.get(0).getAsInt());
            }
            Event eventToAdd = new Event(new Name(eventType.toString()), eventType,
                    eventStart, eventEnd, module, new Location(location));
            return new AddEventCommand(eventToAdd, isRepeated, endRepeatDate, frequency);
        } catch (IOException e) {
            throw new EventNotFoundException();
        }
    }

    /**
     * Checks if user provided {@code eventSlot} matches the {@code classNumber}.
     *
     * @param classNumber Class number from JSON.
     * @param eventSlot   Event slot
     * @return Boolean representing whether they match.
     */
    private static boolean areSameEventSlot(String classNumber, String eventSlot) {
        String loweredClassNumber = classNumber.trim().toLowerCase();
        if (eventSlot.equals(loweredClassNumber)) {
            return true;
        }
        while (loweredClassNumber.length() > 0 && eventSlot.length() > 0
                && loweredClassNumber.charAt(0) == eventSlot.charAt(0)) {
            loweredClassNumber = loweredClassNumber.substring(1);
            eventSlot = eventSlot.substring(1);
        }
        while (loweredClassNumber.length() > 0 && eventSlot.length() > 0
                && loweredClassNumber.charAt(loweredClassNumber.length() - 1)
                == eventSlot.charAt(eventSlot.length() - 1)) {
            loweredClassNumber = loweredClassNumber.substring(0, loweredClassNumber.length() - 1);
            eventSlot = eventSlot.substring(0, eventSlot.length() - 1);
        }
        if (loweredClassNumber.length() > eventSlot.length()) {
            for (int i = 0; i < loweredClassNumber.length(); i++) {
                if (loweredClassNumber.charAt(i) != '0') {
                    return false;
                }
            }
            return true;
        } else if (eventSlot.length() > loweredClassNumber.length()) {
            for (int i = 0; i < eventSlot.length(); i++) {
                if (eventSlot.charAt(i) != '0') {
                    return false;
                }
            }
            return true;
        } else {
            return eventSlot.equalsIgnoreCase(loweredClassNumber);
        }
    }

    private static JsonObject getModule(ModuleCode moduleCode) throws IOException {
        char firstCharacter = moduleCode.toString().charAt(0);
        InputStream jsonFileStream = MainApp.class.getResourceAsStream("/modules/" + firstCharacter + "Modules.json");
        byte[] content = jsonFileStream.readAllBytes();
        JsonObject jsonObject = JsonParser.parseString(new String(content)).getAsJsonObject();
        return jsonObject.get(moduleCode.toString()).getAsJsonObject();
    }

    private static JsonArray getTimetable(Module module) throws IOException {
        AcademicYear academicYear = module.getAcademicYear();
        JsonObject moduleNeeded = getModule(module.getModuleCode());
        JsonArray semesterData = moduleNeeded.getAsJsonArray("semesterData");
        JsonArray timetable = null;
        for (int i = 0; i < semesterData.size(); i++) {
            JsonObject semester = semesterData.get(i).getAsJsonObject();
            int semesterNumber = semester.get("semester").getAsInt();
            if (semesterNumber == academicYear.getSemester()) {
                timetable = semester.get("timetable").getAsJsonArray();
            }
        }
        if (timetable == null) {
            timetable = ((JsonObject) semesterData.get(0)).get("timetable").getAsJsonArray();
        }
        return timetable;
    }
}
