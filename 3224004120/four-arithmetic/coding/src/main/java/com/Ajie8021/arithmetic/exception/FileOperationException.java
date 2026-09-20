package com.Ajie8021.arithmetic.exception;


/**
 * 文件读写操作失败时抛出异常。
 */
public class FileOperationException extends RuntimeException {
    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
