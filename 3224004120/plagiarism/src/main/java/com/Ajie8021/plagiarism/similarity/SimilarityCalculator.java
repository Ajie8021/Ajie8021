package com.Ajie8021.plagiarism.similarity;

public interface SimilarityCalculator {
    double calculate(String originalText, String plagiarismText);
}
