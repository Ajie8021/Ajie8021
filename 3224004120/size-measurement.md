#### 计算工作量

##### 1. 测量目的

本项目按照 PSP（Personal Software Process）记录开发工作量，并对软件规模和测试产出进行量化。目标是建立“软件规模—工作量估计->实际工作量->偏差分析”的对应关系，为项目复盘和过程改进提供数据。

##### 2. 当前可确认的规模

当前实现包含以下主要职责/模块：

| 模块 | 主要职责 |
|---|---|
| `Main` | 命令行入口、参数检查 |
| `PlagiarismApplication` | 组织完整查重流程 |
| `FileService` | 文件读取与写入 |
| `TextPreprocessor` | 文本及 HTML 预处理 |
| `SimilarityCalculator` | 相似度计算接口 |
| `LcsSimilarityCalculator` | 当前 LCS 相似度计算实现 |
| `ResultFormatter` | 结果格式化 |
| `exception` | 参数、读写异常处理 |

测试部分目前包含 4 个测试类、23 个测试用例：

| 测试类 | 数量 |
|---|---:|
| `DefaultResultFormatterTest` | 4 |
| `DefaultFileServiceTest` | 3 |
| `DefaultTextPreprocessorTest` | 6 |
| `LcsSimilarityCalculatorTest` | 10 |
| **合计** | **23** |

当前单元测试结果为 23/23 通过，失败 0，错误 0，跳过 0。

##### 3. LOC（代码行数）测量

最终版本应统计生产代码 LOC、测试代码 LOC 和总 Java LOC。本报告不人为估计当前 LOC，因为现有 PSP 记录中没有经过确认的最终源码行数。

PowerShell 可使用以下方式统计生产代码：

```powershell
Get-ChildItem -Recurse -Filter *.java src\main\java |
    Get-Content |
    Measure-Object -Line
```

统计测试代码：

```powershell
Get-ChildItem -Recurse -Filter *.java src\test\java |
    Get-Content |
    Measure-Object -Line
```

最终报告应同时注明统计口径，例如是否包含空行和注释，以保证不同阶段数据可比较。

##### 4. PSP 工作量测量

项目开始前已经建立 PSP 预计时间表。开发结束后，应补充各阶段实际耗时，并计算偏差：

```text
时间偏差 = 实际时间 - 预计时间
相对偏差率 = (实际时间 - 预计时间) / 预计时间 × 100%
```

建议最终填写：

| 阶段 | 预计时间 | 实际时间 | 偏差 | 偏差率 |
|---|---:|---:|---:|---:|
| Planning | 已记录 | 待补充 | 待计算 | 待计算 |
| Requirements Analysis | 已记录 | 待补充 | 待计算 | 待计算 |
| Design | 已记录 | 待补充 | 待计算 | 待计算 |
| Coding | 已记录 | 待补充 | 待计算 | 待计算 |
| Testing | 已记录 | 待补充 | 待计算 | 待计算 |
| **Total** | 已记录 | 待补充 | 待计算 | 待计算 |

实际时间应以开发日志为依据，不应事后凭记忆估计。

##### 5. 测试规模

当前已完成：

- 测试类：4 个；
- 单元测试：23 个；
- 通过：23 个；
- 失败：0 个；
- 错误：0 个；
- 跳过：0 个；
- 通过率：100%。

本次 `mvn test` 的 Maven 总执行时间为 2.315 s。该值表示一次测试命令的执行耗时，不等同于人工测试工作量。

##### 6. 测量结论

目前可靠的规模数据为“8 项主要职责/模块、4 个测试类、23 个单元测试”。最终提交前应补齐最终源码 LOC 和 PSP 实际总工时，使本报告与 PSP Actual Time 保持一致。

后续完成官方样例、覆盖率、Profiler 和性能优化后，应重新统计最终版本规模，并记录 Production LOC、Test LOC、Total LOC、测试数量、覆盖率、性能指标和实际 PSP 工时。
