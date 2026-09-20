package com.Ajie8021.arithmetic.calculator;

import com.Ajie8021.arithmetic.model.BinaryExpression;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.model.Fraction;
import com.Ajie8021.arithmetic.model.NumberExpression;
import com.Ajie8021.arithmetic.model.Operator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExpressionCalculatorTest {

    private final ExpressionCalculator calculator =
            new ExpressionCalculator();

    @Test
    void shouldCalculateAddition() {
        Expression expression =
                new BinaryExpression(
                        new NumberExpression(new Fraction(1)),
                        Operator.ADD,
                        new NumberExpression(new Fraction(2))
                );

        assertEquals(
                new Fraction(3),
                calculator.calculate(expression)
        );
    }

    @Test
    void shouldCalculateMixedExpression() {
        Expression expression =
                new BinaryExpression(
                        new NumberExpression(new Fraction(1)),
                        Operator.ADD,
                        new BinaryExpression(
                                new NumberExpression(new Fraction(2)),
                                Operator.MULTIPLY,
                                new NumberExpression(new Fraction(3))
                        )
                );

        assertEquals(
                new Fraction(7),
                calculator.calculate(expression)
        );
    }
}
