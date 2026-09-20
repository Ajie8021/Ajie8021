package com.Ajie8021.arithmetic.cli;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandLineParserTest {

    private final CommandLineParser parser =
            new CommandLineParser();

    @Test
    void shouldParseGenerationArguments() {
        CommandLineOptions options =
                parser.parse(new String[]{"-n", "10", "-r", "100"});

        assertEquals(CommandLineOptions.Mode.GENERATE, options.mode());
        assertEquals(10, options.number());
        assertEquals(100, options.range());
    }

    @Test
    void shouldParseGradingArguments() {
        CommandLineOptions options =
                parser.parse(new String[]{"-e", "Exercises.txt", "-a", "Answers.txt"});

        assertEquals(CommandLineOptions.Mode.GRADE, options.mode());
    }

    @Test
    void shouldRejectMissingRange() {
        assertThrows(
                RuntimeException.class,
                () -> parser.parse(new String[]{"-n", "10"})
        );
    }

    @Test
    void shouldRejectMissingNumber() {
        assertThrows(
                RuntimeException.class,
                () -> parser.parse(new String[]{"-r", "10"})
        );
    }

    @Test
    void shouldRejectUnknownArgument() {
        assertThrows(
                RuntimeException.class,
                () -> parser.parse(new String[]{"-x", "10"})
        );
    }

    @Test
    void shouldRejectMixedModes() {
        assertThrows(
                RuntimeException.class,
                () -> parser.parse(
                        new String[]{
                                "-n", "10", "-r", "10",
                                "-e", "Exercises.txt", "-a", "Answers.txt"
                        }
                )
        );
    }
}
