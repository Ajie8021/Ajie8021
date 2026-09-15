#### Java 论文查重项目编码规范

##### 1. 文档说明

###### 1.1 编写目的

本文档规定论文查重项目的 Java 编码规范，用于统一项目中的：

- Java 版本与项目结构；
- 类、接口、方法和变量命名；
- 代码格式；
- 注释方式；
- 异常处理；
- 文件输入输出；
- 算法模块实现；
- 单元测试；
- Git 提交规范；
- 代码质量与静态检查。

本规范的目标是提高代码的**可读性、可维护性、可测试性和可扩展性**，同时满足项目作业对代码质量分析、单元测试、性能分析和 Git 过程管理的要求。

##### 2. 基本开发规范

###### 2.1 开发语言

项目统一使用：

- Java

具体 JDK 版本应与项目实际构建环境保持一致。

建议在 `pom.xml` 中统一声明 Java 版本，避免开发环境和测试环境使用不同版本。

例如：

```java
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
</properties>
```

实际版本以项目最终确定的 JDK 为准。

###### 2.2 构建工具

项目建议使用 Maven 进行依赖管理、编译、测试和打包。

主要任务包括：

```
compile    编译项目
test       执行单元测试
package    打包项目
```

最终需要生成：`main.jar`

并按照作业要求发布到 GitHub Releases。

##### 3. 项目目录规范

项目采用 Maven 标准目录结构：

```
project-root/
├── docs/
│   ├── requirements.md
│   ├── design-spec.md
│   ├── design-review.md
│   └── coding-standard.md
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── xxx/
│   │               └── plagiarism/
│   │                   ├── Main.java
│   │                   ├── io/
│   │                   ├── preprocess/
│   │                   ├── similarity/
│   │                   ├── format/
│   │                   └── exception/
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── xxx/
│                   └── plagiarism/
│
├── pom.xml
├── README.md
├── .gitignore
└── ...
```

###### 包职责：

| 包                   | 职责                   |
| -------------------- | ---------------------- |
| `com.xxx.plagiarism` | 程序入口及核心协调逻辑 |
| `io`                 | 文件读取与写入         |
| `preprocess`         | 文本预处理             |
| `similarity`         | 相似度计算             |
| `format`             | 结果格式化             |
| `exception`          | 自定义异常             |
| `src/test`           | 单元测试               |

不同模块之间应保持明确的职责边界。

##### 4. 命名规范

###### 4.1 包名

包名全部使用小写字母。

正确：

```
com.xxx.plagiarism.similarity
```

错误：

```
com.xxx.plagiarism.Similarity
com.xxx.plagiarism.SimilarityModule
```

###### 4.2 类名

类名采用 **UpperCamelCase**，即每个单词首字母大写。

正确：

```
Main
FileService
TextPreprocessor
LcsSimilarityCalculator
ResultFormatter
```

错误：

```
fileService
file_service
FILE_SERVICE
```

类名应体现其职责，不使用含义模糊的名称。

不推荐：

```
Utils
Helper
Test1
Manager
```

如果确实需要工具类，应使用能够表达具体职责的名称，例如：

```
TextUtils
FileUtils
```

###### 4.3 接口名

接口名称采用 **UpperCamelCase**。

例如：

```java
public interface SimilarityCalculator {
}
```

接口名称应表达抽象能力，而不是具体实现。

正确：

```
SimilarityCalculator
TextPreprocessor
FileService
```

不推荐：

```
SimilarityCalculatorInterface
ISimilarityCalculator
```

除非项目后续明确采用其他统一命名规则。

###### 4.4 方法名

方法名采用 **lowerCamelCase**。

正确：

```
calculateSimilarity()
readFile()
preprocess()
formatResult()
validateArguments()
```

方法名应体现动作或行为。

不推荐：

```
data()
file()
result()
process()
```

###### 4.5 变量名

