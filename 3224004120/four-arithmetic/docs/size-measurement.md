#  工作量与规模度量

## 1. 度量目的

本阶段对项目的开发规模和实际工作量进行统计，为 PSP 中的过程数据分析以及后续的 Postmortem 提供依据。

主要统计：

- 项目源代码规模；
- 测试代码规模；
- Java 类数量；
- 测试类数量；
- 主要模块数量；
- 实际开发时间；
- 各开发阶段时间分布。

## 2. 项目规模

项目采用 Java + Maven 实现，主要由以下模块组成：

```tex
com.Ajie8021.arithmetic
├─ Main
├─ cli
├─ model
├─ calculator
├─ validator
├─ normalizer
├─ generator
├─ parser
├─ io
├─ grader
└─ exception
```

### 2.1 源代码模块

| 模块       | 主要职责                       |
| ---------- | ------------------------------ |
| Main       | 程序入口                       |
| cli        | 命令行参数解析                 |
| model      | 表达式、分数、运算符等数据模型 |
| calculator | 表达式计算                     |
| validator  | 表达式合法性验证               |
| normalizer | 表达式规范化和去重             |
| generator  | 题目生成                       |
| parser     | 表达式解析                     |
| io         | 文件读写                       |
| grader     | 答案批改                       |
| exception  | 自定义异常                     |

## 3. 类规模统计

当前项目生产代码主要包含以下 Java 类：

| 模块       | 类数量 |
| ---------- | ------ |
| CLI        | 2      |
| Model      | 6      |
| Calculator | 1      |
| Validator  | 1      |
| Normalizer | 1      |
| Generator  | 2      |
| Parser     | 1      |
| IO         | 1      |
| Grader     | 2      |
| Exception  | 4      |
| Main       | 1      |
| **合计**   | **22** |

测试代码按照模块进行划分，共包含多个独立测试类，与生产代码形成对应关系。

## 4. 代码规模

代码规模可以使用 IDE 的代码统计功能进行测量：

| 项目              | 数量       |
| ----------------- | ---------- |
| Java 生产代码文件 | 22         |
| Java 测试代码文件 | 9          |
| 生产代码行数      | 约 900行   |
| 测试代码行数      | 约 600 行  |
| Java 总代码规模   | 约 1500 行 |

## 5. 测试规模

测试代码按照功能模块进行组织：

```tex
FractionTest
ExpressionCalculatorTest
ExpressionValidatorTest
ExpressionNormalizerTest
ExpressionGeneratorTest
ExerciseGeneratorTest
ExpressionParserTest
CommandLineParserTest
GraderTest
```

测试覆盖了：

- 数据模型；
- 核心计算；
- 合法性检查；
- 等价判断；
- 随机生成；
- 表达式解析；
- CLI；
- 批改。

测试代码并不是集中在一个测试文件中，而是按照生产代码模块进行划分，有利于维护和定位问题。

## 6. PSP 实际时间统计

在完成项目后，将开发过程中的实际时间与之前的 Estimate 进行比较。

示例记录如下：

| PSP 阶段         | 预计时间/min | 实际时间/min |
| ---------------- | ------------ | ------------ |
| Planning         | 30           | 30           |
| Analysis         | 60           | 80           |
| Design Spec      | 90           | 100          |
| Design Review    | 30           | 30           |
| Coding Standard  | 30           | 30           |
| Design           | 90           | 120          |
| Coding           | 300          | 360          |
| Code Review      | 60           | 50           |
| Test             | 120          | 100          |
| Test Report      | 60           | 50           |
| Size Measurement | 30           | 30           |
| Postmortem       | 30           | 30           |
| **Total**        | **940**      | **1010**     |

实际时间约为：

```tex
1010 min ≈ 16.8 h
```

## 7. 预计时间与实际时间分析

从统计结果来看：

```tex
预计总时间：940 min
实际总时间：1010 min
时间偏差：70 min
```

实际开发时间比预计时间增加约：

```tex
70 / 940 × 100% ≈ 7.4%
```

整体偏差处于可以接受的范围。

其中主要偏差来自：设计复审不够仔细，写代码时发现文件设计上出了问题，改来改去多花了不少时间。

### Analysis

需求分析阶段实际花费时间较多，主要原因是需要将题目中的自然语言要求转换为明确的软件需求，例如：

- 什么属于重复题目；
- 如何定义表达式等价；
- 分数如何表示；
- 除法结果如何判断；
- 如何处理括号和运算符优先级。

### Design

设计阶段比预计时间略长，主要原因是项目采用了表达式树结构，需要提前设计：

```tex
Expression
├─ NumberExpression
└─ BinaryExpression
```

同时还需要考虑：

```tex
ExpressionGenerator
ExpressionValidator
ExpressionNormalizer
ExpressionParser
ExpressionCalculator
```

之间的职责划分。

### Coding

编码阶段耗时最多，主要原因是项目包含随机生成、表达式解析、分数计算、重复判断等多个相互关联的模块。

## 8. 工作量分析

从项目规模来看，该项目并不属于大型软件，但包含了较完整的软件开发流程：

```tex
需求分析
   ↓
概要设计
   ↓
详细设计
   ↓
编码
   ↓
单元测试
   ↓
功能测试
   ↓
性能分析
   ↓
测试报告
   ↓
项目总结
```

因此，本项目的主要工作量并不只是编写 Java 代码，还包括需求澄清、架构设计、测试设计、异常处理以及文档编写。

## 9. 度量结论

本项目生产代码规模约为千行左右，测试代码规模约为数百行，共划分为多个独立模块。

从 PSP 数据来看，实际开发时间略高于最初估计，主要原因是需求分析和设计阶段对表达式等价、分数计算以及题目合法性等问题进行了较多细化。

这些数据可以作为下一次类似项目进行时间估计时的参考。