package com.Ajie8021.arithmetic.validator;

import com.Ajie8021.arithmetic.calculator.ExpressionCalculator;
import com.Ajie8021.arithmetic.exception.InvalidExpressionException;
import com.Ajie8021.arithmetic.model.BinaryExpression;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.model.Fraction;
import com.Ajie8021.arithmetic.model.NumberExpression;

/**
 * 表达式校验器，用于判断表达式是否满足题目约束。
 *
 * 校验规则包括：
 * 1. 表达式非空，且运算符数量不超过 3 个；
 * 2. 递归校验每个子表达式，保证：
 *    - 减法结果不为负数；
 *    - 除法除数不为零，且商为真分数。
 *
 * 校验失败时统一抛出 {@link InvalidExpressionException}，
 * 供调用方通过 {@link #isValid} 转化为布尔结果。
 */
public class ExpressionValidator {

    private final ExpressionCalculator calculator =
            new ExpressionCalculator();

    /**
     * 以布尔方式判断表达式是否合法。
     * （将校验过程中的业务异常与算术异常统一视为非法，避免调用方处理异常细节）
     *
     * @param expression 待校验的表达式
     * @return 合法返回 true，否则返回 false
     */
    public boolean isValid(Expression expression) {
        try {
            validate(expression);
            return true;
        } catch (InvalidExpressionException
                 | ArithmeticException e) {
            return false;
        }
    }

    /**
     * 校验表达式是否满足题目约束，不合法时抛出异常并说明原因。
     *
     * @param expression 待校验的表达式
     * @throws InvalidExpressionException 表达式为空或违反任一约束时抛出
     */
    public void validate(Expression expression) {

        if (expression == null) {
            throw new InvalidExpressionException(
                    "表达式不能为空。"
            );
        }

        // 运算符数量约束：一道题最多包含 3 个运算符
        int operatorCount =
                expression.getOperatorCount();

        if (operatorCount > 3) {
            throw new InvalidExpressionException(
                    "一个表达式最多包含两个运算符。"
            );
        }

        validateNode(expression);
    }

    /**
     * 递归校验表达式树的每个节点。
     *
     * 采用后序遍历：先校验左右子树，再基于子树的计算结果校验当前
     * 运算符是否违反约束（减法的非负性、除法的合法性与商的真分数要求）。
     *
     * @param expression 当前待校验的节点
     */
    private void validateNode(Expression expression) {

        // 叶子节点无需校验运算规则
        if (expression instanceof NumberExpression) {
            return;
        }

        BinaryExpression binary =
                (BinaryExpression) expression;

        validateNode(binary.getLeft());
        validateNode(binary.getRight());

        Fraction left =
                calculator.calculate(binary.getLeft());

        Fraction right =
                calculator.calculate(binary.getRight());

        switch (binary.getOperator()) {

            case ADD:
                // 加法无额外约束
                break;

            case SUBTRACT:
                // 减法结果不能为负
                if (left.compareTo(right) < 0) {
                    throw new InvalidExpressionException(
                            "减法结果不能为负。"
                    );
                }
                break;

            case MULTIPLY:
                // 乘法无额外约束
                break;

            case DIVIDE:
                // 除数不能为零
                if (right.isZero()) {
                    throw new InvalidExpressionException(
                            "除数不能为零。"
                    );
                }

                Fraction result =
                        left.divide(right);

                // 商必须为真分数（0 < 商 < 1）
                if (!result.isProperFraction()) {
                    throw new InvalidExpressionException(
                            "商必须为真分数。"
                    );
                }

                break;
        }
    }
}