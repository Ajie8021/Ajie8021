package com.Ajie8021.plagiarism.format;

import java.util.Locale;

/**
 * 结果格式化。
 *
 * 负责最终输出格式：保留两位小数。
 * 使用 Locale.ROOT 避免不同地区的小数点符号差异。
 */
public class DefaultResultFormatter implements ResultFormatter {

    @Override
    public String format(double similarity) {
        if (Double.isNaN(similarity) || Double.isInfinite(similarity)) {
            throw new IllegalArgumentException("相似度必须为有限值。");
        }
        return String.format(Locale.ROOT, "%.2f", similarity);
    }
}
