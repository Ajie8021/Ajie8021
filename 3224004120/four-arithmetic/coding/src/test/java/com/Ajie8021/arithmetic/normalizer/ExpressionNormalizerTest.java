package com.Ajie8021.arithmetic.normalizer;

import com.Ajie8021.arithmetic.model.BinaryExpression;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.model.Fraction;
import com.Ajie8021.arithmetic.model.NumberExpression;
import com.Ajie8021.arithmetic.model.Operator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionNormalizerTest {

    @Test
    void additionOperandsCanBeSwapped() {

        Expression first = expression(1, Operator.ADD, 2);
        Expression second = expression(2, Operator.ADD, 1);

        assertTrue(ExpressionNormalizer.isEquivalent(first, second));
    }

    @Test
    void multiplicationOperandsCanBeSwapped() {

        Expression first = expression(3, Operator.MULTIPLY, 4);

        Expression second = expression(4, Operator.MULTIPLY, 3);

        assertTrue(ExpressionNormalizer.isEquivalent(first, second));
    }

    @Test
    void differentTreeStructureShouldNotBeRecognized() {

        // 3 + 2 + 1 即 (3 + 2) + 1
        Expression first =
                expression(
                        expression(3, Operator.ADD, 2),
                        Operator.ADD, 1
                );

        // 1 + 2 + 3
        Expression second =
                expression(
                        expression(1, Operator.ADD, 2),
                        Operator.ADD, 3
                );

        assertFalse(ExpressionNormalizer.isEquivalent(first, second));
    }

    private Expression expression(int left, Operator operator, int right) {

        return new BinaryExpression(
                new NumberExpression(new Fraction(left)),
                operator,
                new NumberExpression(new Fraction(right))
        );
    }

    private Expression expression(Expression left, Operator operator, int right) {

        return new BinaryExpression(
                left, operator,
                new NumberExpression(new Fraction(right))
        );
    }
}
