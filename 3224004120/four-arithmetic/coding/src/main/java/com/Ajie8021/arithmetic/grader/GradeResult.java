package com.Ajie8021.arithmetic.grader;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 批改结果，保存答对与答错的题目编号。
 *
 * 编号为题目在文件中的序号（从 1 开始），由 {@link #format()} 按题目要求的格式汇总输出。
 */
public class GradeResult {

    private final List<Integer> correct;
    private final List<Integer> wrong;

    public GradeResult(
            List<Integer> correct,
            List<Integer> wrong) {

        this.correct = correct;
        this.wrong = wrong;
    }

    public List<Integer> getCorrect() {
        return correct;
    }

    public List<Integer> getWrong() {
        return wrong;
    }

    /**
     * 按题目要求的格式生成批改结果文本。
     *
     * 输出形如：
     * Correct: 2 (1, 3)
     * Wrong: 1 (2)
     *
     * 使用 {@link System#lineSeparator()} 换行，保证跨平台一致。
     * @return 批改结果文本
     */
    public String format() {

        return "Correct: "
                + correct.size()
                + " ("
                + join(correct)
                + ")"
                + System.lineSeparator()
                + System.lineSeparator()
                + "Wrong: "
                + wrong.size()
                + " ("
                + join(wrong)
                + ")"
                + System.lineSeparator();
    }

    /**
     * 将题目编号列表拼接为以 ", " 分隔的字符串。
     *
     * 空列表返回空字符串，使 format() 输出 "Correct: 0 ()" 而非报错。
     */
    private String join(List<Integer> numbers) {

        return numbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }
}