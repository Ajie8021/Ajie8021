package com.Ajie8021.arithmetic.grader;

import com.Ajie8021.arithmetic.exception.InvalidExpressionException;
import com.Ajie8021.arithmetic.io.FileManager;
import com.Ajie8021.arithmetic.calculator.ExpressionCalculator;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.model.Fraction;
import com.Ajie8021.arithmetic.parser.ExpressionParser;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 批改器，负责比对习题与答案并给出批改结果。
 *
 * 流程：
 * 1. 读取题目文件与答案文件，忽略空行；
 * 2. 逐题解析表达式并计算标准答案，与文件中答案比对；
 * 3. 记录答对与答错的题号，封装为 {@link GradeResult}。
 *
 * 题号以文件中的实际顺序为准（从 1 开始），空行不占编号。
 */
public class Grader {

    private final FileManager fileManager;
    private final ExpressionParser parser;
    private final ExpressionCalculator calculator;

    public Grader() {
        this.fileManager = new FileManager();
        this.parser = new ExpressionParser();
        this.calculator = new ExpressionCalculator();
    }

    /**
     * 批改习题文件与答案文件。
     *
     * @param exerciseFile 习题文件路径
     * @param answerFile   答案文件路径
     * @return 批改结果
     * @throws InvalidExpressionException 题目与答案数量不一致，
     *                                    或某行格式非法时抛出
     */
    public GradeResult grade(
            Path exerciseFile,
            Path answerFile) {

        // 忽略空行，避免文件末尾换行或分隔空行影响题号对应
        List<String> exerciseLines = removeBlankLines(fileManager.readLines(exerciseFile));
        List<String> answerLines = removeBlankLines(fileManager.readLines(answerFile));

        // 题目数与答案数必须一一对应，否则无法批改
        if (exerciseLines.size() != answerLines.size()) {
            throw new InvalidExpressionException(
                    "习题数量与答案数量不匹配。"
            );
        }

        List<Integer> correct = new ArrayList<>();
        List<Integer> wrong = new ArrayList<>();

        for (int i = 0; i < exerciseLines.size(); i++) {
            String exerciseLine = exerciseLines.get(i);
            String answerLine = answerLines.get(i);

            Fraction expected = calculateExercise(exerciseLine);
            Fraction actual = parseAnswer(answerLine);

            // 题号从 1 开始，便于与文件中的显示编号一致
            if (expected.equals(actual)) {
                correct.add(i + 1);
            } else {
                wrong.add(i + 1);
            }
        }

        return new GradeResult(correct, wrong);
    }

    /**
     * 从习题行中提取表达式并计算结果。
     *
     * 习题行格式形如 "1. 1/2 + 1/3 ="，通过定位 '.' 与最后一个 '='截取二者之间的表达式文本（字符串仅用于输入输出）。
     *
     * @param line 习题行
     * @return 表达式的计算结果
     * @throws InvalidExpressionException 行格式非法时抛出
     */
    private Fraction calculateExercise(String line) {

        int dotIndex = line.indexOf('.');
        int equalIndex = line.lastIndexOf('=');

        // 必须同时存在 '.' 和 '='，且 '.' 在 '=' 之前
        if (dotIndex < 0 || equalIndex < 0 || dotIndex >= equalIndex) {
            throw new InvalidExpressionException(
                    "无效习题行: " + line
            );
        }

        String expressionText = line.substring(dotIndex + 1, equalIndex).trim();

        Expression expression = parser.parse(expressionText);

        return calculator.calculate(expression);
    }

    /**
     * 解析答案行中的分数。
     *
     * @param line 答案行
     * @return 答案对应的分数
     * @throws InvalidExpressionException 答案格式非法时抛出
     */
    private Fraction parseAnswer(String line) {

        String answer = line.trim();

        /*
         * 同时支持：3/5、1. 3/5
         */
        if (answer.matches("^\\d+\\s*\\.\\s*.*$")) {
            answer = answer.replaceFirst("^\\d+\\s*\\.\\s*", "").trim();
        }

        try {
            return Fraction.parse(answer);
        } catch (IllegalArgumentException e) {
            throw new InvalidExpressionException(
                    "无效答案: " + line
            );
        }
    }

    /**
     * 去除空行并修剪每行首尾空白。
     *
     * 使批改不受文件末尾空行、行首缩进等格式差异影响，保证题号连续且与习题一一对应。
     *
     * @param lines 原始行列表
     * @return 去除空白行后的行列表
     */
    private List<String> removeBlankLines(
            List<String> lines) {

        return lines.stream().map(String::trim)
                .filter(line -> !line.isEmpty()).toList();
    }
}