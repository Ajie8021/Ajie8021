package com.Ajie8021.arithmetic.cli;

import com.Ajie8021.arithmetic.exception.InvalidArgumentException;
import java.nio.file.Path;

/**
 * 命令行参数解析器。
 *
 * 支持两种互斥的运行模式：
 * 1. 出题模式：通过 -n 与 -r 指定题目数量和数值范围；
 * 2. 批改模式：通过 -e 与 -a 指定题目文件和答案文件。
 *
 * 两种模式的参数不能混用，参数非法时统一抛出
 * {@link InvalidArgumentException}，由上层输出提示信息。
 */
public class CommandLineParser {

    /**
     * 解析命令行参数并返回选项对象。
     *
     * @param args 命令行参数数组
     * @return 解析后的命令行选项
     * @throws InvalidArgumentException 参数缺失、非法或模式混用时抛出
     */
    public CommandLineOptions parse(String[] args) {

        if (args == null || args.length == 0) {
            throw new InvalidArgumentException(
                    "未提供参数。"
            );
        }

        Integer number = null;
        Integer range = null;

        Path exerciseFile = null;
        Path answerFile = null;

        boolean help = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "-h":
                case "--help":
                    help = true;
                    break;

                case "-n":
                    number = parsePositiveInteger(nextValue(args, ++i, "-n"), "-n");
                    break;

                case "-r":
                    range = parsePositiveInteger(nextValue(args, ++i, "-r"), "-r");
                    break;

                case "-e":
                    exerciseFile = Path.of(nextValue(args, ++i, "-e"));
                    break;

                case "-a":
                    answerFile = Path.of(nextValue(args, ++i, "-a"));
                    break;

                default:
                    throw new InvalidArgumentException(
                            "未知参数: " + arg
                    );
            }
        }

        /*
         * 复用异常传递"请求帮助"信号：
         * 上层捕获到 message 为 "HELP" 时输出帮助信息而非报错。
         */
        if (help) {
            throw new InvalidArgumentException("HELP");
        }

        // 依据已解析的选项判断进入哪种模式
        boolean generationMode = number != null || range != null;
        boolean gradingMode = exerciseFile != null || answerFile != null;

        if (generationMode && gradingMode) {
            throw new InvalidArgumentException(
                    "生成模式和判分模式不能混用。"
            );
        }

        if (generationMode) {
            if (number == null) {
                throw new InvalidArgumentException(
                        "生成模式下必须指定 -n。"
                );
            }

            if (range == null) {
                throw new InvalidArgumentException(
                        "生成模式下必须指定 -r。"
                );
            }

            return new CommandLineOptions(
                    CommandLineOptions.Mode.GENERATE,
                    number, range, null, null
            );
        }

        if (gradingMode) {
            // 批改模式下 -e 与 -a 必须成对出现
            if (exerciseFile == null || answerFile == null) {
                throw new InvalidArgumentException(
                        "-e和-a必须一起提供。"
                );
            }

            return new CommandLineOptions(
                    CommandLineOptions.Mode.GRADE,
                    0, 0, exerciseFile, answerFile
            );
        }

        throw new InvalidArgumentException(
                "未指定有效操作。"
        );
    }

    /**
     * 读取选项后紧跟的参数值。
     * 若选项位于末尾而缺少取值，则抛出异常提示缺失。
     *
     * @param args   命令行参数数组
     * @param index  取值所在的下标
     * @param option 选项名，用于错误提示
     * @return 选项对应的参数值
     * @throws InvalidArgumentException 取值越界（选项缺少值）时抛出
     */
    private String nextValue(String[] args, int index, String option) {

        if (index >= args.length) {
            throw new InvalidArgumentException(
                    "缺少参数值：" + option
            );
        }

        return args[index];
    }

    /**
     * 将文本解析为正整数。
     *
     * 同时校验"格式合法"与"数值为正"两个条件，任一不满足均视为参数非法。
     *
     * @param text   待解析的文本
     * @param option 选项名，用于错误提示
     * @return 解析得到的正整数
     * @throws InvalidArgumentException 文本非整数或数值非正时抛出
     */
    private int parsePositiveInteger(String text, String option) {

        try {
            int value = Integer.parseInt(text);

            if (value <= 0) {
                throw new InvalidArgumentException(
                        option + " 应该是正数。"
                );
            }

            return value;

        } catch (NumberFormatException e) {
            throw new InvalidArgumentException(
                    option + " 应为正整数。"
            );
        }
    }

    /**
     * 返回命令行帮助文本。
     *
     * 以文本块形式集中维护用法说明，便于与解析逻辑同步更新。
     *
     * @return 帮助信息字符串
     */
    public static String help() {

        return """
                Elementary Arithmetic Exercise Generator

                Generate exercises:
                  java -jar arithmetic-project-1.0.0.jar -n 10 -r 10

                Options:
                  -n <number>     Number of exercises
                  -r <range>      Value range, values are less than range
                  -e <file>       Exercise file for grading
                  -a <file>       Answer file for grading
                  -h              Show help

                Generation output:
                  Exercises.txt

                Grading output:
                  Grade.txt
                """;
    }
}