# 代码规范

## 1. 适用范围

本规范适用于本项目所有 Java 源代码、测试代码及相关配置。

项目采用 **Java + Maven** 开发，遵循 Java 常用编码规范，保证代码具有良好的可读性、可维护性和可测试性。

## 2. 命名规范

### 2.1 类名

采用 `UpperCamelCase` 命名。

```tex
Fraction
ExpressionGenerator
ExpressionCalculator
CommandLineParser
```

### 2.2 方法名和变量名

采用 `lowerCamelCase` 命名。

```tex
calculateResult()
generateExpression()
operatorCount
maxRange
```

### 2.3 常量

使用全大写字母和下划线：

```tex
MAX_OPERATOR_COUNT
DEFAULT_OUTPUT_FILE
```

### 2.4 枚举

枚举类型使用 `UpperCamelCase`，枚举值使用大写字母和下划线：

```java
public enum Operator {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE
}
```

### 2.5 布尔变量

使用具有明确语义的名称，例如：

```tex
isValid
hasDuplicate
isFraction
```

## 3. 格式规范

### 3.1 缩进

使用 **4 个空格**进行缩进，不使用 Tab。

### 3.2 大括号

采用 Java 常见的 K&R 风格：

```java
if (condition) {
    doSomething();
} else {
    doSomethingElse();
}
```

### 3.3 行长度

单行代码原则上不超过 **120 个字符**。

过长的表达式、方法调用或参数列表应适当换行。

### 3.4 空行

不同逻辑代码块之间使用空行分隔，提高代码可读性。

## 4. 类与方法设计

### 4.1 单一职责

一个类应该负责相对独立的一项功能。

例如：

```tex
Fraction
    → 负责分数表示和运算

ExpressionCalculator
    → 负责表达式计算

ExpressionGenerator
    → 负责表达式生成

FileManager
    → 负责文件读写
```

避免创建包含大量无关功能的“万能类”。

### 4.2 方法长度

方法应保持简洁，一个方法主要完成一个功能。

如果一个方法过长或包含多个明显独立的处理步骤，应考虑拆分为多个方法。

### 4.3 参数数量

方法参数应尽量保持较少。

如果参数较多，应考虑使用对象封装相关数据。

## 5. 面向对象设计规范

### 5.1 封装

类的成员变量原则上使用 `private`，通过方法访问：

```java
private long numerator;
private long denominator;
```

避免直接暴露内部数据。

### 5.2 接口与实现

如果一个功能存在多种实现方式，可以根据需要使用接口或抽象类进行抽象。

本项目暂不为了形式而增加不必要的接口。

### 5.3 枚举

固定的有限选项优先使用 `enum`。

例如四则运算符使用：

```tex
Operator.ADD
Operator.SUBTRACT
Operator.MULTIPLY
Operator.DIVIDE
```

而不是使用字符串或数字表示。

## 6. 数据处理规范

### 6.1 分数统一表示

项目中的自然数和分数统一使用 `Fraction` 表示。

例如：

```tex
2       → 2/1
1/2     → 1/2
2'3/4   → 11/4
```

避免在不同模块中分别使用 `int`、`double` 表示数学结果。

### 6.2 避免浮点数计算

四则运算涉及分数，因此原则上不使用 `float` 或 `double` 保存计算结果，以避免浮点数精度问题。

优先使用整数分子、分母进行精确计算。

## 7. 表达式相关规范

### 7.1 使用结构化对象

表达式使用表达式树等结构化对象表示，不直接依赖字符串完成核心计算。

### 7.2 字符串仅用于输入输出

字符串主要用于：

- 命令行参数；
- 文件读写；
- 表达式最终显示。

核心计算和重复判断应基于表达式对象完成。

### 7.3 重复题判断

重复题判断统一通过 `ExpressionNormalizer` 完成。

其他模块不应自行实现重复题判断逻辑，避免出现多套不一致的判断规则。

## 8. 异常处理

### 8.1 不允许静默忽略异常

不能使用空的 `catch`：

```java
try {
    ...
} catch (Exception e) {
}
```

异常必须进行合理处理或向上层传递。

### 8.2 用户输入错误

命令行参数错误、文件不存在等用户可以修正的问题，应输出明确的提示信息。

例如：

```powershell
Error: missing required parameter -r.
Usage: java -jar Arithmetic.jar -n <number> -r <range>
```

### 8.3 不使用异常代替正常流程控制

对于正常情况下可能发生的判断，应优先使用条件判断，而不是依赖异常控制程序流程。

## 9. 注释规范

### 9.1 注释原则

注释主要解释：

- 为什么这样设计；
- 算法的关键思路；
- 不明显的业务规则；
- 复杂代码的处理逻辑。

避免对显而易见的代码进行无意义注释。

例如不推荐：

```java
i++; // i加1
```

### 9.2 类和公共方法

核心类和公共方法可以使用 Javadoc：

```java
/**
 * Represents an exact fraction and provides arithmetic operations.
 */
public class Fraction {
}
```

### 9.3 复杂算法

对于表达式规范化、重复题判断等复杂算法，应添加必要的说明，解释算法的目的和主要步骤。

## 10. 集合与资源使用

### 10.1 集合

根据用途选择合适的数据结构。

例如：

```java
Set<String>
```

用于保存已经生成的题目规范形式，以提高重复判断效率。

### 10.2 文件资源

文件读写应使用 `try-with-resources` 或其他可靠的资源管理方式，确保文件资源能够正确关闭。

## 11. 测试代码规范

测试代码放置在：

```tex
src/test/java
```

测试类命名：

```tex
被测试类名 + Test
```

例如：

```tex
FractionTest
ExpressionCalculatorTest
ExpressionNormalizerTest
ExpressionGeneratorTest
```

测试方法名称应能够说明测试内容，例如：

```tex
shouldCalculateFractionAddition()
shouldRejectNegativeSubtraction()
shouldDetectDuplicateExpression()
```

测试应尽量做到：

- 一个测试主要验证一个行为；
- 测试结果具有确定性；
- 同一测试可以重复运行；
- 同时覆盖正常情况、边界情况和异常情况。

## 12. Maven 项目规范

项目使用 Maven 管理。

标准目录结构：

```tex
src/
├── main/
│   └── java/
└── test/
    └── java/
```

依赖统一在 `pom.xml` 中管理。

构建和测试优先使用：

```powershell
mvn test
```

打包使用：

```powershell
mvn package
```

不将 Maven 生成的 `target/` 等构建产物提交到 Git 仓库。

## 13. Git 提交规范

每完成一个具有独立意义的功能或修改后进行一次提交。

提交信息应简洁描述本次修改，例如：

```tex
feat: 实现分数
feat: 实现表达式计算器
feat: 实现习题生成器
feat: 添加重复表达式检测
test: 添加分数单元测试
fix: 处理除零错误
perf: 优化重复检测
docs: 更新设计文档
```

避免使用：

```tex
update
test
修改
改了一下
```

等无法说明修改内容的提交信息。

## 14. 代码质量要求

提交代码前应检查：

- 是否存在编译错误；
- 是否存在明显的警告；
- 是否存在重复代码；
- 是否存在无用变量和无用方法；
- 命名是否清晰；
- 异常处理是否完整；
- 核心功能是否具有对应测试；
- 是否符合本规范。

最终代码应能够正常完成：

```powershell
mvn test
mvn package
```