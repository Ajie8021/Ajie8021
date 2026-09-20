package com.Ajie8021.arithmetic.generator;

import com.Ajie8021.arithmetic.model.BinaryExpression;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.model.Fraction;
import com.Ajie8021.arithmetic.model.NumberExpression;
import com.Ajie8021.arithmetic.model.Operator;
import com.Ajie8021.arithmetic.validator.ExpressionValidator;

import java.util.Random;

/**
 * 习题表达式生成器。
 *
 * 按指定的运算符数量和数值范围随机生成表达式树。
 * 通过注入 {@link Random} 支持在测试中传入固定种子，使生成结果可复现。
 */
public class ExpressionGenerator {

    private final Random random;
    private final ExpressionValidator validator;

    public ExpressionGenerator() {
        this(new Random());
    }

    public ExpressionGenerator(Random random) {
        this.random = random;
        this.validator = new ExpressionValidator();
    }

    /**
     * 生成一道满足约束的习题表达式。
     *
     * @param operatorCount 运算符数量，取值范围 0~3
     * @param range         操作数的数值范围上限
     * @return 生成的表达式树
     * @throws IllegalArgumentException 当运算符数量或范围不合法时抛出
     */
    public Expression generate(int operatorCount, int range) {

        if (operatorCount < 0 || operatorCount > 3) {
            throw new IllegalArgumentException(
                    "运算符数量为 1 或 2。"
            );
        }

        if (range <= 0) {
            throw new IllegalArgumentException(
                    "操作数的数值范围上限应为正数。"
            );
        }

        Expression expression =
                generateExpression(operatorCount, range);

        return expression;
    }

    /**
     * 递归生成表达式树。
     *
     * 递归出口为 operatorCount == 0，此时生成一个数值叶子节点；
     * 否则随机将剩余运算符分配给左右子树，再随机选取当前节点的运算符。
     *
     * @param operatorCount 当前子树应包含的运算符数量
     * @param range         操作数的数值范围上限
     * @return 生成的子树
     */
    private Expression generateExpression(
            int operatorCount,
            int range) {

        if (operatorCount == 0) {
            return new NumberExpression(
                    generateOperand(range)
            );
        }

        /*
         * 将剩余运算符随机分配给左右子树。
         */
        int leftOperatorCount =
                random.nextInt(operatorCount);

        int rightOperatorCount =
                operatorCount - 1 - leftOperatorCount;

        Expression left =
                generateExpression(
                        leftOperatorCount,
                        range
                );

        Expression right =
                generateExpression(
                        rightOperatorCount,
                        range
                );

        Operator operator =
                randomOperator();

        return new BinaryExpression(
                left,
                operator,
                right
        );
    }

    /**
     * 生成一个操作数。
     *
     * 当范围足够大（range >= 3）时，约一半概率生成真分数，
     * 否则生成自然数。真分数要求分子小于分母且均为正，
     * 与 {@link Fraction#isProperFraction} 的约定一致。
     *
     * @param range 操作数的数值范围上限
     * @return 生成的操作数
     */
    private Fraction generateOperand(int range) {

        /*
         * 一半概率生成自然数，一半概率生成真分数。
         */
        boolean generateFraction =
                range >= 3 && random.nextBoolean();

        if (!generateFraction) {
            long value = random.nextInt(range);
            return new Fraction(value);
        }

        // 分母至少为 2，保证存在合法的真分数
        int denominator =
                2 + random.nextInt(range - 2);

        // 分子取值范围 1 ~ denominator-1，保证为真分数
        int numerator =
                1 + random.nextInt(denominator - 1);

        return new Fraction(
                numerator,
                denominator
        );
    }

    /**
     * 等概率随机选取一个运算符。
     */
    private Operator randomOperator() {
        Operator[] operators = Operator.values();

        return operators[
                random.nextInt(operators.length)
                ];
    }
}
