package com.Ajie8021.arithmetic.model;

import java.math.BigInteger;
import java.util.Objects;

/**
 * 表示精确分数并提供四则运算。
 *
 * 项目中的自然数和分数统一使用本类表示（如 2 表示为 2/1），使用 BigInteger 而非 double 计算，避免浮点数精度问题。
 * 构造时始终约分，并保证分母为正、负号统一由分子承载，从而使相等分数的对象状态唯一，便于 equals 与重复题判断。
 */
public final class Fraction implements Comparable<Fraction> {

    private final BigInteger numerator;
    private final BigInteger denominator;

    public Fraction(long numerator) {
        this(BigInteger.valueOf(numerator), BigInteger.ONE);
    }

    public Fraction(long numerator, long denominator) {
        this(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }

    public Fraction(BigInteger numerator, BigInteger denominator) {
        if (denominator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("分母不能为0。");
        }

        // 统一将负号放在分子，保证分母始终为正
        if (denominator.signum() < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }

        // 构造时立即约分，保证相同数值的分数具有一致的对象状态
        BigInteger gcd = numerator.gcd(denominator);

        this.numerator = numerator.divide(gcd);
        this.denominator = denominator.divide(gcd);
    }

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public Fraction add(Fraction other) {
        BigInteger newNumerator =
                numerator.multiply(other.denominator)
                        .add(other.numerator.multiply(denominator));

        BigInteger newDenominator = denominator.multiply(other.denominator);

        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction subtract(Fraction other) {
        BigInteger newNumerator =
                numerator.multiply(other.denominator)
                        .subtract(other.numerator.multiply(denominator));

        BigInteger newDenominator = denominator.multiply(other.denominator);

        return new Fraction(newNumerator, newDenominator);
    }

    public Fraction multiply(Fraction other) {
        return new Fraction(
                numerator.multiply(other.numerator),
                denominator.multiply(other.denominator)
        );
    }

    public Fraction divide(Fraction other) {
        if (other.numerator.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("被除数为0。");
        }

        return new Fraction(
                numerator.multiply(other.denominator),
                denominator.multiply(other.numerator)
        );
    }

    public boolean isZero() {
        return numerator.equals(BigInteger.ZERO);
    }

    /**
     * 判断是否为真正的正真分数：
     * 0 < numerator < denominator
     */
    public boolean isProperFraction() {
        return numerator.signum() > 0 && numerator.compareTo(denominator) < 0;
    }

    public boolean isInteger() {
        return denominator.equals(BigInteger.ONE);
    }

    public Fraction abs() {
        return new Fraction(numerator.abs(), denominator);
    }

    /**
     * 交叉相乘比较大小，避免转换为浮点数造成精度损失。
     */
    @Override
    public int compareTo(Fraction other) {
        return numerator.multiply(other.denominator).compareTo(other.numerator.multiply(denominator));
    }

    /**
     * 按题目要求输出：
     * 3/5
     * 1'1/2
     * 2
     */
    @Override
    public String toString() {
        if (numerator.equals(BigInteger.ZERO)) {
            return "0";
        }

        if (denominator.equals(BigInteger.ONE)) {
            return numerator.toString();
        }

        if (numerator.signum() < 0) {
            return numerator + "/" + denominator;
        }

        if (numerator.compareTo(denominator) < 0) {
            return numerator + "/" + denominator;
        }

        // 假分数转换为带分数形式，如 11/4 → 2'3/4
        BigInteger[] divideAndRemainder = numerator.divideAndRemainder(denominator);

        BigInteger whole = divideAndRemainder[0];
        BigInteger remainder = divideAndRemainder[1];

        if (remainder.equals(BigInteger.ZERO)) {
            return whole.toString();
        }

        return whole + "'" + remainder + "/" + denominator;
    }

    /**
     * 解析分数文本，支持三种形式：
     * 普通整数（2）、真分数/假分数（3/5、11/4）、带分数（1'1/2）。
     *
     * 带分数要求各部分均为正且分数部分为真分数，与题目中自然数与真分数的表示约定保持一致。
     */
    public static Fraction parse(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("空分数。");
        }

        text = text.trim();

        // mixed number，例如 1'1/2
        int apostropheIndex = text.indexOf('\'');

        if (apostropheIndex >= 0) {
            String wholePart = text.substring(0, apostropheIndex).trim();
            String fractionPart = text.substring(apostropheIndex + 1).trim();

            int slashIndex = fractionPart.indexOf('/');

            if (slashIndex <= 0 || slashIndex == fractionPart.length() - 1) {
                throw new IllegalArgumentException(
                        "无效带分数: " + text
                );
            }

            BigInteger whole = new BigInteger(wholePart);
            BigInteger numerator = new BigInteger(fractionPart.substring(0, slashIndex));

            BigInteger denominator = new BigInteger(fractionPart.substring(slashIndex + 1));

            if (whole.signum() < 0
                    || numerator.signum() <= 0
                    || denominator.signum() <= 0
                    || numerator.compareTo(denominator) >= 0) {
                throw new IllegalArgumentException(
                        "无效带分数: " + text
                );
            }

            BigInteger totalNumerator = whole.multiply(denominator).add(numerator);

            return new Fraction(totalNumerator, denominator);
        }

        // 普通整数
        if (!text.contains("/")) {
            return new Fraction(new BigInteger(text), BigInteger.ONE);
        }

        int slashIndex = text.indexOf('/');

        if (slashIndex <= 0 || slashIndex == text.length() - 1) {
            throw new IllegalArgumentException(
                    "无效分数: " + text
            );
        }

        BigInteger numerator = new BigInteger(text.substring(0, slashIndex).trim());

        BigInteger denominator = new BigInteger(text.substring(slashIndex + 1).trim());

        return new Fraction(numerator, denominator);
    }

    /**
     * 基于约分后的分子分母判断相等，
     * 保证等值分数（如 1/2 与 2/4）被视为相同对象。
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Fraction other)) {
            return false;
        }

        return numerator.equals(other.numerator)
                && denominator.equals(other.denominator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numerator, denominator);
    }
}