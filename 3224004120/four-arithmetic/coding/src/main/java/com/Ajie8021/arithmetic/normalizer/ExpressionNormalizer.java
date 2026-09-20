package com.Ajie8021.arithmetic.normalizer;

import com.Ajie8021.arithmetic.model.BinaryExpression;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.model.NumberExpression;

import java.util.Objects;

/**
 * 表达式规范化器，用于将表达式转换为唯一的规范形式，以支持重复题判断。
 *
 * 规范化思路：
 * 1. 数值节点输出为 N[值]；
 * 2. 二元节点输出为 E[运算符,左,右]；
 * 3. 对满足交换律的运算符（+、×），当左子树规范串大于右子树时交换二者，
 *    从而消除交换律带来的等价形式差异。
 *
 * 项目中的重复题判断统一通过本类完成，其他模块不应自行实现
 * 重复判断逻辑，避免出现多套不一致的判断规则（规范 7.3）。
 */
public final class ExpressionNormalizer {

    /** 工具类，禁止实例化。 */
    private ExpressionNormalizer() {}

    /**
     * 将表达式转换为规范字符串。
     *
     * 递归处理表达式树，对满足交换律的运算进行左右子树的排序，
     * 保证数学等价的表达式得到相同的规范形式。
     *
     * @param expression 待规范化的表达式
     * @return 表达式的规范形式字符串
     */
    public static String normalize(Expression expression) {

        // 叶子节点：以 N[值] 形式输出
        if (expression instanceof NumberExpression number) {
            return "N[" + number.getValue() + "]";
        }

        BinaryExpression binary = (BinaryExpression) expression;

        String left = normalize(binary.getLeft());
        String right = normalize(binary.getRight());

        // 交换律处理：保证可交换运算的左右子树顺序唯一
        if (binary.getOperator().isCommutative() && left.compareTo(right) > 0) {
            String temp = left;
            left = right;
            right = temp;
        }

        // 二元节点：以 E[运算符,左,右] 形式输出
        return "E[" + binary.getOperator().name()
                + "," + left + "," + right + "]";
    }

    /**
     * 判断两个表达式是否等价（即是否为重复题）。
     *
     * 通过比较二者的规范形式判断，避免直接比较表达式树的结构。
     *
     * @param first  第一个表达式
     * @param second 第二个表达式
     * @return 等价返回 true，否则返回 false
     */
    public static boolean isEquivalent(Expression first, Expression second) {
        return Objects.equals(normalize(first), normalize(second));
    }
}