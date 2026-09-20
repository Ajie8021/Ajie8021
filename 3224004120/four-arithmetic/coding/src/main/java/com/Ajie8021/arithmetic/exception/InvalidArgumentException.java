package com.Ajie8021.arithmetic.exception;


/**
 * 命令行参数非法时抛出异常。
 */
public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String message) {
        super(message);
    }
}