局部变量和成员变量采用 **lowerCamelCase**。

正确：

```
originalText
plagiarismText
similarity
outputPath
normalizedText
```

错误：

```
OriginalText
original_text
ORIGINALTEXT
```

变量名应具有明确含义，避免单个字母或无意义名称。

不推荐：

```java
String a;
String b;
String temp;
String data;
```

但循环变量等简单场景可以使用：

```java
for (int i = 0; i < length; i++) {
}
```

###### 4.6 常量

常量全部使用大写字母，单词之间使用下划线。

例如：

```java
private static final int MAX_ARGUMENT_COUNT = 3;
private static final String DEFAULT_ENCODING = "UTF-8";
```

##### 5. 类设计规范

###### 5.1 单一职责

一个类原则上只负责一个明确的功能。

例如：

```
FileService
    负责文件读写

TextPreprocessor
    负责文本预处理

SimilarityCalculator
    负责相似度计算

ResultFormatter
    负责结果格式化
```

禁止将所有功能集中到 `Main.java` 中。

不应出现类似：

```java
public class Main {
    // 解析参数
    // 读取文件
    // HTML处理
    // 文本清洗
    // LCS计算
    // 结果格式化
    // 异常处理
}
```

`Main` 只负责程序启动、参数协调和模块调用。

###### 5.2 方法长度

方法应保持较小规模，一个方法尽量只完成一个明确任务。

如果一个方法同时完成：

```
参数检查
文件读取
HTML解析
文本清洗
相似度计算
结果格式化
文件写入
```

应拆分成多个方法或交由不同模块完成。

###### 5.3 避免重复代码

相同逻辑出现多次时，应提取为方法或独立模块。

例如，不应在多个地方重复进行：

```java
Files.readString(...)
```

文件读取应统一由 `FileService` 负责。

##### 6. 接口设计规范

项目采用接口与实现分离的方式设计核心功能。

###### 6.1 相似度计算接口

统一使用：

```java
public interface SimilarityCalculator {
    double calculate(String originalText, String plagiarismText);
}
```

调用方只依赖：`SimilarityCalculator`

而不直接依赖具体算法实现。

例如：

```java
SimilarityCalculator calculator = new LcsSimilarityCalculator();
```

这样可以在后续实验中替换不同的相似度算法，而不需要修改上层业务代码。

###### 6.2 文本预处理接口

```java
public interface TextPreprocessor {
    String preprocess(String text);
}
```

具体实现负责：

- HTML 内容处理；
- 标签处理；
- HTML 实体处理；
- 空白字符处理；
- 根据最终确定的需求进行文本规范化。

注意：**预处理规则必须以需求分析和样例验证结果为依据，不得未经验证擅自删除可能影响查重结果的内容。**

###### 6.3 文件服务接口

```java
public interface FileService {
    String read(String path);
    void write(String path, String content);
}
```

文件路径处理和文件 I/O 应集中管理。

###### 6.4 结果格式化接口

```java
public interface ResultFormatter {
    String format(double similarity);
}
```

格式化模块负责最终输出规则，例如保留两位小数。

##### 7. 输入输出规范

###### 7.1 命令行参数

程序必须支持：

```
java -jar main.jar [original file] [plagiarism file] [answer file]
```

因此程序启动时应检查参数数量。

推荐：

```java
if (args.length != 3) {
    throw new InvalidArgumentException(
            "Expected 3 arguments."
    );
}
```

不得默认依赖当前工作目录。

###### 7.2 文件路径

输入参数按照作业要求使用**绝对路径**。

程序不得：

- 修改用户输入的文件；
- 访问无关文件；
- 访问网络资源；
- 读取项目目录之外与任务无关的数据。

###### 7.3 字符编码

文本文件读取必须明确指定字符编码。

例如：

```java
Files.readString(
        Path.of(path),
        StandardCharsets.UTF_8
);
```

禁止依赖操作系统默认编码。

