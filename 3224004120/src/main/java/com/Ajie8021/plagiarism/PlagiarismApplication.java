package com.Ajie8021.plagiarism;

import com.Ajie8021.plagiarism.format.ResultFormatter;
import com.Ajie8021.plagiarism.io.FileService;
import com.Ajie8021.plagiarism.preprocess.TextPreprocessor;
import com.Ajie8021.plagiarism.similarity.SimilarityCalculator;

/**
 * 查重流程的核心协调逻辑。
 *
 * 仅负责按顺序调用各模块，不直接实现文件 I/O、预处理、相似度计算或结果格式化。
 * 通过构造器注入依赖，便于替换实现和进行单元测试。
 */
public class PlagiarismApplication {

    private final FileService fileService;
    private final TextPreprocessor preprocessor;
    private final SimilarityCalculator calculator;
    private final ResultFormatter formatter;

    public PlagiarismApplication(
            FileService fileService,
            TextPreprocessor preprocessor,
            SimilarityCalculator calculator,
            ResultFormatter formatter) {
        this.fileService = fileService;
        this.preprocessor = preprocessor;
        this.calculator = calculator;
        this.formatter = formatter;
    }

    /**
     * 执行一次完整的查重流程：
     * 读取原文与查重文本 → 文本预处理 → 相似度计算 → 格式化 → 写入答案文件。
     */
    public void run(String originalPath, String plagiarismPath, String answerPath) {
        String original = fileService.read(originalPath);
        String plagiarism = fileService.read(plagiarismPath);

        String processedOriginal = preprocessor.preprocess(original);
        String processedPlagiarism = preprocessor.preprocess(plagiarism);

        double similarity = calculator.calculate(
                processedOriginal,
                processedPlagiarism
        );

        String result = formatter.format(similarity);
        fileService.write(answerPath, result);
    }
}
