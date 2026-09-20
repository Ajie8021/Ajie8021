package com.Ajie8021.arithmetic.calculator;

import com.Ajie8021.arithmetic.model.BinaryExpression;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.model.Fraction;
import com.Ajie8021.arithmetic.model.NumberExpression;
import com.Ajie8021.arithmetic.model.Operator;

/**
 * 表达式计算器，对表达式树进行求值。
 *
 * 采用递归方式自底向上计算：叶子节点直接返回其数值，二元节点先计算左右子树，再按运算符合并。
 * 全程使用 {@link Fraction} 进行精确运算，避免浮点数误差。
 */
public class ExpressionCalculator {

    /**
     * 计算表达式树的值。
     *
     * 递归出口为数值叶子节点；
     * 对二元节点，先分别求出左右子树的结果，再依据运算符执行对应的分数运算。
     *
     * @param expression 待计算的表达式树
     * @return 表达式的精确计算结果
     * @throws ArithmeticException 当出现除以零时由 {@link Fraction#divide} 抛出
     */
    public Fraction calculate(Expression expression) {

        if (expression instanceof NumberExpression numberExpression) {
            return numberExpression.getValue();
        }

        // 非叶子节点必然是二元运算节点
        BinaryExpression binary = (BinaryExpression) expression;

        Fraction left = calculate(binary.getLeft());

        Fraction right = calculate(binary.getRight());

        Operator operator = binary.getOperator();

        return switch (operator) {
            case ADD -> left.add(right);
            case SUBTRACT -> left.subtract(right);
            case MULTIPLY -> left.multiply(right);
            case DIVIDE -> left.divide(right);
        };
    }
}