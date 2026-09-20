package com.Ajie8021.arithmetic.exception;


/**
 * 习题生成失败时抛出异常。
 */
public class GenerationException extends RuntimeException {
    public GenerationException(String message) {
        super(message);
    }
}
