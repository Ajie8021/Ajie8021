package com.Ajie8021.plagiarism.io;

public interface FileService {
    String read(String path);
    void write(String path, String content);
}
