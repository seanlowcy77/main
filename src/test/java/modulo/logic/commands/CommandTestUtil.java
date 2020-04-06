package modulo.logic.commands;

import static modulo.logic.parser.CliSyntax.PREFIX_ACADEMIC_YEAR;
import static modulo.logic.parser.CliSyntax.PREFIX_MODULE;
import static modulo.logic.parser.CliSyntax.PREFIX_NAME;
import static modulo.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static modulo.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import modulo.logic.commands.exceptions.CommandException;
import modulo.model.Model;
import modulo.model.Modulo;
import modulo.model.module.Module;


/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil<DESC_CS2103> {

    public static final String VALID_CODE_CS2103 = "CS2103";
    public static final String VALID_CODE_CS2103_LOWER_CASE = "cs2103";
    public static final String VALID_CODE_CS2105 = "CS2105";
    public static final String VALID_NAME_CS2103 = "Software Engineering";
    public static final String VALID_NAME_CS2105 = "Introduction to Computer Networking";
    public static final int VALID_ACADEMICYEAR_CS2103 = 2019 / 2020;
    public static final int VALID_SEMESTER_CS2103 = 2;
    public static final int VALID_ACADEMICYEAR_CS2105 = 2020 / 2021;
    public static final int VALID_SEMESTER_CS2105 = 1;
    public static final String VALID_DESCRIPTION_CS2103 = "This module introduces the necessary conceptual and "
            + "analytical tools for systematic and rigorous development of software systems";
    public static final String VALID_DESCRIPTION_CS2105 = "This module aims to provide a broad introduction to "
            + "computer networks and network application programming.";

    public static final String CODE_DESC_CS2103 = " " + PREFIX_MODULE + VALID_CODE_CS2103;
    public static final String CODE_DESC_CS2105 = " " + PREFIX_MODULE + VALID_CODE_CS2105;
    public static final String NAME_DESC_CS2103 = " " + PREFIX_NAME + VALID_NAME_CS2103;
    public static final String NAME_DESC_CS2105 = " " + PREFIX_NAME + VALID_NAME_CS2105;
    public static final String ACEDEMICYEAR_DESC_CS2103 = " " + PREFIX_ACADEMIC_YEAR + VALID_ACADEMICYEAR_CS2103;
    public static final String ACEDEMICYEAR_DESC_CS2105 = " " + PREFIX_ACADEMIC_YEAR + VALID_ACADEMICYEAR_CS2105;
    public static final String SEMESTER_DESC_CS2103 = " " + PREFIX_SEMESTER + VALID_SEMESTER_CS2103;
    public static final String SEMESTER_DESC_CS2105 = " " + PREFIX_SEMESTER + VALID_SEMESTER_CS2105;

    public static final String INVALID_MODULE_CODE_CS2000 = "cs2000";
    public static final String INVALID_CODE_DESC = " " + PREFIX_MODULE + "CS210&"; // '&' not allowed in module code.
    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME
            + "So###$$$$ftware"; // '#' and '$' not allowed in module code.
    // Modulo does not support Academic Year 2021 / 2022 ;
    public static final String INVALID_ACEDEMICYEAR_DESC = " " + PREFIX_ACADEMIC_YEAR + 2021 / 2022;
    // invalid semester 3
    public static final String INVALID_SEMESTER_DESC = " " + PREFIX_SEMESTER + 3;

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        Modulo expectedModulo = new Modulo(actualModel.getModulo());
        List<Module> expectedFilteredList = new ArrayList<>(actualModel.getFilteredModuleList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedModulo, actualModel.getModulo());
        assertEquals(expectedFilteredList, actualModel.getFilteredModuleList());
    }

    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.

     public static void showModuleAtIndex(Model model, Index targetIndex) {
     assertTrue(targetIndex.getZeroBased() < model.getFilteredModuleList().size());

     Module module = model.getFilteredModuleList().get(targetIndex.getZeroBased());
     final String[] splitName = module.getName().fullName.split("\\s+");
     model.updateFilteredModuleList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));
     assertEquals(1, model.getFilteredModuleList().size());
     }

     /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s address book.

     public static void showEventAtIndex(Model model, Index targetIndex) {
     assertTrue(targetIndex.getZeroBased() < model.getFilteredEventList().size());

     Event event = model.getFilteredEventList().get(targetIndex.getZeroBased());
     final String[] splitName = event.getName().fullName.split("\\s+");
     model.updateFilteredModuleList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));
     assertEquals(1, model.getFilteredModuleList().size());
     } */


}
