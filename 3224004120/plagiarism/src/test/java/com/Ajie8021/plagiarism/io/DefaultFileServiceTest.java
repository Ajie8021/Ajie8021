package com.Ajie8021.plagiarism.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DefaultFileServiceTest {
    private final DefaultFileService service = new DefaultFileService();

    @Test void readShouldReturnUtf8Content(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("input.txt");
        Files.writeString(file, "中文", StandardCharsets.UTF_8);
        assertEquals("中文", service.read(file.toString()));
    }

    @Test void writeShouldCreateUtf8File(@TempDir Path dir) throws Exception {
        Path file = dir.resolve("output.txt");
        service.write(file.toString(), "中文");
        assertEquals("中文", Files.readString(file, StandardCharsets.UTF_8));
    }

    @Test void readMissingFileShouldThrow() {
        assertThrows(RuntimeException.class,
                () -> service.read("/path/that/does/not/exist.txt"));
    }
}