这样可以避免中文文本在不同操作系统中出现乱码。

##### 8. 文本预处理规范

论文查重程序的文本预处理必须保持稳定、可测试。

###### 8.1 预处理原则

预处理应遵循：

1. 明确；
2. 可重复；
3. 可测试；
4. 不进行无依据的信息删除。

对于普通文本，应尽可能保留论文正文内容。

###### 8.2 HTML 文件

样例中存在 HTML 类文本，因此程序需要考虑 HTML 内容。

处理 HTML 时，应重点考虑：

```
HTML 标签
<script>
<style>
HTML entity
换行
空白字符
正文内容
```

不能简单地使用：

```java
text.replaceAll("<.*?>", "");
```

就认为完成了 HTML 处理。

如果后续验证发现样例需要更加严格的 HTML 解析，应采用专门的解析方式。

###### 8.3 空白字符

空格、换行、制表符等空白字符的处理方式必须在算法验证阶段确定。

例如：

```
Hello World
Hello    World
```

是否视为相同，需要根据需求和样例确定。

因此，编码阶段不得随意添加：

```
text.replaceAll("\\s+", "");
```

除非设计文档已经明确规定。

###### 8.4 大小写

英文大小写是否敏感属于算法规则的一部分。

在规则尚未最终确认前，不应擅自执行：

```java
text.toLowerCase()
```

或：

```java
text.toUpperCase()
```

###### 8.5 标点符号

标点符号是否参与相似度计算也属于算法规则。

在没有明确需求支持之前，不应直接删除`，。！？,.!?`等字符。

##### 9. 相似度计算规范

###### 9.1 模块隔离

相似度计算必须独立于文件 I/O 和命令行处理。

例如：

```java
public class LcsSimilarityCalculator
        implements SimilarityCalculator {

    @Override
    public double calculate(
            String originalText,
            String plagiarismText) {

        // similarity algorithm
    }
}
```

这样可以直接对算法进行单元测试。

###### 9.2 算法规则

当前设计阶段将 LCS 作为**候选算法/第一版实现方案**。

不得在代码注释或文档中将其描述为“题目官方规定算法”，除非通过样例或题目明确规则得到验证。

当前候选思路：

```
原文 A
改写文 B
      ↓
计算 A、B 的最长公共子序列 LCS
      ↓
根据最终确认的重复率公式计算结果
      ↓
格式化输出
```

候选实现应与算法模块解耦，方便后续根据样例验证结果调整。

###### 9.3 算法实现规范

如果采用动态规划，应明确 DP 状态含义。

例如：

```
dp[i][j]
表示两个文本前 i、j 个字符的最长公共子序列长度
```

状态转移：

```java
A[i - 1] == B[j - 1]
dp[i][j] = dp[i - 1][j - 1] + 1

//否则：
dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
```

代码应与上述定义保持一致。

###### 9.4 算法复杂度

初版算法可以优先采用易于理解和验证的实现。

例如二维 DP：

```
时间复杂度：O(N × M)
空间复杂度：O(N × M)
```

其中：

```
N = 原文长度
M = 待查文本长度
```

是否需要进一步优化，应根据：

1. 样例测试；
2. 单元测试；
3. 实际运行时间；
4. Profiler 性能分析；

进行判断。

不得在没有性能数据的情况下为了“看起来高级”而过早优化。

##### 10. 异常处理规范

###### 10.1 禁止吞掉异常

不允许：

```java
try {
    ...
} catch (Exception e) {
}
```

也不允许：

```java
catch (Exception e) {
    e.printStackTrace();
}
```

然后继续执行可能已经失效的程序逻辑。

###### 10.2 异常分类

建议根据设计文档建立明确的异常类型，例如：

```
InvalidArgumentException
FileReadException
FileWriteException
```

异常名称应能够表达错误原因。

###### 10.3 参数异常

程序必须处理：

