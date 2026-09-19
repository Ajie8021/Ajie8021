package com.Ajie8021.plagiarism;

import com.Ajie8021.plagiarism.exception.InvalidArgumentException;
import com.Ajie8021.plagiarism.format.DefaultResultFormatter;
import com.Ajie8021.plagiarism.io.DefaultFileService;
import com.Ajie8021.plagiarism.preprocess.DefaultTextPreprocessor;
import com.Ajie8021.plagiarism.similarity.LcsSimilarityCalculator;

/**
 * 程序入口。
 */
public final class Main {

    private static final int REQUIRED_ARGUMENT_COUNT = 3;

    private Main() {
    }

    public static void main(String[] args) {
        try {
            validateArguments(args);

            PlagiarismApplication application = new PlagiarismApplication(
                    new DefaultFileService(),
                    new DefaultTextPreprocessor(),
                    new LcsSimilarityCalculator(),
                    new DefaultResultFormatter()
            );

            application.run(args[0], args[1], args[2]);
        } catch (RuntimeException e) {
            // 统一输出错误信息并以非零状态码退出，便于调用方判断失败。
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * 校验命令行参数：必须为 3 个，且每个路径非空。
     */
    private static void validateArguments(String[] args) {
        if (args == null || args.length != REQUIRED_ARGUMENT_COUNT) {
            throw new InvalidArgumentException(
                    "用法：java -jar main.jar [原文文件] [查重文件] [答案文件]"
            );
        }
        for (String arg : args) {
            if (arg == null || arg.isBlank()) {
                throw new InvalidArgumentException("文件路径不能为空。");
            }
        }
    }
}
