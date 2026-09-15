package com.Ajie8021.plagiarism.similarity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LcsSimilarityCalculatorTest {
    private final LcsSimilarityCalculator calculator = new LcsSimilarityCalculator();

    @Test void identicalTextsShouldReturnOne() {
        assertEquals(1.0, calculator.calculate("abc", "abc"));
    }
    @Test void disjointTextsShouldReturnZero() {
        assertEquals(0.0, calculator.calculate("abc", "xyz"));
    }
    @Test void deletionShouldReduceScore() {
        assertEquals(2.0 / 3.0, calculator.calculate("abc", "ab"), 1e-12);
    }
    @Test void additionShouldNotReduceOriginalCoverage() {
        assertEquals(1.0, calculator.calculate("abc", "abcxyz"));
    }
    @Test void modificationShouldChangeScore() {
        assertEquals(2.0 / 3.0, calculator.calculate("abc", "abx"), 1e-12);
    }
    @Test void reorderedCharactersShouldUseSubsequence() {
        assertEquals(2.0 / 3.0, calculator.calculate("abc", "bac"), 1e-12);
    }
    @Test void chineseTextShouldWork() {
        assertEquals(1.0, calculator.calculate("你好世界", "你好世界"));
    }
    @Test void mixedTextShouldWork() {
        assertEquals(1.0, calculator.calculate("Java论文2026", "Java论文2026"));
    }
    @Test void emptyPlagiarismShouldReturnZeroForNonEmptyOriginal() {
        assertEquals(0.0, calculator.calculate("abc", ""));
    }
    @Test void bothEmptyShouldUseConfiguredBoundaryRule() {
        assertEquals(1.0, calculator.calculate("", ""));
    }
}

