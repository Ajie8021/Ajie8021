package com.Ajie8021.arithmetic.model;

/**
 * 四则运算符枚举。
 *
 * 使用枚举而非字符串或数字表示运算符，避免非法值，
 * 并集中承载运算符的显示符号、优先级和交换性等元信息。
 */
public enum Operator {

    ADD("+", 1, true),
    SUBTRACT("-", 1, false),
    MULTIPLY("×", 2, true),
    DIVIDE("÷", 2, false);

    // 运算符的显示符号，用于表达式输出。
    private final String symbol;

    // 运算优先级，数值越大优先级越高，用于表达式求值与规范化。
    private final int precedence;

    // 是否满足交换律，用于重复题判断时判断操作数能否互换。
    private final boolean commutative;

    Operator(String symbol, int precedence, boolean commutative) {
        this.symbol = symbol;
        this.precedence = precedence;
        this.commutative = commutative;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPrecedence() {
        return precedence;
    }

    public boolean isCommutative() {
        return commutative;
    }

    /**
     * 根据符号字符解析运算符。
     *
     * 同时兼容程序内部使用的数学符号（×、÷）与命令行/文件中的ASCII符号（*、/），便于统一解析用户输入。
     *
     * @param symbol 运算符字符
     * @return 对应的运算符枚举
     * @throws IllegalArgumentException 当字符不是合法运算符时抛出
     */
    public static Operator fromSymbol(char symbol) {
        return switch (symbol) {
            case '+' -> ADD;
            case '-' -> SUBTRACT;
            case '×', '*' -> MULTIPLY;
            case '÷', '/' -> DIVIDE;
            default -> throw new IllegalArgumentException(
                    "Unknown operator: " + symbol
            );
        };
    }
}