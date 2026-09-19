package com.Ajie8021.plagiarism.io;

import com.Ajie8021.plagiarism.exception.FileReadException;
import com.Ajie8021.plagiarism.exception.FileWriteException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 文件读写服务实现。
 *
 * 所有文件路径处理与 I/O 集中在此处，统一使用 UTF-8 编码，
 */
public class DefaultFileService implements FileService {

    @Override
    public String read(String path) {
        if (path == null || path.isBlank()) {
            throw new FileReadException("输入路径为空。", null);
        }
        try {
            return Files.readString(Path.of(path), StandardCharsets.UTF_8);
        } catch (IOException | RuntimeException e) {
            // 转换为项目自定义异常，并保留原始异常便于定位问题。
            throw new FileReadException("读取文件失败：" + path, e);
        }
    }

    @Override
    public void write(String path, String content) {
        if (path == null || path.isBlank()) {
            throw new FileWriteException("输出路径为空。", null);
        }
        try {
            Files.writeString(Path.of(path), content, StandardCharsets.UTF_8);
        } catch (IOException | RuntimeException e) {
            // 转换为项目自定义异常，并保留原始异常便于定位问题。
            throw new FileWriteException("写入文件失败：" + path, e);
        }
    }
}
