package com.Ajie8021.arithmetic.generator;

import com.Ajie8021.arithmetic.exception.GenerationException;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.normalizer.ExpressionNormalizer;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ExerciseGeneratorTest {

    @Test
    void shouldGenerateRequestedNumberOfExercises() {

        ExerciseGenerator generator = new ExerciseGenerator(new Random(12345));
        List<Expression> exercises = generator.generate(100, 20);

        assertEquals(100, exercises.size());
    }

    @Test
    void generatedExercisesShouldBeUnique() {

        ExerciseGenerator generator = new ExerciseGenerator(new Random(12345));
        List<Expression> exercises = generator.generate(100, 20);

        Set<String> normalized = new HashSet<>();

        for (Expression exercise : exercises) {
            assertTrue(
                    normalized.add(
                            ExpressionNormalizer.normalize(exercise)
                    )
            );
        }
    }

    @Test
    void shouldRejectImpossibleGeneration() {

        ExerciseGenerator generator = new ExerciseGenerator(new Random(12345));

        assertThrows(
                GenerationException.class, () -> generator.generate(10000, 1)
        );
    }
}
