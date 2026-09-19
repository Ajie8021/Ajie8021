package com.Ajie8021.plagiarism.similarity;

/**
 * 基于字符级 LCS 的算法实现。
 *
 * 当前选用的公式为 LCS(original, plagiarism) / original.length()。
 * 便于后续根据样例验证结果调整。
 */
public class LcsSimilarityCalculator implements SimilarityCalculator {

    @Override
    public double calculate(String originalText, String plagiarismText) {
        if (originalText == null || plagiarismText == null) {
            throw new IllegalArgumentException("文本不能为 null。");
        }

        int originalLength = originalText.length();
        if (originalLength == 0) {
            return handleEmptyOriginal(plagiarismText);
        }

        int lcsLength = lcsLength(originalText, plagiarismText);
        return (double) lcsLength / originalLength;
    }

    /**
     * 空原文的边界规则单独隔离，便于在需求确认后修改，
     * 而不影响 LCS 主体实现。
     */
    private double handleEmptyOriginal(String plagiarismText) {
        return plagiarismText.isEmpty() ? 1.0 : 0.0;
    }

    /**
     * 计算两个文本的 LCS 长度。
     * 将较短文本放在 DP 列方向，以减少内存占用。
     */
    private int lcsLength(String a, String b) {
        if (a.length() < b.length()) {
            return lcsWithColumns(a, b);
        }
        return lcsWithColumns(b, a);
    }

    /**
     * 使用滚动数组（两行）计算 LCS 长度。
     * 返回值与哪个输入作为行/列无关。
     */
    private int lcsWithColumns(String shorter, String longer) {
        int m = shorter.length();
        int n = longer.length();
        int[] previous = new int[m + 1];
        int[] current = new int[m + 1];

        for (int i = 1; i <= n; i++) {
            char c = longer.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                if (c == shorter.charAt(j - 1)) {
                    current[j] = previous[j - 1] + 1;
                } else {
                    current[j] = Math.max(previous[j], current[j - 1]);
                }
            }

            // 交换 previous 与 current，复用数组，避免重复创建。
            int[] temp = previous;
            previous = current;
            current = temp;
        }
        return previous[m];
    }
}
