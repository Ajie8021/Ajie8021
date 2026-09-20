package com.Ajie8021.arithmetic.parser;

import com.Ajie8021.arithmetic.exception.InvalidExpressionException;
import com.Ajie8021.arithmetic.model.BinaryExpression;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.model.Fraction;
import com.Ajie8021.arithmetic.model.NumberExpression;
import com.Ajie8021.arithmetic.model.Operator;

/**
 * 表达式解析器，将中缀表达式字符串转换为表达式树。
 *
 * 采用递归下降法，按运算符优先级分层：
 * parseAddSubtract → parseMultiplyDivide → parsePrimary → parseNumber。
 * 通过逐层下降，使括号与优先级自然体现在树的嵌套结构中。
 */
public class ExpressionParser {

    private String input;
    private int position;

    /**
     * 解析表达式字符串并返回表达式树。
     *
     * @param text 中缀表达式文本，支持 + - × ÷ * / 及括号、分数、带分数
     * @return 解析得到的表达式树
     * @throws InvalidExpressionException 表达式为空、格式非法或存在无法解析的多余字符时抛出
     */
    public Expression parse(String text) {

        if (text == null || text.isBlank()) {
            throw new InvalidExpressionException(
                    "表达式为空."
            );
        }

        input = text;
        position = 0;

        Expression expression = parseAddSubtract();

        skipSpaces();

        // 确保整个输入被完整消费，否则说明存在非法字符
        if (position != input.length()) {
            throw new InvalidExpressionException(
                    "位置处出现未规定的字符 " + position + ": " + input.charAt(position)
            );
        }

        return expression;
    }

    /**
     * 解析加减表达式（优先级最低）。
     *
     * 左结合：反复解析乘除项，遇到 + 或 - 时构造二元节点，保证同级运算按从左到右的顺序组成左深树。
     */
    private Expression parseAddSubtract() {

        Expression expression = parseMultiplyDivide();

        while (true) {

            skipSpaces();

            if (match('+')) {
                Expression right = parseMultiplyDivide();
                expression = new BinaryExpression(expression, Operator.ADD, right);
            } else if (match('-')) {
                Expression right = parseMultiplyDivide();
                expression = new BinaryExpression(expression, Operator.SUBTRACT, right);
            } else {
                break;
            }
        }

        return expression;
    }

    /**
     * 解析乘除表达式（优先级高于加减）。
     *
     * 同时兼容数学符号（× ÷）与 ASCII 符号（* /），便于解析用户在不同环境下输入的表达式。
     */
    private Expression parseMultiplyDivide() {
        Expression expression = parsePrimary();

        while (true) {
            skipSpaces();

            if (match('×') || match('*')) {
                Expression right = parsePrimary();
                expression = new BinaryExpression(expression, Operator.MULTIPLY, right);
            } else if (match('÷') || match('/')) {
                Expression right = parsePrimary();
                expression = new BinaryExpression(expression, Operator.DIVIDE, right);
            } else {
                break;
            }
        }

        return expression;
    }

    /**
     * 解析基本单元：括号表达式或数值。
     *
     * 遇到左括号时递归解析内部表达式，并强制要求右括号闭合。
     */
    private Expression parsePrimary() {
        skipSpaces();

        if (match('(')) {
            Expression expression = parseAddSubtract();

            skipSpaces();

            if (!match(')')) {
                throw new InvalidExpressionException(
                        "缺少右括号。"
                );
            }

            return expression;
        }

        return parseNumber();
    }

    /**
     * 解析数值，支持三种形式：整数、真分数/假分数、带分数。
     *
     * 由于分数中的 / 与除法运算符 / 字符相同，这里通过
     * "斜杠后是否紧跟数字"来区分二者：
     * 3/5 视为分数，3 / 5 视为除法。
     */
    private Expression parseNumber() {
        skipSpaces();

        int start = position;

        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            position++;
        }

        if (start == position) {
            throw new InvalidExpressionException(
                    "位置处应为数字 "
                            + position
            );
        }

        /*
         * 普通分数如3/5
         *
         * 注意这里不能直接无条件消费 /，因为 1 / 2 中的 / 是运算符。
         * 只有当 / 后面紧接着数字时，才认为它属于分数。
         */
        if (position < input.length() && input.charAt(position) == '/') {
            int slashPosition = position;

            if (slashPosition + 1 < input.length() && Character.isDigit(
                    input.charAt(slashPosition + 1))) {
                position++;

                int denominatorStart = position;

                while (position < input.length() && Character.isDigit(input.charAt(position))) {
                    position++;
                }

                String numerator = input.substring(start, slashPosition);

                String denominator = input.substring(denominatorStart, position);

                return new NumberExpression(
                        Fraction.parse(numerator + "/" + denominator)
                );
            }
        }

        /*
         * 混合数：
         * 1'1/2
         */
        if (position < input.length() && input.charAt(position) == '\'') {
            position++;

            int numeratorStart = position;

            while (position < input.length() && Character.isDigit(input.charAt(position))) {
                position++;
            }

            // 带分数必须形如 整数'分子/分母，缺一不可
            if (numeratorStart == position
                    || position >= input.length()
                    || input.charAt(position) != '/') {
                throw new InvalidExpressionException(
                        "无效带分数。"
                );
            }

            position++;

            int denominatorStart = position;

            while (position < input.length() && Character.isDigit(input.charAt(position))) {
                position++;
            }

            if (denominatorStart == position) {
                throw new InvalidExpressionException(
                        "无效带分数。"
                );
            }

            String whole =
                    input.substring(start, start +
                            (input.indexOf('\'', start) - start));

            String numerator =
                    input.substring(
                            numeratorStart,
                            input.indexOf('/', numeratorStart)
                    );

            String denominator =
                    input.substring(denominatorStart, position);

            return new NumberExpression(
                    Fraction.parse(whole + "'" + numerator + "/" + denominator)
            );
        }

        String number = input.substring(start, position);

        return new NumberExpression(Fraction.parse(number));
    }

    /**
     * 尝试消费指定字符：匹配则前移游标并返回 true，否则位置不变。
     *
     * 这是递归下降解析器的基本操作，用于"试探 + 前进"。
     */
    private boolean match(char expected) {
        skipSpaces();

        if (position < input.length() && input.charAt(position) == expected) {
            position++;
            return true;
        }

        return false;
    }

    /**
     * 跳过空白字符，使解析器容忍表达式中的任意空格。
     */
    private void skipSpaces() {
        while (position < input.length()
                && Character.isWhitespace(
                input.charAt(position))) {
            position++;
        }
    }
}
