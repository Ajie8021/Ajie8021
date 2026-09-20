package com.Ajie8021.arithmetic.grader;

import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class GraderTest {

    @Test
    void shouldGradeAnswersCorrectly() throws Exception {

        Path exerciseFile = Files.createTempFile("Exercises", ".txt");
        Path answerFile = Files.createTempFile("Answers", ".txt");

        Files.write(
                exerciseFile,
                """
                1. 1 + 2 =
                2. 3 × 4 =
                3. 1/2 + 1/4 =
                """.lines().toList(),
                StandardCharsets.UTF_8
        );

        Files.write(
                answerFile,
                """
                3
                12
                3/4
                """.lines().toList(),
                StandardCharsets.UTF_8
        );

        Grader grader = new Grader();
        GradeResult result = grader.grade(exerciseFile, answerFile);

        assertEquals(3, result.getCorrect().size());
        assertEquals(0, result.getWrong().size());

        Files.deleteIfExists(exerciseFile);
        Files.deleteIfExists(answerFile);
    }

    @Test
    void shouldIdentifyWrongAnswers() throws Exception {

        Path exerciseFile = Files.createTempFile("Exercises", ".txt");
        Path answerFile = Files.createTempFile("Answers", ".txt");

        Files.writeString(
                exerciseFile,
                """
                1. 1 + 2 =
                2. 3 × 4 =
                """,
                StandardCharsets.UTF_8
        );

        Files.writeString(
                answerFile,
                """
                3
                10
                """,
                StandardCharsets.UTF_8
        );

        Grader grader = new Grader();
        GradeResult result = grader.grade(exerciseFile, answerFile);

        assertEquals(1, result.getCorrect().size());
        assertEquals(1, result.getWrong().size());
        assertEquals(1, result.getCorrect().get(0));
        assertEquals(2, result.getWrong().get(0));

        Files.deleteIfExists(exerciseFile);
        Files.deleteIfExists(answerFile);
    }
}
