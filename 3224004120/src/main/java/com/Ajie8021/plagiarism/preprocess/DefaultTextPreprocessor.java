package com.Ajie8021.plagiarism.preprocess;

import java.util.regex.Pattern;

/**
 * 文本预处理实现。
 *
 * HTML 处理仅限当前样例分析所支持的规则：
 * 移除标签、注释、script/style 块，并解码常见 HTML 实体。
 * 不删除普通标点、不改变字母大小写，因为这些问题尚未被题目规则确认，
 * 规范要求不得擅自假设。
 */
public class DefaultTextPreprocessor implements TextPreprocessor {

    private static final Pattern COMMENT =
            Pattern.compile("(?is)<!--.*?-->");
    private static final Pattern SCRIPT =
            Pattern.compile("(?is)<script\\b[^>]*>.*?</script\\s*>");
    private static final Pattern STYLE =
            Pattern.compile("(?is)<style\\b[^>]*>.*?</style\\s*>");
    private static final Pattern TAG =
            Pattern.compile("(?is)<[^>]+>");

    @Override
    public String preprocess(String text) {
        if (text == null) {
            throw new IllegalArgumentException("文本不能为 null。");
        }

        // 先移除注释、脚本、样式和标签，再解码实体，避免实体内容干扰标签匹配。
        String result = text;
        result = COMMENT.matcher(result).replaceAll(" ");
        result = SCRIPT.matcher(result).replaceAll(" ");
        result = STYLE.matcher(result).replaceAll(" ");
        result = TAG.matcher(result).replaceAll(" ");
        result = decodeCommonEntities(result);
        return normalizeWhitespace(result);
    }

    /**
     * 将连续空白字符合并为单个空格，并去除首尾空白。
     */
    private String normalizeWhitespace(String text) {
        return text.replaceAll("\\s+", " ").trim();
    }

    /**
     * 解码常见 HTML 实体。
     * 实体表有限，仅覆盖当前样例中出现的类型。
     */
    private String decodeCommonEntities(String text) {
        return text
                .replace("&nbsp;", " ")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&apos;", "'");
    }
}
