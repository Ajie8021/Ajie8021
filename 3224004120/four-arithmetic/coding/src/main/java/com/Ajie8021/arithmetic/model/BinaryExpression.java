package com.Ajie8021.arithmetic.model;

import java.util.Objects;

/**
 * 表达式树中的二元运算节点，由左操作数、运算符和右操作数构成。
 * 通过左右子节点递归组合成完整的表达式树，使核心计算与重复判断基于结构化对象完成。
 */
public final class BinaryExpression implements Expression {

    private final Expression left;
    private final Operator operator;
    private final Expression right;

    public BinaryExpression(
            Expression left,
            Operator operator,
            Expression right) {

        this.left = Objects.requireNonNull(left);
        this.operator = Objects.requireNonNull(operator);
        this.right = Objects.requireNonNull(right);
    }

    public Expression getLeft() {
        return left;
    }

    public Operator getOperator() {
        return operator;
    }

    public Expression getRight() {
        return right;
    }

    /**
     * 运算符总数 = 当前节点 1 个 + 左右子树各自的运算符数量。
     * 用于校验题目是否满足指定的运算符个数要求。
     */
    @Override
    public int getOperatorCount() {
        return 1
                + left.getOperatorCount()
                + right.getOperatorCount();
    }
}
