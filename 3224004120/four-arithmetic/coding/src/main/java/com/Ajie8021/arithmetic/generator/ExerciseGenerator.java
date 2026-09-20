package com.Ajie8021.arithmetic.generator;

import com.Ajie8021.arithmetic.exception.GenerationException;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.normalizer.ExpressionNormalizer;
import com.Ajie8021.arithmetic.validator.ExpressionValidator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 习题集生成器。
 *
 * 在 {@link ExpressionGenerator} 生成单个表达式的基础上，
 * 负责批量生成合法且互不重复的习题列表：
 * 1. 通过 {@link ExpressionValidator} 过滤掉违反约束的表达式；
 * 2. 通过 {@link ExpressionNormalizer} 的规范形式去重。
 *
 * 使用注入的 {@link Random} 保证测试结果可复现。
 */
public class ExerciseGenerator {

    private final ExpressionGenerator expressionGenerator;
    private final ExpressionValidator validator;
    private final Random random;

    public ExerciseGenerator() {
        this(new Random());
    }

    public ExerciseGenerator(Random random) {
        this.random = random;
        this.expressionGenerator = new ExpressionGenerator(random);
        this.validator = new ExpressionValidator();
    }

    /**
     * 批量生成指定数量的合法且不重复的习题。
     *
     * @param count 需要生成的习题数量
     * @param range 操作数的数值范围上限
     * @return 习题表达式列表
     * @throws GenerationException 当数量或范围非法，或无法生成足够多的
     *                             不重复习题时抛出
     */
    public List<Expression> generate(
            int count,
            int range) {

        if (count <= 0) {
            throw new GenerationException(
                    "习题数量必须为正数。"
            );
        }

        if (range <= 0) {
            throw new GenerationException(
                    "范围必须为正数。"
            );
        }

        List<Expression> exercises =
                new ArrayList<>(count);

        // 保存已生成题目的规范形式，用于高效去重
        Set<String> normalizedExpressions =
                new HashSet<>(count * 2);

        /*
         * 防止在取值范围过小、无法生成足够不同题目时死循环，如-n 10000 -r 1
         */
        long maxAttempts =
                Math.max(10_000L, count * 500L);

        long attempts = 0;

        while (exercises.size() < count
                && attempts < maxAttempts) {

            attempts++;

            /*
             * 题目最多 3 个运算符，这里生成 1~3 个运算符。
             */
            int operatorCount = 1 + random.nextInt(3);

            Expression expression =
                    expressionGenerator.generate(
                            operatorCount,
                            range
                    );

            // 过滤掉违反约束的表达式
            if (!validator.isValid(expression)) {
                continue;
            }

            String normalized =
                    ExpressionNormalizer.normalize(
                            expression
                    );

            // 规范形式重复则视为同一道题，跳过
            if (!normalizedExpressions.add(normalized)) {
                continue;
            }

            exercises.add(expression);
        }

        if (exercises.size() < count) {
            throw new GenerationException(
                    "不能生成 " + count + " 道唯一有效习题，" + "请增大 -r。"
            );
        }

        return exercises;
    }
}