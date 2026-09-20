package com.Ajie8021.arithmetic.parser;

import com.Ajie8021.arithmetic.calculator.ExpressionCalculator;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.model.Fraction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionParserTest {

    private final ExpressionParser parser = new ExpressionParser();
    private final ExpressionCalculator calculator = new ExpressionCalculator();

    @Test
    void shouldParseInteger() {
        Expression expression = parser.parse("5");
        assertEquals(new Fraction(5), calculator.calculate(expression));
    }

    @Test
    void shouldParseFraction() {
        Expression expression = parser.parse("1/2");
        assertEquals(new Fraction(1, 2), calculator.calculate(expression));
    }

    @Test
    void shouldParseMixedFraction() {
        Expression expression = parser.parse("1'1/2");
        assertEquals(new Fraction(3, 2), calculator.calculate(expression));
    }

    @Test
    void shouldRespectOperatorPrecedence() {
        Expression expression = parser.parse("1 + 2 × 3");
        assertEquals(new Fraction(7), calculator.calculate(expression));
    }

    @Test
    void shouldRespectParentheses() {
        Expression expression = parser.parse("(1 + 2) × 3");
        assertEquals(new Fraction(9), calculator.calculate(expression));
    }

    @Test
    void shouldRejectInvalidExpression() {
        assertThrows(RuntimeException.class, () -> parser.parse("1 +"));
    }
}
