package com.Ajie8021.arithmetic.model;

import java.util.Objects;

/**
 * 表达式树中的叶子节点，表示一个具体的数值。
 *
 * 与 {@link Operator} 相对，用于承载运算的最终数值，本身不包含运算符，因此运算符数量恒为 0。
 * 数值统一使用 {@link Fraction} 表示，避免浮点数精度问题。
 */
public final class NumberExpression implements Expression {

    private final Fraction value;

    public NumberExpression(Fraction value) {
        this.value = Objects.requireNonNull(value);
    }

    public Fraction getValue() {
        return value;
    }

    /**
     * 叶子节点不含任何运算符。
     */
    @Override
    public int getOperatorCount() {
        return 0;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}