```
参数数量错误
参数为空
路径为空
```

例如：

```java
if (args == null || args.length != 3) {
    throw new InvalidArgumentException(
            "Exactly three arguments are required."
    );
}
```

###### 10.4 文件异常

需要处理：

```
文件不存在
文件无法读取
文件无法写入
路径非法
权限不足
```

底层异常应转换成项目能够理解的异常。

例如：

```java
try {
    return Files.readString(
            Path.of(path),
            StandardCharsets.UTF_8
    );
} catch (IOException e) {
    throw new FileReadException(
            "Failed to read file: " + path,
            e
    );
}
```

保留原始异常作为 cause，便于调试。

##### 11. `Main` 类规范

`Main` 是程序入口，但不是业务逻辑中心。

推荐结构：

```java
public class Main {

    public static void main(String[] args) {
        validateArguments(args);

        FileService fileService = ...;
        TextPreprocessor preprocessor = ...;
        SimilarityCalculator calculator = ...;
        ResultFormatter formatter = ...;

        String original = fileService.read(args[0]);
        String plagiarism = fileService.read(args[1]);

        String processedOriginal =
                preprocessor.preprocess(original);

        String processedPlagiarism =
                preprocessor.preprocess(plagiarism);

        double similarity =
                calculator.calculate(
                        processedOriginal,
                        processedPlagiarism
                );

        String result = formatter.format(similarity);

        fileService.write(args[2], result);
    }
}
```

`Main` 不应直接包含：

- LCS 动态规划；
- HTML 解析；
- 大量字符串处理；
- 文件底层操作；
- 复杂异常处理逻辑。

##### 12. 注释规范

###### 12.1 注释原则

注释应解释：

- 为什么这样做；
- 算法思想；
- 复杂逻辑；
- 特殊边界条件；
- 非直观设计决策。

不应大量解释显而易见的代码。

不推荐：

```java
// i 加 1
i++;
```

推荐：

```java
// 使用滚动数组保存上一行 DP 状态，降低空间复杂度。
```

###### 12.2 类注释

核心类建议添加类级注释：

```java
/**
 * Calculates text similarity using the configured similarity algorithm.
 */
public class LcsSimilarityCalculator
        implements SimilarityCalculator {
}
```

###### 12.3 方法注释

对公共接口和复杂方法添加 JavaDoc。

例如：

```java
/**
 * Calculates the similarity between the original text
 * and the plagiarism text.
 *
 * @param originalText original document
 * @param plagiarismText document to compare
 * @return similarity score
 */
double calculate(
        String originalText,
        String plagiarismText
);
```

###### 12.4 不保留无意义注释

禁止大量出现：

```java
// 创建对象
FileService fileService = new FileService();

// 调用方法
fileService.read(path);

// 返回结果
return result;
```

这类注释不会提高代码可读性。

##### 13. 代码格式规范

统一使用 IDE 自动格式化工具。

基本要求：

- 使用 4 个空格进行缩进；
- 不使用 Tab 进行代码缩进；
- 大括号采用 Java 常见风格；
- 运算符两侧适当留空格；
- 一个方法只完成一个主要任务；
- 避免过长代码行；
- import 按 IDE 规则自动整理；
- 删除未使用的 import。

例如：

```java
if (similarity >= threshold) {
    return result;
}
```

而不是：

```java
if(similarity>=threshold){
return result;
}
```

##### 14. 魔法数字规范

代码中不应直接出现缺乏含义的数字。

不推荐：

```java
if (args.length != 3) {
}
```

更推荐：

```java
private static final int REQUIRED_ARGUMENT_COUNT = 3;

if (args.length != REQUIRED_ARGUMENT_COUNT) {
}
```

但对于明显属于循环控制的情况：

```java
for (int i = 0; i < length; i++) {
}
```

不需要为了形式而创建常量。

##### 15. 字符串处理规范

字符串操作应注意性能。

