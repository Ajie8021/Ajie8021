package com.Ajie8021.arithmetic.exception;


/**
 * 表达式非法时抛出异常。
 */
public class InvalidExpressionException extends RuntimeException {

    public InvalidExpressionException(String message) {
        super(message);
    }
}
