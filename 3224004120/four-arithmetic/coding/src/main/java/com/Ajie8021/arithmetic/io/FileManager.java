package com.Ajie8021.arithmetic.io;

import com.Ajie8021.arithmetic.calculator.ExpressionCalculator;
import com.Ajie8021.arithmetic.exception.FileOperationException;
import com.Ajie8021.arithmetic.model.Expression;
import com.Ajie8021.arithmetic.model.ExpressionFormatter;
import com.Ajie8021.arithmetic.model.Fraction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件读写管理类。
 *
 * 负责习题文件、成绩文件的写入以及文本文件的读取。
 */
public class FileManager {

    /**
     * 将习题列表格式化后写入文件。
     *
     * 每道习题按照“题号 + 表达式 + 等号”的格式输出，
     * 便于生成最终的习题文件。
     *
     * @param file     输出文件路径
     * @param exercises 习题列表
     * @throws FileOperationException 文件写入失败时抛出
     */
    public void writeExercises(Path file, List<Expression> exercises) {

        List<String> lines = new ArrayList<>(exercises.size());

        // 将表达式对象转换为适合文件输出的字符串。
        for (int i = 0; i < exercises.size(); i++) {
            String expression = ExpressionFormatter.format(exercises.get(i));
            lines.add((i + 1) + ". " + expression + " =");
        }

        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // 将底层文件异常转换为项目统一的文件操作异常。
            throw new FileOperationException(
                    "该文件写入失败: " + file, e
            );
        }
    }

    /**
     * 按 UTF-8 编码读取文件中的所有文本行。
     *
     * @param file 文件路径
     * @return 文件中的文本行
     * @throws FileOperationException 文件读取失败时抛出
     */
    public List<String> readLines(Path file) {

        try {
            return Files.readAllLines(
                    file,
                    StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            // 统一封装文件读取异常，避免底层 IOException 直接向上暴露。
            throw new FileOperationException(
                    "该文件读取失败: " + file, e
            );
        }
    }

    /**
     * 将习题对应的答案写入文件。
     *
     * 每行格式为 "序号. 答案"，与 {@link #writeExercises} 的题号一一对应，
     * 便于批改时按行比对。答案由 {@link ExpressionCalculator} 精确计算得到
     *
     * @param file       目标文件路径
     * @param exercises  待计算答案的习题列表
     * @param calculator 表达式计算器，由调用方传入以复用同一实例
     * @throws FileOperationException 文件写入失败时抛出
     */
    public void writeAnswers(Path file, List<Expression> exercises, ExpressionCalculator calculator) {

        List<String> lines = new ArrayList<>(exercises.size());

        for (int i = 0; i < exercises.size(); i++) {
            Fraction answer = calculator.calculate(exercises.get(i));
            // 题号从 1 开始，与习题文件中的编号保持一致
            lines.add((i + 1) + ". " + answer);
        }

        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileOperationException(
                    "该文件写入失败: " + file, e
            );
        }
    }

    /**
     * 将成绩内容写入指定文件。
     *
     * @param file    成绩文件路径
     * @param content 要写入的成绩内容
     * @throws FileOperationException 文件写入失败时抛出
     */
    public void writeGrade(Path file, String content) {

        try {
            Files.writeString(file, content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // 统一处理成绩文件写入异常。
            throw new FileOperationException(
                    "写入判分文件失败: " + file, e
            );
        }
    }
}