在大量循环中禁止频繁使用：

```java
result = result + character;
```

应根据实际场景使用：

```java
StringBuilder
```

例如：

```java
StringBuilder builder = new StringBuilder();

for (char c : text.toCharArray()) {
    builder.append(c);
}

String result = builder.toString();
```

但不应为了“优化”而在普通字符串拼接场景中机械使用复杂结构。

##### 16. 性能规范

###### 16.1 基本原则

本项目存在明确的性能要求，因此算法实现必须考虑：

- 时间复杂度；
- 空间复杂度；
- 输入规模；
- 内存占用；
- GC 压力；
- 重复字符串创建。

###### 16.2 不进行无依据优化

开发阶段首先保证：

```
正确性
↓
可读性
↓
可测试性
↓
性能分析
↓
针对瓶颈优化
```

而不是一开始就进行复杂优化。

###### 16.3 Profiler

第一版实现完成后，根据作业要求使用 Profiling Tools、JProfiler 或其他符合要求的性能分析工具进行分析。

需要重点观察：

```
CPU Hotspots
Memory
方法调用次数
耗时最高的方法
```

重点关注：

```
SimilarityCalculator
LcsSimilarityCalculator
TextPreprocessor
```

如果性能瓶颈位于 LCS DP，应根据 Profiling 数据决定是否采用：

```
滚动数组
一维 DP
减少对象创建
减少字符串复制
其他算法优化
```

优化后必须重新执行测试，确认：

```
功能正确
性能提升
没有引入新的错误
```

##### 17. 单元测试规范

###### 17.1 测试框架

项目建议使用 JUnit 5。

测试代码放在：

```
src/test/java/
```

并保持与生产代码相对应的包结构。

###### 17.2 测试命名

测试方法名应体现测试场景。

推荐：

```java
calculate_shouldReturnOne_whenTextsAreIdentical()

calculate_shouldReturnZero_whenTextsHaveNoCommonContent()

preprocess_shouldRemoveHtmlTags_whenInputContainsHtml()

read_shouldThrowException_whenFileDoesNotExist()
```

避免：

```java
test1()
test2()
test3()
```

###### 17.3 最低测试数量

根据作业要求，项目至少需要10 个测试用例，实际开发建议不少于 20 个，以覆盖主要功能和边界情况。

###### 17.4 测试覆盖范围

至少包括：

| 类型        | 测试内容             |
| ----------- | -------------------- |
| 完全相同    | 原文与查重文完全相同 |
| 完全不同    | 没有公共内容         |
| 增加        | 原文基础上增加内容   |
| 删除        | 删除部分原文         |
| 修改        | 部分字符发生变化     |
| 重排        | 内容顺序发生变化     |
| 混合修改    | 多种修改同时出现     |
| 空文本      | 空输入               |
| 中文        | 中文文本             |
| 英文        | 英文文本             |
| 中英文混合  | 混合文本             |
| 标点        | 含标点文本           |
| 空白字符    | 不同空白情况         |
| HTML        | HTML 样例            |
| HTML entity | 含实体字符           |
| 长文本      | 较大输入             |
| 非法路径    | 文件不存在           |
| 参数错误    | 参数数量不正确       |

##### 18. 测试与生产代码分离

测试代码不得放在：

```
src/main/java
```

测试代码必须放在：

```
src/test/java
```

生产代码中不得保留：

```java
System.out.println("test");
```

等临时调试代码。

##### 19. 代码质量检查

项目应定期进行代码质量检查。

重点关注：

- 编译警告；
- 未使用变量；
- 未使用 import；
- 潜在 NullPointerException；
- 重复代码；
- 过长方法；
- 过长类；
- 异常吞掉；
- 不合理的类型转换；
- 不必要的对象创建；
- 命名问题。

最终提交前应尽量做到：

```
Build 成功
Test 全部通过
无明显编译警告
无明显静态检查问题
```

