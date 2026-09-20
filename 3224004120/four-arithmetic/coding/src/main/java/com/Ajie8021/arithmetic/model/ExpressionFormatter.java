package com.Ajie8021.arithmetic.model;

/**
 * 表达式计算相关功能的入口类。
 *
 * 承载表达式格式化逻辑，负责将表达式树还原为
 * 可读的中缀字符串（含括号），供文件输出与显示使用。
 */
public final class ExpressionFormatter {

    /**
     * 表达式格式化器，将表达式树转换为中缀字符串。
     *
     * 输出时按运算符优先级与左右结合性决定是否补括号，保证格式化结果在数学语义上与原表达式树一致。
     */

    private ExpressionFormatter() {
    }

    public static String format(Expression expression) {
        return formatExpression(expression);
    }

    private static String formatExpression(Expression expression) {

        if (expression instanceof NumberExpression numberExpression) {
            return numberExpression.getValue().toString();
        }

        // 非叶子节点必然是二元运算节点
        BinaryExpression binary = (BinaryExpression) expression;

        String left = formatChild(binary.getLeft(), binary.getOperator(), false);
        String right = formatChild(binary.getRight(), binary.getOperator(), true);

        return left + " " + binary.getOperator().getSymbol() + " " + right;
    }

    /**
     * 格式化子表达式，并根据是否需要括号决定是否包裹。
     *
     * 判断规则：
     * 1. 子表达式优先级低于父运算符时必须加括号；
     * 2. 右子表达式优先级与父运算符相同时必须加括号，以保留原始表达式树的结合结构。
     *
     * @param child          待格式化的子表达式
     * @param parentOperator 父节点的运算符
     * @param rightChild     是否为右子节点
     * @return 格式化后的字符串，必要时含括号
     */
    private static String formatChild(Expression child, Operator parentOperator, boolean rightChild) {

        if (!(child instanceof BinaryExpression binaryChild)) {
            return formatExpression(child);
        }

        int childPrecedence = binaryChild.getOperator().getPrecedence();
        int parentPrecedence = parentOperator.getPrecedence();

        boolean needParentheses = childPrecedence < parentPrecedence;

        /*
         * 右子树如果和父节点优先级相同，为了保持原始二叉树结构，需要加括号。
         * 例如 1 + (2 + 3)
         */
        if (rightChild && childPrecedence == parentPrecedence) {
            needParentheses = true;
        }

        String result = formatExpression(child);

        if (needParentheses) {
            return "(" + result + ")";
        }

        return result;
    }
}
