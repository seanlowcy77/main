package modulo.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import modulo.logic.parser.AcademicYearParser;
import modulo.logic.parser.exceptions.ParseException;

public class AcademicYearParserTest {
    @Test
    public void parse_semesterOneStartDate_success() {
        try {
            assertEquals(LocalDate.of(2017, 8, 14).toString(),
                    AcademicYearParser.parseAcademicYear(2017, 1)[0].toString());
            assertEquals(LocalDate.of(2018, 8, 13).toString(),
                    AcademicYearParser.parseAcademicYear(2018, 1)[0].toString());
            assertEquals(LocalDate.of(2019, 8, 12).toString(),
                    AcademicYearParser.parseAcademicYear(2019, 1)[0].toString());
            assertEquals(LocalDate.of(2020, 8, 10).toString(),
                    AcademicYearParser.parseAcademicYear(2020, 1)[0].toString());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_semesterOneEndDate_success() {
        try {
            assertEquals(LocalDate.of(2017, 12, 9).toString(),
                    AcademicYearParser.parseAcademicYear(2017, 1)[1].toString());
            assertEquals(LocalDate.of(2018, 12, 8).toString(),
                    AcademicYearParser.parseAcademicYear(2018, 1)[1].toString());
            assertEquals(LocalDate.of(2019, 12, 7).toString(),
                    AcademicYearParser.parseAcademicYear(2019, 1)[1].toString());
            assertEquals(LocalDate.of(2020, 12, 5).toString(),
                    AcademicYearParser.parseAcademicYear(2020, 1)[1].toString());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_semesterTwoStartDate_success() {
        try {
            assertEquals(LocalDate.of(2018, 1, 15).toString(),
                    AcademicYearParser.parseAcademicYear(2017, 2)[0].toString());
            assertEquals(LocalDate.of(2019, 1, 14).toString(),
                    AcademicYearParser.parseAcademicYear(2018, 2)[0].toString());
            assertEquals(LocalDate.of(2020, 1, 13).toString(),
                    AcademicYearParser.parseAcademicYear(2019, 2)[0].toString());
            assertEquals(LocalDate.of(2021, 1, 11).toString(),
                    AcademicYearParser.parseAcademicYear(2020, 2)[0].toString());
        } catch (ParseException e) {
            fail();
        }
    }

    @Test
    public void parse_semesterTwoEndDate_success() {
        try {
            assertEquals(LocalDate.of(2018, 5, 12).toString(),
                    AcademicYearParser.parseAcademicYear(2017, 2)[1].toString());
            assertEquals(LocalDate.of(2019, 5, 11).toString(),
                    AcademicYearParser.parseAcademicYear(2018, 2)[1].toString());
            assertEquals(LocalDate.of(2020, 5, 9).toString(),
                    AcademicYearParser.parseAcademicYear(2019, 2)[1].toString());
            assertEquals(LocalDate.of(2021, 5, 8).toString(),
                    AcademicYearParser.parseAcademicYear(2020, 2)[1].toString());
        } catch (ParseException e) {
            fail();
        }
    }
}