##### 20. Git 提交规范

项目开发过程应通过 Git 保留完整开发过程，而不是最后一次性提交全部代码。

###### 20.1 提交原则

每完成一个相对独立的工作阶段进行一次提交。

例如：

```
docs: add requirements analysis
docs: add design specification
docs: complete design review
docs: add coding standard
feat: implement file service
feat: implement text preprocessing
feat: implement similarity calculator
feat: implement result formatter
test: add similarity calculator tests
test: add file service tests
refactor: optimize similarity calculation
fix: handle empty input
perf: optimize lcs memory usage
```

###### 20.2 提交信息

建议使用以下格式：

```
<type>: <description>
```

其中：

```
docs      文档
feat      新功能
fix       修复问题
test      测试
refactor  重构
perf      性能优化
build     构建相关
```

例如：

```
feat: implement lcs similarity calculator
```

###### 20.3 不推荐的提交信息

不推荐：

```
update
modify
test
111
aaa
final
final2
真的最终版
```

提交信息应能够说明本次提交完成了什么。

##### 21. 调试代码规范

正式提交前必须删除：

```java
System.out.println(...)
System.err.println(...)
```

等临时调试代码。

如果确实需要日志，应统一使用项目确定的日志方案，而不是在业务代码中随意输出调试信息。

##### 22. TODO 规范

如果某项功能暂时未完成，可以使用：

```java
// TODO: validate the final similarity formula against official samples
```

但提交最终版本前，应检查所有 TODO。

不得存在影响最终功能的未完成 TODO。

##### 23. 算法验证规范

由于当前题目给出的部分样例存在：

```
add
del
dis_1
dis_10
dis_15
mix
rep
```

等不同类型，因此算法实现不能只通过“完全相同”的测试来判断正确。

每次修改相似度算法后，都应重新验证主要样例。

推荐建立：

```
样例输入
   ↓
程序运行
   ↓
实际输出
   ↓
标准输出
   ↓
结果比较
```

如果结果不一致，应优先检查：

1. 文本预处理；
2. HTML 处理；
3. 字符编码；
4. LCS 实现；
5. 相似度公式；
6. 输出格式；

而不是直接修改某个测试数据来“适配结果”。

##### 24. 边界情况规范

程序至少需要考虑以下情况：

###### 24.1 空原文

```java
original = ""
plagiarism = "some text"
```

需要按照设计文档中确定的规则处理。

###### 24.2 空查重文本

```java
original = "some text"
plagiarism = ""
```

需要保证不会发生：

```
除零
数组越界
异常退出
```

###### 24.3 两个文件均为空

必须定义明确行为。

该行为应在需求分析和设计文档中保持一致。

###### 24.4 文件不存在

程序应产生明确异常，而不是：

```
NullPointerException
```

或直接崩溃。

###### 24.5 特殊字符

需要考虑：

```
中文
英文
数字
标点
换行
制表符
HTML entity
```

##### 25. 可测试性规范

设计代码时，应尽可能使核心逻辑可以脱离文件系统单独测试。

例如：

```java
String original = "这是原文";
String plagiarism = "这是改写后的文本";

double result = calculator.calculate(original, plagiarism);
```

这样无需创建真实文件即可测试核心算法。

文件 I/O 则单独测试：

```java
fileService.read(path);
fileService.write(path, content);
```

这种设计可以降低测试复杂度，并提高测试覆盖率。

##### 26. 依赖方向规范

模块之间建议保持以下依赖方向：

```
Main
 ↓
Service / Core Modules
 ↓
Specific Implementations
```

例如：

```
Main
 ├── FileService
 ├── TextPreprocessor
 ├── SimilarityCalculator
 └── ResultFormatter
```

核心算法不应反过来依赖：

```
Main
FileService
ResultFormatter
```

例如`LcsSimilarityCalculator`，不应该负责读取文件或写结果。

##### 27. 禁止事项

