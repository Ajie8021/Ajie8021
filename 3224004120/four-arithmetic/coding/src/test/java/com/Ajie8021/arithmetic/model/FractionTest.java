package com.Ajie8021.arithmetic.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FractionTest {

    @Test
    void shouldAddFractions() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(1, 3);

        assertEquals(new Fraction(5, 6), a.add(b));
    }

    @Test
    void shouldSubtractFractions() {
        Fraction a = new Fraction(3, 4);
        Fraction b = new Fraction(1, 4);

        assertEquals(new Fraction(1, 2), a.subtract(b));
    }

    @Test
    void shouldMultiplyFractions() {
        Fraction a = new Fraction(2, 3);
        Fraction b = new Fraction(3, 4);

        assertEquals(new Fraction(1, 2), a.multiply(b));
    }

    @Test
    void shouldDivideFractions() {
        Fraction a = new Fraction(1, 2);
        Fraction b = new Fraction(3, 4);

        assertEquals(new Fraction(2, 3), a.divide(b));
    }

    @Test
    void shouldFormatMixedFraction() {
        Fraction fraction = new Fraction(5, 2);

        assertEquals("2'1/2", fraction.toString());
    }
}