package modulo.logic.parser;

import static modulo.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static modulo.commons.core.Messages.MESSAGE_INVALID_INDEX;
import static modulo.logic.commands.CommandTestUtil.VALID_CODE_CS2103_LOWER_CASE;
import static modulo.testutil.Assert.assertThrows;

import static modulo.testutil.TypicalIndexes.INDEX_FIRST_ITEM;
import static modulo.testutil.TypicalIndexes.INDEX_THOUSANDTH_ITEM;
import static modulo.logic.commands.CommandTestUtil.INVALID_MODULE_CODE_CS2000;
import static modulo.logic.parser.CommandParserTestUtil.assertParseFailure;
import static modulo.logic.parser.CommandParserTestUtil.assertParseSuccess;

import modulo.logic.commands.DeleteCommand;
import modulo.logic.predicate.NameContainsKeywordsPredicate;
import org.junit.jupiter.api.Test;


import modulo.logic.parser.exceptions.ParseException;

import java.util.Arrays;


public class DeleteCommandParserTest {

    private DeleteCommandParser deleteCommandParser = new DeleteCommandParser();

    @Test
    public void parse_emptyInput_throwsParseException() {
        assertThrows(ParseException.class, () -> deleteCommandParser.parse(""));
        assertParseFailure(deleteCommandParser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_inputIsZeroInteger_throwsParseException() {
        assertThrows(ParseException.class, () -> deleteCommandParser.parse("0"));
        assertParseFailure(deleteCommandParser, "0",
                MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_inputIsNegativeInteger_throwsParseException() {
        assertThrows(ParseException.class, () -> deleteCommandParser.parse("-1"));
        assertParseFailure(deleteCommandParser, "-1",
                MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_inputIsValidInteger_returnsDeleteCommand() {
        DeleteCommand expectedDeleteCommand = new DeleteCommand(INDEX_FIRST_ITEM, false);
        assertParseSuccess(deleteCommandParser, "1", expectedDeleteCommand);

    }

    @Test
    public void parse_inputIntegerOutOfRange_returnsDeleteCommand() {
        DeleteCommand expectedDeleteCommand = new DeleteCommand(INDEX_THOUSANDTH_ITEM, false);
        assertParseSuccess(deleteCommandParser, "1000", expectedDeleteCommand);

    }

    @Test
    public void parse_inputIsInvalidString_returnsDeleteCommand() {
        DeleteCommand expectedDeleteCommand = new DeleteCommand(new NameContainsKeywordsPredicate(Arrays.asList(INVALID_MODULE_CODE_CS2000)), false);
        assertParseSuccess(deleteCommandParser, INVALID_MODULE_CODE_CS2000, expectedDeleteCommand);
    }

    @Test
    public void parse_inputIsAll_returnsDeleteCommand() {
        DeleteCommand expectedDeleteCommand =
                new DeleteCommand(new NameContainsKeywordsPredicate(Arrays.asList("")), false);
        assertParseSuccess(deleteCommandParser, "all", expectedDeleteCommand);
    }

    @Test
    public void parse_inputIsValidString_returnsDeleteCommand() {
        DeleteCommand expectedDeleteCommand =
                new DeleteCommand(new NameContainsKeywordsPredicate(Arrays.asList(VALID_CODE_CS2103_LOWER_CASE)), false);
        assertParseSuccess(deleteCommandParser, "CS2103", expectedDeleteCommand);
    }

    /*
    DEADLINE DELETION
     */

    @Test
    public void parse_inputDeleteDeadlineEmpty_throwsParseException() {
        assertThrows(ParseException.class, () -> deleteCommandParser.parse("d/"));
        assertParseFailure(deleteCommandParser, " d/",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
    // TODO: Solve bug for reading 'd/' inputs

    @Test
    public void parse_inputDeleteDeadlineAll_returnsDeleteCommand() throws ParseException {
        DeleteCommand expectedDeleteCommand =
                new DeleteCommand(new NameContainsKeywordsPredicate(Arrays.asList("")), true);
        assertParseSuccess(deleteCommandParser, " d/all", expectedDeleteCommand);

    }

    @Test
    public void parse_inputDeleteDeadlineIndex_returnsDeleteCommand() {
        DeleteCommand expectedDeleteCommand =
                new DeleteCommand(INDEX_FIRST_ITEM, true);
        assertParseSuccess(deleteCommandParser, " d/1", expectedDeleteCommand);
    }

    @Test
    public void parse_inputDeleteDeadlineIndexZero_returnsDeleteCommand() {
        assertThrows(ParseException.class, () -> deleteCommandParser.parse(" d/0"));
        assertParseFailure(deleteCommandParser, " d/0",
                MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_inputDeleteDeadlineIndexOutOfRange_returnsDeleteCommand() {
        assertParseSuccess(deleteCommandParser, " d/1000",
                new DeleteCommand(INDEX_THOUSANDTH_ITEM, true));
    }


}