项目中禁止出现以下情况：

###### 27.1 硬编码输入文件

禁止：

```java
String path = "C:\\Users\\xxx\\Desktop\\test.txt";
```

程序必须使用命令行参数。

###### 27.2 硬编码测试答案

禁止：

```java
if (fileName.equals("add.txt")) {
    return 0.83;
}
```

不得针对样例文件名直接返回结果。

###### 27.3 针对样例作弊

禁止根据：

```
文件名
文件长度
固定字符串
样例特征
```

直接判断答案。

算法必须能够处理未知测试数据。

###### 27.4 网络访问

程序运行期间不得访问网络获取查重结果。

###### 27.5 无关文件访问

不得扫描或读取与输入参数无关的文件。

###### 27.6 全局可变状态

除非确有必要，否则避免使用大量`static`可变全局变量。

##### 28. 最终代码提交检查清单

提交 GitHub 前执行以下检查。

###### 28.1 功能检查

- 支持三个命令行参数
- 支持绝对路径
- 可以读取原文
- 可以读取查重文本
- 可以写入答案文件
- 输出格式正确
- 浮点数保留两位小数
- 样例全部通过
- 边界情况处理正常

###### 28.2 代码质量检查

- 类名符合规范
- 方法名符合规范
- 变量名具有明确含义
- 没有明显重复代码
- 没有无意义注释
- 没有临时调试代码
- 没有未处理的 TODO
- 没有明显编译警告
- 异常处理合理

###### 28.3 测试检查

- 至少 10 个单元测试
- 核心算法有独立测试
- 文件操作有测试
- 文本预处理有测试
- 异常情况有测试
- 边界情况有测试
- 测试全部通过
- 已生成测试覆盖率报告

###### 28.4 性能检查

- 已完成基础性能测试
- 已使用 Profiler 分析
- 已确定热点方法
- 已记录优化前性能
- 已完成必要优化
- 已记录优化后性能
- 优化后测试全部通过

###### 28.5 Git 检查

- 代码已提交
- 文档已提交
- 测试代码已提交
- 提交信息具有明确含义
- GitHub 仓库结构正确
- 已生成最终 `main.jar`
- `main.jar` 已发布到 GitHub Releases

##### 29. 编码阶段执行顺序

完成本规范后，进入具体设计与编码阶段。

推荐按照以下顺序实施：

```
需求分析
   ↓
设计文档
   ↓
设计复审
   ↓
编码规范
   ↓
具体类设计
   ↓
FileService
   ↓
TextPreprocessor
   ↓
SimilarityCalculator
   ↓
LcsSimilarityCalculator
   ↓
ResultFormatter
   ↓
Main
   ↓
单元测试
   ↓
样例验证
   ↓
代码质量检查
   ↓
Profiler 性能分析
   ↓
性能优化
   ↓
回归测试
   ↓
打包 main.jar
   ↓
GitHub Release
   ↓
项目总结与 PSP 实际时间统计
```

其中，相似度算法在进入正式实现后仍应通过样例进行验证。**如果实验结果证明当前 LCS 方案与题目标准不一致，应修改具体算法设计，而不是通过硬编码样例结果来规避问题。**

##### 30. 规范总结

本项目编码阶段遵循以下核心原则：

> **职责分离、接口抽象、先保证正确性、再进行性能优化、测试驱动验证、Git 持续记录。**

代码实现应与前面的需求分析和设计文档保持一致。

尤其需要注意：

```
需求没有确认的规则 → 不擅自假设
算法没有验证 → 不宣称最终正确
性能没有测量 → 不进行无依据优化
功能没有测试 → 不认为已经完成
代码没有检查 → 不直接作为最终版本提交
```

最终代码应同时满足：

```
功能正确
+
代码清晰
+
异常可控
+
测试充分
+
性能达标
+
过程可追踪
```

以满足论文查重项目的功能要求和软件工程过程要求。