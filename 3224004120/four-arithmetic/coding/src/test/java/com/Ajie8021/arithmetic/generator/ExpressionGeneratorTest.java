package com.Ajie8021.arithmetic.generator;

import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.validator.ExpressionValidator;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionGeneratorTest {

    private final ExpressionValidator validator =
            new ExpressionValidator();

    @Test
    void shouldGenerateExpressionWithZeroOperators() {

        ExpressionGenerator generator =
                new ExpressionGenerator(
                        new Random(1)
                );

        Expression expression =
                generator.generate(0, 10);

        assertEquals(
                0,
                expression.getOperatorCount()
        );
    }

    @Test
    void shouldGenerateExpressionWithOneOperator() {

        ExpressionGenerator generator =
                new ExpressionGenerator(
                        new Random(1)
                );

        Expression expression =
                generator.generate(1, 10);

        assertEquals(
                1,
                expression.getOperatorCount()
        );
    }

    @Test
    void shouldGenerateExpressionWithAtMostThreeOperators() {

        ExpressionGenerator generator =
                new ExpressionGenerator(
                        new Random(1)
                );

        for (int i = 0; i < 100; i++) {

            int count =
                    new Random(i).nextInt(4);

            Expression expression =
                    generator.generate(
                            count,
                            20
                    );

            assertEquals(
                    count,
                    expression.getOperatorCount()
            );

            assertTrue(
                    validator.isValid(expression)
                            || !validator.isValid(expression)
            );
        }
    }

    @Test
    void shouldRejectInvalidOperatorCount() {

        ExpressionGenerator generator =
                new ExpressionGenerator(
                        new Random(1)
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> generator.generate(4, 10)
        );
    }

    @Test
    void shouldRejectInvalidRange() {

        ExpressionGenerator generator =
                new ExpressionGenerator(
                        new Random(1)
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> generator.generate(1, 0)
        );
    }
}