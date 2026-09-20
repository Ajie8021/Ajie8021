package com.Ajie8021.arithmetic.validator;

import com.Ajie8021.arithmetic.model.BinaryExpression;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.model.Fraction;
import com.Ajie8021.arithmetic.model.NumberExpression;
import com.Ajie8021.arithmetic.model.Operator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpressionValidatorTest {

    private final ExpressionValidator validator = new ExpressionValidator();

    @Test
    void subtractionCannotProduceNegativeResult() {
        Expression expression =
                new BinaryExpression(
                        new NumberExpression(new Fraction(1)),
                        Operator.SUBTRACT,
                        new NumberExpression(new Fraction(2))
                );

        assertFalse(validator.isValid(expression));
    }

    @Test
    void divisionResultCanBeProperFraction() {
        Expression expression =
                new BinaryExpression(
                        new NumberExpression(new Fraction(2)),
                        Operator.DIVIDE,
                        new NumberExpression(new Fraction(3))
                );

        assertTrue(validator.isValid(expression));
    }

    @Test
    void divisionResultCannotBeGreaterThanOne() {
        Expression expression =
                new BinaryExpression(
                        new NumberExpression(new Fraction(3)),
                        Operator.DIVIDE,
                        new NumberExpression(new Fraction(2))
                );

        assertFalse(validator.isValid(expression));
    }

    @Test
    void divisionByZeroIsInvalid() {
        Expression expression =
                new BinaryExpression(
                        new NumberExpression(new Fraction(1)),
                        Operator.DIVIDE,
                        new NumberExpression(new Fraction(0))
                );

        assertFalse(validator.isValid(expression));
    }
}
