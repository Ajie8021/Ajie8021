package com.Ajie8021.plagiarism.format;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultResultFormatterTest {
    private final DefaultResultFormatter formatter = new DefaultResultFormatter();

    @Test void zeroShouldHaveTwoDecimals() {
        assertEquals("0.00", formatter.format(0.0));
    }
    @Test void oneShouldHaveTwoDecimals() {
        assertEquals("1.00", formatter.format(1.0));
    }
    @Test void valueShouldBeRoundedToTwoDecimals() {
        assertEquals("0.83", formatter.format(0.8333333));
    }
    @Test void smallValueShouldBeRoundedCorrectly() {
        assertEquals("0.01", formatter.format(0.005));
    }
}
