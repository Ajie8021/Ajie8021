package com.Ajie8021.arithmetic;

import com.Ajie8021.arithmetic.cli.CommandLineOptions;
import com.Ajie8021.arithmetic.cli.CommandLineParser;
import com.Ajie8021.arithmetic.exception.InvalidArgumentException;
import com.Ajie8021.arithmetic.generator.ExerciseGenerator;
import com.Ajie8021.arithmetic.grader.GradeResult;
import com.Ajie8021.arithmetic.grader.Grader;
import com.Ajie8021.arithmetic.io.FileManager;
import com.Ajie8021.arithmetic.model.Expression;

import java.nio.file.Path;
import java.util.List;

/**
 * 程序入口，负责解析命令行参数并分发到对应模式。
 *
 * 支持两种模式（由 {@link CommandLineParser} 判定）：
 * 1. 出题模式：生成习题并写入 Exercises.txt；
 * 2. 批改模式：比对习题与答案，结果写入 Grade.txt。
 *
 * 参数非法时输出提示与用法说明，并以非零状态退出。
 */
public class Main {

    public static void main(String[] args) {

        CommandLineParser parser = new CommandLineParser();

        try {
            CommandLineOptions options = parser.parse(args);

            switch (options.mode()) {
                case GENERATE ->
                        generate(options.number(), options.range());

                case GRADE ->
                        grade(options.exerciseFile(), options.answerFile());
            }

        } catch (InvalidArgumentException e) {

            /*
             * CommandLineParser 用 message 为 "HELP" 的异常传递
             * "请求帮助"信号，这里据此输出帮助信息而非报错。
             */
            if ("HELP".equals(e.getMessage())) {
                System.out.println(CommandLineParser.help());
                return;
            }

            System.err.println("Error: " + e.getMessage());

            // 参数错误时附带用法说明，帮助用户修正
            System.err.println();
            System.err.println(CommandLineParser.help());

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * 出题模式：生成习题并写入默认输出文件 Exercises.txt。
     *
     * @param number 习题数量
     * @param range  操作数数值范围上限
     */
    private static void generate(int number, int range) {

        ExerciseGenerator generator = new ExerciseGenerator();
        List<Expression> exercises = generator.generate(number, range);

        Path output = Path.of("Exercises.txt");
        FileManager fileManager = new FileManager();
        fileManager.writeExercises(output, exercises);

        System.out.println("Generated " + exercises.size() + " exercises.");
        System.out.println("Output: " + output.toAbsolutePath());
    }

    /**
     * 批改模式：比对习题与答案文件，结果写入默认输出文件 Grade.txt。
     *
     * @param exerciseFile 习题文件路径
     * @param answerFile   答案文件路径
     */
    private static void grade(Path exerciseFile, Path answerFile) {

        Grader grader = new Grader();
        GradeResult result = grader.grade(exerciseFile, answerFile);

        FileManager fileManager = new FileManager();
        Path output = Path.of("Grade.txt");
        fileManager.writeGrade(output, result.format());

        System.out.println("Grading completed.");
        System.out.println("Output: " + output.toAbsolutePath()
        );
    }
}